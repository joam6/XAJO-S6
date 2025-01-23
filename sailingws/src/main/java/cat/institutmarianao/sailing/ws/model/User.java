package cat.institutmarianao.sailing.ws.model;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(oneOf = { Client.class, Admin.class }, discriminatorProperty = "role")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
/*JPA*/
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.EXTERNAL_PROPERTY, 
    property = "role"  // el campo que se usa como discriminador
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Client.class, name = "CLIENT"),
    @JsonSubTypes.Type(value = Admin.class, name = "ADMIN")
})
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";

    public enum Role {
        ADMIN, CLIENT
    }

    public static final int MIN_USERNAME = 2;
    public static final int MAX_USERNAME = 25;
    public static final int MIN_PASSWORD = 10;

    @EqualsAndHashCode.Include
    @Id
    @NotNull
    @Size(min = MIN_USERNAME, max = MAX_USERNAME)
    @Column(name = "username", nullable = false, length = MAX_USERNAME, unique=true)
    protected String username;

    @NotNull
    @Size(min = MIN_PASSWORD)
    @Column(name = "password", nullable = false)
    protected String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role", nullable = false, insertable=false, updatable=false)
    protected Role role;

    public abstract String getInfo(); 
    
    public boolean isAdmin() {
        return false;
    }

}
