package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
/* An admin is identified in the user table with role=ADMIN */
@DiscriminatorValue(User.ADMIN)

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Admin extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getInfo() {
		return "";
	}
	
	@Override
	public boolean isAdmin() {
		// TODO Auto-generated method stub
		return true;
	}
}
