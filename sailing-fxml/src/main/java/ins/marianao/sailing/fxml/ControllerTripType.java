package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.services.ServiceQueryTripTypes;
import cat.institutmarianao.sailing.ws.model.TripType;

public class ControllerTripType implements Initializable {
    
    @FXML private BorderPane viewLoginForm;
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
        reloadTripTypes();
    }
    
    private void reloadTripTypes() {
        tripTypeTable.setEditable(false);
        final ServiceQueryTripTypes queryTripTypes = new ServiceQueryTripTypes();
        
        queryTripTypes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                tripTypeTable.setEditable(true);
                tripTypeTable.getItems().clear();
                ObservableList<TripType> tripTypes = FXCollections.observableArrayList(queryTripTypes.getValue());
                tripTypeTable.setItems(tripTypes);
            }
        });

        queryTripTypes.setOnFailed(new OnFailedEventHandler("Error al obtener los tipos de viaje"));
        queryTripTypes.start();
    }
}
