package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(User.CLIENT)  // El nombre de la tabla
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MIN_FULL_NAME = 3;
	public static final int MAX_FULL_NAME = 100;
	
	@Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME)
	@NotNull
	@Column(name = "full_name" , length = MAX_FULL_NAME)  // Mapea el atributo 'fullName' a la columna 'full_name' en la base de datos
	protected String fullName;

	@Pattern(regexp = "^[0-9]+$")
	@Size(min = 10, max = 15)
    @Column(name = "phone")  // Mapea el atributo 'phone' a la columna 'phone' en la base de datos
	protected Integer phone;
	
	@Override
	public String getInfo() {
		return this.fullName+" ("+this.phone+")";
	}
	
}
