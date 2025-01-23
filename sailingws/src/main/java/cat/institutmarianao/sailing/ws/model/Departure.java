package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.Date;



import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;   
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "departures")
@JsonInclude(JsonInclude.Include.NON_NULL)  // Para excluir campos nulos en el JSON
public class Departure implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TZ = "Europe/Madrid";
	
	/* Lombok */
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive
	@Column (name = "id", nullable = false)
	protected Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn (name = "trip_type_id", nullable = false)
	private TripType tripType;
	
    @NotNull
	@Column (name  = "date" , nullable = false)
	private Date date;

    @Column(name = "departure", nullable = false)
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
