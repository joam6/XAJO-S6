package ins.marianao.sailing.fxml;  

import java.net.URL;  
import java.sql.Date;  
import java.util.ResourceBundle;  

import cat.institutmarianao.sailing.ws.model.Booking;  
import cat.institutmarianao.sailing.ws.model.Trip;  
import cat.institutmarianao.sailing.ws.model.TripType;  
import cat.institutmarianao.sailing.ws.model.TripType.Category;  
import ins.marianao.sailing.fxml.manager.ResourceManager;  
import javafx.fxml.FXML;  
import javafx.fxml.Initializable;  
import javafx.scene.control.TableColumn;  
import javafx.scene.control.TableView;  
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;  

public class ControllerBooking implements Initializable {  

	@FXML private VBox viewBookingForm; // Correct, matches FXML  

    @FXML private TableView<Booking> bookingTable; // Table to display booking information.  
    @FXML private TableColumn<Trip, Category> tCategory; // Column for Trip Category.  
    @FXML private TableColumn<TripType, String> tTitle; // Column for Trip Title.  
    @FXML private TableColumn<Booking, Integer> tMax; // Column for Maximum capacity.  
    @FXML private TableColumn<TripType, Integer> tBooked; // Column for Booked spots.  
    @FXML private TableColumn<Booking, String> tStatus; // Column for Booking Status.  
    @FXML private TableColumn<Trip, Date> tDate; // Column for Trip Date.  
    @FXML private TableColumn<TripType, String> tDeparture; // Column for Departure details.  
    @FXML private TableColumn<Trip, Integer> tPlaces; // Column for available places.  
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
        // ResourceManager.getInstance().getMenuController().trip(this.fromDate.getValue(), this.toDate.getValue());  
    }  
}  