package ins.marianao.sailing.fxml;  


import java.net.URL;  
import java.util.ResourceBundle;  
import cat.institutmarianao.sailing.ws.model.Client;  
import cat.institutmarianao.sailing.ws.model.User;  
import ins.marianao.sailing.fxml.manager.ResourceManager;  
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

public class ControllerEditUser implements Initializable {  

	@FXML private Label titleRegister;
	@FXML private TextField etxtUsername;
	@FXML private PasswordField etxtPassword;
	@FXML private PasswordField etxtConfirmPassword;
	@FXML private TextField etxtFullName;
	@FXML private TextField etxtPhone;
	@FXML private Button btnEdit;


	public void initialize(URL url, ResourceBundle resource) {  
		// Inicialización del controlador  
	}  


	@FXML
	public void registerClick(ActionEvent event) {  
		String username = this.etxtUsername.getText();  
		String password = this.etxtPassword.getText();  
		String confirmPassword = this.etxtConfirmPassword.getText();  
		String fullName = this.etxtFullName.getText();  
		String phoneText = this.etxtPhone.getText().trim();
		if (!phoneText.isEmpty()) {
			try {
				Integer phone = Integer.parseInt(phoneText);
			} catch (NumberFormatException e) {
				System.out.println("Error: El valor ingresado no es un número válido.");
			}
		} else {
			System.out.println("Error: El campo está vacío.");
		}
		// Validation  
		if (!validateForm(username, password, confirmPassword, fullName, phoneText)) {  
			return; // Exit if validation fails  
		}  

		// Create Client object  
		Client newClient = new Client();  
		newClient.setUsername(username);  
		newClient.setPassword(password);  
		newClient.setFullName(fullName);  
		newClient.setRole(User.Role.CLIENT); // Ensure the role is CLIENT  

		// Validación de campos  
		if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phoneText.isEmpty()) {  
			ControllerMenu.showError("Error", "Todos los campos son obligatorios.", null);  
			return;  
		}  
		// Set phone number and handle potential NumberFormatException  
		try {  
			newClient.setPhone(Integer.parseInt(phoneText));  
		} catch (NumberFormatException e) {  
			showAlert("Error", "El número de teléfono debe ser un valor numérico.", AlertType.ERROR);  
			return;  
		}  

		if (!password.equals(confirmPassword)) {  
			ControllerMenu.showError("Error", "Las contraseñas no coinciden.", null);  
			return;  
		}  

		// Create and execute the service to save the user  
		try {  
			ServiceSaveUser serviceSaveUser = new ServiceSaveUser(newClient);  
			serviceSaveUser.setOnSucceeded(e -> showAlert("Éxito", "Usuario registrado correctamente", AlertType.INFORMATION));  
			serviceSaveUser.setOnFailed(e -> {  
				Throwable exception = e.getSource().getException();  
				showAlert("Error", "Hubo un error al registrar el usuario: " + exception.getMessage(), AlertType.ERROR);  
			});  
			serviceSaveUser.start(); // Execute the service  
		} catch (Exception e) {  
			e.printStackTrace();  
			showAlert("Error", "Error al registrar el usuario: " + e.getMessage(), AlertType.ERROR);  
		}  
	}  

	private boolean validateForm(String username, String password, String confirmPassword, String fullName, String phone) {  
		// Check for mandatory fields  
		if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {  
			showAlert("Error", "Todos los campos son obligatorios.", AlertType.ERROR);  
			return false;  
		}  
		// Confirm passwords match  
		if (!password.equals(confirmPassword)) {  
			showAlert("Error", "Las contraseñas no coinciden.", AlertType.ERROR);  
			return false;  
		}  
		return true; // Validation passed  
	}  


	private void showAlert(String title, String message, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}

