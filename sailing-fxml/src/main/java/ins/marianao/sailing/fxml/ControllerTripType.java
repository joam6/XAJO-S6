package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import cat.institutmarianao.sailing.ws.model.TripType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class ControllerTripType implements Initializable {
	
	@FXML private BorderPane viewLoginForm;

	
	 // Text fields
    @FXML private TextField pricefrom;
    @FXML private TextField priceto;
    @FXML private TextField placeto;
    @FXML private TextField placeform;
    @FXML private TextField durationform;
    @FXML private TextField durationto;
    
    @FXML private SplitMenuButton all;
    
    /*
	@FXML private MenuBar menuBar;
	@FXML private Menu mnTrips;
	@FXML private MenuItem mnItBooking;
	@FXML private MenuItem mnItTrips;
	@FXML private Menu mnUsers;
	@FXML private MenuItem mnItAddUser;
	@FXML private MenuItem mnItUserDirectory;
	@FXML private MenuItem mnItImport;
	@FXML private MenuItem mnItExport;
	@FXML private Menu mnProfile;
	@FXML private MenuItem mnItEditProfile;
	@FXML private MenuItem mnItLogoff;
	@FXML private Menu mnLogin;
	@FXML private MenuItem mnItLogin;
	@FXML private MenuItem mnItRegister;*/
	
	//id , category , departures , description duration , max_places , price , title
	
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
		//super.initialize(url, resource);
		
		
	}
	private void reloadTripTypes() {
	    // Obtener los filtros de la interfaz de usuario
	    String category = this.cmbCategory.getValue();      // Filtro de categoría
	    Double price = this.txtPriceSearch.getText().isEmpty() ? null : Double.valueOf(this.txtPriceSearch.getText());  // Filtro de precio
	    Integer duration = this.txtDurationSearch.getText().isEmpty() ? null : Integer.valueOf(this.txtDurationSearch.getText());  // Filtro de duración
	    Integer maxPlaces = this.txtMaxPlacesSearch.getText().isEmpty() ? null : Integer.valueOf(this.txtMaxPlacesSearch.getText());  // Filtro de plazas

	    // Desactivar la edición de la tabla mientras cargamos los datos
	    this.tripTypeTable.setEditable(false);

	    // Crear el servicio con los filtros
	    final ServiceQueryTripTypes queryTripTypes = new ServiceQueryTripTypes(category, price, duration, maxPlaces);

	    // Definir lo que sucede cuando la tarea se completa exitosamente
	    queryTripTypes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	        @Override
	        public void handle(WorkerStateEvent t) {
	            // Rehabilitar la edición de la tabla
	        	tripTypeTable.setEditable(true);

	            // Limpiar la tabla antes de agregar los nuevos elementos
	            tripTypeTable.getItems().clear();

	            // Obtener la lista de tipos de viaje desde el servicio
	            ObservableList<TripType> tripTypes = FXCollections.observableArrayList(queryTripTypes.getValue());

	            // Establecer los elementos de la tabla
	            tripTypeTable.setItems(tripTypes);
	        }
	    });

	    // Definir lo que sucede cuando la tarea falla
	    queryTripTypes.setOnFailed(new OnFailedEventHandler("Error al obtener los tipos de viaje"));

	    // Iniciar la consulta
	    queryTripTypes.start();
	}



		

}
