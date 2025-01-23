package cat.institutmarianao.sailing.ws.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
@DiscriminatorValue(Action.RESCHEDULING)
/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Rescheduling extends Action {
	private static final long serialVersionUID = 1L;

	private String reason;
	
	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.DATE)
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.DATE_PATTERN, timezone = Departure.TZ)
	private Date oldDate;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.TIME)
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.TIME_PATTERN, timezone = Departure.TZ)
	private Date oldDeparture;
	
	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.DATE)
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.DATE_PATTERN, timezone = Departure.TZ)
	private Date newDate;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.TIME)
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.TIME_PATTERN, timezone = Departure.TZ)
	private Date newDeparture;
	
	@Override
	public String getInfo() {
		return this.reason;
	}
}
