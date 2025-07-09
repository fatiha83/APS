package com.fyp.aps.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role;
    private String name;
    private String mykad;
    private String organization;
    private String title;


    //Constructor

    public User(){}

    public User (Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    //getters & setter
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMykad(){
        return mykad;
    }

    public void setMykad(String mykad){
        this.mykad = mykad;
    }

    public String getOrganization(){
        return organization;
    }

    public void setOrganization(String organization){
        this.organization = organization;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

}


