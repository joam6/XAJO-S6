package ins.marianao.sailing.fxml;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

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

public class ControllerTrip implements Initializable{
	
	@FXML private BorderPane viewTripForm;
	@FXML private TableView<Trip> tripsTable;
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
	public void initialize(URL location, ResourceBundle resources) {
		//ResourceManager.getInstance().getMenuController().trip(	this.fromDate.getValue(),
		//														this.toDate.getValue()
	}

}
