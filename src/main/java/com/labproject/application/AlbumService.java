package com.labproject.application;

import com.labproject.application.dto.input.AlbumDTO;
import com.labproject.application.dto.output.*;
import com.labproject.domain.*;
import com.labproject.persistence.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final ForumRepository forumRepository;
    private final StickerRepository stickerRepository;
    private final CollectionAlbumRepository collectionAlbumRepository;
    private final CollectionStickerRepository collectionStickerRepository;

    @Autowired
    public AlbumService(ModelMapper modelMapper, UserRepository userRepository, AlbumRepository albumRepository, ForumRepository forumRepository, StickerRepository stickerRepository, CollectionAlbumRepository collectionAlbumRepository, CollectionStickerRepository collectionStickerRepository, SaleStickerRepository saleStickerRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
        this.forumRepository = forumRepository;
        this.stickerRepository = stickerRepository;
        this.collectionAlbumRepository = collectionAlbumRepository;
        this.collectionStickerRepository = collectionStickerRepository;
    }

    public void createAlbum(Long userID, AlbumDTO albumDTO) {
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Forum forum = new Forum();
        forumRepository.save(forum);
        Album album = modelMapper.map(albumDTO, Album.class);
        album.setEditor(user);
        album.setForum(forum);
        albumRepository.save(album);
    }

    public List<VisualizeAlbumDTO> getInfoAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        return albums.stream().map(album -> {
            List<VisualizeSectionDTO> sectionDTOList = album.getSections().stream()
                    .map(section -> new VisualizeSectionDTO(section.getTitle(), section.getStickers().size()))

                    .toList();

            return new VisualizeAlbumDTO(
                    album.getTitle(),
                    album.getBeginDate(),
                    album.getEndingDate(),
                    Collections.singletonList(sectionDTOList)
            );
        }).collect(Collectors.toList());
    }

    public List<VisualizeAlbumDTO> getAvailableAlbums() {
        List<Album> albums = albumRepository.findByPublicAvailabilityAndDate(LocalDate.now()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Albums not found."));
        return albums.stream().map(album -> {
            List<VisualizeSectionDTO> sections = album.getSections().stream()
                    .map(section -> new VisualizeSectionDTO(section.getTitle(), section.getStickers().size()))
                    .toList();
            return new VisualizeAlbumDTO(
                    album.getTitle(),
                    album.getBeginDate(),
                    album.getEndingDate(),
                    Collections.singletonList(sections)
            );
        }).collect(Collectors.toList());
    }

    public List<VisualizeCollectionUserDTO> getUsersCollectingAlbum(long albumID) {
        List<VisualizeCollectionUserDTO> users = userRepository.findUsersCollectingAlbum(albumID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Album with ID " + albumID + " does not exist."));
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No users found collecting album with ID " + albumID);
        }
        return users;
    }


    @Transactional
    public void changePublicAvailability(long collectionAlbumID) {
        CollectionAlbum album = collectionAlbumRepository.findById(collectionAlbumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found."));
        album.setPublicAvailability(!album.getPublicAvailability());
    }

    public VisualizeAlbumDTO getAlbumDetails(long albumID) {
        CollectionAlbum album = collectionAlbumRepository.findById(albumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<AlbumSection> sections = album.getAlbum().getSections();
        List<VisualizeSectionWithStickerDTO> visualizeSectionDTOList = sections.stream()
                .map(section -> new VisualizeSectionWithStickerDTO(
                        section.getTitle(),
                        section.getStickers().stream()
                                .map(sticker -> modelMapper.map(sticker, VisualizeStickerDTO.class))
                                .collect(Collectors.toList())
                ))
                .toList();
        return new VisualizeAlbumDTO(
                album.getAlbum().getTitle(),
                album.getAlbum().getBeginDate(),
                album.getAlbum().getEndingDate(),
                Collections.singletonList(visualizeSectionDTOList)
        );
    }

    public VisualizeStickerDTO getStickerByAlbumAndNumber(long albumId, int number) {
        return stickerRepository.findVisualizeStickerByAlbumAndNumber(albumId, number).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sticker not found for album ID " + albumId + " and number " + number));
    }

    public void addCollectionAlbum(Long userID, long albumID) {
        Album album = albumRepository.findById(albumID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        if (!album.getPublicAvailability()) {
            throw new IllegalArgumentException("The album is not public at this moment");
        }
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().getName().equals(EnumRoles.FREE) && collectionAlbumRepository.existsByOwnerAndPublicAvailabilityTrue(userID)) {
            throw new IllegalStateException("FREE users can only have one active collection album.");
        }
        CollectionAlbum collectionAlbum = new CollectionAlbum(user, album, true);
        collectionAlbumRepository.save(collectionAlbum);
    }

    public List<VisualizeAlbumDTO> getMyActiveAlbums(long userID) {
        List<CollectionAlbum> activeAlbums = collectionAlbumRepository.findByUserIdAndDateActive(userID, LocalDate.now()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (activeAlbums.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No active albums found for the user.");
        }
        return activeAlbums.stream().map(collectionAlbum -> {
            Album album = collectionAlbum.getAlbum();
            List<AlbumSection> sections = album.getSections();

            List<VisualizeSectionWithStickerDTO> visualizeSectionDTOList = sections.stream()
                    .map(section -> new VisualizeSectionWithStickerDTO(
                            section.getTitle(),
                            section.getStickers().stream()
                                    .map(sticker -> modelMapper.map(sticker, VisualizeStickerDTO.class))
                                    .collect(Collectors.toList())
                    ))
                    .toList();

            return new VisualizeAlbumDTO(
                    album.getTitle(),
                    album.getBeginDate(),
                    album.getEndingDate(),
                    Collections.singletonList(visualizeSectionDTOList)
            );
        }).toList();
    }

    public List<VisualizeCollectionAlbumDTO> getMyEndedAlbums(Long userID) {
        return collectionAlbumRepository.findEndedAlbumsByUserId(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public VisualizeCollectionStickerDTO obtainSticker(Long userID, long albumID, long stickerID) {
        markStickerAsObtained(userID, albumID, stickerID);
        return modelMapper.map(collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(userID, stickerID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), VisualizeCollectionStickerDTO.class);
    }

    public void markStickerAsObtained(Long userID, long albumID, long stickerID) {
        Sticker sticker = stickerRepository.findById(stickerID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser collectionUser = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Album album = albumRepository.findById(albumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            CollectionSticker collectionSticker = collectionStickerRepository.findCollectionStickerByAlbumAndOwnerWithSpecificSticker(album, collectionUser, sticker).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            collectionSticker.incrementQuantity();
            collectionStickerRepository.save(collectionSticker);

        } catch (ResponseStatusException e) {
            CollectionSticker collectionSticker = new CollectionSticker(sticker);
            CollectionAlbum collectionAlbum = collectionAlbumRepository.findByOwnerAndAlbum(collectionUser, album).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            collectionAlbum.addSticker(collectionSticker);
            collectionStickerRepository.save(collectionSticker);
            collectionAlbumRepository.save(collectionAlbum);
        }
    }

    public List<HasStickerDTO> checkStickers(Long userID, long albumID) {
        List<HasStickerDTO> stickers = collectionStickerRepository.findAllStickersWithQuantities(albumID, userID);

        if (stickers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return stickers;
    }

    public List<HasStickerDTO> getMissingStickers(Long userID, long albumID) {
        List<HasStickerDTO> missingStickers = collectionStickerRepository.findMissingStickersByAlbumAndUser(albumID, userID);
        if (missingStickers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No missing stickers found for the album and user.");
        }
        return missingStickers;
    }

    public List<HasStickerDTO> getCollectedStickers(Long userID, long albumID) {
        return collectionStickerRepository.findAllByCollectionOrderBySectionAndNumberAsc(albumID, userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


}