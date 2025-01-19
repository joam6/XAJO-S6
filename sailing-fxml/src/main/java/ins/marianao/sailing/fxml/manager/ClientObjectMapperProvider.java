package ins.marianao.sailing.fxml.manager;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/* Example
	ClientObjectMapperProvider com = new ClientObjectMapperProvider();
	ObjectMapper om = com.getContext(AuthCredentials.class);
	System.out.println(om.writeValueAsString(authCredentials));
*/

@Provider
public class ClientObjectMapperProvider implements ContextResolver<ObjectMapper> {
	final ObjectMapper defaultObjectMapper;
	 
    public ClientObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }
 
    @Override
    public ObjectMapper getContext(Class<?> type) {
    	System.out.println("ObjectMapper context "+type);
        return defaultObjectMapper;
    }
 
    private static ObjectMapper createDefaultMapper() {
    	System.out.println("Create ObjectMapper");
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
        		.disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
        		.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        
        mapper.setDateFormat(df);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return mapper;
    }
}
