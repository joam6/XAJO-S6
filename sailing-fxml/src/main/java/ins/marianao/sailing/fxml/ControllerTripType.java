package ins.marianao.sailing.fxml;

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
import javafx.scene.control.TextField;
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


	@FXML private TableView<TripType> tripTypeTable;
	@FXML private TableColumn<TripType, Number> colID;
	@FXML private TableColumn<TripType, String> colCategoria;
	@FXML private TableColumn<TripType, String> colDepartures;
	@FXML private TableColumn<TripType, String> colDescription;
	@FXML private TableColumn<TripType, Number> colDuration;
	@FXML private TableColumn<TripType, Integer> colMaxPlaza;
	@FXML private TableColumn<TripType, Double> colPrecio;
	@FXML private TableColumn<TripType, String> colTitulo;
	@FXML private ComboBox<Pair<String,String>> categorySelect;



	@Override
	public void initialize(URL url, ResourceBundle resource) {
	    // Configurar el ComboBox de categorías
	    List<Pair<String, String>> categories = Stream.of(TripType.Category.values())
	        .map(category -> new Pair<>(category.name(), resource.getString("text.TripType." + category.name())))
	        .collect(Collectors.toList());

	    ObservableList<Pair<String, String>> listCategories = FXCollections.observableArrayList(categories);
	    listCategories.add(0, null); // Agregar opción "Todas las categorías" al inicio

	    this.categorySelect.setItems(listCategories);
	    this.categorySelect.setConverter(Formatters.getStringPairConverter("Triptype"));

	    // Agregar listener para recargar los tipos de viaje cuando cambia la selección
	    this.categorySelect.valueProperty().addListener((observable, oldValue, newValue) -> reloadTripTypes());

	    // Configurar las columnas de la tabla
	    this.colCategoria.setCellValueFactory(cellData -> 
	        new SimpleStringProperty(resource.getString("text.Triptype." + cellData.getValue().getCategory().name()))
	    );
	    this.colCategoria.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colDepartures.setCellValueFactory(new PropertyValueFactory<>("departures"));
	    this.colDepartures.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
	    this.colDescription.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colDuration.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(Double.class)));

	    this.colMaxPlaza.setCellValueFactory(new PropertyValueFactory<>("places"));
	    this.colMaxPlaza.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("price"));
	    this.colPrecio.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colTitulo.setCellValueFactory(new PropertyValueFactory<>("title"));
	    this.colTitulo.setCellFactory(TextFieldTableCell.forTableColumn());

	    // Recargar los tipos de viaje al inicio
	    this.reloadTripTypes();
	}

	private void reloadTripTypes() {
		if (tripTypeTable == null) {
	        System.err.println("La tabla tripTypeTable no está inicializada.");
	        return;
	    }
		// Desactivar la edición de la tabla mientras se cargan los datos

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
			tripTypeTable.setEditable(false);  
			tripTypeTable.getItems().clear();   

			List<TripType> tripTypes = queryTripTypes.getValue();  
			System.out.println("Fetched trip types: " + (tripTypes != null ? tripTypes.size() : 0));  
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