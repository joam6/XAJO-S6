package ins.marianao.sailing.fxml;

import java.net.URL;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import cat.institutmarianao.sailing.ws.model.Booking;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceQueryTripType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerBooking implements Initializable {

	@FXML
	private VBox viewBookingForm; // Correct, matches FXML

	@FXML private TableView<Booking> bookingTable; // Table to display booking information.
	@FXML private TableColumn<TripType, Category> tCategory; // Column for Trip Category.
	@FXML private TableColumn<TripType, String> tTitle; // Column for Trip Title.
	@FXML private TableColumn<TripType, Integer> tMax; // Column for Maximum capacity.
	@FXML private TableColumn<TripType, Integer> tBooked; // Column for Booked spots.
	@FXML private TableColumn<Booking, String> tStatus; // Column for Booking Status.
	@FXML private TableColumn<Trip, Date> tDate; // Column for Trip Date.
	@FXML private TableColumn<TripType, String> tDeparture; // Column for Departure details.
	@FXML private TableColumn<TripType, Integer> tPlaces; // Column for available places.
	@FXML private TableColumn<Booking, String> tComments; // Column for comments related to bookings.

	@FXML
	private void bookingMenuClick() {
		if (viewBookingForm instanceof VBox) {
			VBox vb = (VBox) viewBookingForm; // Safe cast now, if this is going through ControllerTrip
			// Further processing for the VBox
		} else {
			System.err.println("viewBookingForm is not a VBox! Current type: " + viewBookingForm.getClass());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize method where you can set up data bindings, etc.
		// Example: Load data into bookingTable or set up listeners
		// ResourceManager.getInstance().getMenuController().trip(this.fromDate.getValue(),
		// this.toDate.getValue());

		this.tCategory.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory()));

		this.tTitle.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//reloadBooking(); // Recarga la lista de usuarios cuando cambia el texto
			}
		});

		this.tMax.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, Integer>, ObservableValue<Integer>>() {  
			@Override  
			public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TripType, Integer> cell) {  
				TripType tripType = cell.getValue();  
				return new SimpleIntegerProperty(tripType.getMaxPlaces()).asObject(); // Usa asObject() para convertir a ObservableValue<Integer>  
			}  
		}); 

		this.tBooked.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, Integer>, ObservableValue<Integer>>() {  
			@Override  
			public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TripType, Integer> cell) {  
				TripType tripType = cell.getValue();  
				return new SimpleIntegerProperty(tripType.getMaxPlaces()).asObject(); // Usa asObject() para convertir a ObservableValue<Integer>  
			}  
		});  
		
		this.tDate.setCellValueFactory(new PropertyValueFactory<>("date"));

		
		this.tDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
		this.tDeparture.setCellFactory(TextFieldTableCell.forTableColumn());
		
		this.tPlaces.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, Integer>, ObservableValue<Integer>>() {  
			@Override  
			public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TripType, Integer> cell) {  
				TripType tripType = cell.getValue();  
				return new SimpleIntegerProperty(tripType.getMaxPlaces()).asObject(); // Usa asObject() para convertir a ObservableValue<Integer>  
			}  
		});
		
		this.tComments.setCellValueFactory(new PropertyValueFactory<>("comments"));
		this.tComments.setCellFactory(TextFieldTableCell.forTableColumn());
	}
	
	/*private void reloadBooking() {  
	    List<Category> selectedCategories = new LinkedList<>();  
	    Pair<String, String> selectedCategoryPair = categorySelect.getValue(); // Supongo que categorySelect está definido  

	    // Verificar que bookingTable está inicializada  
	    if (bookingTable == null) {  
	        System.err.println("La tabla bookingTable no está inicializada.");  
	        return;  
	    }  

	    // Desactivar la edición de la tabla mientras se cargan los datos  
	    bookingTable.setEditable(false);  
	    bookingTable.getItems().clear();  // Limpiar la tabla antes de añadir nuevos datos  

	    // Preparar el filtro de categorías  
	    if (selectedCategoryPair != null && selectedCategoryPair.getKey() != null) {  
	        try {  
	            selectedCategories.add(Category.valueOf(selectedCategoryPair.getKey())); // Agregar la categoría seleccionada  
	        } catch (IllegalArgumentException e) {  
	            System.err.println("Categoría no válida: " + selectedCategoryPair.getKey());  
	            return; // Salir si la categoría no es válida  
	        }  
	    }  

	     

	    // Iniciar la consulta para obtener las reservas  
	    final ServiceQueryTripType queryBookings = new ServiceQueryTripType(selectedCategories, null, null, null, null, null, null);  

	    // Configurar el manejador de éxito para cuando la consulta termine  
	    queryBookings.setOnSucceeded(event -> {  
	        List<TripType> bookings = queryBookings.getValue(); // Cambia esto si el servicio de consulta devuelve otro tipo de objeto  
	        System.out.println("Fetched bookings: " + (bookings != null ? bookings.size() : 0));  

	        if (bookings != null) {  
	            ObservableList<TripType> observableBookings = FXCollections.observableArrayList(bookings);  
	            bookingTable.setItems(observableBookings);  
	        } else {  
	            System.err.println("No se encontraron reservas.");  
	        }  
	    });  

	    // Configurar el manejador de fallo para manejar errores en la consulta  
	    queryBookings.setOnFailed(new OnFailedEventHandler(  
	            ResourceManager.getInstance().getText("error.viewBookings.web.service")  
	    ));  

	    // Iniciar la consulta  
	    queryBookings.start();  
	}  */
}