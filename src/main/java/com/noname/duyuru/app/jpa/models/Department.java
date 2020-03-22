package com.noname.duyuru.app.jpa.models;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "topic_id")
public class Department extends Topic {
    private String classesUri;
    private String classElementSelector;
    private String classAnnSelector;
    private String classAnnTitleSelector;
    private String classAnnLinkSelector;

    @Basic
    public String getClassesUri() {
        return classesUri;
    }

    public void setClassesUri(String classesUri) {
        this.classesUri = classesUri;
    }

    @Basic
    public String getClassElementSelector() {
        return classElementSelector;
    }

    public void setClassElementSelector(String classElementSelector) {
        this.classElementSelector = classElementSelector;
    }

    public String getClassAnnSelector() {
        return classAnnSelector;
    }

    public void setClassAnnSelector(String classAnnSelector) {
        this.classAnnSelector = classAnnSelector;
    }

    public String getClassAnnTitleSelector() {
        return classAnnTitleSelector;
    }

    public void setClassAnnTitleSelector(String classAnnTitleSelector) {
        this.classAnnTitleSelector = classAnnTitleSelector;
    }

    public String getClassAnnLinkSelector() {
        return classAnnLinkSelector;
    }

    public void setClassAnnLinkSelector(String classAnnLinkSelector) {
        this.classAnnLinkSelector = classAnnLinkSelector;
    }
}
