package com.gelo.spirum.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by olejk4 on 13.10.17.
 */
@XmlRootElement
public class UserRegisterForm {
    UserRegisterForm(){}

    public UserRegisterForm(String firstName,String lastName,String username,String password, String email,String profilePic) {
        this.firstName= firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePic = profilePic;
    }






    @Column( length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String firstName;

    @Column(length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String lastName;
    @Column( length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;


    @Column( length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;
    @Column( length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;
    @NotNull
    private String profilePic;

    public String getProfilePic()
    {
        return profilePic;
    }

    public void setProfilePic(String profilePic)
    {
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
