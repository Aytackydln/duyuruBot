package com.noname.duyuru.app.jpa.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//TODO cachable on existsById?
public class Topic implements Serializable {
    private String id;
    private String departmentId;
    private String baseLink;
    private String boardAppend;
    private String annSelector;
    private String annTitleSelector;
    private String annLinkSelector;
    private List<Subscription> subscriptions;
    //TODO subscribers ekle

    @Override
    public String toString() {
        return id;
    }

    @Id
    @EqualsAndHashCode.Include
    public final String getId() {
        return id;
    }

    //should be also an @Id in the future
    public String getDepartmentId() {
        return departmentId;
    }

    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "topic")
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Transient
    public final String getAnnouncementLink() {
        return getBaseLink() + getBoardAppend();
    }
}
