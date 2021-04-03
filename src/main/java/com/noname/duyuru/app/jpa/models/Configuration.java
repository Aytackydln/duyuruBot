package com.noname.duyuru.app.jpa.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Configuration {
    private String property;
    private String value;

    @Id
    @EqualsAndHashCode.Include
    public String getProperty() {
        return property;
    }
}
