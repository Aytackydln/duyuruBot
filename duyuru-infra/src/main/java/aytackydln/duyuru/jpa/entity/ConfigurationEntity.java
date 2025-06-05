package aytackydln.duyuru.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity(name = "Configuration")
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ConfigurationEntity {
    private String property;
    private String value;

    @Id
    public String getProperty() {
        return property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConfigurationEntity that = (ConfigurationEntity) o;

        return Objects.equals(getProperty(), that.getProperty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }
}
