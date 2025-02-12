package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import ins.marianao.sailing.fxml.services.ServiceSaveUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Inicialización del controlador
	}


	@FXML
	public void registerClick(ActionEvent event) {  
		String userType = this.menuUser.getText();  
		String username = this.txtUsername.getText();  
		String password = this.txtPassword.getText();  
		String confirmPassword = this.txtConfirmPassword.getText();  
		String fullName = this.txtFullName.getText();  
		String phoneText = this.txtPhone.getText().trim();
		if (!phoneText.isEmpty()) {
			try {
				Integer phone = Integer.parseInt(phoneText);
			} catch (NumberFormatException e) {
				System.out.println("Error: El valor ingresado no es un número válido.");
			}
		} else {
			System.out.println("Error: El campo está vacío.");
		}

		// Validación de campos  
		if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phoneText.isEmpty()) {  
			ControllerMenu.showError("Error", "Todos los campos son obligatorios.", null);  
			return;  
		}  

		if (!password.equals(confirmPassword)) {  
			ControllerMenu.showError("Error", "Las contraseñas no coinciden.", null);  
			return;  
		}  

	}

	private void showAlert(String title, String message, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}

