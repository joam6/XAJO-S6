package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        // Configurar las columnas de la tabla
        configureTableColumns();

        // Recargar los tipos de viaje al inicializar el controlador
        reloadTripTypes();
    }

    /**
     * Configura las columnas de la tabla para que muestren los datos correspondientes del modelo TripType.
     */
    private void configureTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id")); // ID del tipo de viaje
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("category")); // Categoría (GROUP/PRIVATE)
        colDepartures.setCellValueFactory(new PropertyValueFactory<>("departures")); // Horarios disponibles
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description")); // Descripción
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration")); // Duración en horas
        colMaxPlaza.setCellValueFactory(new PropertyValueFactory<>("maxPlaces")); // Máximo número de plazas
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("price")); // Precio
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("title")); // Título
    }

    /**
     * Recarga los tipos de viaje desde el servicio y actualiza la tabla.
     */
    private void reloadTripTypes() {
        // Crear una instancia del servicio para consultar los tipos de viaje
        final ServiceQueryTripTypes queryTripTypes = new ServiceQueryTripTypes(null, null); // Sin filtros

        // Manejar el evento cuando la consulta se completa exitosamente
        queryTripTypes.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                // Limpiar los elementos actuales de la tabla
                tripTypeTable.getItems().clear();

                // Convertir el resultado en una lista observable y asignarlo a la tabla
                ObservableList<TripType> tripTypes = FXCollections.observableArrayList(queryTripTypes.getValue());
                tripTypeTable.setItems(tripTypes);
            }
        });

        // Manejar errores en caso de fallo en la consulta
        queryTripTypes.setOnFailed(new OnFailedEventHandler("Error al obtener los tipos de viaje"));

        // Iniciar la tarea de consulta
        queryTripTypes.start();
    }

    /**
     * Método para recargar los tipos de viaje manualmente (por ejemplo, al hacer clic en un botón).
     */
    @FXML
    private void onReloadButton(ActionEvent event) {
        reloadTripTypes();
    }
}