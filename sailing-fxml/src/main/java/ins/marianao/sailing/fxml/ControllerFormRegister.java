package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;
<<<<<<< HEAD
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
=======

>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git
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

<<<<<<< HEAD
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
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String fullName = txtFullName.getText();
        String phone = txtPhone.getText();
=======
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
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
        // Validación del formulario
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "Todos los campos son obligatorios", AlertType.ERROR);
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Las contraseñas no coinciden", AlertType.ERROR);
            return;
        }
=======
	    // Validación de campos  
	    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {  
	        ControllerMenu.showError("Error", "Todos los campos son obligatorios.", null);  
	        return;  
	    }  

	    if (!password.equals(confirmPassword)) {  
	        ControllerMenu.showError("Error", "Las contraseñas no coinciden.", null);  
	        return;  
	    }  
>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git

<<<<<<< HEAD
        Client newClient = new Client();
        newClient.setUsername(username);
        newClient.setPassword(password);
        newClient.setFullName(fullName);
        newClient.setRole(User.Role.CLIENT); // Asegurar que el rol es CLIENT
        try {
            newClient.setPhone(Integer.parseInt(phone));
        } catch (NumberFormatException e) {
            showAlert("Error", "El número de teléfono debe ser un valor numérico.", AlertType.ERROR);
            return;
        }

        // Crear el servicio para guardar el usuario sin autenticación
        try {
            ServiceSaveUser serviceSaveUser = new ServiceSaveUser(newClient); // El segundo parámetro indica si se necesita autenticación
            serviceSaveUser.setOnSucceeded(e -> {
                showAlert("Éxito", "Usuario registrado correctamente", AlertType.INFORMATION);
            });
            serviceSaveUser.setOnFailed(e -> {
                Throwable exception = e.getSource().getException();
                showAlert("Error", "Hubo un error al registrar el usuario: " + exception.getMessage(), AlertType.ERROR);
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
=======
	    // Llamada al método de registro  
	    ResourceManager.getInstance().getMenuController().register(userType, username, password, confirmPassword, fullName, phone);  
	} 
}

>>>>>>> branch 'master' of https://github.com/joam6/XAJO-S6.git
