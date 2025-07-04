package aytackydln.duyuru.jpa.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "User")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Setter
@NamedEntityGraph(
        name = "user-subscriptions",
        attributeNodes = {
                @NamedAttributeNode("subscriptions")
        }
)
public class UserEntity implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String language;
    private String status;
    private List<SubscriptionEntity> subscriptions;

    @Id
    @EqualsAndHashCode.Include
    public long getId() {
        return id;
    }

    @Column(name = "name")
    @JsonAlias("first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("username")
    @ToString.Include
    public String getUsername() {
        return username;
    }

    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    @ToString.Include
    public String getStatus() {
        return status;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    public List<SubscriptionEntity> getSubscriptions() {
        return subscriptions;
    }

    @Transient
    @ToString.Include
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public boolean hasSubscriptions(){
        return Hibernate.isInitialized(subscriptions);
    }
}
