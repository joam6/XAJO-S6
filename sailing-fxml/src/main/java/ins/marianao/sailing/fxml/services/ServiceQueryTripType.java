package ins.marianao.sailing.fxml.services;

import java.util.LinkedList;
import java.util.List;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class ServiceQueryTripType extends ServiceQueryBase<TripType> {
    
	
	public static final String PATH_REST_USERS = "TripType";
	private Category[] categories;     
	
    public ServiceQueryTripType() {
        super();
    }

    @Override
	protected List<TripType> customCall() throws Exception {
        // Obtención del cliente HTTP utilizando el gestor de recursos.
        Client client = ResourceManager.getInstance().getWebClient();

        // Construcción de la URL para la API añadiendo el path de los usuarios y el de consulta
		WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url"))
                                    .path("trip_type");     // Agrega la ruta de triptypes

		if (this.categories != null) {
			for (Category category : categories) {
				webTarget = webTarget.queryParam("Category", category.name()); // Agrega cada rol a la URL
			}
		}
		
		Invocation.Builder invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);

        // Lista que almacenará los usuarios obtenidos desde la API
		List<TripType> TripTypes = new LinkedList<TripType>();

		// Manejo de la solicitud y procesamiento de la respuesta
		try {
            // Realiza la solicitud GET a la API
			Response response = invocationBuilder.get(); 

            // Verifica si hay errores en la respuesta HTTP (por ejemplo, código 404 o 500)
			ResourceManager.getInstance().checkResponseErrors(response);

            // Deserializa la respuesta de la API en una lista de objetos TripType
			TripTypes = response.readEntity(new GenericType<List<TripType>>(){});

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
		return TripTypes;
	}
}

