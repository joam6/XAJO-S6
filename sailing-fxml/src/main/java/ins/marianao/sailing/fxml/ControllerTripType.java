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
<<<<<<< HEAD
import javafx.scene.control.ComboBox;
=======
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git
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
    @FXML private SplitMenuButton categorySelect;
    
    
    @FXML private TableView<TripType> tripTypeTable;
    @FXML private TableColumn<TripType, Number> colID;
    @FXML private TableColumn<TripType, String> colCategoria;
    @FXML private TableColumn<TripType, String> colDepartures;
    @FXML private TableColumn<TripType, String> colDescription;
    @FXML private TableColumn<TripType, Number> colDuration;
    @FXML private TableColumn<TripType, Number> colMaxPlaza;
    @FXML private TableColumn<TripType, Float> colPrecio;
    @FXML private TableColumn<TripType, String> colTitulo;
    //@FXML private ComboBox<Pair<String,String>> cmbcategory;
    @FXML private SplitMenuButton cmbcategory;


    @Override
    public void initialize(URL url, ResourceBundle resource) {
        // Configurar las columnas de la tabla
<<<<<<< HEAD

    	// Crear una lista de roles disponibles desde el modelo de usuario
        /*List<Pair<String,String>> categories = Stream.of(TripType.Category.values()
        		).map(new Function<Category,Pair<String,String>>() {
			@Override
			public Pair<String, String> apply(Category t) {
				 String key = t.name();
	                return new Pair<String, String>(key, resource.getString("text.Triptype."+key));
			}
        }).collect(Collectors.toList());

        // Convertir la lista de roles a un ObservableList que puede ser usado en la ComboBox
        ObservableList<Pair<String,String>> listcategory = FXCollections.observableArrayList(categories);
        listcategory.add(0, null); // Añadir un valor nulo al principio de la lista para la opción de todos los roles
        
        /*this.colCategoria.setId(listcategory);
        this.colCategoria.setConverter(Formatters.getStringPairConverter("User"));
    	
        this.colCategoria.valueProperty().addListener(new ChangeListener<Pair<String,String>>() {
            @Override
            public void changed(ObservableValue<? extends Pair<String,String>> observable, Pair<String,String> oldValue, Pair<String,String> newValue) {
            	reloadTripTypes();
            }
        });

        // Llamar a la función que recarga los usuarios al inicio
        this.reloadTripTypes();
    	
      //DEFINIR SI EL USUARIO ES ADMIN / CLIENTE EN LA TABLA
        this.colCategoria.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TripType, String> cellData) {
                String key = cellData.getValue().getCategory().toString();
                return new SimpleStringProperty(resource.getString("text.Triptype.."+key)); // Muestra el nombre del rol en texto
            }
        });*/
        
        
=======
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git
        this.colCategoria.setCellValueFactory(new PropertyValueFactory<TripType,String>("Category"));
        
        
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
    
<<<<<<< HEAD
    private void reloadTripTypes() {
        // Obtener el filtro seleccionado desde la ComboBox
       Pair<String, String> selectedCategory = this.cmbcategory.getValue(); // Suponiendo que tienes una ComboBox llamada cmbCategory
=======
    /*private void reloadTripTypes() {
        Role[] roles = null;
        Pair<String,String> role = this.cmbRole.getValue();
        String search = this.txtFullnameSearch.getText();
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

        // Desactivar la edición de la tabla mientras se cargan los datos
        this.tripTypeTable.setEditable(false);

        // Preparar el filtro de categorías
        Category[] categories = null;
        if (selectedCategory != null && selectedCategory.getKey() != null) {
            try {
                categories = new Category[]{Category.valueOf(selectedCategory.getKey())};
            } catch (IllegalArgumentException e) {
                // Manejar el caso donde el valor de la clave no sea válido
                System.err.println("Categoría no válida: " + selectedCategory.getKey());
                return;
            }
        }

        // Iniciar la consulta para obtener los tipos de viaje
        final ServiceQueryTripType queryTripTypes = new ServiceQueryTripType();
        
        // Configurar el manejador de éxito para cuando la consulta termine
        queryTripTypes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                // Reactivar la edición de la tabla
                tripTypeTable.setEditable(true);

                // Limpiar los datos actuales de la tabla
                tripTypeTable.getItems().clear();

                // Obtener los resultados de la consulta y asignarlos a la tabla
                ObservableList<TripType> tripTypes = FXCollections.observableArrayList(queryTripTypes.getValue());
                tripTypeTable.setItems(tripTypes);
            }
        });

        // Configurar el manejador de fallo para manejar errores en la consulta
        queryTripTypes.setOnFailed(new OnFailedEventHandler(
            ResourceManager.getInstance().getText("error.viewTripTypes.web.service")
        ));

<<<<<<< HEAD
        // Iniciar la consulta
        queryTripTypes.start();
    }
=======
        queryUsers.start(); // Iniciar la consulta
    }*/

    
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git
}