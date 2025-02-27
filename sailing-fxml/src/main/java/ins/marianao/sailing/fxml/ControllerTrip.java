package ins.marianao.sailing.fxml;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class ControllerTrip implements Initializable{

	@FXML private ComboBox<Client> clientSelector;
	@FXML private ComboBox<Pair<String, String>> categorySelector;
	@FXML private ComboBox<Pair<String, String>> statusSelector;
	@FXML private DatePicker fromDate;
	@FXML private DatePicker toDate;
	@FXML private ButtonBar buttonBar;
	
	@FXML private TableColumn<Trip, Void> action1;
	@FXML private TableColumn<Trip, Void> action2;
	@FXML private TableColumn<Trip, Void> action3;
	@FXML private BorderPane viewTripForm;
	@FXML private TableView<Trip> tripTable;
	@FXML private TableColumn<Trip, String> tripClient;
	@FXML private TableColumn<Trip, Category> tripCategory;
	@FXML private TableColumn<TripType, String> tripTitle;
	@FXML private TableColumn<TripType, Integer> tripMaxPlaces;
	@FXML private TableColumn<Trip, String> tripBooked;
	@FXML private TableColumn<Trip, String> tripStatus;// No aparece en ninguna tabla
	@FXML private TableColumn<Trip, Date> tripDate;
	@FXML private TableColumn<TripType, String> tripDeparture;
	@FXML private TableColumn<Trip, Integer> tripPlaces;
	@FXML private TableColumn<Trip, String> tripComments; //No esta en ninguna tabla.

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Configuración de categorías
		List<Pair<String, String>> categories = Stream.of(Category.values())
				.map(category -> new Pair<>(category.name(), resource.getString("text.Category." + category.name())))
				.collect(Collectors.toList());
		categories.add(0, new Pair<>("ALL", resource.getString("text.Category.ALL"))); // Opción por defecto

		// Configuración del ComboBox para las categorías
		categorySelector.setItems(FXCollections.observableArrayList(categories));

		// Configuración del ComboBox para las categorías
		/*  this.tripCategory.setCellFactory(ComboBoxTableCell.forTableColumn(Category.values())); // Para mostrar categorías en un combo
	    this.tripCategory.setCellValueFactory(cellData -> 
	        new SimpleObjectProperty<>(cellData.getValue().getCategory()));  // Usando la categoría

	    // Configuración de las otras columnas
	    this.tripTitle.setCellValueFactory(cellData -> 
	        new SimpleObjectProperty<>(cellData.getValue().getTripType().getTitle()));  // Corregido para acceder a TripType
	    this.tripMaxPlaces.setCellValueFactory(cellData -> 
	        new SimpleIntegerProperty(cellData.getValue().getMaxPlaces()).asObject());*/

		this.tripTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		this.tripBooked.setCellValueFactory(new PropertyValueFactory<>("booked"));
		this.tripStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		this.tripDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		this.tripDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
		this.tripPlaces.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getPlaces()).asObject());

		this.tripComments.setCellValueFactory(new PropertyValueFactory<>("comments"));

	    configureActionColumn(action1, "Acción 1");
	    configureActionColumn(action2, "Acción 2");
	    configureActionColumn(action3, "Acción 3");


		// Cargar los viajes al inicio
		this.reloadTrip();
	}


	private void reloadTrip() {
		if (tripTable == null) {
			System.err.println("La tabla tripTable no está inicializada.");
			return;
		}

		// Desactivar la edición de la tabla mientras se cargan los datos
		tripTable.setEditable(false);
		tripTable.getItems().clear();  // Limpiar la tabla antes de añadir nuevos datos

		//Date datefrom = this.datefrom.getText();
		//Date dateto = this.dateto.getText();

		// Iniciar la consulta para obtener los viajes
		final ServiceQueryTrip queryTrip = new ServiceQueryTrip(null, null, null,null , null);

		// Configurar el manejador de éxito para cuando la consulta termine
		queryTrip.setOnSucceeded(event -> {
			List<Trip> trips = queryTrip.getValue();
			System.out.println("Fetched trips: " + (trips != null ? trips.size() : 0));

			if (trips != null) {
				ObservableList<Trip> observableTrips = FXCollections.observableArrayList(trips);
				tripTable.setItems(observableTrips);
			} else {
				System.err.println("No se encontraron viajes.");
			}
		});

		// Configurar el manejador de fallo para manejar errores en la consulta
		queryTrip.setOnFailed(new OnFailedEventHandler(
				ResourceManager.getInstance().getText("error.viewTrips.web.service")
				));

		// Iniciar la consulta
		queryTrip.start();
	}

	private void configureActionColumn(TableColumn<Trip, Void> column, String columnName) {
		// Configurar el título de la columna
		column.setText(columnName);

		// Establecer el factoría de celdas con botón
		column.setCellValueFactory(param -> null);
		column.setCellFactory(param -> new TableCell<Trip, Void>() {
			private final Button btnAction = new Button("•••"); // Icono de puntos

			{
				btnAction.setOnAction(event -> {
					Trip trip = getTableView().getItems().get(getIndex());
					showReservationPopup(trip);
				});

				// Hacer el botón más pequeño y centrado
				btnAction.setStyle("-fx-padding: 0 2; -fx-min-width: 25px;");
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				// Mostrar el botón solo si la fila tiene datos y el estado es RESERVA
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

		// Agregar botones personalizados
		ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
		ButtonType btnModificar = new ButtonType("Modificar", ButtonBar.ButtonData.OTHER);
		ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(btnAceptar, btnModificar, btnCancelar);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent()) {
			switch (result.get().getText()) {
			case "Modificar":
				System.out.println("Modificar reserva del viaje: " + trip.getId());
				break;
			case "Cancelar":
				System.out.println("Cancelar reserva del viaje: " + trip.getId());
				break;
			case "Aceptar":
				System.out.println("Aceptar reserva del viaje: " + trip.getId());
				break;
			}
		}
	}

}
