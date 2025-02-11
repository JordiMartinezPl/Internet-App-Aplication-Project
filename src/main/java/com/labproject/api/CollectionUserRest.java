package com.labproject.api;

import com.labproject.application.CollectionUserService;
import com.labproject.application.dto.input.CollectionUserDTO;
import com.labproject.application.dto.input.UpdateUserDTO;
import com.labproject.application.dto.output.VisualizeCollectionUserDTO;
import com.labproject.application.dto.output.VisualizeUserWithStickersDTO;
import com.labproject.security.authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class CollectionUserRest {

    private final CollectionUserService collectionUserService;

    @Autowired
    public CollectionUserRest(CollectionUserService collectionUserService) {
        this.collectionUserService = collectionUserService;
    }

    //SPRINT 1 STUDENT 1 (1)
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid CollectionUserDTO collectionUserDTO) {
        collectionUserService.registerUser(collectionUserDTO);
    }

    //SPRINT 1 STUDENT 1 (2)
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserID(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        Long userId = AuthenticationService.getIDFromToken();
        collectionUserService.updateUserProfile(userId, updateUserDTO);
    }

    //SPRINT 1 STUDENT 1 (3)
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeCollectionUserDTO> getAllUsers() {
        return collectionUserService.getAllUsers();
    }

    //SPRINT 1 STUDENT 1 (4)
    @GetMapping("/lastUsersRegistered")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeCollectionUserDTO> getLastRegisteredUsers(@RequestParam LocalDate date) {
        return collectionUserService.getLastRegisteredUsers(date);
    }

    //STUDENT 4 STUDENT 2 (1)
    @PostMapping("/block")
    @ResponseStatus(HttpStatus.OK)
    public void blockUser(@RequestParam String email) {
        Long userId = AuthenticationService.getIDFromToken();
        collectionUserService.blockUser(userId, email);
    }

    //SPRINT 4 STUDENT 2 (3)
    @GetMapping("/allOrderedByActivity")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeCollectionUserDTO> getAllOrderedByActivity() {
        return collectionUserService.getAllUsersOrderedByActivity();
    }


    //SPRINT 4 STUDENT 3 (1)

    @GetMapping("/exchange/topCandidates")
    public List<VisualizeUserWithStickersDTO> getTopCandidates() {
        Long userId = AuthenticationService.getIDFromToken();
        return collectionUserService.getTopCandidates(userId);
    }
}


