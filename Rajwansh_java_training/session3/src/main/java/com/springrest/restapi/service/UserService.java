package com.springrest.restapi.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.springrest.restapi.model.User;
import com.springrest.restapi.repository.UserRepository;
import com.springrest.restapi.exception.UserNotFoundException;
import com.springrest.restapi.exception.InvalidRequestException;
import java.util.stream.Collectors;


@Service

public class UserService{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // I am created a method to search for users based on optional criteria such as name, age, and role. This method retrieves all users from the repository and then filters them based on the provided criteria. If a criterion is not provided  null or blank, it is ignored in the filtering process. The filtered list of users is then returned as the result.
    public List<User> searchUsers(String name, Integer age, String role) {
        List<User> allUsers = userRepository.getAllUsers();
 
        return allUsers.stream()
                .filter(user -> matchesName(user, name))
                .filter(user -> matchesAge(user, age))
                .filter(user -> matchesRole(user, role))
                .collect(Collectors.toList());
    }
     
     //I created helper methods to check if a user matches the provided search criteria. 
     private boolean matchesName(User user, String name) {
        if (name == null || name.isBlank()) {
            return true;
        }
        return user.getName().equalsIgnoreCase(name.trim());
    }
    
    // I created a helper method to check if a user matches the provided age criteria. If the age parameter is null, it returns true, indicating that the age criterion should be ignored. Otherwise, it compares the user's age with the provided age and returns true if they match, or false if they do not.
    private boolean matchesAge(User user, Integer age) {
        if (age == null) {
            return true;
        }
        return user.getAge() == age;
    }
    

    // I created a helper method to check if a user matches the provided role criteria. If the role parameter is null or blank, it returns true, indicating that the role criterion should be ignored. Otherwise, it compares the user's role with the provided role ignoring case and trimming whitespace and returns true if they match, or false if they do not.
    private boolean matchesRole(User user, String role) {
        if (role == null || role.isBlank()) {
            return true;
        }
        return user.getRole().equalsIgnoreCase(role.trim());
    }
    
    // I am creating a method to delete a user by their ID with confirmation. This method first checks if the confirmation parameter is provided and true. If not, it returns a message indicating that confirmation is required. Then, it checks if a user with the specified ID exists in the repository. If the user does not exist, it throws a UserNotFoundException. If the user exists, it proceeds to delete the user from the repository and returns a success message confirming the deletion.
    public String deleteUser(int id, Boolean confirm) {
        if (confirm == null || !confirm) {
            return "Confirmation required";
        }
 
        User user = userRepository.getUserById(id);
       if (user == null) {
     throw new UserNotFoundException("User with ID " + id + " not found.");
    }
 
        userRepository.deleteById(id);
        return "User with ID " + id + " has been successfully deleted.";
    }

    // I am creating a method to validate the submission of a new user. This method checks if the required fields (title, description, category, and submittedBy) are not null or blank. If any of these fields are invalid, it throws an InvalidRequestException with a message indicating which field is required and must not be null or empty
    public void validateSubmission(String title, String description,
                                   String category, String submittedBy) {
        if (isNullOrBlank(title)) {
            throw new InvalidRequestException("Field 'title' must not be null or empty.");
        }
        if (isNullOrBlank(description)) {
            throw new InvalidRequestException("Field 'description' must not be null or empty.");
        }
        if (isNullOrBlank(category)) {
            throw new InvalidRequestException("Field 'category' must not be null or empty.");
        }
        if (isNullOrBlank(submittedBy)) {
            throw new InvalidRequestException("Field 'submittedBy' must not be null or empty.");
        }
    }
 
    private boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }


}