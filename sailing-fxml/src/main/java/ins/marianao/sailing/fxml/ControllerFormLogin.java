package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ControllerFormLogin implements Initializable {
	@FXML private BorderPane viewLoginForm;

	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private Button btnLogin;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		
	}

	/**
	 * Called when btnLogin button is fired.
	 *
	 * @param event the action event.
	 */
	@FXML
	public void loginClick(ActionEvent event) {

		ResourceManager.getInstance().getMenuController().login(this.txtUsername.getText(),
																this.txtPassword.getText());
	}
}
