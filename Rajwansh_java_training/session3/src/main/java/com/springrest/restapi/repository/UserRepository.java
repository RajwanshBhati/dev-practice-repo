package com.springrest.restapi.repository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.springrest.restapi.model.User;

@Repository
public class UserRepository{

    private List<User> users= new ArrayList<>();
    

    // I am creaating the constructor of the UserRepository class where I am adding some sample user data to the users list. This will allow me to test the functionality of my REST API without needing a database connection. 
    public UserRepository(){
        users.add(new User(1, "Priya Sharma",   28, "USER",  "priya.sharma@gmail.com"));
        users.add(new User(2, "Rahul Mehta",    30, "ADMIN", "rahul.mehta@gmail.com"));
        users.add(new User(3, "Anjali Gupta",   25, "USER",  "anjali.gupta@gmail.com"));
        users.add(new User(4, "Vikram Singh",   30, "USER",  "vikram.singh@gmail.com"));
        users.add(new User(5, "Sneha Patil",    35, "MANAGER","sneha.patil@gmail.com"));
        users.add(new User(6, "Arjun Nair",     22, "USER",  "arjun.nair@gmail.com"));
        users.add(new User(7, "Meera Iyer",     40, "ADMIN", "meera.iyer@gmail.com"));

    }
    

    // I am creating methods to get all users
    public List<User> getAllUsers(){
        return users;
    }
    

    // I am creating a method to get a user by their ID. This method uses Java Streams to filter the users list and find the user with the matching ID. If no user is found, it returns null.
    public User getUserById(int id){
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    
    // I am creating a method to delete a user by their ID. This method uses the removeIf method of the List interface to remove the user with the matching ID from the users list. It returns true if a user was removed, and false otherwise.
    public boolean deleteById(int id) {
        return users.removeIf(user -> user.getId() == id);
    } 
    
    // I am creating a method to save a new user to the users list. This method takes a User object as a parameter, adds it to the users list, and returns the saved user. 
    public User save(User user) {
        users.add(user);
        return user;
    }


}