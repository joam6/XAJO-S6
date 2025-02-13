package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class ControllerTrip implements Initializable{
	
	@FXML private BorderPane viewTripForm;
	@FXML private SplitMenuButton categorySelector;
		@FXML private MenuItem Private;
		@FXML private MenuItem Group;
	@FXML private SplitMenuButton clientSelector;
	@FXML private SplitMenuButton statusSelector;
		@FXML private MenuItem Reserved;
		@FXML private MenuItem Done;
		@FXML private MenuItem Cancelled;
	@FXML private DatePicker fromDate;
	@FXML private DatePicker toDate;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ResourceManager.getInstance().getMenuController().trip(	this.fromDate.getValue(),
		//														this.toDate.getValue()
	}

}
