package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

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
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String phone = txtPhone.getText();

        // Validación del formulario
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "Todos los campos son obligatorios", AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Las contraseñas no coinciden", AlertType.ERROR);
            return;
        }
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setPhone(phone);

        // Crear el servicio para guardar el usuario
        try {
            ServiceSaveUser serviceSaveUser = new ServiceSaveUser(newUser);

            // Enviar la solicitud en un hilo separado
            serviceSaveUser.setOnSucceeded(e -> {
                // El usuario fue registrado correctamente
                showAlert("Éxito", "Usuario registrado correctamente", AlertType.INFORMATION);
                // Aquí puedes redirigir a otra vista si lo deseas
            });

            serviceSaveUser.setOnFailed(e -> {
                // Hubo un error al registrar al usuario
                showAlert("Error", "Hubo un error al registrar el usuario", AlertType.ERROR);
            });

            serviceSaveUser.start(); // Ejecutar el servicio

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al registrar el usuario: " + e.getMessage(), AlertType.ERROR);
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

