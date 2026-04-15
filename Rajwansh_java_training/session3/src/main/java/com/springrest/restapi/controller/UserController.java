package com.springrest.restapi.controller;

import com.springrest.restapi.dto.ApiResponse;
import com.springrest.restapi.dto.SubmitRequest;
import com.springrest.restapi.model.User;
import com.springrest.restapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // I am creating a GET endpoint to search for users based on optional criteria such as name, age, and role. The endpoint accepts query parameters for these criteria and delegates the search logic to the UserService. The results are returned in a standardized ApiResponse format, indicating success or if no users matched the given filters.
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(
            @RequestParam(required = false) String name, //use RequestParam to make the parameters optional
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String role) {

        List<User> results = userService.searchUsers(name, age, role);

        if (results.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.success("No users matched the given filter(s).", results)
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully.", results)
        );
    }

    // I am creating a POST endpoint to submit data with validation. The endpoint accepts a SubmitRequest object in the request body, which contains fields title, description, category, and submittedBy. 
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitRequest>> submitData(
            @RequestBody SubmitRequest request) {

        // I check if the request body is null and return a bad request response if it is. 
        if (request == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Request body must not be null."));
        }

       
        userService.validateSubmission(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getSubmittedBy()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Data submitted successfully.", request));
    }

    // I am creating a DELETE endpoint to delete a user by their ID with confirmation. The endpoint accepts the user ID as a path variable and an optional confirm query parameter. 
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable int id,
            @RequestParam(required = false) Boolean confirm) {

        String resultMessage = userService.deleteUser(id, confirm);

       
        if (resultMessage.equals("Confirmation required")) {
            return ResponseEntity.ok(
                    ApiResponse.error("Confirmation required. Pass confirm=true to proceed.")
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(resultMessage, null)
        );
    }
}