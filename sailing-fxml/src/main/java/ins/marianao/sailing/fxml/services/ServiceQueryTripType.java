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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ServiceQueryTripType extends ServiceQueryBase<TripType> {
	
	
	public static final String PATH_REST_TRIP_TYPES = "triptypes"; // Ruta básica para los usuarios en la API.
	
	private Category category;
	private Double priceFrom;
	private Double priceTo;
	private Integer maxPlacesFrom;
	private Integer maxPlacesTo;
	private Integer durationFrom;
	private Integer durationTo;
	
    public ServiceQueryTripType() {
        super();
    }


    @Override
	protected List<TripType> customCall() throws Exception {
        // Obtención del cliente HTTP utilizando el gestor de recursos.
        Client client = ResourceManager.getInstance().getWebClient();

		WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url"))
                .path(PATH_REST_TRIP_TYPES)      // Agrega la ruta base de usuarios
                .path(PATH_QUERY_ALL);      // Agrega la ruta de consulta de todos los usuarios

		

		if (this.category != null) {
				webTarget = webTarget.queryParam("category", category); // Agrega cada rol a la URL
			
		}
		
		if (this.priceFrom != null) {
	        webTarget = webTarget.queryParam("priceFrom", this.priceFrom);
	    }

	    if (this.priceTo != null) {
	        webTarget = webTarget.queryParam("priceTo", this.priceTo);
	    }

	    if (this.maxPlacesFrom != null) {
	        webTarget = webTarget.queryParam("maxPlacesFrom", this.maxPlacesFrom);
	    }

	    if (this.maxPlacesTo != null) {
	        webTarget = webTarget.queryParam("maxPlacesTo", this.maxPlacesTo);
	    }

	    if (this.durationFrom != null) {
	        webTarget = webTarget.queryParam("durationFrom", this.durationFrom);
	    }

	    if (this.durationTo != null) {
	        webTarget = webTarget.queryParam("durationTo", this.durationTo);
	    }

		
		Invocation.Builder invocationBuilder = null;
		if (ResourceManager.getInstance().isAuthenticated()) invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);
		else invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		
		

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

