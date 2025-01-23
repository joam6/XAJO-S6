package cat.institutmarianao.sailing.ws.model;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Action.DONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Done extends Action {
	private static final long serialVersionUID = 1L;

	@Column(name="comments", length=255, nullable = true)
	private String comments;
	
	@Override
	public String getInfo() {
		return this.comments;
	}
}
