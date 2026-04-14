package com.springrest.restapi.model;


// Here I am creating a User class with some attributes and their getter and setter methods. This class will be used to represent the user data in my REST API.
public class User{

    private int id;
    private String name;
    private int age;
    private String email;
    private String role;

    public User(){

    }

    public User(int id, String name, int age, String email, String role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }
    
    //I created getter and setter methods for each attribute to allow access and modification of the user data. 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    // Here I am overriding the toString() method to provide a string representation of the User object because  It will allow me to easily print the user details in a readable format.
    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}