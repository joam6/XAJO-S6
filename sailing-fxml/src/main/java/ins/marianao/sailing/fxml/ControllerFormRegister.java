package ins.marianao.sailing.fxml;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceSaveUser;
import ins.marianao.sailing.fxml.utils.Formatters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Pair;

public class ControllerFormRegister {

    @FXML
    private ComboBox<Pair<String, String>> menuUser;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtPhone;

    private boolean isNewUser = true;

    public void prepareForNewUser() {
        this.isNewUser = true;
    }

    @FXML
    public void registerClick(ActionEvent event) {
        // Obtener el rol seleccionado


        // Validación del nombre completo
        String fullName = this.txtFullName.getText().trim();
        if (fullName.isEmpty()) {
            showError("Error", "El nombre completo no puede estar vacío.");
            return;
        }

        // Validación de contraseñas
        String password = this.txtPassword.getText().trim();
        String confirmPassword = this.txtConfirmPassword.getText().trim();
        if (!password.equals(confirmPassword)) {
            showError("Error", "Las contraseñas no coinciden.");
            return;
        }

        // Validación del nombre de usuario
        String username = this.txtUsername.getText().trim();
        if (username.isEmpty()) {
            showError("Error", "El nombre de usuario no puede estar vacío.");
            return;
        }

        // Registrar al usuario
        try {
            registerUser(username, password, fullName, null);
        } catch (Exception e) {
            showError("Error", "No se pudo registrar el usuario: " + e.getMessage());
        }
    }

    private void registerUser(String username, String password, String fullName, User.Role role) throws Exception {
        // Crear el objeto User utilizando el patrón Builder
        Client user = Client.builder()
                .username(username)
                .password(password)
                .fullName(fullName)
                .role(role)
                .build();

        // Si el rol es CLIENT, validar y establecer el número de teléfono
        if (role == Role.CLIENT) {
            String phoneText = this.txtPhone.getText().trim();
            if (phoneText.isEmpty()) {
                throw new IllegalArgumentException("El número de teléfono no puede estar vacío para clientes.");
            }

            try {
                int phoneNumber = Integer.parseInt(phoneText);
                if (phoneText.length() > 9) {
                    throw new IllegalArgumentException("El número de teléfono no puede tener más de 9 dígitos.");
                }
                user.setPhone(phoneNumber);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Número de teléfono no válido.");
            }
        }

        // Ejecutar el servicio para guardar el usuario
        ServiceSaveUser serviceSaveUser = new ServiceSaveUser(user);
        serviceSaveUser.setOnSucceeded(e -> showSuccessMessage("Éxito", "Usuario registrado correctamente."));
        serviceSaveUser.setOnFailed(e -> showError("Error", "No se pudo registrar el usuario: " + e.getSource().getException().getMessage()));
        serviceSaveUser.start();
    }

    @FXML
    public void initialize() {
        // Configuración del ComboBox de roles
        boolean isAdmin = ResourceManager.getInstance().isAdmin();


        // Validación para el campo teléfono
        this.txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.txtPhone.setText(oldValue); // Permitir solo números
            } else if (newValue.length() > 9) {
                this.txtPhone.setText(oldValue); // Limitar a 9 dígitos
            }
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}