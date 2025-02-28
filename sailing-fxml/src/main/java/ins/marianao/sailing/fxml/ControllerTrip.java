package ins.marianao.sailing.fxml;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceQueryTrip;
import ins.marianao.sailing.fxml.services.ServiceQueryTripType;
import ins.marianao.sailing.fxml.utils.Formatters;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerTrip implements Initializable {

    @FXML private ComboBox<Client> clientSelector;
    @FXML private ComboBox<Pair<String, String>> categorySelector;
    @FXML private ComboBox<Pair<String, String>> statusSelector;
    @FXML private DatePicker fromDate;
    @FXML private DatePicker toDate;
    @FXML private ButtonBar buttonBar;

    @FXML private TableView<Trip> tripTable;
    @FXML private TableColumn<Trip, Number> tripid;
    @FXML private TableColumn<Trip, String> tripClient;
    @FXML private TableColumn<Trip, Category> tripCategory;
    @FXML private TableColumn<Trip, String> tripTitle;
    @FXML private TableColumn<Trip, Integer> tripMaxPlaces;
    @FXML private TableColumn<Trip, Integer> tripBooked;
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
        
        // Configura el filtro para la categoría
        setupCategoryFilter(resource);
    }

    private void setupCategoryFilter(ResourceBundle resource) {
        // Crear la lista de categorías con sus traducciones
        List<Pair<String, String>> categories = Stream.of(Category.values())
                .map(category -> new Pair<>(category.name(), resource.getString("text.Category." + category.name())))
                .collect(Collectors.toList());

        // Agregar la opción "Todas las categorías" al principio de la lista
        categories.add(0, new Pair<>("ALL", resource.getString("text.Category.ALL"))); 
        
        // Asignar la lista de categorías al ComboBox
        categorySelector.setItems(FXCollections.observableArrayList(categories));
        
        // Establecer un conversor para la visualización de los elementos en el ComboBox
        categorySelector.setConverter(Formatters.getStringPairConverter("Category"));

        // Recargar la lista de viajes cuando cambie la selección del ComboBox
        categorySelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Si se selecciona "ALL", no filtramos nada, mostramos todos los viajes
            if (newValue == null || newValue.getKey().equals("ALL")) {
                // No filtrar, mostrar todos los viajes
                tripTable.setItems(FXCollections.observableArrayList(tripTable.getItems()));
            } else {
                // Si se seleccionó una categoría específica
                Category selectedCategory = Category.valueOf(newValue.getKey());  // Convierte la clave a un valor de la enumeración Category

                // Crear un nuevo FilteredList para aplicar el filtro
                FilteredList<Trip> filteredTrips = new FilteredList<>(FXCollections.observableArrayList(tripTable.getItems()));

                // Filtrar los viajes según la categoría seleccionada
                filteredTrips.setPredicate(trip -> {
                    // Asegurarse de que el tipo de viaje no sea nulo
                    Category category = trip.getDeparture().getTripType().getCategory();
                    return category != null && category.equals(selectedCategory);
                });

                // Establecer la lista filtrada como fuente de datos para la tabla
                tripTable.setItems(filteredTrips);
            }
        });
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

    	this.tripid.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Trip, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Trip, Number> usuari) {
                return new SimpleLongProperty(tripTable.getItems().indexOf(usuari.getValue()) + 1); // Muestra el número de fila
            }
        });

    	this.tripCategory.setCellValueFactory(cellData ->  
        new SimpleObjectProperty<>(cellData.getValue().getDeparture().getTripType().getCategory()));

    	this.tripCategory.setCellFactory(ComboBoxTableCell.forTableColumn(Category.values()));
    	
    	tripClient.setCellValueFactory(cellData -> {
    	    // Get the client associated with the current trip
    	    Client client = cellData.getValue().getClient();
    	    // Check if the client is not null and return their full name
    	    String fullName = (client != null) ? client.getFullName() : "";
    	    return new SimpleStringProperty(fullName);
    	});
        tripCategory.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeparture().getTripType().getCategory()));
        tripTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeparture().getTripType().getTitle()));
        tripMaxPlaces.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDeparture().getTripType().getMaxPlaces()).asObject());
        tripBooked.setCellValueFactory(cellData ->
        new SimpleIntegerProperty(cellData.getValue().getDeparture().getBookedPlaces()).asObject());

        tripStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tripDeparture.setCellValueFactory(cellData -> 
        new SimpleStringProperty(sdf2.format(cellData.getValue().getDeparture().getDeparture())));
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
