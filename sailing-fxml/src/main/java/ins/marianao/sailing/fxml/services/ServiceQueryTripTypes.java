package ins.marianao.sailing.fxml.services;

import java.util.LinkedList;
import java.util.List;
import cat.institutmarianao.sailing.ws.model.TripType;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class ServiceQueryTripTypes extends ServiceQueryBase<TripType> {
    
    public ServiceQueryTripTypes() {
        super();
    }

    @Override
    protected List<TripType> customCall() throws Exception {
        WebTarget webTarget = ClientBuilder.newClient().target("http://localhost:8443/api/trips");
        Invocation.Builder invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);
        List<TripType> tripTypes = new LinkedList<>();

        try {
            Response response = invocationBuilder.get();
            ResourceManager.getInstance().checkResponseErrors(response);
            tripTypes = response.readEntity(new GenericType<List<TripType>>(){});
        } catch (ProcessingException e) {
            e.printStackTrace();
            throw new Exception(ResourceManager.getInstance().getText("error.service.processing") + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return tripTypes;
    }
}
