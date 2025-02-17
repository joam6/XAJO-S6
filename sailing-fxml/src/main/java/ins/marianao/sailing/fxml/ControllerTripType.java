package ins.marianao.sailing.fxml;

import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceQueryTripTypes;
import ins.marianao.sailing.fxml.services.ServiceQueryUsers;
import cat.institutmarianao.sailing.ws.model.TripType;
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

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        // Configurar las columnas de la tabla
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
    
    /*private void reloadTripTypes() {
        Role[] roles = null;
        Pair<String,String> role = this.cmbRole.getValue();
        String search = this.txtFullnameSearch.getText();

        this.TripTypeTable.setEditable(false); // Desactivar la edición de la tabla mientras se cargan los datos

        // Aplicar el filtro de roles si es necesario
        if (role != null) roles = new Role[] { Role.valueOf(role.getKey()) };

        final ServiceQueryUsers queryUsers = new ServiceQueryUsers(roles, search);

        // Iniciar la consulta para obtener los usuarios
        queryUsers.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                TripTypeTable.setEditable(true); // Reactivar la edición cuando la consulta se complete
                TripTypeTable.getItems().clear(); // Limpiar la tabla

                ObservableList<User> users = FXCollections.observableArrayList(queryUsers.getValue()); // Obtener los usuarios
                TripTypeTable.setItems(users); // Establecer los usuarios en la tabla
            }
        });

        // Definir lo que sucede si la consulta falla
        queryUsers.setOnFailed(new OnFailedEventHandler(ResourceManager.getInstance().getText("error.viewUsers.web.service")));

        queryUsers.start(); // Iniciar la consulta
    }*/

    
}