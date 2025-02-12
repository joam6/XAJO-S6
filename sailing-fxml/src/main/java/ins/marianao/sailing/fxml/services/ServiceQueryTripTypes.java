package ins.marianao.sailing.fxml.services;

import java.util.LinkedList;
import java.util.List;
import jakarta.ws.rs.ProcessingException; 
import jakarta.ws.rs.client.Client; 
import jakarta.ws.rs.client.Invocation; 
import jakarta.ws.rs.client.ResponseProcessingException; 
import jakarta.ws.rs.client.WebTarget; 
import jakarta.ws.rs.core.GenericType; 
import jakarta.ws.rs.core.Response; 

import cat.institutmarianao.sailing.ws.model.TripType; 
import cat.institutmarianao.sailing.ws.model.TripType.Category;

import ins.marianao.sailing.fxml.manager.ResourceManager;

public class ServiceQueryTripTypes extends ServiceQueryBase<TripType> {
    public static final String PATH_REST_TRIP_TYPES = "trip_types"; 
    public static final String PATH_QUERY_ALL = "query/all"; 

    private Category[] categories; 
    private String title;          
    public ServiceQueryTripTypes(Category[] categories, String title) {
        this.categories = categories; 
        this.title = title;           
    }

    @Override
    protected List<TripType> customCall() throws Exception {
        Client client = ResourceManager.getInstance().getWebClient();

        WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url"))
                                    .path(PATH_REST_TRIP_TYPES)     
                                    .path(PATH_QUERY_ALL);           

        if (this.categories != null) {
            for (Category category : categories) {
                webTarget = webTarget.queryParam("categories", category.name()); 
            }
        }

        if (this.title != null && !this.title.isBlank()) {
            webTarget = webTarget.queryParam("title", title);
        }

        Invocation.Builder invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);

        List<TripType> tripTypes = new LinkedList<>();

        try {
            Response response = invocationBuilder.get();

            ResourceManager.getInstance().checkResponseErrors(response);

            tripTypes = response.readEntity(new GenericType<List<TripType>>() {});
        } catch (ResponseProcessingException e) {
            e.printStackTrace();
            throw new Exception(ResourceManager.getInstance().getText("error.service.response.processing") + " " + e.getMessage());
        } catch (ProcessingException e) {
            e.printStackTrace();
            throw new Exception(ResourceManager.getInstance().getText("error.service.processing") + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e; 
        }

        // Devuelve la lista de tipos de viaje obtenidos
        return tripTypes;
    }
}