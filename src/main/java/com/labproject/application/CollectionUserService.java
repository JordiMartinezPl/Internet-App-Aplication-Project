package com.labproject.application;

import com.labproject.application.dto.input.CollectionUserDTO;
import com.labproject.application.dto.input.UpdateUserDTO;
import com.labproject.application.dto.output.VisualizeCollectionStickerDTO;
import com.labproject.application.dto.output.VisualizeCollectionUserDTO;
import com.labproject.application.dto.output.VisualizeUserWithStickersDTO;
import com.labproject.domain.CollectionUser;
import com.labproject.persistence.CollectionStickerRepository;
import com.labproject.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CollectionUserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CollectionStickerRepository collectionStickerRepository;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public CollectionUserService(UserRepository userRepository, CollectionStickerRepository collectionStickerRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
        this.modelMapper = new ModelMapper();
        this.collectionStickerRepository = collectionStickerRepository;
    }


    public void registerUser(CollectionUserDTO collectionUserDTO) {
        CollectionUser collectionUser = modelMapper.map(collectionUserDTO, CollectionUser.class);
        collectionUser.setPassword(encoder.encode(collectionUser.getPassword()));
        if (collectionUser.getDateOfRegistration() == null) collectionUser.setDateOfRegistration(LocalDate.now());
        userRepository.save(collectionUser);
    }

    @Transactional
    public void updateUserProfile(Long userID, UpdateUserDTO updateUserDTO) {
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (updateUserDTO.getName() != null) user.setName(updateUserDTO.getName());
        if (updateUserDTO.getSecondName() != null) user.setSecondName(updateUserDTO.getSecondName());
        if (updateUserDTO.getEmail() != null) user.setEmail(updateUserDTO.getEmail());
        if (updateUserDTO.getPassword() != null) user.setPassword(encoder.encode(updateUserDTO.getPassword()));
        if (updateUserDTO.getDateOfBirth() != null) user.setDateOfBirth(updateUserDTO.getDateOfBirth());

    }

    public List<VisualizeCollectionUserDTO> getAllUsers() {
        return userRepository.findAllUsersOrderedBySecondName().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<VisualizeCollectionUserDTO> getLastRegisteredUsers(LocalDate date) {
        return userRepository.findUsersRegisteredAfterDate(date).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void blockUser(Long userId, String emailToBlock) {
        CollectionUser user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser userToBlock = userRepository.findByEmail(emailToBlock).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getBlockedUsers().contains(userToBlock) || user.getId().equals(userToBlock.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        user.addBlockUser(userToBlock);
    }

    public List<VisualizeCollectionUserDTO> getAllUsersOrderedByActivity() {
        return userRepository.findAllUsersOrderedByMessageCount().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<VisualizeUserWithStickersDTO> getTopCandidates(Long userID) {
        Pageable pageable = PageRequest.of(0, 3);

        List<VisualizeUserWithStickersDTO> visualizeUserWithStickersDTOS = userRepository.findTop3UsersWithMostTotalExchangeable(pageable, userID);
        List<VisualizeCollectionStickerDTO> collectionStickers = userRepository.findTopThreeCollectionStickers(pageable, userID);
        for (VisualizeUserWithStickersDTO v : visualizeUserWithStickersDTOS) {
            v.setVisualizeCollectionSticker(collectionStickerRepository.findExchangeableStickersBetweenUsers(userID, userRepository.findByEmail(v.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getId()));
        }
        return visualizeUserWithStickersDTOS;
    }

}