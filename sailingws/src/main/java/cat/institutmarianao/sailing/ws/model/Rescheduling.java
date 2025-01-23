package cat.institutmarianao.sailing.ws.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("Rescheduling")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rescheduling extends Action {
	private static final long serialVersionUID = 1L;

	@Column(name = "reason", length = 255, nullable = true)
	private String reason;
	
	@Column(name="old_date", nullable = true)
	private Date oldDate;

	@Column(name="old_departure", nullable = true)
	private Date oldDeparture;
	
	@Column(name="new_date", nullable = true)
	private Date newDate;

	@Column (name="new_departure", nullable = true)
	private Date newDeparture;
	
	@Override
	public String getInfo() {
		return this.reason;
	}
}
