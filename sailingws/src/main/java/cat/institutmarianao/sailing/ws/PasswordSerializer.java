package cat.institutmarianao.sailing.ws;

import java.io.IOException;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PasswordSerializer extends JsonSerializer<String> {
	
	@Override
	public void serialize(String password, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		ResourceBundle applicationBundle = ResourceBundle.getBundle("application");
		
		if (password == null || password.isBlank() || !applicationBundle.containsKey("spring.jackson.serialize.password") ||
				!"true".equals(applicationBundle.getString("spring.jackson.serialize.password"))) {
			password = null;
		} 
		gen.writeString(password);
	}

}
