package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
/* Mapping JPA Indexes */
@Table(name = "departures", indexes = { 
		@Index(name = "triptype_date_departure", columnList = "trip_type_id, date DESC, departure", unique = true) 
})
/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Departure implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TZ = "Europe/Madrid";
	
	/* Validation */
	@Null(groups = OnTripCreate.class) // Must be null on inserts
	@NotNull(groups = OnTripUpdate.class) // Must be not null on updates
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	/* Lombok */
	@EqualsAndHashCode.Include
	protected Long id;
	
	/* Validation */
	@NotNull
	/* JPA */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private TripType tripType;
	
	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.DATE)
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.DATE_PATTERN, timezone = TZ)
	private Date date;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.TIME)
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.TIME_PATTERN, timezone = TZ)
	private Date departure;
	
	/* Hibernate */
	@Formula("(SELECT COALESCE(SUM(t.places), 0) "
			+ "FROM trips t INNER JOIN actions a ON a.trip_id = t.id " 
			+ "WHERE a.type <> '"+Action.CANCELLATION+"' AND "
			+ "t.departure_id = id AND "
			+ "a.date = (SELECT MAX(last.date) FROM actions last WHERE last.trip_id = a.trip_id) "
			+ ")")
	// Lombok
	@Setter(AccessLevel.NONE)
	private int bookedPlaces;
}
