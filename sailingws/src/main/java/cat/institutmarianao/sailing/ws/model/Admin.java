package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserUpdate;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(User.ADMIN) // Es utilizada en el contexto de mapeo de herencia en JPA (Java Persistence API) para especificar el valor discriminador de una subclase.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Admin extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getInfo() {
		return "";
	}
	
	@Override
	public boolean isAdmin() {
		return true;
	}
}
