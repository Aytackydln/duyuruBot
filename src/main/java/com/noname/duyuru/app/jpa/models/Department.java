package com.noname.duyuru.app.jpa.models;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "topic_id")
public class Department extends Topic{
	private String classesUri;
	private String classElementSelector;

	public String getClassesUri(){
		return classesUri;
	}

	public void setClassesUri(String classesUri){
		this.classesUri=classesUri;
	}

	public String getClassElementSelector(){
		return classElementSelector;
	}

	public void setClassElementSelector(String classElementSelector){
		this.classElementSelector=classElementSelector;
	}
}
