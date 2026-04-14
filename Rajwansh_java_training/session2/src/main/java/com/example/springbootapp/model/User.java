package com.example.springbootapp.model;

public class User{

private Long id;
private String name;
private String email;
private String role;

public User(){

}

// Here I am creating a constructor for the User class that takes all the fields as parameters. This constructor allows me to create a new User object with specific values for id, name, email, and role when needed. 
public User(Long id, String name, String email, String role){
    this.id=id;
    this.name=name;
    this.email=email;
    this.role = role;
}


// I am creating getter and setter methods for each field in the User class for accessing and modifying the values of the fields. 
public Long getId(){
    return id;
}

public void setId(Long id){
  this.id=id;
}


public String getName(){
    return name;
}

public void setName(String name){
    this.name=name;
}


public String getEmail(){
    return email;
}

public void setEmail(String email){
    this.email=email;
}

 public String getRole() { 
    return role; 
}

public void setRole(String role) { 
    this.role = role; 
}



}