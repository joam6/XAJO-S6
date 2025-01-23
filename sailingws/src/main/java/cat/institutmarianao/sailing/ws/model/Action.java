package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionUpdate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
/* Mapping JPA Indexes */
@Table(name = "actions", indexes = { @Index(name = "type", columnList = "type", unique = false),
		@Index(name = "trip_x_date", columnList = "trip_id, date DESC", unique = true) })
/* JPA Inheritance strategy is single table */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*
 * Maps different JPA objects depDONEing on his type attribute (Opening,
 * Assignment, Intervention or Close)
 */
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
/* JSON annotations */
/*
 * Maps JSON data to Booking, Rescheduling, Cancellation or Done
 * depending on property type
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({ @Type(value = Booking.class, name = Action.BOOKING),
				@Type(value = Rescheduling.class, name = Action.RESCHEDULING),
				@Type(value = Cancellation.class, name = Action.CANCELLATION),
				@Type(value = Done.class, name = Action.DONE)})
/* Swagger */
@Schema(oneOf = { Booking.class, Rescheduling.class, Cancellation.class, Done.class}, discriminatorProperty = "type")
/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Values for type - MUST be constants */
	public static final String BOOKING = "BOOKING";
	public static final String RESCHEDULING = "RESCHEDULING";
	public static final String CANCELLATION = "CANCELLATION";
	public static final String DONE = "DONE";

	public enum Type {
		BOOKING, RESCHEDULING, CANCELLATION, DONE
	}

	/* Validation */
	@Null(groups = { OnTripCreate.class, OnActionCreate.class }) // Must be null on inserts
	@NotNull(groups = OnActionUpdate.class) // Must be not null on updates
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	/* Lombok */
	@EqualsAndHashCode.Include
	protected Long id;

	/* Validation */
	@NotNull
	/* Lombok */
	@NonNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(name = "type", insertable = false, updatable = false, nullable = false)
	protected Type type;

	/* Validation */
	@NotNull
	@Valid
	/* JPA */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	protected User performer;

	/* Validation */
	@NotNull
	/* JPA */
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.DATE_TIME_PATTERN, timezone = Departure.TZ)
	protected Date date = new Date();

	/* Validation */
	@NotNull(groups = OnTripCreate.class) // The JSON do not have the trip reference (trip has no id yet)
	//@NotNull(groups = { OnActionCreate.class })  // Due to JsonIgnore
	/* JPA */
	@ManyToOne(optional = false)
	@JoinColumn(name = "trip_id", nullable = false)
	/* JSON */
	@JsonIgnore			// Avoid JSON Infinite recursion
	protected Trip trip;

	@Transient
	protected Long idTrip;
	
	/* JSON */
	@JsonIgnore
	public String getInfo() {
		return "";
	}
}
