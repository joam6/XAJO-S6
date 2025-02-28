package ins.marianao.sailing.fxml.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.model.User;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class ServiceQueryTrip extends ServiceQueryBase<Trip> {
    
	public static final String PATH_REST_TRIP = "trips"; // Ruta básica para los usuarios en la API.
	public static final String PATH_REST_TRIP_TYPES = "triptypes";
	
	private List<User> clientSelector;
	private List<Category> categorySelector;
	private List<Status> statusSelector;
	private DatePicker fromDate;
	private DatePicker toDate;


	public ServiceQueryTrip(List<User> clientSelector, List<Category> categorySelector, List<Status> statusSelector,
			DatePicker fromDate, DatePicker toDate) {
		super();
		this.clientSelector = clientSelector;
		this.categorySelector = categorySelector;
		this.statusSelector = statusSelector;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}





	@Override
	protected List<Trip> customCall() throws Exception {
		// Obtención del cliente HTTP utilizando el gestor de recursos.
		Client client = ResourceManager.getInstance().getWebClient();

		WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url"))
				.path(PATH_REST_TRIP)      // Agrega la ruta base de usuarios
				.path(PATH_QUERY_ALL);      // Agrega la ruta de consulta de todos los usuarios
		

		
		if (this.clientSelector != null && !this.clientSelector.isEmpty()) {
			for (User user : this.clientSelector) {
				webTarget = webTarget.queryParam("client", user.getUsername());
			}
		}
	
		if (this.categorySelector != null && !this.categorySelector.isEmpty()) {
			for (Category category : this.categorySelector) {
				webTarget = webTarget.queryParam("category", category.name());
			}
		}
		
		if (this.statusSelector != null && !this.statusSelector.isEmpty()) {
			for (Status statu : this.statusSelector) {
				webTarget = webTarget.queryParam("status", statu.name());
			}
		}
		
		if (this.fromDate != null) {
			webTarget = webTarget.queryParam("datefrom", this.fromDate);
		}
		
		if (this.toDate!= null) {
            webTarget = webTarget.queryParam("dateto", this.toDate);
        }

		Invocation.Builder invocationBuilder = null;
		if (ResourceManager.getInstance().isAuthenticated()) invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);
		else invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);


		// Lista que almacenará los usuarios obtenidos desde la API
		List<Trip> Trip = new LinkedList<Trip>();

		// Manejo de la solicitud y procesamiento de la respuesta
		try {
			// Realiza la solicitud GET a la API
			Response response = invocationBuilder.get(); 

			// Verifica si hay errores en la respuesta HTTP (por ejemplo, código 404 o 500)
			ResourceManager.getInstance().checkResponseErrors(response);

			// Deserializa la respuesta de la API en una lista de objetos TripType
			Trip = response.readEntity(new GenericType<List<Trip>>(){});
			
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
		return Trip;
	}
	
}
