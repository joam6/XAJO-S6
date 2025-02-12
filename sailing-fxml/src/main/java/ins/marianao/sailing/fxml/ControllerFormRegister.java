package ins.marianao.sailing.fxml;  

<<<<<<< HEAD
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
=======
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

public class ControllerFormRegister implements Initializable {  
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

    @FXML private BorderPane viewFormRegister;  
    @FXML private Label titleRegister;  
    @FXML private SplitMenuButton menuUser;  
    @FXML private TextField txtUsername;  
    @FXML private PasswordField txtPassword;  
    @FXML private PasswordField txtConfirmPassword;  
    @FXML private TextField txtFullName;  
    @FXML private TextField txtPhone;  
    @FXML private Button btnRegister;  

<<<<<<< HEAD
	@FXML private BorderPane viewFormRegister;
=======
    @Override  
    public void initialize(URL url, ResourceBundle resource) {  
        // Inicialización del controlador  
    }  
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
	@FXML private Label titleRegister;
	@FXML private SplitMenuButton menuUser;
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private PasswordField txtConfirmPassword;
	@FXML private TextField txtFullName;
	@FXML private TextField txtPhone;
	@FXML private Button btnRegister;
=======
    @FXML  
    public void registerClick(ActionEvent event) {  
        // Retrieve user inputs  
        String userType = this.menuUser.getText();  
        String username = this.txtUsername.getText();  
        String password = this.txtPassword.getText();  
        String confirmPassword = this.txtConfirmPassword.getText();  
        String fullName = this.txtFullName.getText();  
        String phone = this.txtPhone.getText();  
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
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
=======
        // Validation  
        if (!validateForm(username, password, confirmPassword, fullName, phone)) {  
            return; // Exit if validation fails  
        }  

        // Create Client object  
        Client newClient = new Client();  
        newClient.setUsername(username);  
        newClient.setPassword(password);  
        newClient.setFullName(fullName);  
        newClient.setRole(User.Role.CLIENT); // Ensure the role is CLIENT  
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
		// Validación de campos  
		if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phoneText.isEmpty()) {  
			ControllerMenu.showError("Error", "Todos los campos son obligatorios.", null);  
			return;  
		}  
=======
        // Set phone number and handle potential NumberFormatException  
        try {  
            newClient.setPhone(Integer.parseInt(phone));  
        } catch (NumberFormatException e) {  
            showAlert("Error", "El número de teléfono debe ser un valor numérico.", AlertType.ERROR);  
            return;  
        }  
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
		if (!password.equals(confirmPassword)) {  
			ControllerMenu.showError("Error", "Las contraseñas no coinciden.", null);  
			return;  
		}  
=======
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
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
	}

	private void showAlert(String title, String message, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}

=======
    private void showAlert(String title, String message, AlertType type) {  
        Alert alert = new Alert(type);  
        alert.setTitle(title);  
        alert.setHeaderText(null);  
        alert.setContentText(message);  
        alert.showAndWait();  
    }  
}
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git
