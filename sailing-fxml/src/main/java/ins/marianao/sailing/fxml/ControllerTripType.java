package ins.marianao.sailing.fxml;

import java.awt.TextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Pair;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceQueryTripType;
import ins.marianao.sailing.fxml.services.ServiceQueryUsers;
import ins.marianao.sailing.fxml.utils.Formatters;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;

public class ControllerTripType implements Initializable {

	@FXML private BorderPane viewLoginForm;

	@FXML private TextField priceFrom;
	@FXML private TextField priceTo;
	@FXML private TextField placeFrom;
	@FXML private TextField placeTo;
	@FXML private TextField durationFrom;
	@FXML private TextField durationTo;
	@FXML private ComboBox<Pair<String,String>> categorySelect;


	@FXML private TableView<TripType> tripTypeTable;
	@FXML private TableColumn<TripType, Number> colID;
	@FXML private TableColumn<TripType, String> colCategoria;
	@FXML private TableColumn<TripType, String> colDepartures;
	@FXML private TableColumn<TripType, String> colDescription;
	@FXML private TableColumn<TripType, Number> colDuration;
	@FXML private TableColumn<TripType, Number> colMaxPlaza;
	@FXML private TableColumn<TripType, Float> colPrecio;
	@FXML private TableColumn<TripType, String> colTitulo;


	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Configurar las columnas de la tabla

		// Crear una lista de categorías disponibles desde el modelo TripType
		List<Pair<String, String>> categories = Stream.of(TripType.Category.values())
				.map(category -> new Pair<>(category.name(), resource.getString("text.Triptype." + category.name())))
				.collect(Collectors.toList());

		// Convertir la lista de categorías a un ObservableList que puede ser usado en el ComboBox
		ObservableList<Pair<String, String>> listCategories = FXCollections.observableArrayList(categories);
		listCategories.add(0, null); // Añadir un valor nulo al principio de la lista para la opción "Todas las categorías"

		// Configurar el ComboBox con la lista de categorías
		this.categorySelect.setItems(listCategories);
		this.categorySelect.setConverter(Formatters.getStringPairConverter("Triptype"));

		// Añadir listener al ComboBox para recargar los tipos de viaje cuando se cambia el valor
		this.categorySelect.valueProperty().addListener(new ChangeListener<Pair<String, String>>() {
			@Override
			public void changed(ObservableValue<? extends Pair<String, String>> observable, Pair<String, String> oldValue, Pair<String, String> newValue) {
				reloadTripTypes(); // Recargar los tipos de viaje cuando cambia la selección
			}
		});

		// Llamar a la función que recarga los tipos de viaje al inicio
		this.reloadTripTypes();


		/* //DEFINIR SI EL USUARIO ES ADMIN / CLIENTE EN LA TABLA
        this.colCategoria.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TripType, String> cellData) {
                String key = cellData.getValue().getCategory().toString();
                return new SimpleStringProperty(resource.getString("text.Triptype.."+key)); // Muestra el nombre del rol en texto
            }
        });*/


		this.colCategoria.setCellValueFactory(new PropertyValueFactory<TripType,String>("Category"));
		this.colCategoria.setCellValueFactory(cellData -> 
		new SimpleStringProperty(resource.getString("text.Triptype." + cellData.getValue().getCategory().name()))
				);

		this.colCategoria.setCellFactory(TextFieldTableCell.forTableColumn());
		/* DIFERENCIAR CATEGORIA PRIVADO / GRUPO 
		 * public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cellData) {
                String key = cellData.getValue().getRole().toString();
                return new SimpleStringProperty(resource.getString("text.User."+key)); // Muestra el nombre del rol en texto
            }
		 */


		this.colDepartures.setCellValueFactory(new PropertyValueFactory<TripType,String>("Departures"));


		this.colDescription.setCellValueFactory(new PropertyValueFactory<TripType,String>("Description"));


		this.colDuration.setCellValueFactory(new PropertyValueFactory<TripType,Number>("Duration"));


		this.colMaxPlaza.setCellValueFactory(new PropertyValueFactory<TripType,Number>("Places"));


		this.colPrecio.setCellValueFactory(new PropertyValueFactory<TripType,Float>("Duration"));


		this.colTitulo.setCellValueFactory(new PropertyValueFactory<TripType,String>("Title"));


	}

	private void reloadTripTypes() {
	    // Desactivar la edición de la tabla mientras se cargan los datos
	    this.tripTypeTable.setEditable(false);

	    // Obtener la categoría seleccionada desde el ComboBox
	    Pair<String, String> selectedCategory = this.categorySelect.getValue();

	    // Preparar el filtro de categorías
	    Category[] categories = null;
	    if (selectedCategory != null && selectedCategory.getKey() != null) {
	        try {
	            categories = new Category[]{Category.valueOf(selectedCategory.getKey())};
	        } catch (IllegalArgumentException e) {
	            System.err.println("Categoría no válida: " + selectedCategory.getKey());
	            return; // Salir si la categoría no es válida
	        }
	    }

	    // Iniciar la consulta para obtener los tipos de viaje
	    final ServiceQueryTripType queryTripTypes = new ServiceQueryTripType();

	    // Configurar el manejador de éxito para cuando la consulta termine
	    queryTripTypes.setOnSucceeded(event -> {
	        tripTypeTable.setEditable(true); // Reactivar la edición de la tabla
	        tripTypeTable.getItems().clear(); // Limpiar los datos actuales

	        // Obtener los resultados de la consulta y asignarlos a la tabla
	        List<TripType> tripTypes = queryTripTypes.getValue();
	        if (tripTypes != null) {
	            ObservableList<TripType> observableTripTypes = FXCollections.observableArrayList(tripTypes);
	            tripTypeTable.setItems(observableTripTypes);
	        } else {
	            System.err.println("No se encontraron tipos de viaje.");
	        }
	    });

	    // Configurar el manejador de fallo para manejar errores en la consulta
	    queryTripTypes.setOnFailed(new OnFailedEventHandler(
	            ResourceManager.getInstance().getText("error.viewTripTypes.web.service")
	    ));

	    // Iniciar la consulta
	    queryTripTypes.start();
	}

}