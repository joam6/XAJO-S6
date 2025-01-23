package cat.institutmarianao.sailing.ws.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
@DiscriminatorValue(Action.DONE)
/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Done extends Action {
	private static final long serialVersionUID = 1L;

	private String comments;
	
	@Override
	public String getInfo() {
		return this.comments;
	}
}
