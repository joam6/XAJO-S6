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
import javafx.scene.control.DatePicker;
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

public class ControllerBooking implements Initializable{
	
	@FXML private BorderPane viewBookingForm;
	
	@FXML private TableView<Booking > bookingTable;
	@FXML private TableColumn<Trip, Category> tCategory;
	@FXML private TableColumn<TripType, String> tTitle;
	@FXML private TableColumn<Booking , Integer> tMax;
	@FXML private TableColumn<TripType, Integer> tBooked;
	@FXML private TableColumn<Booking , String> tStatus; // No aparece en ninguna tabla
	@FXML private TableColumn<Trip, Date> tDate;
	@FXML private TableColumn<TripType, String> tDeparture;
	@FXML private TableColumn<Trip, Integer> tPlaces;
	@FXML private TableColumn<Booking , String> tComments; // No aparece en ninguna tabla

	
	


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ResourceManager.getInstance().getMenuController().trip(	this.fromDate.getValue(),
		//														this.toDate.getValue()
	}

}
