package com.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labproject.LabProjectApplication;
import com.labproject.application.dto.input.CollectionUserDTO;
import com.labproject.application.dto.input.UpdateUserDTO;
import com.labproject.domain.CollectionUser;
import com.labproject.persistence.UserRepository;
import com.labproject.security.authentication.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabProjectApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthenticationService authenticationService;


    private CollectionUserDTO validUserDTO;
    private CollectionUserDTO invalidUserDTO;

    //SPRINT 1 STUDENT 1 (1)

    @Test
    @DirtiesContext
    void testRegisterUser_ShouldRegisterUser() throws Exception {
        validUserDTO = new CollectionUserDTO();
        validUserDTO.setName("Alice");
        validUserDTO.setSecondName("Green");
        validUserDTO.setEmail("alice.green@example.com");
        validUserDTO.setPassword("strongpaSsword2324");
        validUserDTO.setDateOfBirth(LocalDate.of(1995, 5, 5));
        validUserDTO.setDateOfRegistration(LocalDate.now());
        String userJson = objectMapper.writeValueAsString(validUserDTO);
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andDo(print());

        Optional<CollectionUser> registeredUser = userRepository.findByEmail(validUserDTO.getEmail());
        assertTrue(registeredUser.isPresent());
        assertEquals(validUserDTO.getName(), registeredUser.get().getName());
        assertEquals(validUserDTO.getSecondName(), registeredUser.get().getSecondName());
    }


    @Test
    void registerUser_InvalidUserDTO_ShouldReturnBadRequest() throws Exception {
        invalidUserDTO = new CollectionUserDTO();
        invalidUserDTO.setName("");
        invalidUserDTO.setSecondName("Green");
        invalidUserDTO.setEmail("alice.green@example.com");
        invalidUserDTO.setPassword("strongpassWord242");
        invalidUserDTO.setDateOfBirth(LocalDate.of(1995, 5, 5));
        invalidUserDTO.setDateOfRegistration(LocalDate.now());
        String userJson = objectMapper.writeValueAsString(invalidUserDTO);
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_InvalidUserDTO_InvalidDate_ShouldReturnBadRequest() throws Exception {
        invalidUserDTO = new CollectionUserDTO();
        invalidUserDTO.setName("Jhon");
        invalidUserDTO.setSecondName("Green");
        invalidUserDTO.setEmail("alice.green@example.com");
        invalidUserDTO.setPassword("strongpassWord242");
        invalidUserDTO.setDateOfBirth(LocalDate.of(2019, 5, 5));
        invalidUserDTO.setDateOfRegistration(LocalDate.now());
        String userJson = objectMapper.writeValueAsString(invalidUserDTO);
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_InvalidUserDTO_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        invalidUserDTO = new CollectionUserDTO();
        invalidUserDTO.setName("Mary");
        invalidUserDTO.setSecondName("Green");
        invalidUserDTO.setEmail("alice.greenexample.com");
        invalidUserDTO.setPassword("strongpassWord242");
        invalidUserDTO.setDateOfBirth(LocalDate.of(2010, 5, 5));
        invalidUserDTO.setDateOfRegistration(LocalDate.now());
        String userJson = objectMapper.writeValueAsString(invalidUserDTO);
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }


    //SPRINT 1 STUDENT 1 (2)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateUser_ShouldUpdate() throws Exception {

        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setName("Jose");
        userDTO.setSecondName("Maria");
        userDTO.setPassword("ValidPassword12");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isOk());

        CollectionUser updatedUser = userRepository.findByEmail("john.doe1@example.com").orElseThrow(() ->
                new AssertionError("User not found in the database."));

        assertEquals("Jose", updatedUser.getName());
        assertEquals("Maria", updatedUser.getSecondName());

        assertTrue(passwordEncoder.matches("ValidPassword12", updatedUser.getPassword()));
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateUser_ShouldThrowException_InvalidEmail() throws Exception {

        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setName("Jose");
        userDTO.setSecondName("Maria");
        userDTO.setPassword("ValidPassword12");
        userDTO.setEmail("invalid.email");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateUser_ShouldThrowException_InvalidPassword() throws Exception {

        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setPassword("invalidPassword");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void updateUser_ShouldThrowException_InvalidNameAndSecondName() throws Exception {

        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setSecondName("");
        userDTO.setName("");


        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDTO);


        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isBadRequest());
    }


    //SPRINT 1 STUDENT 1 (3)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void testGetAllUsers_AsAdmin_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(20));
    }

    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void testGetAllUsers_AsFreeUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/user/all"))
                .andExpect(status().isForbidden());
    }

    //SPRINT 1 STUDENT 1 (4)

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void testGetLastRegisteredUsers_Last5Days_ShouldReturnCorrectCount() throws Exception {

        LocalDate referenceDate = LocalDate.of(2023, 10, 14);


        mockMvc.perform(get("/api/user/lastUsersRegistered")
                        .param("date", referenceDate.toString()))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].secondName").value("Gonzalez"))
                .andExpect(jsonPath("$[1].secondName").value("Rodriguez"))
                .andExpect(jsonPath("$[2].secondName").value("Martinez"))
                .andExpect(jsonPath("$[3].secondName").value("Robinson"))
                .andExpect(jsonPath("$[4].secondName").value("Clark"))
                .andExpect(jsonPath("$[5].secondName").value("Lewis"));
    }


    //SPRINT 4 STUDENT 2 (1)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @DirtiesContext
    void shouldBlockUserSuccessfully() throws Exception {
        String emailToBlock = "jane.smith2@example.com";

        mockMvc.perform(post("/api/user/block")
                        .param("email", emailToBlock))
                .andExpect(status().isOk());

        CollectionUser user = userRepository.findByEmail("john.doe1@example.com")
                .orElseThrow(() -> new AssertionError("Authenticated user not found"));

        CollectionUser blockedUser = userRepository.findByEmail(emailToBlock)
                .orElseThrow(() -> new AssertionError("Blocked user not found"));

        boolean isBlocked = user.getBlockedUsers().stream()
                .anyMatch(blocked -> blocked.getEmail().equals(blockedUser.getEmail()));
        assertTrue(isBlocked, "The blocked user should be in the blocked users list");

    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void BlockUser_ShouldThrowException_NonExist() throws Exception {
        String emailToBlock = "nonExistan@example.com";

        mockMvc.perform(post("/api/user/block")
                        .param("email", emailToBlock))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @DirtiesContext
    void BlockUse_ShouldThrowError_InvalidToBlockYourself() throws Exception {
        String emailToBlock = "john.doe1@example.com";

        mockMvc.perform(post("/api/user/block")
                        .param("email", emailToBlock))
                .andExpect(status().isConflict());

    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @DirtiesContext
    void BlockUser_ShouldThrowException_AlreadyBlockedUser() throws Exception {
        String emailToBlock = "jane.smith2@example.com";

        mockMvc.perform(post("/api/user/block")
                        .param("email", emailToBlock))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/user/block")
                        .param("email", emailToBlock))
                .andExpect(status().isConflict());


    }


    //SPRINT 4 STUDENT 2 (3)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void testGetAllOrderedByActivity() throws Exception {
        mockMvc.perform(get("/api/user/allOrderedByActivity"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            [
                                {"name":"John","secondName":"Doe","email":"john.doe1@example.com","dateOfBirth":"1990-01-01","dateOfRegistration":"2023-10-01","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Jane","secondName":"Smith","email":"jane.smith2@example.com","dateOfBirth":"1992-02-02","dateOfRegistration":"2023-10-02","role":{"enumID":2,"name":"PREMIUM"}},
                                {"name":"Robert","secondName":"Brown","email":"robert.brown3@example.com","dateOfBirth":"1988-03-03","dateOfRegistration":"2023-10-03","role":{"enumID":3,"name":"FREE"}},
                                {"name":"Emily","secondName":"Davis","email":"emily.davis4@example.com","dateOfBirth":"1991-04-04","dateOfRegistration":"2023-10-04","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Michael","secondName":"Wilson","email":"michael.wilson5@example.com","dateOfBirth":"1993-05-05","dateOfRegistration":"2023-10-05","role":{"enumID":2,"name":"PREMIUM"}},
                                {"name":"Sarah","secondName":"Taylor","email":"sarah.taylor6@example.com","dateOfBirth":"1990-06-06","dateOfRegistration":"2023-10-06","role":{"enumID":3,"name":"FREE"}},
                                {"name":"David","secondName":"Anderson","email":"david.anderson7@example.com","dateOfBirth":"1987-07-07","dateOfRegistration":"2023-10-07","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Laura","secondName":"Thomas","email":"laura.thomas8@example.com","dateOfBirth":"1994-08-08","dateOfRegistration":"2023-10-08","role":{"enumID":2,"name":"PREMIUM"}},
                                {"name":"James","secondName":"Jackson","email":"james.jackson9@example.com","dateOfBirth":"1992-09-09","dateOfRegistration":"2023-10-09","role":{"enumID":3,"name":"FREE"}},
                                {"name":"Emma","secondName":"White","email":"emma.white10@example.com","dateOfBirth":"1989-10-10","dateOfRegistration":"2023-10-10","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Olivia","secondName":"Harris","email":"olivia.harris11@example.com","dateOfBirth":"1991-11-11","dateOfRegistration":"2023-10-11","role":{"enumID":2,"name":"PREMIUM"}},
                                {"name":"Lucas","secondName":"Martin","email":"lucas.martin12@example.com","dateOfBirth":"1988-12-12","dateOfRegistration":"2023-10-12","role":{"enumID":3,"name":"FREE"}},
                                {"name":"Sophia","secondName":"Lee","email":"sophia.lee13@example.com","dateOfBirth":"1990-01-13","dateOfRegistration":"2023-10-13","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Ethan","secondName":"Perez","email":"ethan.perez14@example.com","dateOfBirth":"1993-02-14","dateOfRegistration":"2023-10-14","role":{"enumID":2,"name":"PREMIUM"}},
                                {"name":"Isabella","secondName":"Gonzalez","email":"isabella.gonzalez15@example.com","dateOfBirth":"1989-03-15","dateOfRegistration":"2023-10-15","role":{"enumID":3,"name":"FREE"}},
                                {"name":"Mason","secondName":"Rodriguez","email":"mason.rodriguez16@example.com","dateOfBirth":"1992-04-16","dateOfRegistration":"2023-10-16","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Ava","secondName":"Martinez","email":"ava.martinez17@example.com","dateOfBirth":"1987-05-17","dateOfRegistration":"2023-10-17","role":{"enumID":2,"name":"PREMIUM"}},
                                {"name":"Logan","secondName":"Robinson","email":"logan.robinson18@example.com","dateOfBirth":"1994-06-18","dateOfRegistration":"2023-10-18","role":{"enumID":3,"name":"FREE"}},
                                {"name":"Mia","secondName":"Clark","email":"mia.clark19@example.com","dateOfBirth":"1990-07-19","dateOfRegistration":"2023-10-19","role":{"enumID":1,"name":"ADMIN"}},
                                {"name":"Benjamin","secondName":"Lewis","email":"benjamin.lewis20@example.com","dateOfBirth":"1991-08-20","dateOfRegistration":"2023-10-20","role":{"enumID":2,"name":"PREMIUM"}}
                            ]
                        """));
    }


    //SPRINT 4 STUDENT 3 (1)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    void shouldGetTop3UsersWithMostExchangeableStickers() throws Exception {
        String expectedResponse = """
                [
                  {
                    "firstName": "Jane",
                    "lastName": "Smith",
                    "email": "jane.smith2@example.com",
                    "visualizeCollectionSticker": [
                      {
                        "collectionStickerId": 1,
                        "sticker": {
                          "stickerID": 1,
                          "numberInAlbum": 1,
                          "name": "Charmander",
                          "description": "Pokémon de tipo fuego inicial de la región Kanto",
                          "imageURL": "url_charmander",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 5
                      },
                      {
                        "collectionStickerId": 3,
                        "sticker": {
                          "stickerID": 7,
                          "numberInAlbum": 7,
                          "name": "Poliwag",
                          "description": "Pokémon de tipo agua con un espiral en su estómago",
                          "imageURL": "url_poliwag",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      },
                      {
                        "collectionStickerId": 5,
                        "sticker": {
                          "stickerID": 12,
                          "numberInAlbum": 12,
                          "name": "Tangela",
                          "description": "Pokémon de tipo planta cubierto de enredaderas",
                          "imageURL": "url_tangela",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 7
                      },
                      {
                        "collectionStickerId": 6,
                        "sticker": {
                          "stickerID": 2,
                          "numberInAlbum": 2,
                          "name": "Vulpix",
                          "description": "Pokémon de tipo fuego con múltiples colas",
                          "imageURL": "url_vulpix",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 3
                      },
                      {
                        "collectionStickerId": 9,
                        "sticker": {
                          "stickerID": 5,
                          "numberInAlbum": 5,
                          "name": "Squirtle",
                          "description": "Pokémon de tipo agua inicial de la región Kanto",
                          "imageURL": "url_squirtle",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 2
                      }
                    ]
                  },
                  {
                    "firstName": "Emily",
                    "lastName": "Davis",
                    "email": "emily.davis4@example.com",
                    "visualizeCollectionSticker": [
                      {
                        "collectionStickerId": 1,
                        "sticker": {
                          "stickerID": 1,
                          "numberInAlbum": 1,
                          "name": "Charmander",
                          "description": "Pokémon de tipo fuego inicial de la región Kanto",
                          "imageURL": "url_charmander",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 5
                      },
                      {
                        "collectionStickerId": 3,
                        "sticker": {
                          "stickerID": 7,
                          "numberInAlbum": 7,
                          "name": "Poliwag",
                          "description": "Pokémon de tipo agua con un espiral en su estómago",
                          "imageURL": "url_poliwag",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      },
                      {
                        "collectionStickerId": 5,
                        "sticker": {
                          "stickerID": 12,
                          "numberInAlbum": 12,
                          "name": "Tangela",
                          "description": "Pokémon de tipo planta cubierto de enredaderas",
                          "imageURL": "url_tangela",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 7
                      },
                      {
                        "collectionStickerId": 15,
                        "sticker": {
                          "stickerID": 9,
                          "numberInAlbum": 9,
                          "name": "Bulbasaur",
                          "description": "Pokémon de tipo planta inicial de la región Kanto",
                          "imageURL": "url_bulbasaur",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 10
                      },
                      {
                        "collectionStickerId": 17,
                        "sticker": {
                          "stickerID": 6,
                          "numberInAlbum": 6,
                          "name": "Psyduck",
                          "description": "Pokémon de tipo agua conocido por sus dolores de cabeza",
                          "imageURL": "url_psyduck",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      }
                    ]
                  },
                  {
                    "firstName": "David",
                    "lastName": "Anderson",
                    "email": "david.anderson7@example.com",
                    "visualizeCollectionSticker": [
                      {
                        "collectionStickerId": 1,
                        "sticker": {
                          "stickerID": 1,
                          "numberInAlbum": 1,
                          "name": "Charmander",
                          "description": "Pokémon de tipo fuego inicial de la región Kanto",
                          "imageURL": "url_charmander",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 5
                      },
                      {
                        "collectionStickerId": 3,
                        "sticker": {
                          "stickerID": 7,
                          "numberInAlbum": 7,
                          "name": "Poliwag",
                          "description": "Pokémon de tipo agua con un espiral en su estómago",
                          "imageURL": "url_poliwag",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      },
                      {
                        "collectionStickerId": 5,
                        "sticker": {
                          "stickerID": 12,
                          "numberInAlbum": 12,
                          "name": "Tangela",
                          "description": "Pokémon de tipo planta cubierto de enredaderas",
                          "imageURL": "url_tangela",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 7
                      },
                      {
                        "collectionStickerId": 19,
                        "sticker": {
                          "stickerID": 11,
                          "numberInAlbum": 11,
                          "name": "Bellsprout",
                          "description": "Pokémon de tipo planta con cuerpo de tallo",
                          "imageURL": "url_bellsprout",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 8
                      }
                    ]
                  }
                ]
                """

                /*"""
                [
                  {
                    "firstName": "Jane",
                    "lastName": "Smith",
                    "email": "jane.smith2@example.com",
                    "visualizeCollectionSticker": [
                      {
                        "collectionStickerId": 1,
                        "sticker": {
                          "stickerID": 1,
                          "numberInAlbum": 1,
                          "name": "Charmander",
                          "description": "Pokémon de tipo fuego inicial de la región Kanto",
                          "imageURL": "url_charmander",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 5
                      },
                      {
                        "collectionStickerId": 3,
                        "sticker": {
                          "stickerID": 7,
                          "numberInAlbum": 7,
                          "name": "Poliwag",
                          "description": "Pokémon de tipo agua con un espiral en su estómago",
                          "imageURL": "url_poliwag",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      },
                      {
                        "collectionStickerId": 5,
                        "sticker": {
                          "stickerID": 12,
                          "numberInAlbum": 12,
                          "name": "Tangela",
                          "description": "Pokémon de tipo planta cubierto de enredaderas",
                          "imageURL": "url_tangela",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 7
                      },
                      {
                        "collectionStickerId": 6,
                        "sticker": {
                          "stickerID": 2,
                          "numberInAlbum": 2,
                          "name": "Vulpix",
                          "description": "Pokémon de tipo fuego con múltiples colas",
                          "imageURL": "url_vulpix",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 3
                      },
                      {
                        "collectionStickerId": 9,
                        "sticker": {
                          "stickerID": 5,
                          "numberInAlbum": 5,
                          "name": "Squirtle",
                          "description": "Pokémon de tipo agua inicial de la región Kanto",
                          "imageURL": "url_squirtle",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 2
                      }
                    ]
                  },
                  {
                    "firstName": "Emily",
                    "lastName": "Davis",
                    "email": "emily.davis4@example.com",
                    "visualizeCollectionSticker": [
                      {
                        "collectionStickerId": 1,
                        "sticker": {
                          "stickerID": 1,
                          "numberInAlbum": 1,
                          "name": "Charmander",
                          "description": "Pokémon de tipo fuego inicial de la región Kanto",
                          "imageURL": "url_charmander",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 5
                      },
                      {
                        "collectionStickerId": 3,
                        "sticker": {
                          "stickerID": 7,
                          "numberInAlbum": 7,
                          "name": "Poliwag",
                          "description": "Pokémon de tipo agua con un espiral en su estómago",
                          "imageURL": "url_poliwag",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      },
                      {
                        "collectionStickerId": 5,
                        "sticker": {
                          "stickerID": 12,
                          "numberInAlbum": 12,
                          "name": "Tangela",
                          "description": "Pokémon de tipo planta cubierto de enredaderas",
                          "imageURL": "url_tangela",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 7
                      },
                      {
                        "collectionStickerId": 15,
                        "sticker": {
                          "stickerID": 9,
                          "numberInAlbum": 9,
                          "name": "Bulbasaur",
                          "description": "Pokémon de tipo planta inicial de la región Kanto",
                          "imageURL": "url_bulbasaur",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 10
                      },
                      {
                        "collectionStickerId": 17,
                        "sticker": {
                          "stickerID": 6,
                          "numberInAlbum": 6,
                          "name": "Psyduck",
                          "description": "Pokémon de tipo agua conocido por sus dolores de cabeza",
                          "imageURL": "url_psyduck",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      }
                    ]
                  },
                  {
                    "firstName": "Michael",
                    "lastName": "Wilson",
                    "email": "michael.wilson5@example.com",
                    "visualizeCollectionSticker": [
                      {
                        "collectionStickerId": 1,
                        "sticker": {
                          "stickerID": 1,
                          "numberInAlbum": 1,
                          "name": "Charmander",
                          "description": "Pokémon de tipo fuego inicial de la región Kanto",
                          "imageURL": "url_charmander",
                          "typeOfSticker": "Tipo Fuego"
                        },
                        "quantity": 5
                      },
                      {
                        "collectionStickerId": 3,
                        "sticker": {
                          "stickerID": 7,
                          "numberInAlbum": 7,
                          "name": "Poliwag",
                          "description": "Pokémon de tipo agua con un espiral en su estómago",
                          "imageURL": "url_poliwag",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 4
                      },
                      {
                        "collectionStickerId": 5,
                        "sticker": {
                          "stickerID": 12,
                          "numberInAlbum": 12,
                          "name": "Tangela",
                          "description": "Pokémon de tipo planta cubierto de enredaderas",
                          "imageURL": "url_tangela",
                          "typeOfSticker": "Tipo Planta"
                        },
                        "quantity": 7
                      },
                      {
                        "collectionStickerId": 18,
                        "sticker": {
                          "stickerID": 6,
                          "numberInAlbum": 6,
                          "name": "Psyduck",
                          "description": "Pokémon de tipo agua conocido por sus dolores de cabeza",
                          "imageURL": "url_psyduck",
                          "typeOfSticker": "Tipo Agua"
                        },
                        "quantity": 3
                      }
                    ]
                  }
                ]
                """;*/;
        mockMvc.perform(get("/api/user/exchange/topCandidates"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse))
                .andDo(print());
    }


}






