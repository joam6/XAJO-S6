package ins.marianao.sailing.fxml;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceQueryTrip;
import ins.marianao.sailing.fxml.services.ServiceQueryTripType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

public class ControllerTrip implements Initializable {

    @FXML private ComboBox<Client> clientSelector;
    @FXML private ComboBox<Pair<String, String>> categorySelector;
    @FXML private ComboBox<Pair<String, String>> statusSelector;
    @FXML private DatePicker fromDate;
    @FXML private DatePicker toDate;
    @FXML private ButtonBar buttonBar;

    @FXML private TableView<Trip> tripTable;
    @FXML private TableColumn<Trip, String> tripClient;
    @FXML private TableColumn<Trip, Category> tripCategory;
    @FXML private TableColumn<Trip, String> tripTitle;
    @FXML private TableColumn<Trip, Integer> tripMaxPlaces;
    @FXML private TableColumn<Trip, String> tripBooked;
    @FXML private TableColumn<Trip, String> tripStatus;
    @FXML private TableColumn<Trip, String> tripDate;
    @FXML private TableColumn<Trip, String> tripDeparture;
    @FXML private TableColumn<Trip, Integer> tripPlaces;
    @FXML private TableColumn<Trip, String> tripComments;
    @FXML private TableColumn<Trip, Void> action1;
    @FXML private TableColumn<Trip, Void> action2;
    @FXML private TableColumn<Trip, Void> action3;
    @FXML private BorderPane viewTripForm;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        configureCategorySelector(resource);
        configureTripTableColumns();
        reloadTrips();
        reloadTripTypes();
    }

    private void configureCategorySelector(ResourceBundle resource) {
        List<Pair<String, String>> categories = FXCollections.observableArrayList();
        for (Category category : Category.values()) {
            categories.add(new Pair<>(category.name(), resource.getString("text.Category." + category.name())));
        }
        categories.add(0, new Pair<>("ALL", resource.getString("text.Category.ALL")));
        categorySelector.setItems(FXCollections.observableArrayList(categories));
    }

    private void configureTripTableColumns() {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

        tripClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        tripCategory.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeparture().getTripType().getCategory()));
        tripTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeparture().getTripType().getTitle()));
        tripMaxPlaces.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDeparture().getTripType().getMaxPlaces()).asObject());
        tripBooked.setCellValueFactory(new PropertyValueFactory<>("booked"));
        tripStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tripDeparture.setCellValueFactory(cellData -> new SimpleStringProperty(sdf2.format(cellData.getValue().getDeparture().getDate())));
        tripPlaces.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlaces()).asObject());
        tripComments.setCellValueFactory(new PropertyValueFactory<>("comments"));
        tripDate.setCellValueFactory(cellData -> new SimpleStringProperty(sdf.format(cellData.getValue().getDeparture().getDate())));
        
        configureActionColumn(action1, "Acción 1");
        configureActionColumn(action2, "Acción 2");
        configureActionColumn(action3, "Acción 3");
    }

    private void reloadTrips() {
        final ServiceQueryTrip queryTrip = new ServiceQueryTrip(null, null, null, null, null);
        queryTrip.setOnSucceeded(event -> {
            List<Trip> trips = queryTrip.getValue();
            if (trips != null) {
                tripTable.setItems(FXCollections.observableArrayList(trips));
            }
        });
        queryTrip.setOnFailed(new OnFailedEventHandler(ResourceManager.getInstance().getText("error.viewTrips.web.service")));
        queryTrip.start();
    }

    private void reloadTripTypes() {
        final ServiceQueryTripType queryTripType = new ServiceQueryTripType(null, null, null, null, null, null, null);
        queryTripType.setOnSucceeded(event -> {
            List<TripType> tripTypes = queryTripType.getValue();
            if (tripTypes != null) {
                Map<Long, TripType> tripTypeMap = tripTypes.stream().collect(Collectors.toMap(TripType::getId, tripType -> tripType));
                //tripTable.getItems().forEach(trip -> trip.setTripType(tripTypeMap.get(trip.getDeparture().getTripType().getId())));
                tripTable.refresh();
            }
        });
        queryTripType.start();
    }

    private void configureActionColumn(TableColumn<Trip, Void> column, String columnName) {
        column.setText(columnName);
        column.setCellFactory(param -> new TableCell<>() {
            private final Button btnAction = new Button("•••");
            {
                btnAction.setOnAction(event -> {
                    Trip trip = getTableView().getItems().get(getIndex());
                    showReservationPopup(trip);
                });
                btnAction.setStyle("-fx-padding: 0 2; -fx-min-width: 25px;");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && getIndex() >= 0) {
                    Trip trip = getTableView().getItems().get(getIndex());
                    setGraphic(trip.getStatus() == Status.RESERVED ? btnAction : null);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    private void showReservationPopup(Trip trip) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reserva Activa");
        alert.setHeaderText("Información de la Reserva");
        alert.setContentText("El viaje con ID " + trip.getId() + " está en estado RESERVA.");
        alert.getButtonTypes().setAll(
            new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE),
            new ButtonType("Modificar", ButtonBar.ButtonData.OTHER),
            new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE)
        );
        alert.showAndWait().ifPresent(result -> System.out.println(result.getText() + " reserva del viaje: " + trip.getId()));
    }
}
