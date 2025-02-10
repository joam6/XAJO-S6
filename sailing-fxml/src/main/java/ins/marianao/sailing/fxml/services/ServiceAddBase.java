package ins.marianao.sailing.fxml.services;  

import ins.marianao.sailing.fxml.manager.ResourceManager;  
import jakarta.ws.rs.ProcessingException;  // Deja esta importación  
import jakarta.ws.rs.client.Client;  
import jakarta.ws.rs.client.Invocation;  
import jakarta.ws.rs.client.ResponseProcessingException;  
import jakarta.ws.rs.client.WebTarget;  
import jakarta.ws.rs.core.Response;  
import javafx.concurrent.Service;  
import javafx.concurrent.Task;  
import com.fasterxml.jackson.databind.ObjectMapper; // Import Jackson ObjectMapper  
import java.io.IOException;  

public abstract class ServiceAddBase<T> extends Service<Void> {  

    protected T entity;  
    protected String[] path;  
    private final ObjectMapper objectMapper = new ObjectMapper(); // Create ObjectMapper instance  

    public ServiceAddBase(T entity, String[] path) throws Exception {  
        if (entity == null) throw new Exception(ResourceManager.getInstance().getText("error.service.entity.null"));  
        if (path == null || path.length == 0) throw new Exception(ResourceManager.getInstance().getText("error.service.path.null"));  
        this.entity = entity;  
        this.path = path;  
    }  

    @Override  
    protected Task<Void> createTask() {  
        return new Task<Void>() {  
            @Override  
            protected Void call() throws Exception {  
                Client webClient = ResourceManager.getInstance().getWebClient();  
                WebTarget webTarget = webClient.target(ResourceManager.getInstance().getParam("web.service.host.url"));  

                // Construir la URL con los diferentes segmentos de ruta  
                for (String part : path) {  
                    webTarget = webTarget.path(part);  
                }  

                // Crear el builder de invocación, puede requerir autenticación  
                Invocation.Builder invocationBuilder = ResourceManager.getInstance().getAuthRequestBuilder(webTarget, true);  

                try {  
                    // Convertir la entidad a JSON  
                    String jsonEntity = objectMapper.writeValueAsString(entity);  
                    
                    // Hacer la solicitud POST con el cuerpo que contiene el usuario a añadir  
                    Response response = invocationBuilder.post(jakarta.ws.rs.client.Entity.entity(jsonEntity, "application/json"));  

                    // Comprobar si la respuesta tiene errores  
                    ResourceManager.getInstance().checkResponseErrors(response);  

                } catch (ResponseProcessingException e) {  
                    e.printStackTrace();  
                    throw new Exception(ResourceManager.getInstance().getText("error.service.response.processing") + " " + e.getMessage());  
                } catch (ProcessingException e) {  
                    e.printStackTrace();  
                    throw new Exception(ResourceManager.getInstance().getText("error.service.processing") + " " + e.getMessage());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new Exception("Error serializing entity to JSON: " + e.getMessage());  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    throw e;  
                }  

                return null;  
            }  
        };  
    }  
}