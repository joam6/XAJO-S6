package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.Formula;

import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripUpdate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
@Table(name = "trips")
/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;

	public static final String RESERVED = "RESERVED";
	public static final String RESCHEDULED = "RESCHEDULED";
	public static final String CANCELLED = "CANCELLED";
	public static final String DONE = "DONE";

	public enum Status {
		RESERVED, RESCHEDULED, CANCELLED, DONE
	}

	/* Validation */
	@Null(groups = OnTripCreate.class) // Must be null on inserts
	@NotNull(groups = OnTripUpdate.class) // Must be not null on updates
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	/* Lombok */
	@EqualsAndHashCode.Include
	/* JSON */
	private Long id;

	/* Validation */
	@NotNull(groups = OnTripCreate.class)
	@Valid
	/* JPA */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Client client;

	@Positive
	private int places;

	/* Validation */
	@NotNull
	/* JPA */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
	private Departure departure;
	
	/* Validation */
	@NotNull(groups = OnTripCreate.class)
	/* JPA */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "trip")
	@Column(nullable = false)
	@OrderBy("date DESC")
	/* Lombok */
	@Singular("track") 
	private List<Action> tracking;

	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	/* Hibernate */
	/*@Formula("(SELECT CASE a.type WHEN '" + Action.BOOKING + "' THEN '" + Trip.RESERVED + "' WHEN '"
			+ Action.RESCHEDULING + "' THEN '" + Trip.RESCHEDULED + "' WHEN '" + Action.CANCELLATION + "' THEN '"
			+ Trip.CANCELLED + "' WHEN '" + Action.DONE + "' THEN '" + Trip.DONE + "' ELSE NULL END FROM actions a "
			+ " WHERE a.date=( SELECT MAX(last_action.date) FROM actions last_action "
			+ " WHERE last_action.trip_id=a.trip_id AND last_action.trip_id=id ))")*/
	@Formula("(SELECT CASE a.type "
			+ "  WHEN '" + Action.BOOKING + "' THEN '" + Trip.RESERVED + "' " 
			+ "  WHEN '" + Action.RESCHEDULING + "' THEN '" + Trip.RESCHEDULED + "' "
			+ "  WHEN '" + Action.CANCELLATION + "' THEN '" + Trip.CANCELLED + "' "
			+ "  WHEN '" + Action.DONE + "' THEN '" + Trip.DONE + "' "
			+ "  ELSE NULL END "
			+ "FROM actions a WHERE a.trip_id = id AND a.date = "
			+ "  (SELECT MAX(last.date) FROM actions last "
			+ "   WHERE last.trip_id = a.trip_id)"
			+ ")")
	// Lombok
	@Setter(AccessLevel.NONE)
	private Status status;
	
}
