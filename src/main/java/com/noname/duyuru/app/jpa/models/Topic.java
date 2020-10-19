package com.noname.duyuru.app.jpa.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof Topic) {
            final Topic otherTopic = ((Topic) obj);
            return id.equals(otherTopic.id);
		}
		return false;
	}

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

    @Id
    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    //should be also an @Id in the future
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getBaseLink() {
        return baseLink;
    }

    public void setBaseLink(String baseLink) {
        this.baseLink = baseLink;
    }

    public String getBoardAppend() {
        return boardAppend;
    }

    public void setBoardAppend(String boardAppend) {
        this.boardAppend = boardAppend;
    }

    public String getAnnSelector() {
        return annSelector;
    }

    public void setAnnSelector(String annSelector) {
        this.annSelector = annSelector;
    }

    public String getAnnTitleSelector() {
        return annTitleSelector;
    }

    public void setAnnTitleSelector(String annTitleSelector) {
        this.annTitleSelector = annTitleSelector;
    }

    public String getAnnLinkSelector() {
        return annLinkSelector;
    }

    public void setAnnLinkSelector(String annLinkSelector) {
        this.annLinkSelector = annLinkSelector;
    }

    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "topic")
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Transient
    public final String getAnnouncementLink() {
        return getBaseLink() + getBoardAppend();
    }
}
