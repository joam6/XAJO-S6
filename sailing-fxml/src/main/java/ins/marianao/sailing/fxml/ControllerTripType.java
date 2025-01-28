package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ControllerTripType extends ControllerMenu {

	 // Text fields
    @FXML private TextField pricefrom;
    @FXML private TextField priceto;
    @FXML private TextField placeto;
    @FXML private TextField placeform;
    @FXML private TextField durationform;
    @FXML private TextField durationto;
    
    @FXML private SplitMenuButton all;
    
    
	@FXML private MenuBar menuBar;
	@FXML private Menu mnTrips;
	@FXML private MenuItem mnItBooking;
	@FXML private MenuItem mnItTrips;
	@FXML private Menu mnUsers;
	@FXML private MenuItem mnItAddUser;
	@FXML private MenuItem mnItUserDirectory;
	@FXML private MenuItem mnItImport;
	@FXML private MenuItem mnItExport;
	@FXML private Menu mnProfile;
	@FXML private MenuItem mnItEditProfile;
	@FXML private MenuItem mnItLogoff;
	@FXML private Menu mnLogin;
	@FXML private MenuItem mnItLogin;
	@FXML private MenuItem mnItRegister;



	  @Override
	    public void initialize(URL location, ResourceBundle resources) {
			this.logOff();

	    }
	  
		private void logOff() {
			try {
				ResourceManager.getInstance().setCurrentUser(null); // Logoff

				// TODO Open trip types view

				this.logoffMenu();
				
			} catch (Exception e) {
				ControllerMenu.showError(ResourceManager.getInstance().getText("error.menu.view.opening"), e.getMessage(), ExceptionUtils.getStackTrace(e));
			}
		}
		
		private void logoffMenu() {
			//this.mnTrips.setVisible(false);
			this.mnTrips.setVisible(false);
			this.mnUsers.setVisible(false);
			this.mnProfile.setVisible(false);
			this.mnLogin.setVisible(true);
		}
		

}
