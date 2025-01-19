package ins.marianao.sailing.fxml.services;


import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.util.StringUtils;
import cat.institutmarianao.sailing.ws.model.User;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;

public class ServiceAuthenticate extends Service<Pair<User, String>>{

	private static final String PATH_AUTHENTICATE = "authenticate";

	private String username;
	private String password;

	public ServiceAuthenticate(String username, String password) throws Exception {
		if (username == null || username.isBlank()) throw new Exception(ResourceManager.getInstance().getText("error.login.find.no.username"));
		if (password == null || password.isBlank()) throw new Exception(ResourceManager.getInstance().getText("error.login.find.no.password"));
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected Task<Pair<User, String>> createTask() {
		return new Task<Pair<User, String>>() {
			@Override
			protected Pair<User, String> call() throws Exception {
				Client webClient = ResourceManager.getInstance().getWebClient();

				WebTarget webTarget = webClient.target(ResourceManager.getInstance().getParam("web.service.host.url")).path(PATH_AUTHENTICATE);
	
				Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
	
				// {"username":"alex","password":"1234"}
				JsonObject credentials = Json.createObjectBuilder()
					    .add("username", username)
					    .add("password", password)
					    .build();
				
				/*AuthCredentials authCredentials = new AuthCredentials(username, password);
				ClientObjectMapperProvider com = new ClientObjectMapperProvider();
				ObjectMapper om = com.getContext(AuthCredentials.class);
				System.out.println(om.writeValueAsString(authCredentials));*/
				
				
				User user = null;
				String bearerToken = "";
				try {
					//Response response = invocationBuilder.post(Entity.entity(authCredentials, MediaType.APPLICATION_JSON));
					Response response = invocationBuilder.post(Entity.entity(credentials.toString(), MediaType.APPLICATION_JSON));
					
					ResourceManager.getInstance().checkResponseErrors(response);
					
					user = response.readEntity(User.class);
					
					bearerToken = response.getHeaderString(ResourceManager.getInstance().getParam("jwt.response.authorization.header"));
					if (StringUtils.hasText(bearerToken) && bearerToken.contains(ResourceManager.getInstance().getParam("jwt.response.authorization.value"))) {
						bearerToken = bearerToken.replaceFirst(ResourceManager.getInstance().getParam("jwt.response.authorization.value"), "").trim();
					}

				} catch (ResponseProcessingException e) {
					e.printStackTrace();
					throw new Exception(ResourceManager.getInstance().getText("error.service.response.processing")+" "+e.getMessage());
				} catch (ProcessingException e) {
					e.printStackTrace();
					throw new Exception(ResourceManager.getInstance().getText("error.service.processing")+" "+e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				
				return new Pair<User, String>(user, bearerToken);
			}
		};
	}

}
