// Importaciones necesarias para manejar las peticiones REST usando Jakarta y otros componentes útiles.
package ins.marianao.sailing.fxml.services;

import java.util.LinkedList;
import java.util.List;

import jakarta.ws.rs.ProcessingException; // Maneja errores durante la solicitud REST
import jakarta.ws.rs.client.Client; // Cliente HTTP para realizar solicitudes REST
import jakarta.ws.rs.client.Invocation; // Invocador para las peticiones REST
import jakarta.ws.rs.client.ResponseProcessingException; // Excepción para problemas al procesar la respuesta
import jakarta.ws.rs.client.WebTarget; // Representa el destino de la solicitud REST
import jakarta.ws.rs.core.GenericType; // Tipo genérico para deserializar respuestas en tipos de Java
import jakarta.ws.rs.core.Response; // Contenedor de la respuesta HTTP

// Importación de modelos de usuario y roles que serán utilizados para manejar los datos.
import cat.institutmarianao.sailing.ws.model.User; 
import cat.institutmarianao.sailing.ws.model.User.Role;

// Importación del manejador de recursos, que proporciona configuraciones de la aplicación.
import ins.marianao.sailing.fxml.manager.ResourceManager;

public class ServiceQueryUsers extends ServiceQueryBase<User> {

    // Definición de la ruta para la API de usuarios.
	public static final String PATH_REST_USERS = "users"; // Ruta básica para los usuarios en la API.
	
    // Atributos de la clase que representan los filtros posibles para la consulta.
	private Role[] roles;      // Filtro de roles de los usuarios
	private String fullName;   // Filtro de nombre completo de los usuarios

    // Constructor que inicializa los filtros para los roles y el nombre completo
	public ServiceQueryUsers(Role[] roles, String fullName) {
		this.roles = roles;   // Asigna los roles proporcionados al atributo de la clase
		this.fullName = fullName; // Asigna el nombre completo proporcionado al atributo de la clase
	}

    // Método principal para hacer la llamada personalizada al servicio web.
	@Override
	protected List<User> customCall() throws Exception {
        // Obtención del cliente HTTP utilizando el gestor de recursos.
		Client client = ResourceManager.getInstance().getWebClient();

        // Construcción de la URL para la API añadiendo el path de los usuarios y el de consulta
		WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url"))
                                    .path(PATH_REST_USERS)      // Agrega la ruta base de usuarios
                                    .path(PATH_QUERY_ALL);      // Agrega la ruta de consulta de todos los usuarios

        // Si se proporcionan roles, se agregan como parámetros de la URL
		if (this.roles != null) {
			for (Role role : roles) {
				webTarget = webTarget.queryParam("roles", role.name()); // Agrega cada rol a la URL
			}
		}
				
        // Si se proporciona un nombre completo, se agrega como parámetro de la URL
		if (this.fullName != null && !this.fullName.isBlank()) {
			webTarget = webTarget.queryParam("fullName", fullName); // Agrega el nombre completo a la URL
		}

        // Construcción de la solicitud HTTP con autenticación y configuración adecuada
		Invocation.Builder invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);

        // Lista que almacenará los usuarios obtenidos desde la API
		List<User> users = new LinkedList<User>();

		// Manejo de la solicitud y procesamiento de la respuesta
		try {
            // Realiza la solicitud GET a la API
			Response response = invocationBuilder.get(); 

            // Verifica si hay errores en la respuesta HTTP (por ejemplo, código 404 o 500)
			ResourceManager.getInstance().checkResponseErrors(response);

            // Deserializa la respuesta de la API en una lista de objetos User
			users = response.readEntity(new GenericType<List<User>>(){});

		} catch (ResponseProcessingException e) {
			// Manejo de errores cuando ocurre un problema al procesar la respuesta
			e.printStackTrace();
			throw new Exception(ResourceManager.getInstance().getText("error.service.response.processing") + " " + e.getMessage());
		} catch (ProcessingException e) {
			// Manejo de errores cuando ocurre un problema al enviar la solicitud
			e.printStackTrace();
			throw new Exception(ResourceManager.getInstance().getText("error.service.processing") + " " + e.getMessage());
		} catch (Exception e) {
			// Captura de excepciones generales
			e.printStackTrace();
			throw e; // Lanza la excepción para que sea manejada por el código que llame a este servicio
		}
		
        // Devuelve la lista de usuarios obtenidos
		return users;
	}
}
