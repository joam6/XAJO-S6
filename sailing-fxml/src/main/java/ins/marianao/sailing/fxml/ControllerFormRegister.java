package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ControllerFormRegister implements Initializable {
	@FXML private BorderPane viewFormRegister;
	
	@FXML private Label titleRegister;
	@FXML private SplitMenuButton menuUser;
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private PasswordField txtConfirmPassword;
	@FXML private TextField txtFullName;
	@FXML private TextField txtPhone;
	@FXML private Button btnRegister;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		
	}
	
	@FXML
	public void registerClick(ActionEvent event) {
		ResourceManager.getInstance().getMenuController().register(	this.txtUsername.getText(),
																	this.txtPassword.getText(),
																	this.txtConfirmPassword.getText(),
																	this.txtFullName.getText(),
																	this.txtPhone.getText());

	}
}

