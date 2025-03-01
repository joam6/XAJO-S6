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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import javafx.collections.FXCollections;

public class ControllerFormRegister {

	@FXML
	private ComboBox<Pair<String, String>> menuUser;

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtConfirmPassword;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private TextField txtFullName;

	@FXML
	private TextField txtPhone;

	@FXML
	private BorderPane viewFormRegister;

	boolean isNewUser;

	public void prepareForNewUser() {
		isNewUser = true;
	}

	@FXML
	public void registerClick(ActionEvent event) {
	    Pair<String, String> selectedRole = this.menuUser.getValue();
	    User.Role role = null;

	    if (selectedRole != null) {
	        try {
	            role = User.Role.valueOf(selectedRole.getKey());
	        } catch (IllegalArgumentException e) {
	            ControllerMenu.showError("Error", "Rol inválido.");
	            return;
	        }
	    }

	    // Validación de los campos según el rol seleccionado
	    if (role == User.Role.CLIENT) {
	        String phoneText = this.txtPhone.getText().trim();
	        if (phoneText.isEmpty()) {
	            ControllerMenu.showError("Error", "El número de teléfono no puede estar vacío.");
	            return;
	        }

	        Integer phoneNumber = null;

	        try {
	            // Intentar convertir el texto a Integer
	            phoneNumber = Integer.parseInt(phoneText);

	            // Verificar que el número no tenga más de 9 dígitos
	            if (phoneText.length() > 9) {
	                ControllerMenu.showError("Error", "El número de teléfono no puede tener más de 9 dígitos.");
	                return;
	            }
	        } catch (NumberFormatException e) {
	            ControllerMenu.showError("Error", "Número de teléfono no válido.");
	            return;
	        }

	        // Si todas las validaciones pasaron, se puede continuar con el registro
	    }

	    // Obtener el nombre completo
	    String fullName = this.txtFullName.getText().trim();
	    if (fullName.isEmpty()) {
	        ControllerMenu.showError("Error", "El nombre completo no puede estar vacío.");
	        return;
	    }

	    // Verificar que las contraseñas coinciden
	    String password = this.txtPassword.getText().trim();
	    String confirmPassword = this.txtConfirmPassword.getText().trim();

	    if (!password.equals(confirmPassword)) {
	        ControllerMenu.showError("Error", "Las contraseñas no coinciden.");
	        return;
	    }

	    // Si las validaciones son correctas, registramos al usuario
	    registerUser(txtUsername.getText(),password,fullName,role);
	}

	private void registerUser(String username, String password, String fullName, User.Role role) {
	    // Crear un objeto User con los datos del formulario
	    User user = Client.builder()
	            .username(username)
	            .password(password)
	            .fullName(fullName)
	            .role(role)    // Usamos el `role` que viene como parámetro
	            .build();

	    try {
	        // Crear una instancia de ServiceSaveUser, pasando el usuario como parámetro
	        ServiceSaveUser serviceSaveUser = new ServiceSaveUser(user);

	        // Ejecutar la solicitud para guardar el usuario

	        // Aquí podrías manejar la respuesta si es necesario

	        // Si todo va bien, mostrar un mensaje de éxito

	    } catch (Exception e) {
	        // Si ocurre un error, mostrar el mensaje de error
	        ControllerMenu.showError("Error", "No se pudo registrar el usuario: " + e.getMessage());
	    }
	}

	@FXML
	public void initialize() {

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


		// Asignamos los elementos al ComboBox
		// Configuramos el listener para cambios en el ComboBox
		this.menuUser.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				User.Role selectedRoleValue = User.Role.valueOf(newVal.getKey());
				boolean isClient = selectedRoleValue == User.Role.CLIENT;
				txtFullName.setDisable(!isClient);
			}
		});


		// Configuramos el listener para cambios en el ComboBox
		this.menuUser.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				User.Role selectedRoleValue = User.Role.valueOf(newVal.getKey());
				boolean isClient = selectedRoleValue == User.Role.CLIENT;
				txtFullName.setDisable(!isClient);
			}
		});
	}
}
