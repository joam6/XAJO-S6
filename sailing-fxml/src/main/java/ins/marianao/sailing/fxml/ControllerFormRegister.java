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
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String fullName = txtFullName.getText();
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