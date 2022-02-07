package aytackydln.duyuru.jpa.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Topic")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//TODO cachable on existsById?
public class TopicEntity implements Serializable {
    private String id;
    private String departmentId;
    private String baseLink;
    private String boardAppend;
    private String annSelector;
    private String annTitleSelector;
    private String annLinkSelector;
    private List<SubscriptionEntity> subscriptions;
    //TODO subscribers ekle

    @Override
    public String toString() {
        return id;
    }

    @Id
    @EqualsAndHashCode.Include
    public String getId() {
        return id;
    }

    //should be also an @Id in the future
    public String getDepartmentId() {
        return departmentId;
    }

    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "topic")
    public List<SubscriptionEntity> getSubscriptions() {
        return subscriptions;
    }

    @Transient
    public final String getAnnouncementLink() {
        return getBaseLink() + getBoardAppend();
    }
}
