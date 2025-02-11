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
	    String userType = this.menuUser.getText();  
	    String username = this.txtUsername.getText();  
	    String password = this.txtPassword.getText();  
	    String confirmPassword = this.txtConfirmPassword.getText();  
	    String fullName = this.txtFullName.getText();  
	    String phone = this.txtPhone.getText();  

	    // Validación de campos  
	    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {  
	        ControllerMenu.showError("Error", "Todos los campos son obligatorios.", null);  
	        return;  
	    }  

	    if (!password.equals(confirmPassword)) {  
	        ControllerMenu.showError("Error", "Las contraseñas no coinciden.", null);  
	        return;  
	    }  

	    // Llamada al método de registro  
	    ResourceManager.getInstance().getMenuController().register(userType, username, password, confirmPassword, fullName, phone);  
	} 
}

