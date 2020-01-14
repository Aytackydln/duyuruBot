package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @deprecated move this to jpa model
 **/
@Deprecated
public class Chat {
    private long id;
    private String username;
    private String firstName;
    private String lastName;

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id=id;
    }

    @JsonProperty("username")
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    @JsonProperty("first_name")
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    @JsonProperty("last_name")
    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }
}
