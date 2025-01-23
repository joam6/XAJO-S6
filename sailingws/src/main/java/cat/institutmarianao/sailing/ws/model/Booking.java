package cat.institutmarianao.sailing.ws.model;

import lombok.Data;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionUpdate;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Action.BOOKING)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking extends Action {
	private static final long serialVersionUID = 1L;
}
