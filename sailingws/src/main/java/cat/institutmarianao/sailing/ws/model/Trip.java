package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonInclude;

import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;   
import jakarta.persistence.Inheritance; 
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@Entity
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "trips")
@JsonInclude(JsonInclude.Include.NON_NULL)  // Para excluir propiedades nulas en la serialización JSON
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

	/* Lombok */
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_username", nullable = false)
	private Client client;

	@NotNull
	@Column(name = "places", nullable = false, length = 11)
	private int places;

	
	@NotNull
	@Positive
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "departure_id", nullable = false)
	private Departure departure;
	
	/* Lombok */
	@Singular("track") 
	@OneToMany(mappedBy = "trip", fetch = FetchType.LAZY)
	private List<Action> tracking;

	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	/* Hibernate */
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
