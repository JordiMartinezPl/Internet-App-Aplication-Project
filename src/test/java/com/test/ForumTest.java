package com.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labproject.LabProjectApplication;
import com.labproject.application.AlbumService;
import com.labproject.application.dto.input.PrivateMessageDTO;
import com.labproject.domain.PrivateMessage;
import com.labproject.persistence.ForumMessageRepository;
import com.labproject.persistence.ForumRepository;
import com.labproject.persistence.PrivateMessageRepository;
import com.labproject.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("LanguageDetectionInspection")
@SpringBootTest(classes = LabProjectApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ForumTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ForumMessageRepository forumMessageRepository;

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumRepository forumRepository;

    //SPRINT 1 STUDENT 3 (2)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void subscribeToAlbumForum_ValidUserAndForum_ShouldReturnAccepted() throws Exception {
        long forumID = 2L;
        mockMvc.perform(post("/api/forum/" + forumID + "/subscribe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void subscribeToAlbumForum_UserAlreadySubscribed_ShouldReturnConflict() throws Exception {

        long forumID = 2L;
        mockMvc.perform(post("/api/forum/" + forumID + "/subscribe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/forum/" + forumID + "/subscribe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void subscribeToAlbumForum_InvalidForumID_ShouldReturnNotFound() throws Exception {
        long invalidForumID = 999L;

        mockMvc.perform(post("/api/forum/" + invalidForumID + "/subscribe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //SPRINT 1 STUDENT 3(3)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void unsubscribeToAlbumForum_ShouldReturnNoContentStatus() throws Exception {
        long forumID = 1L;

        mockMvc.perform(delete("/api/forum/{forumID}/unsubscribe", forumID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void unsubscribeToAlbumForum_ShouldReturnConflict() throws Exception {
        long forumID = 2L;

        mockMvc.perform(delete("/api/forum/{forumID}/unsubscribe", forumID))
                .andExpect(status().isConflict());
    }

    //SPRINT 1 STUDENT 3 (4)

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllPublicMessages_ShouldReturnMessagesList() throws Exception {

        long forumID = 1L;
        String date = "2024-11-01";

        mockMvc.perform(get("/api/forum/{id}/publicMessages", forumID)
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].forumMessageID").value(5))
                .andExpect(jsonPath("$[0].body").value("Thanks for the welcome! Let’s trade."))
                .andExpect(jsonPath("$[0].sendDate").value("2024-11-15T10:10:00"))
                .andExpect(jsonPath("$[0].forumId").value(1))
                .andExpect(jsonPath("$[0].senderEmail").value("robert.brown3@example.com"))
                .andExpect(jsonPath("$[0].replyTo").value(1))
                .andExpect(jsonPath("$[0].readBy").value(false))
                .andExpect(jsonPath("$[1].forumMessageID").value(4))
                .andExpect(jsonPath("$[1].body").value("Looking forward to trading Pokemon stickers!"))
                .andExpect(jsonPath("$[1].sendDate").value("2024-11-15T10:05:00"))
                .andExpect(jsonPath("$[1].forumId").value(1))
                .andExpect(jsonPath("$[1].senderEmail").value("jane.smith2@example.com"))
                .andExpect(jsonPath("$[1].replyTo").isEmpty())
                .andExpect(jsonPath("$[1].readBy").value(false))
                .andExpect(jsonPath("$[2].forumMessageID").value(3))
                .andExpect(jsonPath("$[2].body").value("Welcome to the Album Pokemon Forum!"))
                .andExpect(jsonPath("$[2].sendDate").value("2024-11-15T10:00:00"))
                .andExpect(jsonPath("$[2].forumId").value(1))
                .andExpect(jsonPath("$[2].senderEmail").value("john.doe1@example.com"))
                .andExpect(jsonPath("$[2].replyTo").isEmpty())
                .andExpect(jsonPath("$[2].readBy").value(true))
                .andDo(print());
    }


    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllPublicMessages_NoDate_ShouldReturnAllMessages() throws Exception {
        long forumID = 1L;

        String expectedJson = """
                [] 
                """;// There is no message from this week

        mockMvc.perform(get("/api/forum/{id}/publicMessages", forumID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllPublicMessages_UserNotSubscribed_ShouldReturnConflict() throws Exception {
        long forumID = 2L;

        mockMvc.perform(get("/api/forum/{id}/publicMessages", forumID))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllPublicMessages_ForumNotValid_ShouldReturnNotFound() throws Exception {
        long forumID = 999L;
        mockMvc.perform(get("/api/forum/{id}/publicMessages", forumID))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    //SPRINT 1 STUDENT 3 (5)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void sendMessageToForum_ShouldReturnCreated() throws Exception {
        long forumID = 1L;
        String forumMessagePayload = """
                    {
                        "body": "This is a test message"
                    }
                """;

        mockMvc.perform(post("/api/forum/{forumID}/message", forumID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(forumMessagePayload))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void sendMessageToForum_userNotSubscribedShouldReturnConflict() throws Exception {

        long forumID = 2L;
        String forumMessagePayload = """
                    {
                        "body": "This is a test message"
                    }
                """;

        mockMvc.perform(post("/api/forum/{forumID}/message", forumID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(forumMessagePayload))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void sendMessageToForum_ShouldReturnNotFound() throws Exception {

        long forumID = 999L;
        String forumMessagePayload = """
                    {
                        "body": "This is a test message"
                    }
                """;

        mockMvc.perform(post("/api/forum/{forumID}/message", forumID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(forumMessagePayload))
                .andExpect(status().isNotFound());
    }

    //SPRINT 2 STUDENT 3 (1)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllSubscribersFromForum_ShouldReturnSubscribers() throws Exception {
        long forumID = 1L;
        String expectedResponse = """
                [
                    {
                        "name": "John",
                        "secondName": "Doe",
                        "email": "john.doe1@example.com",
                        "dateOfBirth": "1990-01-01",
                        "dateOfRegistration": "2023-10-01",
                        "role": {
                            "enumID": 1,
                            "name": "ADMIN"
                        }
                    },
                    {
                        "name": "Jane",
                        "secondName": "Smith",
                        "email": "jane.smith2@example.com",
                        "dateOfBirth": "1992-02-02",
                        "dateOfRegistration": "2023-10-02",
                        "role": {
                            "enumID": 2,
                            "name": "PREMIUM"
                        }
                    },
                    {
                        "name": "Robert",
                        "secondName": "Brown",
                        "email": "robert.brown3@example.com",
                        "dateOfBirth": "1988-03-03",
                        "dateOfRegistration": "2023-10-03",
                        "role": {
                            "enumID": 3,
                            "name": "FREE"
                        }
                    }
                ]
                """;

        mockMvc.perform(get("/api/forum/{id}/subscribers", forumID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllSubscribersFromForum_invalidForum_ShouldReturnNotFound() throws Exception {
        long forumID = 999L;

        mockMvc.perform(get("/api/forum/{id}/subscribers", forumID))
                .andExpect(status().isNotFound())
                .andDo(print());
    }



    //SPRINT 2 STUDENT 3 (2)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateReadMessage_ShouldMarkMessageAsRead() throws Exception {
        long messageID = 1L;
        mockMvc.perform(post("/api/forum/forumMessage/{messageID}/read", messageID))
                .andExpect(status().isOk());

        boolean isReaded = forumMessageRepository.findById((messageID)).get().getReadBy().stream()
                .anyMatch(reader -> reader.getEmail().equals("john.doe1@example.com"));
        assertTrue(isReaded);

    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateReadMessage_invalidMessage_ShouldReturnNotFound() throws Exception {
        long messageID = Long.MAX_VALUE;
        mockMvc.perform(post("/api/forum/forumMessage/{messageID}/read", messageID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateReadPrivateMessage_ShouldMarkPrivateMessageAsRead() throws Exception {
        long messageID = 1L;
        mockMvc.perform(post("/api/forum/privateMessage/{messageID}/read", messageID))
                .andExpect(status().isOk());

        assertTrue(privateMessageRepository.findById((messageID)).get().isRead());
    }

    @Test
    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateReadPrivateMessage_InvalidReceiver_ShouldReturnConflict() throws Exception {
        long messageID = 1L;
        mockMvc.perform(post("/api/forum/privateMessage/{messageID}/read", messageID))
                .andExpect(status().isUnauthorized());

        assertFalse(privateMessageRepository.findById((messageID)).get().isRead());
    }

    @Test
    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateReadPrivateMessage_AlreadyReaded_ShouldReturnConflict() throws Exception {
        long messageID = 3L;
        assertTrue(privateMessageRepository.findById((messageID)).get().isRead());

        mockMvc.perform(post("/api/forum/forumMessage/{messageID}/read", messageID))
                .andExpect(status().isConflict());

    }


    //SPRINT 2 STUDENT 3 (3)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void sendDirectMessage_ShouldSendMessage() throws Exception {
        long forumID = 1L;
        String body = """
                {
                    "body": "Hello, this is a direct message",
                        "receiverEmail": "jane.smith2@example.com"
                }
                """;

        mockMvc.perform(post("/api/forum/{forumID}/sendDirectMessage", forumID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void sendDirectMessage_InvalidReceiver_ShouldReturnNotFound() throws Exception {
        long forumID = Long.MAX_VALUE;
        String body = """
                {
                    "body": "Hello, this is a direct message",
                        "receiverEmail": "notFound@example.com"
                }
                """;
        mockMvc.perform(post("/api/forum/{forumID}/sendDirectMessage", forumID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void sendDirectMessage_UsersNotInSameForum_ShouldReturnNotFound() throws Exception {
        long forumID = 1L;
        String body = """
                {
                    "body": "Hello, this is a direct message",
                        "receiverEmail": "emily.davis4@example.com"
                }
                """;

        mockMvc.perform(post("/api/forum/{forumID}/sendDirectMessage", forumID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    //SPRINT 2 STUDENT 3 (4)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void replayDirectMessage_WithValidData_ShouldSendReply() throws Exception {
        long forumId = 1L;
        long messageID = 1L;
        String body = """
                {
                    "body": "Hey, are you interested in trading stickers?",
                    "receiverEmail": "jane.smith2@example.com"
                }
                """;

        mockMvc.perform(post("/api/forum/{forumId}/message/{messageID}/replyDirectMessage", forumId, messageID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void replayDirectMessage_InvalidMessageId_ShouldReturnNotFound() throws Exception {
        long forumId = 1L;
        long messageID = Long.MAX_VALUE;
        String body = """
                {
                    "body": "Hey, are you interested in trading stickers?",
                        "receiverEmail": "jane.smith2@example.com"
                }
                """;

        mockMvc.perform(post("/api/forum/{forumId}/message/{messageID}/replyDirectMessage", forumId, messageID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void replayDirectMessage_InvalidReceiverEmail_ShouldReturnNotFound() throws Exception {
        long forumId = 1L;
        long messageID = 1L;
        String body = """
                {
                    "body": "Hey, are you interested in trading stickers?",
                        "receiverEmail": "notFound@example.com"
                }
                """;

        mockMvc.perform(post("/api/forum/{forumId}/message/{messageID}/replyDirectMessage", forumId, messageID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void replayDirectMessage_InvalidMessageBody_ShouldReturnNotFound() throws Exception {
        long forumId = 1L;
        long messageID = 1L;
        String body = """
                {
                    "body": " ",
                        "receiverEmail": "jane.smith2@example.com"
                }
                """;

        mockMvc.perform(post("/api/forum/{forumId}/message/{messageID}/replyDirectMessage", forumId, messageID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    //SPRINT 2 STUDENT 3 (5)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllMessages_ShouldReturnMessagesByDate() throws Exception {

        String expectedMessages = """
                [
                    {
                        "forumMessageID": 6,
                        "body": "Welcome to the Album 2 Forum!",
                        "sendDate": "2024-11-15T10:20:00",
                        "forumId": 2,
                        "senderEmail": "emily.davis4@example.com",
                        "replyTo": null,
                        "readBy": false
                    },
                    {
                        "forumMessageID": 5,
                        "body": "Thanks for the welcome! Let’s trade.",
                        "sendDate": "2024-11-15T10:10:00",
                        "forumId": 1,
                        "senderEmail": "robert.brown3@example.com",
                        "replyTo": 1,
                        "readBy": false
                    },
                    {
                        "forumMessageID": 9,
                        "body": "Let’s trade stickers and complete our albums!",
                        "sendDate": "2024-11-15T10:10:00",
                        "forumId": 3,
                        "senderEmail": "robert.brown3@example.com",
                        "replyTo": 1,
                        "readBy": false
                    },
                    {
                        "forumMessageID": 4,
                        "body": "Looking forward to trading Pokemon stickers!",
                        "sendDate": "2024-11-15T10:05:00",
                        "forumId": 1,
                        "senderEmail": "jane.smith2@example.com",
                        "replyTo": null,
                        "readBy": false
                    },
                    {
                        "forumMessageID": 8,
                        "body": "Excited to collect all Saiyan stickers!",
                        "sendDate": "2024-11-15T10:05:00",
                        "forumId": 3,
                        "senderEmail": "jane.smith2@example.com",
                        "replyTo": null,
                        "readBy": false
                    },
                    {
                        "forumMessageID": 3,
                        "body": "Welcome to the Album Pokemon Forum!",
                        "sendDate": "2024-11-15T10:00:00",
                        "forumId": 1,
                        "senderEmail": "john.doe1@example.com",
                        "replyTo": null,
                        "readBy": true
                    },
                    {
                        "forumMessageID": 7,
                        "body": "Welcome to the Dragon Ball Forum!",
                        "sendDate": "2024-11-15T10:00:00",
                        "forumId": 3,
                        "senderEmail": "john.doe1@example.com",
                        "replyTo": null,
                        "readBy": false
                    }
                ]
                """;

        mockMvc.perform(get("/api/forum/publishMessage")
                        .param("date", "2024-11-01"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedMessages, true))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void getAllMessages_InvalidRole_ShouldReturnForbidden() throws Exception {


        mockMvc.perform(get("/api/forum/publishMessage")
                        .param("date", "2024-11-01"))
                .andExpect(status().isForbidden()).andDo(print());
    }

    //SPRINT 4 STUDENT 2 (2)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void shouldSendSingleTimeMessageAndMarkAsRead() throws Exception {
        long forumId = 1L;
        PrivateMessageDTO privateMessageDTO = new PrivateMessageDTO();
        privateMessageDTO.setReceiverEmail("jane.smith2@example.com");
        privateMessageDTO.setBody("This is a single-time message test.");

        String privateMessageDTOJson = objectMapper.writeValueAsString(privateMessageDTO);

        mockMvc.perform(post("/api/forum/{forumId}/singleTimeMessage", forumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(privateMessageDTOJson))
                .andExpect(status().isOk())
                .andDo(print());

        PrivateMessage savedMessage = privateMessageRepository.findAll().stream()
                .filter(message -> message.getBody().equals("This is a single-time message test."))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Message was not saved correctly"));

        assertEquals("This is a single-time message test.", savedMessage.getBody(), "Message body should match");
        assertEquals("jane.smith2@example.com", savedMessage.getReceiver().getEmail(), "Receiver email should match");
        assertEquals("john.doe1@example.com", savedMessage.getSender().getEmail(), "Sender email should match");
        assertTrue(savedMessage.isOneTimeMessage(), "Message should be marked as one-time");
        assertNotNull(savedMessage.getSendDate(), "Send date should be set");

    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateReadMessage_OneTimeMessage_ShouldDeleteIt() throws Exception {
        long messageID = 5L;
        mockMvc.perform(post("/api/forum/privateMessage/{messageID}/read", messageID));
        boolean found = forumMessageRepository.findById((messageID)).get().getReadBy().stream()
                .anyMatch(reader -> reader.getEmail().equals("john.doe1@example.com"));
        assertFalse(found);
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void shouldFailToSendSingleTimeMessageWhenUserNotSubscribed() throws Exception {
        mockMvc.perform(post("/api/forum/2/singleTimeMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "body": "This is a single-time message test.",
                                  "receiverEmail": "jane.smith2@example.com"
                                }
                                """))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void shouldFailToSendSingleTimeMessageWhenReceiverNotFound() throws Exception {
        mockMvc.perform(post("/api/forum/1/singleTimeMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                                {
                                  "body": "This is a single-time message test.",
                                  "receiverEmail": "nonexistent.user@example.com"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    //SPRINT 4 STUDENT 2 (4)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void shouldGetAllForumsOrderedByMessagesSuccessfully() throws Exception {
        mockMvc.perform(get("/api/forum/allOrderedByMessages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].albumName").value("Album Pokemon"))
                .andExpect(jsonPath("$[0].forumID").value(1))
                .andExpect(jsonPath("$[0].albumID").value(1))
                .andExpect(jsonPath("$[1].albumName").value("Album Dragon Ball"))
                .andExpect(jsonPath("$[1].forumID").value(3))
                .andExpect(jsonPath("$[1].albumID").value(3))
                .andExpect(jsonPath("$[2].albumName").value("Album 2"))
                .andExpect(jsonPath("$[2].forumID").value(2))
                .andExpect(jsonPath("$[2].albumID").value(2))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void shouldGetAllForumsOrderedByMessages_InvalidUser() throws Exception {
        mockMvc.perform(get("/api/forum/allOrderedByMessages"))
                .andExpect(status().isForbidden());
    }
}