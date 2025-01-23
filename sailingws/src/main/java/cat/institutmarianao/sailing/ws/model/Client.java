package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
/* A client is identified in the user table with role=CLIENT */
@DiscriminatorValue(User.CLIENT)

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Client extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MIN_FULL_NAME = 3;
	public static final int MAX_FULL_NAME = 100;

	/* Validation */
	// @NotBlank(groups = OnUserCreate.class)
	@Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME)
	/* JPA */
	@Column(name = "full_name")
	protected String fullName;

	/* Validation */
	@Positive
	protected Integer phone;
	
	@Override
	public String getInfo() {
		return this.fullName+" ("+this.phone+")";
	}
	
}
