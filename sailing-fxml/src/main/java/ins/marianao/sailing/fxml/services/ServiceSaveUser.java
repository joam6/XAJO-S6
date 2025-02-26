package ins.marianao.sailing.fxml.services;

import cat.institutmarianao.sailing.ws.model.User;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceSaveBase.Method;

public class ServiceSaveUser extends ServiceSaveBase<User> {

    private static final String PATH_SAVE_USER = "user";

    /**
     * Constructor principal para crear una solicitud de registro de usuario.
     *
     * @param user El objeto User (o Client) que se va a registrar.
     * @throws Exception Si ocurre algún error durante la inicialización.
     */
    public ServiceSaveUser(User user) throws Exception {
        super(
            user, // El objeto User a enviar
            User.class, // La clase del objeto esperado como respuesta
            new String[] {
                ServiceQueryUsers.PATH_REST_USERS, // Primer segmento de la ruta
                PATH_SAVE_USER, // Segundo segmento de la ruta
                user.getUsername() // Tercer segmento (nombre de usuario)
            },
            Method.POST, // Método HTTP utilizado
            false // Indica que esta solicitud NO requiere autenticación
        );

        if (user == null || user.getUsername() == null) {
            throw new Exception("El usuario o el nombre de usuario no pueden ser nulos.");
        }
    }
}