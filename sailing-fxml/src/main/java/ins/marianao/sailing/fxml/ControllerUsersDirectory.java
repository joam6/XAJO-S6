package ins.marianao.sailing.fxml;

// Importación de bibliotecas necesarias para el manejo de interfaces gráficas y lógica de negocio.
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User.Role;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceDeleteUser;
import ins.marianao.sailing.fxml.services.ServiceQueryUsers;
import ins.marianao.sailing.fxml.utils.ColumnButton;
import ins.marianao.sailing.fxml.utils.Formatters;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerUsersDirectory extends AbstractControllerPDF {
    // Definición de los elementos de la interfaz gráfica
    @FXML private BorderPane viewUsersDirectory;

    @FXML private ComboBox<Pair<String,String>> cmbRole;
    @FXML private TextField txtFullnameSearch;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Number> colIndex;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colFullname;
    @FXML private TableColumn<User, String> colPhone;
    @FXML private TableColumn<User, Boolean> colUpdate;
    @FXML private TableColumn<User, Boolean> colDelete;

    /**
     * Método que inicializa el controlador y configura los componentes de la interfaz.
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        super.initialize(url, resource);

        // Añadir listener al campo de texto de búsqueda, para recargar los usuarios al cambiar el texto
        this.txtFullnameSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                reloadUsers(); // Recarga la lista de usuarios cuando cambia el texto
            }
        });

        // Crear una lista de roles disponibles desde el modelo de usuario
        List<Pair<String,String>> roles = Stream.of(User.Role.values()).map(new Function<Role,Pair<String,String>>() {
            @Override
            public Pair<String,String> apply(Role t) {
                String key = t.name();
                return new Pair<String, String>(key, resource.getString("text.User."+key));
            }
        }).collect(Collectors.toList());

        // Convertir la lista de roles a un ObservableList que puede ser usado en la ComboBox
        ObservableList<Pair<String,String>> listRoles = FXCollections.observableArrayList(roles);
        listRoles.add(0, null); // Añadir un valor nulo al principio de la lista para la opción de todos los roles

        // Configurar la ComboBox con la lista de roles
        this.cmbRole.setItems(listRoles);
        this.cmbRole.setConverter(Formatters.getStringPairConverter("User"));

        // Añadir listener a la ComboBox para recargar los usuarios cuando se cambia el valor
        this.cmbRole.valueProperty().addListener(new ChangeListener<Pair<String,String>>() {
            @Override
            public void changed(ObservableValue<? extends Pair<String,String>> observable, Pair<String,String> oldValue, Pair<String,String> newValue) {
                reloadUsers();
            }
        });

        // Llamar a la función que recarga los usuarios al inicio
        this.reloadUsers();

        // Configuración de la tabla de usuarios para habilitar la edición
        this.usersTable.setEditable(true);
        this.usersTable.getSelectionModel().setCellSelectionEnabled(true);

        // Configuración de la columna de índice (número de fila)
        this.colIndex.setMinWidth(40);
        this.colIndex.setMaxWidth(60);
        
        // Cuenta numero de ROWs en la tabla
        this.colIndex.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<User, Number> usuari) {
                return new SimpleLongProperty(usersTable.getItems().indexOf(usuari.getValue()) + 1); // Muestra el número de fila
            }
        });

        // Configuración de la columna de rol
        this.colRole.setMinWidth(100);
        this.colRole.setMaxWidth(140);
        
        //DEFINIR SI EL USUARIO ES ADMIN / CLIENTE EN LA TABLA
        this.colRole.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cellData) {
                String key = cellData.getValue().getRole().toString();
                return new SimpleStringProperty(resource.getString("text.User."+key)); // Muestra el nombre del rol en texto
            }
        });
        this.colRole.setCellFactory(TextFieldTableCell.forTableColumn());
        this.colRole.setEditable(false); // No se puede editar directamente el rol

        // Configuración de la columna de nombre de usuario
        this.colUsername.setMinWidth(120);
        this.colUsername.setMaxWidth(160);
        this.colUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        this.colUsername.setCellFactory(TextFieldTableCell.forTableColumn());
        this.colUsername.setEditable(false);

        // Configuración de la columna de nombre completo (solo clientes)
        this.colFullname.setMinWidth(200);
        this.colFullname.setMaxWidth(Double.MAX_VALUE);
        
        //DEFINIR EL NOMBRE COMPLETO DEL USUARIO 
        this.colFullname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> user) {
                return new SimpleStringProperty(user.getValue().isAdmin() ? "" : ((Client) user.getValue()).getFullName()); // Solo mostrar nombre completo si no es administrador
            }
        });

        // Configuración de la columna de teléfono (solo clientes)
        this.colPhone.setMinWidth(140);
        this.colPhone.setMaxWidth(180);
        this.colPhone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> user) {
                return new SimpleStringProperty(user.getValue().isAdmin() ? "" : Formatters.getPhoneFormatter().toString(((Client) user.getValue()).getPhone())); // Formatear el teléfono
            }
        });

        // Configuración de la columna de actualización (botón)
        this.colUpdate.setMinWidth(50);
        this.colUpdate.setMaxWidth(70);
        this.colUpdate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<User, Boolean> cell) {
                return new SimpleBooleanProperty(false); // Establecer valor booleano por defecto
            }
        });
        this.colUpdate.setCellFactory(new ColumnButton<User, Boolean>(ResourceManager.getInstance().getText("fxml.text.viewUsers.col.update"),
                new Image(getClass().getResourceAsStream("resources/view-details.png"))) {
            @Override
            public void buttonAction(User user) {
                ResourceManager.getInstance().getMenuController().openUserForm(user); // Abre el formulario para actualizar el usuario
            }
        });

        // Configuración de la columna de eliminación (botón)
        this.colDelete.setMinWidth(50);
        this.colDelete.setMaxWidth(70);
        this.colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<User, Boolean> cell) {
                return new SimpleBooleanProperty(false); // Establecer valor booleano por defecto
            }
        });
        this.colDelete.setCellFactory(new ColumnButton<User, Boolean>(ResourceManager.getInstance().getText("fxml.text.viewUsers.col.delete"),
                new Image(getClass().getResourceAsStream("resources/recycle-bin.png"))) {
            @Override
            public void buttonAction(User usuari) {
                try {
                    boolean result = ControllerMenu.showConfirm(ResourceManager.getInstance().getText("fxml.text.viewUsers.delete.title"), 
                                                                ResourceManager.getInstance().getText("fxml.text.viewUsers.delete.text"));
                    if (result) {
                        deleteUsuari(usuari); // Eliminar el usuario si se confirma
                    }
                } catch (Exception e) {
                    ControllerMenu.showError(resource.getString("error.viewUsers.delete"), e.getMessage(), ExceptionUtils.getStackTrace(e)); // Manejo de errores
                }
            }
        });

        // Ajustar la altura de la vista según el tamaño de la ventana
        ResourceManager.getInstance().getStage().heightProperty().addListener(event -> {
            viewUsersDirectory.setPrefHeight(ResourceManager.getInstance().getStage().getHeight());
        });
    }

    /**
     * Método que elimina un usuario.
     */
    private void deleteUsuari(User user) throws Exception {
        final ServiceDeleteUser deleteUser = new ServiceDeleteUser(user);

        // Definir la acción cuando la eliminación se realiza correctamente
        deleteUser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                reloadUsers(); // Recargar los usuarios después de la eliminación
            }
        });

        // Definir la acción cuando la eliminación falla
        deleteUser.setOnFailed(new OnFailedEventHandler(ResourceManager.getInstance().getText("error.viewUsers.delete.web.service")));

        deleteUser.start(); // Iniciar el servicio de eliminación
    }

    /**
     * Método que recarga la lista de usuarios según los filtros aplicados.
     */
    private void reloadUsers() {
        Role[] roles = null;
        Pair<String,String> role = this.cmbRole.getValue();
        String search = this.txtFullnameSearch.getText();

        this.usersTable.setEditable(false); // Desactivar la edición de la tabla mientras se cargan los datos

        // Aplicar el filtro de roles si es necesario
        if (role != null) roles = new Role[] { Role.valueOf(role.getKey()) };

        final ServiceQueryUsers queryUsers = new ServiceQueryUsers(roles, search);

        // Iniciar la consulta para obtener los usuarios
        queryUsers.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                usersTable.setEditable(true); // Reactivar la edición cuando la consulta se complete
                usersTable.getItems().clear(); // Limpiar la tabla

                ObservableList<User> users = FXCollections.observableArrayList(queryUsers.getValue()); // Obtener los usuarios
                usersTable.setItems(users); // Establecer los usuarios en la tabla
            }
        });

        // Definir lo que sucede si la consulta falla
        queryUsers.setOnFailed(new OnFailedEventHandler(ResourceManager.getInstance().getText("error.viewUsers.web.service")));

        queryUsers.start(); // Iniciar la consulta
    }

    /**
     * Método que genera el contenido HTML para el reporte en PDF.
     */
    @Override
    protected String htmlContentToPDF() {
        List<User> users = this.usersTable.getItems();
        Pair<String,String> role = this.cmbRole.getValue();
        String search = this.txtFullnameSearch.getText();

        List<Pair<String,String>> filters = new LinkedList<>();
        if (role != null) filters.add(new Pair<>(ResourceManager.getInstance().getText("fxml.text.viewUsers.role"), role.getValue()));
        if (search != null && !search.isBlank()) filters.add(new Pair<>(ResourceManager.getInstance().getText("fxml.text.viewUsers.search.prompt"), "\""+search+"\""));

        return ResourceManager.getInstance().usersDirectoryHtml(users, filters); // Generar el HTML
    }

    /**
     * Método que define el título del documento PDF.
     */
    @Override
    protected String documentTitle() {
        return ResourceManager.getInstance().getText("fxml.text.viewUsers.report.title"); // Título del documento PDF
    }

    /**
     * Método que define el nombre del archivo PDF generado.
     */
    @Override
    protected String documentFileName() {
        return ResourceManager.FILE_REPORT_USERS; // Nombre del archivo PDF
    }
}
