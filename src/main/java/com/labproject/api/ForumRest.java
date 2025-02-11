package com.labproject.api;

import com.labproject.application.ForumService;
import com.labproject.application.dto.input.ForumMessageDTO;
import com.labproject.application.dto.input.PrivateMessageDTO;
import com.labproject.application.dto.output.VisualizeCollectionUserDTO;
import com.labproject.application.dto.output.VisualizeForumDTO;
import com.labproject.application.dto.output.VisualizeMessageDTO;
import com.labproject.security.authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forum")
public class ForumRest {

    private final ForumService forumService;

    @Autowired
    public ForumRest(ForumService forumService) {
        this.forumService = forumService;
    }


    //SPRINT 1 STUDENT 3 (2)
    @PostMapping("/{forumID}/subscribe")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void subscribeToAlbumForum(@PathVariable long forumID) throws Throwable {
        Long userID = AuthenticationService.getIDFromToken();
        forumService.subscribeToAlbumForum(userID, forumID);
    }

    //SPRINT 1 STUDENT 3 (3)
    @DeleteMapping("/{forumID}/unsubscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribeToAlbumForum(@PathVariable long forumID) throws Throwable {
        Long userID = AuthenticationService.getIDFromToken();
        forumService.unsubscribeToAlbumForum(userID, forumID);
    }

    //SPRINT 1 STUDENT 3 (4)
    @GetMapping("/{id}/publicMessages")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeMessageDTO> getAllPublicMessages(@PathVariable long id, Optional<LocalDate> date) {
        Long userID = AuthenticationService.getIDFromToken();
        return forumService.getAllMessages(id, date, userID);
    }

    //SPRINT 1 STUDENT 3 (5)
    @PostMapping("/{forumID}/message")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessageToForum(@PathVariable long forumID, @RequestBody @Valid ForumMessageDTO forumMessageDTO) {
        Long userID = AuthenticationService.getIDFromToken();
        forumService.sendMessageToForum(userID, forumID, forumMessageDTO);
    }

    @GetMapping("/subscribed")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeForumDTO> getSubscribedForum() {
        Long userID = AuthenticationService.getIDFromToken();
        return forumService.getSubscribedForum(userID);
    }


    //SPRINT 2 STUDENT 3 (1)
    @GetMapping("/{id}/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeCollectionUserDTO> getAllSubscribersFromForum(@PathVariable long id) {
        Long userID = AuthenticationService.getIDFromToken();
        return forumService.getAllSubscribersFromForum(id, userID);
    }

    //SPRINT 2 STUDENT 3 (2)
    @PostMapping("/forumMessage/{messageID}/read")
    @ResponseStatus(HttpStatus.OK)
    public void updateReadMessage(@PathVariable long messageID) {
        Long userID = AuthenticationService.getIDFromToken();
        forumService.updateReadMessage(userID, messageID);
    }


    //SPRINT 4 STUDENT 2 (2) CREATION UPDATE READ FOR PRIVATE MESSAGE
    @PostMapping("/privateMessage/{messageID}/read") // FUNCIONA
    @ResponseStatus(HttpStatus.OK)
    public void updateReadPrivateMessage(@PathVariable long messageID) {
        Long userID = AuthenticationService.getIDFromToken();
        forumService.updatePrivateReadMessage(userID, messageID);
    }


    //SPRINT 2 STUDENT 3 (3)
    @PostMapping("{forumID}/sendDirectMessage")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendDirectMessage(@PathVariable long forumID, @RequestBody @Valid PrivateMessageDTO privateMessageDTO) {
        Long senderID = AuthenticationService.getIDFromToken();
        forumService.sendDirectMessage(senderID, forumID, privateMessageDTO, false, Optional.empty());
    }

    //SPRINT 2 STUDENT 3 (4)
    @PostMapping("/{forumId}/message/{messageID}/replyDirectMessage")
    @ResponseStatus(HttpStatus.CREATED)
    public void replayDirectMessage(@PathVariable long messageID, @PathVariable long forumId, @RequestBody @Valid PrivateMessageDTO privateMessageDTO) {
        Long senderID = AuthenticationService.getIDFromToken();
        forumService.replyDirectMessage(senderID, forumId, messageID, privateMessageDTO);
    }

    //SPRINT 2 STUDENT 3 (5)
    @GetMapping("/publishMessage")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeMessageDTO> getAllMessages(@RequestParam LocalDate date) {
        Long userId = AuthenticationService.getIDFromToken();
        return forumService.getAllMessagesByDate(date, userId);
    }

    //SPRINT 4 STUDENT 2 (2)
    @PostMapping("/{forumId}/singleTimeMessage")
    public void sendSingleTimeMessage(@PathVariable long forumId, @RequestBody @Valid PrivateMessageDTO privateMessageDTO) {
        Long userId = AuthenticationService.getIDFromToken();
        forumService.sendDirectMessage(userId, forumId, privateMessageDTO, true, Optional.empty());
    }


    //SPRINT 4 STUDENT 2 (4)
    @GetMapping("/allOrderedByMessages")
    public List<VisualizeForumDTO> getAllOrderedByMessages() {
        return forumService.getAllForumsOrderedByMessages();
    }
}