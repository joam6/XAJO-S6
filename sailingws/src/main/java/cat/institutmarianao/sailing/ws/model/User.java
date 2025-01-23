package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cat.institutmarianao.sailing.ws.PasswordSerializer;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
/* Mapping JPA Indexes */
@Table(name = "users", indexes = { @Index(name = "role", columnList = "role", unique = false),
		@Index(name = "full_name", columnList = "full_name", unique = false),
		@Index(name = "role_x_full_name", columnList = "role, full_name", unique = false) })
/* JPA Inheritance strategy is single table */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*
 * Maps different JPA objects depending on his role attribute (ADMIN or CLIENT)
 */
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
/* JSON annotations */
/*
 * Maps JSON data to Receptionist, LogisticsManager or Courier instance depending on
 * property role
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role", visible = true)
@JsonSubTypes({ @Type(value = Client.class, name = User.CLIENT),
				@Type(value = Admin.class, name = User.ADMIN) })
/* Swagger */
@Schema(oneOf = { Client.class, Admin.class }, discriminatorProperty = "role")
/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/* Values for role - MUST be constants (can not be enums) */
	public static final String ADMIN = "ADMIN";
	public static final String CLIENT = "CLIENT";

	public enum Role {
		ADMIN, CLIENT
	}

	public static final int MIN_USERNAME = 2;
	public static final int MAX_USERNAME = 25;
	public static final int MIN_PASSWORD = 10;

	/* Validation */
	@NotBlank
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	/* JPA */
	@Id
	@Column(unique = true, nullable = false)
	/* Lombok */
	@EqualsAndHashCode.Include
	protected String username;

	/* JSON */
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Not present in generated JSON
	@JsonSerialize(using = PasswordSerializer.class)		// Custom serializer needed 
	/* Validation */
	@NotNull(groups = OnUserCreate.class)
	@NotBlank(groups = OnUserCreate.class)
	//@Size(min = MIN_PASSWORD)
	/* JPA */
	@Column(nullable = false)
	protected String password;

	/* Validation */
	@NotNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(name = "role", insertable = false, updatable = false, nullable = false)
	protected Role role;
	
	/* JSON */
	@JsonIgnore
	public abstract String getInfo(); 
	
	/* JSON */
	@JsonIgnore
	public boolean isAdmin() {
		return false;
	}

}
