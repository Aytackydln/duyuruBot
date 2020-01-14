package com.noname.duyuru.app.jpa.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Configuration {
    private String property;
    private String value;

    @Id
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
	}

    public void setValue(String value){
		this.value=value;
	}
}
