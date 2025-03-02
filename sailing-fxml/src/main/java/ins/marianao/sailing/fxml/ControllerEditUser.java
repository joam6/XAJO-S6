package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceSaveUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControllerEditUser implements Initializable {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtFullName;
    @FXML private TextField txtPhone;
    
	@FXML
	private BorderPane ViewEditUser;

    private ResourceBundle resource;

    @FXML
	public void initialize(URL location, ResourceBundle resources) {

		// Validaciones para el campo teléfono
    	txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtPhone.setText(oldValue);
			} else if (newValue.length() > 9) {
				txtPhone.setText(oldValue);
			}
		});


		String password = this.txtPassword.getText().trim();
		String confirmPassword = this.txtConfirmPassword.getText().trim();

		if (!password.equals(confirmPassword)) {
			ControllerMenu.showError("Error", "Las contraseñas no coinciden.");
			return;
		}


		// Verificamos si es admin
		boolean isAdmin = ResourceManager.getInstance().isAdmin();

		// Cargamos el comboBox con los roles disponibles
		List<Pair<String, String>> roles = Stream.of(User.Role.values())
				.filter(role -> isAdmin || role == Role.CLIENT)
				.map(role -> new Pair<>(role.name(), ResourceManager.getInstance().getText("text.User." + role.name())))
				.collect(Collectors.toList());
		ObservableList<Pair<String, String>> listRoles = FXCollections.observableArrayList(roles);


	}
    @FXML
    public void registerClick() {
        // Obtener valores de los campos
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String fullName = txtFullName.getText().trim();
        String phoneText = txtPhone.getText().trim();

        // Validar formulario
        if (!validateForm(username, password, confirmPassword, fullName, phoneText)) {
            return;
        }

        // Crear objeto Client
        Client newClient = new Client();
        newClient.setUsername(username);
        newClient.setPassword(password);
        newClient.setFullName(fullName);
        newClient.setRole(User.Role.CLIENT);

        try {
            // Convertir el número de teléfono a entero
            newClient.setPhone(Integer.parseInt(phoneText));
        } catch (NumberFormatException e) {
            showAlert("Error", "El número de teléfono debe ser un valor numérico.", AlertType.ERROR);
            return;
        }

        // Ejecutar servicio para guardar el usuario
        executeSaveUserService(newClient);
    }

    private boolean validateForm(String username, String password, String confirmPassword, String fullName, String phone) {
        // Verificar campos obligatorios
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "Todos los campos son obligatorios.", AlertType.ERROR);
            return false;
        }

        // Verificar coincidencia de contraseñas
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Las contraseñas no coinciden.", AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void executeSaveUserService(Client client) {
        try {
            ServiceSaveUser serviceSaveUser = new ServiceSaveUser(client);
            serviceSaveUser.setOnSucceeded(e -> showAlert("Éxito", "Usuario registrado correctamente.", AlertType.INFORMATION));
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

    private void showAlert(String titleKey, String messageKey, AlertType type) {
        // Obtener mensajes internacionales desde el ResourceBundle
        String title = resource.getString(titleKey);
        String message = resource.getString(messageKey);

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}