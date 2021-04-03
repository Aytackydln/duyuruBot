package com.noname.duyuru.app.jpa.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "topic_id")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString
public class Department extends Topic {
    private String classesUri;
    private String classElementSelector;
    private String classAnnSelector;
    private String classAnnTitleSelector;
    private String classAnnLinkSelector;
}
