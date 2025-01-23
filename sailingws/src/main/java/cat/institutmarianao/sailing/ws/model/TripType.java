package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import cat.institutmarianao.sailing.ws.validation.groups.OnTripTypeCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripTypeUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* JPA annotations */
@Entity
@Table(name = "trip_types")
/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TripType implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MIN_DEPARTURE_HOUR = 6;
	public static final int MAX_DEPARTURE_HOUR = 14;
	public static final String GROUP = "GROUP";
	public static final String PRIVATE = "PRIVATE";

	public enum Category {
		GROUP, PRIVATE
	}

	/* Validation */
	@Null(groups = OnTripTypeCreate.class) // Must be null on inserts
	@NotNull(groups = OnTripTypeUpdate.class) // Must be not null on updates
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	/* Lombok */
	@EqualsAndHashCode.Include
	private Long id;

	/* Validation */
	@NotNull
	private String title;

	/* Validation */
	@NotNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(nullable = false)
	private Category category;

	/* Validation */
	@NotNull
	/* JPA */
	@Column(nullable = false)
	private String description;

	/* Validation */
	@Positive
	private double price;

	/* JPA */
	/*@ElementCollection
	@CollectionTable(name = "trip_type_departures", joinColumns = @JoinColumn(name = "trip_type_id"))
	@Column(name = "departure", nullable = false)
	@Temporal(TemporalType.TIME)
	private Set<Date> departures;*/
	
	private String departures;	// Comma-separated values: 9:30;11:30;13:30
 
	/* Validation */
	@Positive
	private int duration;

	/* Validation */
	@Positive
	/* JPA */
	@Column(name = "max_places")
	private int maxPlaces;
}
