package com.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labproject.LabProjectApplication;
import com.labproject.application.AlbumService;
import com.labproject.application.dto.input.AlbumDTO;
import com.labproject.application.dto.input.AlbumSectionDTO;
import com.labproject.application.dto.input.StickerDTO;
import com.labproject.domain.*;
import com.labproject.persistence.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabProjectApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AlbumTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private StickerRepository stickerRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private CollectionStickerRepository collectionStickerRepository;
    @Autowired
    private SaleStickerRepository saleStickerRepository;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private CollectionAlbumRepository collectionAlbumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    //SPRINT 1 STUDENT 2 (1) AND SPRINT 1 STUDENT 3 (1)

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void compareCreateFullAlbum_withSectionsAndStickers_ShouldReturnCreatedStatus() throws Exception {

        StickerDTO sticker1 = new StickerDTO("Sticker 1", "Description 1", "ImageURL1", 1, "type");
        StickerDTO sticker2 = new StickerDTO("Sticker 2", "Description 2", "ImageURL2", 2, "type2");


        AlbumSectionDTO section1 = new AlbumSectionDTO("Section 1", "Description for section 1", List.of(sticker1));
        AlbumSectionDTO section2 = new AlbumSectionDTO("Section 2", "Description for section 2", List.of(sticker2));

        AlbumDTO album = new AlbumDTO("Album Title", "Album Description", LocalDate.now(), LocalDate.now().plusMonths(1), true, List.of(section1, section2));

        int initialSize = albumRepository.findAll().size();

        mockMvc.perform(post("/api/albums/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(album)))
                .andExpect(status().isCreated());

        List<Album> persistedAlbums = albumRepository.findAll();
        assertEquals(initialSize + 1, persistedAlbums.size());

        Album persistedAlbum = persistedAlbums.get(albumRepository.findAll().size() - 1);
        assertEquals("Album Title", persistedAlbum.getTitle());
        assertEquals("Album Description", persistedAlbum.getDescription());
        assertEquals(LocalDate.now(), persistedAlbum.getBeginDate());
        assertEquals(LocalDate.now().plusMonths(1), persistedAlbum.getEndingDate());
        assertTrue(persistedAlbum.getPublicAvailability());

        List<AlbumSection> persistedSections = persistedAlbum.getSections();
        assertEquals(2, persistedSections.size());

        assertTrue(persistedSections.stream().anyMatch(section ->
                section.getTitle().equals("Section 1") && section.getDescription().equals("Description for section 1")
        ));
        assertTrue(persistedSections.stream().anyMatch(section ->
                section.getTitle().equals("Section 2") && section.getDescription().equals("Description for section 2")
        ));

        List<Sticker> persistedStickers1 = persistedAlbum.getSections().getFirst().getStickers();
        List<Sticker> persistedStickers2 = persistedAlbum.getSections().get(1).getStickers();

        assertTrue(persistedStickers1.getFirst().getName().equals("Sticker 1") && sticker1.getDescription().equals("Description 1")
        );
        assertTrue(persistedStickers2.getFirst().getName().equals("Sticker 2") && sticker2.getDescription().equals("Description 2")
        );
    }


    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void createAlbum_InvalidAlbumDTO_ShouldReturnBadRequestStatus() throws Exception {
        AlbumDTO invalidAlbum = new AlbumDTO("hello", null, null, null, true, List.of());

        mockMvc.perform(post("/api/albums/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAlbum)))
                .andExpect(status().isBadRequest());
    }

    //SPRINT 1 STUDENT 2 (2)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getInfoAllAlbums_ShouldReturnCorrectAlbums() throws Exception {
        String expectedResponse = """
                [
                    {
                        "name": "Album Pokemon",
                        "beginDate": "2024-01-01",
                        "endingDate": "2025-01-01",
                        "visualizeSectionDTOList": [
                            [
                                { "sectionName": "Tipo Fuego", "numberOfStickers": 4 },
                                { "sectionName": "Tipo Agua", "numberOfStickers": 4 },
                                { "sectionName": "Tipo Planta", "numberOfStickers": 4 }
                            ]
                        ]
                    },
                    {
                        "name": "Album 2",
                        "beginDate": "2024-01-01",
                        "endingDate": "2025-01-01",
                        "visualizeSectionDTOList": [
                            [
                                { "sectionName": "Sección 1 del Album 2", "numberOfStickers": 0 }
                            ]
                        ]
                    },
                    {
                        "name": "Album Dragon Ball",
                        "beginDate": "2023-01-01",
                        "endingDate": "2024-01-01",
                        "visualizeSectionDTOList": [
                            [
                                { "sectionName": "Saiyans", "numberOfStickers": 4 },
                                { "sectionName": "Namekians", "numberOfStickers": 3 },
                                { "sectionName": "Villains", "numberOfStickers": 3 }
                            ]
                        ]
                    }
                ]
                """;

        mockMvc.perform(get("/api/albums/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }


    //SPRINT 1 STUDENT 2 (3)
    @Test
    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getAllAvailableAlbums_ShouldReturnCorrectAvailableAlbums() throws Exception {

        String expectedResponse = """
                [
                    {
                        "name": "Album Pokemon",
                        "beginDate": "2024-01-01",
                        "endingDate": "2025-01-01",
                        "visualizeSectionDTOList": [
                            [
                                { "sectionName": "Tipo Fuego", "numberOfStickers": 4 },
                                { "sectionName": "Tipo Agua", "numberOfStickers": 4 },
                                { "sectionName": "Tipo Planta", "numberOfStickers": 4 }
                            ]
                        ]
                    },
                    {
                        "name": "Album 2",
                        "beginDate": "2024-01-01",
                        "endingDate": "2025-01-01",
                        "visualizeSectionDTOList": [
                            [
                                { "sectionName": "Sección 1 del Album 2", "numberOfStickers": 0 }
                            ]
                        ]
                    }
                ]
                """;

        mockMvc.perform(get("/api/albums/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    //SPRINT 2 STUDENT 1 (1)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void changePublicAvailability_ShouldTogglePublicAvailability() throws Exception {
        long albumID = 1L;

        boolean initialPublicAvailability = collectionAlbumRepository.findById(albumID)
                .orElseThrow(() -> new RuntimeException("Album not found"))
                .getPublicAvailability();

        mockMvc.perform(post("/api/albums/" + albumID + "/publicAvailability"))
                .andExpect(status().isOk());

        boolean updatedPublicAvailability = collectionAlbumRepository.findById(albumID)
                .orElseThrow(() -> new RuntimeException("Album not found"))
                .getPublicAvailability();

        assertNotEquals(initialPublicAvailability, updatedPublicAvailability,
                "Availability not changed ");
    }


    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void changePublicAvailability_ShouldReturnNotFoundWhenAlbumDoesNotExist() throws Exception {
        long albumID = 999L;
        mockMvc.perform(post("/api/albums/" + albumID + "/publicAvailability"))
                .andExpect(status().isNotFound());
    }
    //SPRINT 2 STUDENT 1 (2)

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getStickerByID_ShouldReturnOkWhenStickerIsObtainedSuccessfully() throws Exception {
        long albumID = 1L;
        int number = 2;
        mockMvc.perform(get("/api/albums/" + albumID + "/number/" + number))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getStickerAlbumAndNumber_ShouldReturnCorrectSticker() throws Exception {

        long albumID = 1L;
        int number = 1;


        String expectedResponse = """
                {
                    "number": 1,
                    "stickerName": "Charmander",
                    "description": "Pokémon de tipo fuego inicial de la región Kanto",
                    "imageURL": "url_charmander",
                    "typeOfSticker": "Tipo Fuego"
                }
                """;

        mockMvc.perform(get("/api/albums/" + albumID + "/number/" + number))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    @Test
    public void findVisualizeStickerByAlbumAndNumber_ShouldReturnNotFoundError() {
        long nonExistentAlbumId = 99L;
        int nonExistentStickerNumber = 999;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            albumService.getStickerByAlbumAndNumber(nonExistentAlbumId, nonExistentStickerNumber);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Sticker not found"));
    }


    //SPRINT 2 STUDENT 1 (3)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void addCollectionAlbum_ShouldSaveCollectionAlbumInDatabase() throws Exception {
        long albumID = 1L;
        int initialSize = collectionAlbumRepository.findAll().size();

        mockMvc.perform(post("/api/albums/" + albumID + "/collectionAlbum")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //verify  CollectionAlbum was saved
        List<CollectionAlbum> savedAlbums = collectionAlbumRepository.findAll();
        assertEquals(initialSize + 1, savedAlbums.size(), "There should be exactly one album in the collection");

        CollectionAlbum savedAlbum = savedAlbums.get(initialSize);
        assertEquals(albumID, savedAlbum.getAlbum().getAlbumId(), "The album ID should match");
        assertEquals("john.doe1@example.com", savedAlbum.getOwner().getEmail(), "The owner should be the authenticated user");
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void addCollectionAlbum_ShouldReturnNotFound_WhenAlbumDoesNotExist() throws Exception {

        long nonExistentAlbumID = 999L;

        mockMvc.perform(post("/api/albums/" + nonExistentAlbumID + "/collectionAlbum")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    //SPRINT 2 STUDENT 1 (4)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getUsersCollectingAlbum_ShouldReturnThreeUsers() throws Exception {
        long albumID = 1L;

        String expectedResponse = """
                [{"name":"John","secondName":"Doe","email":"john.doe1@example.com","dateOfBirth":"1990-01-01","dateOfRegistration":"2023-10-01","role":{"enumID":1,"name":"ADMIN"}},
                 {"name":"Jane","secondName":"Smith","email":"jane.smith2@example.com","dateOfBirth":"1992-02-02","dateOfRegistration":"2023-10-02","role":{"enumID":2,"name":"PREMIUM"}},
                 {"name":"Robert","secondName":"Brown","email":"robert.brown3@example.com","dateOfBirth":"1988-03-03","dateOfRegistration":"2023-10-03","role":{"enumID":3,"name":"FREE"}},
                 {"name":"Emily","secondName":"Davis","email":"emily.davis4@example.com","dateOfBirth":"1991-04-04","dateOfRegistration":"2023-10-04","role":{"enumID":1,"name":"ADMIN"}},
                 {"name":"Michael","secondName":"Wilson","email":"michael.wilson5@example.com","dateOfBirth":"1993-05-05","dateOfRegistration":"2023-10-05","role":{"enumID":2,"name":"PREMIUM"}},
                 {"name":"David","secondName":"Anderson","email":"david.anderson7@example.com","dateOfBirth":"1987-07-07","dateOfRegistration":"2023-10-07","role":{"enumID":1,"name":"ADMIN"}}]
                """;

        mockMvc.perform(get("/api/albums/" + albumID + "/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getUsersCollectingAlbum_ShouldReturnNotFoundForInvalidAlbum() throws Exception {
        long invalidAlbumId = 999L;

        mockMvc.perform(get("/api/albums/" + invalidAlbumId + "/users"))
                .andExpect(status().isNotFound());
    }

    //SPRINT 2 STUDENT 1 (5)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getAlbumDetails_ShouldReturnExactDetails() throws Exception {
        long albumID = 1L;

        String expectedResponse = """
                {
                    "name": "Album Pokemon",
                    "beginDate": "2024-01-01",
                    "endingDate": "2025-01-01",
                    "visualizeSectionDTOList": [
                        [
                            {
                                "sectionName": "Tipo Fuego",
                                "stickers": [
                                    { "number": 1, "stickerName": "Charmander", "description": "Pokémon de tipo fuego inicial de la región Kanto", "imageURL": "url_charmander", "typeOfSticker": "Tipo Fuego" },
                                    { "number": 2, "stickerName": "Vulpix", "description": "Pokémon de tipo fuego con múltiples colas", "imageURL": "url_vulpix", "typeOfSticker": "Tipo Fuego" },
                                    { "number": 3, "stickerName": "Growlithe", "description": "Pokémon de tipo fuego conocido por su lealtad", "imageURL": "url_growlithe", "typeOfSticker": "Tipo Fuego" },
                                    { "number": 4, "stickerName": "Magmar", "description": "Pokémon de tipo fuego con aspecto volcánico", "imageURL": "url_magmar", "typeOfSticker": "Tipo Fuego" }
                                ]
                            },
                            {
                                "sectionName": "Tipo Agua",
                                "stickers": [
                                    { "number": 5, "stickerName": "Squirtle", "description": "Pokémon de tipo agua inicial de la región Kanto", "imageURL": "url_squirtle", "typeOfSticker": "Tipo Agua" },
                                    { "number": 6, "stickerName": "Psyduck", "description": "Pokémon de tipo agua conocido por sus dolores de cabeza", "imageURL": "url_psyduck", "typeOfSticker": "Tipo Agua" },
                                    { "number": 7, "stickerName": "Poliwag", "description": "Pokémon de tipo agua con un espiral en su estómago", "imageURL": "url_poliwag", "typeOfSticker": "Tipo Agua" },
                                    { "number": 8, "stickerName": "Lapras", "description": "Pokémon de tipo agua que ayuda a cruzar el océano", "imageURL": "url_lapras", "typeOfSticker": "Tipo Agua" }
                                ]
                            },
                            {
                                "sectionName": "Tipo Planta",
                                "stickers": [
                                    { "number": 9, "stickerName": "Bulbasaur", "description": "Pokémon de tipo planta inicial de la región Kanto", "imageURL": "url_bulbasaur", "typeOfSticker": "Tipo Planta" },
                                    { "number": 10, "stickerName": "Oddish", "description": "Pokémon de tipo planta con hojas en la cabeza", "imageURL": "url_oddish", "typeOfSticker": "Tipo Planta" },
                                    { "number": 11, "stickerName": "Bellsprout", "description": "Pokémon de tipo planta con cuerpo de tallo", "imageURL": "url_bellsprout", "typeOfSticker": "Tipo Planta" },
                                    { "number": 12, "stickerName": "Tangela", "description": "Pokémon de tipo planta cubierto de enredaderas", "imageURL": "url_tangela", "typeOfSticker": "Tipo Planta" }
                                ]
                            }
                        ]
                    ]
                }
                """;

        mockMvc.perform(get("/api/albums/{albumID}/details", albumID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getAlbumDetails_ShouldReturn404WhenAlbumNotFound() throws Exception {
        long nonExistentAlbumID = 999L;

        mockMvc.perform(get("/api/albums/{albumID}/details", nonExistentAlbumID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //SPRINT 2 STUDENT 1 (6)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getMyActiveAlbums_ShouldReturnActiveAlbumsWithSectionsAndStickers() throws Exception {
        String expectedResponse = """
                [
                    {
                        "name": "Album Pokemon",
                        "beginDate": "2024-01-01",
                        "endingDate": "2025-01-01",
                        "visualizeSectionDTOList": [
                            [
                                {
                                    "sectionName": "Tipo Fuego",
                                    "stickers": [
                                        { "number": 1, "stickerName": "Charmander", "description": "Pokémon de tipo fuego inicial de la región Kanto", "imageURL": "url_charmander", "typeOfSticker": "Tipo Fuego" },
                                        { "number": 2, "stickerName": "Vulpix", "description": "Pokémon de tipo fuego con múltiples colas", "imageURL": "url_vulpix", "typeOfSticker": "Tipo Fuego" },
                                        { "number": 3, "stickerName": "Growlithe", "description": "Pokémon de tipo fuego conocido por su lealtad", "imageURL": "url_growlithe", "typeOfSticker": "Tipo Fuego" },
                                        { "number": 4, "stickerName": "Magmar", "description": "Pokémon de tipo fuego con aspecto volcánico", "imageURL": "url_magmar", "typeOfSticker": "Tipo Fuego" }
                                    ]
                                },
                                {
                                    "sectionName": "Tipo Agua",
                                    "stickers": [
                                        { "number": 5, "stickerName": "Squirtle", "description": "Pokémon de tipo agua inicial de la región Kanto", "imageURL": "url_squirtle", "typeOfSticker": "Tipo Agua" },
                                        { "number": 6, "stickerName": "Psyduck", "description": "Pokémon de tipo agua conocido por sus dolores de cabeza", "imageURL": "url_psyduck", "typeOfSticker": "Tipo Agua" },
                                        { "number": 7, "stickerName": "Poliwag", "description": "Pokémon de tipo agua con un espiral en su estómago", "imageURL": "url_poliwag", "typeOfSticker": "Tipo Agua" },
                                        { "number": 8, "stickerName": "Lapras", "description": "Pokémon de tipo agua que ayuda a cruzar el océano", "imageURL": "url_lapras", "typeOfSticker": "Tipo Agua" }
                                    ]
                                },
                                {
                                    "sectionName": "Tipo Planta",
                                    "stickers": [
                                        { "number": 9, "stickerName": "Bulbasaur", "description": "Pokémon de tipo planta inicial de la región Kanto", "imageURL": "url_bulbasaur", "typeOfSticker": "Tipo Planta" },
                                        { "number": 10, "stickerName": "Oddish", "description": "Pokémon de tipo planta con hojas en la cabeza", "imageURL": "url_oddish", "typeOfSticker": "Tipo Planta" },
                                        { "number": 11, "stickerName": "Bellsprout", "description": "Pokémon de tipo planta con cuerpo de tallo", "imageURL": "url_bellsprout", "typeOfSticker": "Tipo Planta" },
                                        { "number": 12, "stickerName": "Tangela", "description": "Pokémon de tipo planta cubierto de enredaderas", "imageURL": "url_tangela", "typeOfSticker": "Tipo Planta" }
                                    ]
                                }
                            ]
                        ]
                    },
                    {
                        "name": "Album 2",
                        "beginDate": "2024-01-01",
                        "endingDate": "2025-01-01",
                        "visualizeSectionDTOList": [
                            [
                                {
                                    "sectionName": "Sección 1 del Album 2",
                                    "stickers": [
                                    ]
                                }
                            ]
                        ]
                    }
                ]
                """;


        mockMvc.perform(get("/api/albums/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @WithUserDetails(value = "benjamin.lewis20@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getMyActiveAlbums_ShouldReturnNotFound_WhenUserHasNoAlbums() throws Exception {
        mockMvc.perform(get("/api/albums/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    //SPRINT 2 STUDENT 1 (7)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getMyEndedAlbums_ShouldReturnAlbumsOrderedByEndingDateWithCorrectFields() throws Exception {

        String expectedResponse = """
                [
                    {
                        "collectionAlbumId": 7,
                        "title": "Album Dragon Ball",
                        "description": "Álbum dedicado a los personajes de Dragon Ball",
                        "beginDate": "2023-01-01",
                        "endingDate": "2024-01-01",
                        "owner": "John",
                        "collectedStickers": null
                    }
                ]
                """;


        mockMvc.perform(get("/api/albums/ended"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    @Test
    @WithUserDetails(value = "mason.rodriguez16@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getMyEndedAlbums_ShouldReturnEmptyListWhenNoEndedAlbums() throws Exception {
        String expectedResponse = "[]";

        mockMvc.perform(get("/api/albums/ended"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse));
    }

    //SPRINT 2 STUDENT 2 (1)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void markStickerAsObtained_ShouldIncrementStickerQuantity() throws Exception {
        long albumID = 1L;
        long stickerID = 1L;


        mockMvc.perform(put("/api/albums/" + albumID + "/sticker/" + stickerID))
                .andExpect(status().isOk());


        Album album = albumRepository.findById(albumID).orElseThrow(() -> new AssertionError("Album not found"));
        CollectionUser collectionUser = userRepository.findByEmail("john.doe1@example.com").orElseThrow(() -> new AssertionError("User not found"));
        CollectionAlbum collectionAlbum = collectionAlbumRepository.findByOwnerAndAlbum(collectionUser, album)
                .orElseThrow(() -> new AssertionError("CollectionAlbum not found"));


        CollectionSticker collectionSticker = collectionStickerRepository
                .findCollectionStickerByAlbumAndOwnerWithSpecificSticker(album, collectionUser, stickerRepository.findById(stickerID).orElseThrow(() -> new AssertionError("Sticker not found")))
                .orElseThrow(() -> new AssertionError("CollectionSticker not found"));


        assertEquals(6, collectionSticker.getQuantity());
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void markStickerAsObtained_ShouldAddANewSticker() throws Exception {
        long albumID = 1L;
        long stickerID = 5L;

        mockMvc.perform(put("/api/albums/" + albumID + "/sticker/" + stickerID))
                .andExpect(status().isOk());

        Album album = albumRepository.findById(albumID).orElseThrow(() -> new AssertionError("Album not found"));
        CollectionUser collectionUser = userRepository.findByEmail("john.doe1@example.com").orElseThrow(() -> new AssertionError("User not found"));
        CollectionSticker collectionSticker = collectionStickerRepository
                .findCollectionStickerByAlbumAndOwnerWithSpecificSticker(album, collectionUser, stickerRepository.findById(stickerID).orElseThrow(() -> new AssertionError("Sticker not found")))
                .orElseThrow(() -> new AssertionError("CollectionSticker not found"));

        assertEquals(1, collectionSticker.getQuantity());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void markStickerAsObtained_ShouldReturnNotFound_WhenStickerDoesNotExist() throws Exception {
        long albumID = 1L;
        long stickerID = 999L;

        mockMvc.perform(put("/api/albums/" + albumID + "/sticker/" + stickerID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void markStickerAsObtained_ShouldReturnNotFound_WhenAlbumDoesNotExist() throws Exception {
        long albumID = 999L;
        long stickerID = 1L;

        mockMvc.perform(put("/api/albums/" + albumID + "/sticker/" + stickerID))
                .andExpect(status().isNotFound());
    }
    //SPRINT 2 STUDENT 2 (2)

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void checkStickersByAlbum_ShouldReturnAllStickersInAlbum() throws Exception {
        long albumID = 1L;
        String expectedResponse = """
                [{"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Charmander","numberInSection":1,"stickerID":1,"quantity":5},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Vulpix","numberInSection":2,"stickerID":2,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Growlithe","numberInSection":3,"stickerID":3,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Magmar","numberInSection":4,"stickerID":4,"quantity":1},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Squirtle","numberInSection":5,"stickerID":5,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Psyduck","numberInSection":6,"stickerID":6,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Poliwag","numberInSection":7,"stickerID":7,"quantity":4},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Lapras","numberInSection":8,"stickerID":8,"quantity":1},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Bulbasaur","numberInSection":9,"stickerID":9,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Oddish","numberInSection":10,"stickerID":10,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Bellsprout","numberInSection":11,"stickerID":11,"quantity":0},
                 {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Tangela","numberInSection":12,"stickerID":12,"quantity":7}]
                """;

        mockMvc.perform(get("/api/albums/" + albumID + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }


    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void checkStickersByAlbum_ShouldReturnNotFound_WhenNoStickersFound() throws Exception {
        long albumIDWithoutStickers = 999L;

        mockMvc.perform(get("/api/albums/" + albumIDWithoutStickers + "/all"))
                .andExpect(status().isNotFound());
    }

    //SPRINT 2 STUDENT 2 (3)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getMissingStickers_ShouldReturnMissingStickers() throws Exception {
        long albumID = 1L;

        String expectedResponse = """
                    [{"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Vulpix","numberInSection":2,"stickerID":2,"quantity":0},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Growlithe","numberInSection":3,"stickerID":3,"quantity":0},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Squirtle","numberInSection":5,"stickerID":5,"quantity":0},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Psyduck","numberInSection":6,"stickerID":6,"quantity":0},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Bulbasaur","numberInSection":9,"stickerID":9,"quantity":0},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Oddish","numberInSection":10,"stickerID":10,"quantity":0},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Bellsprout","numberInSection":11,"stickerID":11,"quantity":0}]
                """;

        mockMvc.perform(get("/api/albums/" + albumID + "/missingStickers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getMissingStickers_ShouldThrowNotFound_WhenAlbumDoesNotExist() throws Exception {
        long albumID = 999L;

        mockMvc.perform(get("/api/albums/" + albumID + "/missingStickers"))
                .andExpect(status().isNotFound());
    }

    //SPRINT 2 STUDENT 2 (2)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getCollectedStickers_ShouldReturnCollectedStickers() throws Exception {
        long albumID = 1L;

        String expectedResponse = """
                    [{"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Charmander","numberInSection":1,"stickerID":1,"quantity":5},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Fuego","albumSectionID":1,"stickerName":"Magmar","numberInSection":4,"stickerID":4,"quantity":1},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Poliwag","numberInSection":7,"stickerID":7,"quantity":4},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Agua","albumSectionID":2,"stickerName":"Lapras","numberInSection":8,"stickerID":8,"quantity":1},
                     {"collectionUserID":1,"name":"John","albumId":1,"sectionTitle":"Tipo Planta","albumSectionID":3,"stickerName":"Tangela","numberInSection":12,"stickerID":12,"quantity":7}]
                """;

        mockMvc.perform(get("/api/albums/" + albumID + "/collectedStickers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getCollectedStickers_ShouldReturnNotFound() throws Exception {
        long albumID = 10L;

        String expectedResponse = """
                    []
                """;

        mockMvc.perform(get("/api/albums/" + albumID + "/collectedStickers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse, true));
    }


    //SPRINT 3 STUDENT 3 (1)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void putOnSaleSticker_ShouldMarkStickerAsOnSale() throws Exception {
        long stickerId = 2L;
        long price = 100L;

        mockMvc.perform(post("/api/albums/sticker/" + stickerId + "/sale")
                        .param("price", String.valueOf(price)))
                .andExpect(status().isAccepted());

        SaleSticker saleSticker = saleStickerRepository.findByStickerIdAndSellerId(stickerId, 2L)
                .orElseThrow(() -> new AssertionError("Sticker no encontrado en venta"));
        assertEquals(price, saleSticker.getPrice());
        assertEquals(2L, saleSticker.getSeller().getId());
    }

    @Test
    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void putOnSaleSticker_ShouldDenyAccessForUserWithoutPermission() throws Exception {
        mockMvc.perform(post("/api/albums/sticker/1/sale")
                        .param("price", "100"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void putOnSaleSticker_ShouldFailWithInvalidPrice() throws Exception {
        mockMvc.perform(post("/api/albums/sticker/1/sale")
                        .param("price", "-50"))
                .andExpect(status().isBadRequest());

    }

    //SPRINT 3 STUDENT 3 (2)
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @DirtiesContext
    @Test
    public void buySaleSticker_ShouldTransferStickerFromSellerToBuyer() throws Exception {
        long saleStickerId = 1L;
        long buyerId = 2L;

        SaleSticker saleSticker = saleStickerRepository.findById(saleStickerId)
                .orElseThrow(() -> new AssertionError("SaleSticker no encontrado."));
        CollectionSticker sellerSticker = saleSticker.getCollectionSticker();
        CollectionUser seller = saleSticker.getSeller();
        CollectionUser buyer = userRepository.findById(buyerId).orElseThrow(() -> new AssertionError("Buyer no encontrado."));
        CollectionAlbum sellerAlbum = collectionAlbumRepository.findByUserAndSticker(seller, sellerSticker.getSticker())
                .orElseThrow(() -> new AssertionError("El álbum del vendedor no contiene este sticker."));
        CollectionAlbum buyerAlbum = collectionAlbumRepository.findByOwnerAndAlbum(buyer, sellerAlbum.getAlbum())
                .orElseThrow(() -> new AssertionError("El comprador no tiene un álbum para este sticker."));

        int initialSellerStickerQuantity = sellerSticker.getQuantity();
        int initialBuyerStickerQuantity = buyerAlbum.getCollectedStickers().stream()
                .filter(cs -> cs.getSticker().equals(sellerSticker.getSticker()))
                .mapToInt(CollectionSticker::getQuantity)
                .findFirst()
                .orElse(0);

        mockMvc.perform(post("/api/albums/saleSticker/{saleStickerId}/buy", saleStickerId))
                .andExpect(status().isAccepted());

        CollectionAlbum updatedSellerAlbum = collectionAlbumRepository.findByOwnerAndAlbum(seller, sellerAlbum.getAlbum())
                .orElseThrow(() -> new AssertionError("El álbum del vendedor debería existir."));

        CollectionSticker updatedSellerSticker = collectionStickerRepository.findById(sellerSticker.getCollectionStickerId())
                .orElse(null);

        CollectionAlbum updatedBuyerAlbum = collectionAlbumRepository.findByOwnerAndAlbum(buyer, sellerAlbum.getAlbum())
                .orElseThrow(() -> new AssertionError("El álbum del comprador debería existir."));

        CollectionSticker updatedBuyerStickerQuantity = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(buyerId, 1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (initialSellerStickerQuantity == 1) {
            assertNull(updatedSellerSticker, "El sticker debería haber sido eliminado del vendedor.");
        } else {
            assertNotNull(updatedSellerSticker, "El sticker del vendedor no debería ser null.");
            assertEquals(initialSellerStickerQuantity - 1, updatedSellerSticker.getQuantity(),
                    "El vendedor debería tener una unidad menos del sticker.");
        }

        assertEquals(initialBuyerStickerQuantity + 1, updatedBuyerStickerQuantity.getQuantity(),
                "El comprador debería tener una unidad más del sticker.");

    }

    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @Test
    public void buySaleSticker_ShouldReturnNotFoundWhenSaleStickerDoesNotExist() throws Exception {
        long invalidSaleStickerId = 999L;

        mockMvc.perform(post("/api/albums/saleSticker/{saleStickerId}/buy", invalidSaleStickerId))
                .andExpect(status().isNotFound());
    }

    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @Test
    public void buySaleSticker_ShouldReturnNotAuth() throws Exception {
        long saleStickerId = 1;

        mockMvc.perform(post("/api/albums/saleSticker/{saleStickerId}/buy", saleStickerId))
                .andExpect(status().isForbidden());
    }

    //SPRINT 3 STUDENT 3 (3)
    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getAllSaleStickerByAlbumID_ShouldReturnListOfSaleStickers() throws Exception {

        long albumId = 1L;


        mockMvc.perform(get("/api/albums/{id}/saleStickers", albumId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stickerID").value(1))
                .andExpect(jsonPath("$[0].stickerName").value("Charmander"))
                .andExpect(jsonPath("$[0].sellerEmail").value("john.doe1@example.com"))
                .andExpect(jsonPath("$[0].price").value(100))
                .andDo(print());
    }


    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void getAllSaleStickerByAlbumID_ShouldReturnEmptyList_WhenAlbumHasNoSaleStickers() throws Exception {
        long albumId = 999L;

        mockMvc.perform(get("/api/albums/{id}/saleStickers", albumId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());
    }


    //SPRINT 4 STUDENT 1 (1)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldGiveStickerToUserSuccessfully() throws Exception {
        long stickerId = 1;
        String receiverEmail = "jane.smith2@example.com";
        mockMvc.perform(post("/api/albums/sticker/{stickerId}/gift", stickerId)
                        .param("receiverEmail", receiverEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        CollectionSticker senderSticker = collectionStickerRepository
                .findCollectionStickerByUserIdAndStickerId(1l, stickerId)
                .orElse(null);

        if (senderSticker != null) {
            assertThat(senderSticker.getQuantity()).isLessThan(5); // quantity was 5 initially
        } else {
            assertThat(senderSticker).isNull();
        }
        CollectionUser receiver = userRepository.findByEmail(receiverEmail).orElseThrow();
        boolean receiverHasSticker = collectionAlbumRepository.findByUserAndSticker(receiver, senderSticker.getSticker()).isPresent();
        assertThat(receiverHasSticker).isTrue();
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnNotFoundWhenSenderDoesNotExist() throws Exception {
        Long nonExistentSenderId = 999L;

        mockMvc.perform(post("/api/albums/sticker/{stickerId}/gift", 999L)
                        .param("receiverEmail", "jane.smith2@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnNotFoundWhenReceiverDoesNotExist() throws Exception {
        String nonExistentEmail = "nonexistent@example.com";

        mockMvc.perform(post("/api/albums/sticker/{stickerId}/gift", 1)
                        .param("receiverEmail", nonExistentEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnNotFoundWhenStickerNotFoundForSender() throws Exception {
        Long nonExistentStickerId = 999L;

        mockMvc.perform(post("/api/albums/sticker/{stickerId}/gift", 3453453)
                        .param("receiverEmail", "hfaijhdfiuahf@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //SPRINT 4 STUDENT 1 (2)
    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldGetInterestingProposalsForSticker() throws Exception {
        long stickerId = 2L;

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
                                    "collectionStickerId": 5,
                                    "sticker": {
                                        "stickerID": 4,
                                        "numberInAlbum": 4,
                                        "name": "Magmar",
                                        "description": "Pokémon de tipo fuego con aspecto volcánico",
                                        "imageURL": "url_magmar",
                                        "typeOfSticker": "Tipo Fuego"
                                    },
                                    "quantity": 2
                                }
                            ]
                        },
                        {
                            "firstName": "Robert",
                            "lastName": "Brown",
                            "email": "robert.brown3@example.com",
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
                                    "collectionStickerId": 5,
                                    "sticker": {
                                        "stickerID": 4,
                                        "numberInAlbum": 4,
                                        "name": "Magmar",
                                        "description": "Pokémon de tipo fuego con aspecto volcánico",
                                        "imageURL": "url_magmar",
                                        "typeOfSticker": "Tipo Fuego"
                                    },
                                    "quantity": 2
                                }
                            ]
                        }
                    ]
                """;

        mockMvc.perform(get("/api/albums/sticker/{stickerId}/users", stickerId))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnEmptyListForNonexistentSticker() throws Exception {
        long nonexistentStickerId = 999L;

        mockMvc.perform(get("/api/albums/sticker/{stickerId}/users", nonexistentStickerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }

    //SPRINT 4 STUDENT 1 (3)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldSendProposalAndCreateExchange() throws Exception {
        long stickerId = 2;
        String receiverEmail = "jane.smith2@example.com";

        mockMvc.perform(post("/api/albums/sticker/{stickerId}/interested", stickerId)
                        .param("receiverEmail", receiverEmail))
                .andExpect(status().isOk());

        Optional<ExchangeSticker> exchangeStickerOptional = exchangeRepository.findAll().stream()
                .filter(exchange -> exchange.getInterestingSticker().getStickerID() == stickerId
                        && exchange.getInterested().getEmail().equals(receiverEmail)
                        && exchange.getOwner().getEmail().equals("john.doe1@example.com"))
                .findFirst();

        assertTrue(exchangeStickerOptional.isPresent(), "The exchange proposal should have been created.");
        ExchangeSticker exchangeSticker = exchangeStickerOptional.get();
        assertEquals(stickerId, exchangeSticker.getInterestingSticker().getStickerID(), "The sticker ID should match.");
        assertEquals(receiverEmail, exchangeSticker.getInterested().getEmail(), "The receiver email should match.");
        assertEquals("john.doe1@example.com", exchangeSticker.getOwner().getEmail(), "The owner email should match.");
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnErrorWhenProposalCannotBeCreated() throws Exception {
        long nonExistentStickerId = 999L;
        String validReceiverEmail = "jane.smith2@example.com";

        mockMvc.perform(post("/api/albums/sticker/{stickerId}/interested", nonExistentStickerId)
                        .param("receiverEmail", validReceiverEmail))
                .andExpect(status().isNotFound())
                .andDo(print());


    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnErrorWhenReceiverEmailIsInvalid() throws Exception {
        long validStickerId = 1L;
        String invalidEmail = "invalid-email";

        mockMvc.perform(post("/api/albums/sticker/{stickerId}/interested", validStickerId)
                        .param("receiverEmail", invalidEmail))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    //SPRINT 4 STUDENT 1 (4)

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    @DirtiesContext
    public void shouldAcceptExchangeProposalSuccessfullyAndVerifyStickersExchanged() throws Exception {
        long exchangeId = 1L;
        long exchangeStickerId = 2L;

        ExchangeSticker exchangeSticker = exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new AssertionError("ExchangeSticker not found"));

        CollectionSticker ownerInitialSticker = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(
                        exchangeSticker.getOwner().getId(), exchangeSticker.getInterestingSticker().getStickerID())
                .orElseThrow(() -> new AssertionError("Owner CollectionSticker not found"));

        CollectionSticker interestedInitialSticker = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(
                        exchangeSticker.getInterested().getId(), exchangeStickerId)
                .orElseThrow(() -> new AssertionError("Interested CollectionSticker not found"));

        int initialOwnerQuantity = ownerInitialSticker.getQuantity();
        int initialInterestedQuantity = interestedInitialSticker.getQuantity();

        mockMvc.perform(post("/api/albums/exchange/{exchangeId}", exchangeId)
                        .param("exchangeSticker", String.valueOf(exchangeStickerId)))
                .andExpect(status().isOk())
                .andDo(print());

        CollectionSticker ownerStickerAfterExchange = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(
                        exchangeSticker.getOwner().getId(), exchangeSticker.getInterestingSticker().getStickerID())
                .orElse(null);

        if (initialOwnerQuantity == 1) {
            assertNull(ownerStickerAfterExchange, "Owner sticker should be removed if quantity was 1");
        } else {
            assertNotNull(ownerStickerAfterExchange, "Owner sticker should still exist if quantity > 1");
            assertEquals(initialOwnerQuantity - 1, ownerStickerAfterExchange.getQuantity(),
                    "Owner sticker quantity should be decremented by 1");
        }

        CollectionSticker interestedGainedSticker = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(
                        exchangeSticker.getInterested().getId(), exchangeSticker.getInterestingSticker().getStickerID())
                .orElseThrow(() -> new AssertionError("Interested should have gained the owner's sticker"));
        assertEquals(1, interestedGainedSticker.getQuantity(), "Interested should have gained 1 unit of the owner's sticker");

        CollectionSticker interestedStickerAfterExchange = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(
                        exchangeSticker.getInterested().getId(), exchangeStickerId)
                .orElse(null);

        if (initialInterestedQuantity == 1) {
            assertNull(interestedStickerAfterExchange, "Interested sticker should be removed if quantity was 1");
        } else {
            assertNotNull(interestedStickerAfterExchange, "Interested sticker should still exist if quantity > 1");
            assertEquals(initialInterestedQuantity - 1, interestedStickerAfterExchange.getQuantity(),
                    "Interested sticker quantity should be decremented by 1");
        }

        CollectionSticker ownerGainedSticker = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(
                        exchangeSticker.getOwner().getId(), exchangeStickerId)
                .orElseThrow(() -> new AssertionError("Owner should have gained the interested's sticker"));
        assertEquals(1, ownerGainedSticker.getQuantity(), "Owner should have gained 1 unit of the interested's sticker");
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnNotFoundWhenExchangeDoesNotExist() throws Exception {
        long invalidExchangeId = 999L;
        long exchangeStickerId = 1L;

        mockMvc.perform(post("/api/albums/exchange/{exchangeId}", invalidExchangeId)
                        .param("exchangeSticker", String.valueOf(exchangeStickerId)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void shouldReturnBadRequestWhenOwnerHasNoStickersToTrade() throws Exception {
        long validExchangeId = 1L;
        long validStickerId = 1L;

        CollectionSticker ownerSticker = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(1L, 1L).orElseThrow(() -> new AssertionError("Owner CollectionSticker not found"));
        ownerSticker.setQuantity(0);
        collectionStickerRepository.save(ownerSticker);

        mockMvc.perform(post("/api/albums/exchange/{exchangeId}", validExchangeId)
                        .param("exchangeSticker", String.valueOf(validStickerId)))
                .andExpect(status().isBadRequest());
    }

    //SPRINT 4 STUDENT 3 (2)

    @Test
    @DirtiesContext
    @WithUserDetails(value = "emily.davis4@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void sendMassiveExchange_shouldCreateExchangeOrder() throws Exception {
        String otherUserEmail = "john.doe1@example.com";

        List<CollectionSticker> stickerFromInterested = new ArrayList<>();//user can give stickers 2 and 5, and receive 1,7,12
        stickerFromInterested.add(collectionStickerRepository.findById(15L).get());
        stickerFromInterested.add(collectionStickerRepository.findById(17L).get());

        mockMvc.perform(post("/api/albums/exchange/fullProposal/send")//should exchange
                        .param("stickerOwnerEmail", otherUserEmail))
                .andExpect(status().isOk());
        List<CollectionSticker> updatedStickerFromInterested = new ArrayList<>();
        updatedStickerFromInterested.add(collectionStickerRepository.findById(15L).get());
        updatedStickerFromInterested.add(collectionStickerRepository.findById(17L).get());

        for (CollectionSticker sticker : stickerFromInterested) {
            assertEquals(sticker.getSticker().getStickerID(), updatedStickerFromInterested.getFirst().getSticker().getStickerID());
            assertEquals(sticker.getQuantity() - 1, updatedStickerFromInterested.getFirst().getQuantity());
            updatedStickerFromInterested.removeFirst();
        }
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "michael.wilson5@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void sendMassiveExchange_noStickersAvailable_ShouldReturnConflict() throws Exception {

        String otherUserEmail = "john.doe1@example.com";
        mockMvc.perform(post("/api/albums/exchange/fullProposal/send")//should exchange
                        .param("stickerOwnerEmail", otherUserEmail))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "robert.brown3@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void sendMassiveExchange_FreeUser_ShouldCreateMax2Stickers() throws Exception {

        String otherUserEmail = "john.doe1@example.com";
        albumService.obtainSticker(3L, 1L, 9L);
        albumService.obtainSticker(3L, 1L, 9L);

        albumService.obtainSticker(3L, 1L, 5L);
        albumService.obtainSticker(3L, 1L, 5L);

        albumService.obtainSticker(3L, 1L, 3L);
        albumService.obtainSticker(3L, 1L, 3L);

        albumService.obtainSticker(1L, 1L, 11L);
        albumService.obtainSticker(1L, 1L, 11L);
        mockMvc.perform(post("/api/albums/exchange/fullProposal/send")//should exchange
                        .param("stickerOwnerEmail", otherUserEmail))
                .andExpect(status().isOk());


        ExchangeSticker exchangeStickerCreated = exchangeRepository.findById(5L).get();
        assertTrue(exchangeStickerCreated.getStickerListForOwner().size() == exchangeStickerCreated.getInterestingStickerList().size() && exchangeStickerCreated.getInterestingStickerList().size() == 2);

    }

    //SPRINT 4 STUDENT 3 (3)
    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void massiveExchange_shouldOpenExchangeOrder() throws Exception {

        String expectedResponse= """
                {
                  "exchangeId": 4,
                  "proposerName": "Jane",
                  "stickersOfOwner": [
                      {
                      "number": 0,
                      "stickerName": "Charmander",
                      "description": "Pokémon de tipo fuego inicial de la región Kanto",
                      "imageURL": "url_charmander",
                      "typeOfSticker": "Tipo Fuego"
                    },
                    {
                      "number": 0,
                      "stickerName": "Poliwag",
                      "description": "Pokémon de tipo agua con un espiral en su estómago",
                      "imageURL": "url_poliwag",
                      "typeOfSticker": "Tipo Agua"
                    }
                  ],
                  "stickersOfProposer": [
                    {
                      "number": 0,
                      "stickerName": "Vulpix",
                      "description": "Pokémon de tipo fuego con múltiples colas",
                      "imageURL": "url_vulpix",
                      "typeOfSticker": "Tipo Fuego"
                    },
                    {
                      "number": 0,
                      "stickerName": "Squirtle",
                      "description": "Pokémon de tipo agua inicial de la región Kanto",
                      "imageURL": "url_squirtle",
                      "typeOfSticker": "Tipo Agua"
                    }
                  ]
                }
                """;

        mockMvc.perform(get("/api/albums/exchange/fullProposal/{exchangeId}", 4L))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse, false)).andDo(print());

    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "james.jackson9@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void massiveExchange_NotTheStickerOwnerOrInterested_shouldReturnConflict() throws Exception {

        mockMvc.perform(get("/api/albums/exchange/fullProposal/{exchangeId}", 4L))
                .andExpect(status().isConflict()).andDo(print());

    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "james.jackson9@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void massiveExchange_NotRealExchange_shouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/api/albums/exchange/fullProposal/{exchangeId}", Long.MAX_VALUE))
                .andExpect(status().isNotFound()).andDo(print());

    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void massiveExchange_shouldRejectExchangeOrder() throws Exception {
        //can only be excecute before 2.5 minutes has passed ExchangeServer inicialization, otherwise the exchange with id 4 will be already rejected because of time
        List<CollectionSticker> stickerFromInterested = new ArrayList<>();//user can give stickers 2 and 5, and receive 1,7,12
        stickerFromInterested.add(collectionStickerRepository.findById(6L).get());
        stickerFromInterested.add(collectionStickerRepository.findById(9L).get());


        mockMvc.perform(post("/api/albums/exchange/fullProposal/reject/{exchangeID}", 4L))
                .andExpect(status().isOk());
        assertEquals(ExchangeStatus.REJECT, exchangeRepository.findById(4L).get().getStatus());

        List<CollectionSticker> updatedStickerFromInterested = new ArrayList<>();
        updatedStickerFromInterested.add(collectionStickerRepository.findById(6L).get());
        updatedStickerFromInterested.add(collectionStickerRepository.findById(9L).get());

        for (CollectionSticker sticker : stickerFromInterested) {
            assertEquals(sticker.getSticker().getStickerID(), updatedStickerFromInterested.getFirst().getSticker().getStickerID());
            assertEquals(sticker.getBlockedCopies() - 1, updatedStickerFromInterested.getFirst().getBlockedCopies());
            updatedStickerFromInterested.removeFirst();
        }

    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void rejectMassiveExchange_UserNotTheOwner_shouldReturnConflict() throws Exception {
        mockMvc.perform(post("/api/albums/exchange/fullProposal/reject/{exchangeID}", 4L))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void rejeectMassiveExchange_OrderNotExistingg_shouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/api/albums/exchange/fullProposal/reject/{exchangeID}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "john.doe1@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void rejectMassiveExchange_shouldAcceptExchangeOrder() throws Exception {
        //can only be excecute before 2.5 minutes has passed ExchangeServer inicialization, otherwise the exchange with id 4 will be already rejected because of time
        List<CollectionSticker> stickerFromInterested = new ArrayList<>();//user can give stickers 2 and 5, and receive 1,7,12
        stickerFromInterested.add(collectionStickerRepository.findById(6L).get());
        stickerFromInterested.add(collectionStickerRepository.findById(9L).get());


        mockMvc.perform(post("/api/albums/exchange/fullProposal/accept/{exchangeID}", 4L))
                .andExpect(status().isOk());
        assertEquals(ExchangeStatus.COMPLETED, exchangeRepository.findById(4L).get().getStatus());

        List<CollectionSticker> updatedStickerFromInterested = new ArrayList<>();
        updatedStickerFromInterested.add(collectionStickerRepository.findById(6L).get());
        updatedStickerFromInterested.add(collectionStickerRepository.findById(9L).get());

        Long[] stickersId = {2L, 5L};
        List<CollectionSticker> stickersOwnerGot = new ArrayList<>(collectionStickerRepository.findCollectionStickerListByOwnerAndStickerList(1L, stickerRepository.findAllById(Arrays.asList(stickersId))).get());
        assertTrue(1 == stickersOwnerGot.getFirst().getQuantity()
                && stickersOwnerGot.getFirst().getSticker().getStickerID() == stickerFromInterested.getFirst().getSticker().getStickerID());
        assertTrue(1 == stickersOwnerGot.getLast().getQuantity()
                && stickersOwnerGot.getLast().getSticker().getStickerID() == stickerFromInterested.getLast().getSticker().getStickerID());
        for (CollectionSticker sticker : stickerFromInterested) {
            assertEquals(sticker.getSticker().getStickerID(), updatedStickerFromInterested.getFirst().getSticker().getStickerID());
            assertEquals(sticker.getBlockedCopies() - 1, updatedStickerFromInterested.getFirst().getBlockedCopies());
            assertEquals(sticker.getQuantity(), updatedStickerFromInterested.getFirst().getQuantity());

            updatedStickerFromInterested.removeFirst();
        }


    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void acceptMassiveExchange_UserNotTheOwner_shouldReturnConflict() throws Exception {
        mockMvc.perform(post("/api/albums/exchange/fullProposal/accept/{exchangeID}", 4L))
                .andExpect(status().isConflict());

    }

    @Test
    @DirtiesContext
    @WithUserDetails(value = "jane.smith2@example.com", userDetailsServiceBeanName = "userLabDetailsService")
    public void acceptMassiveExchange_NonExistingExchange_shouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/api/albums/exchange/fullProposal/accept/{exchangeID}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());

    }
}


