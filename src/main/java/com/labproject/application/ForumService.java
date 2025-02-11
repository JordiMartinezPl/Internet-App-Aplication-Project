package com.labproject.application;

import com.labproject.application.dto.input.ForumMessageDTO;
import com.labproject.application.dto.input.PrivateMessageDTO;
import com.labproject.application.dto.output.VisualizeCollectionUserDTO;
import com.labproject.application.dto.output.VisualizeForumDTO;
import com.labproject.application.dto.output.VisualizeMessageDTO;
import com.labproject.domain.CollectionUser;
import com.labproject.domain.Forum;
import com.labproject.domain.ForumMessage;
import com.labproject.domain.PrivateMessage;
import com.labproject.persistence.ForumMessageRepository;
import com.labproject.persistence.ForumRepository;
import com.labproject.persistence.PrivateMessageRepository;
import com.labproject.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ForumService {


    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final ForumMessageRepository forumMessageRepository;
    private final PrivateMessageRepository privateMessageRepository;

    @Autowired
    public ForumService(UserRepository userRepository, ForumRepository forumRepository, ForumMessageRepository forumMessageRepository, PrivateMessageRepository privateMessageRepository) {
        this.userRepository = userRepository;
        this.forumRepository = forumRepository;
        this.forumMessageRepository = forumMessageRepository;
        this.privateMessageRepository = privateMessageRepository;
    }


    @Transactional
    public void subscribeToAlbumForum(Long userID, long forumID) throws Throwable {
        Forum forum = forumRepository.findById(forumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getSubscribedForums().contains(forum)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        user.subscribeToForum(forum);
    }

    public void unsubscribeToAlbumForum(Long userID, long forumID) throws Throwable {
        Forum forum = forumRepository.findById(forumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!user.getSubscribedForums().contains(forum)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        user.unSubscribeToForum(forum);
        userRepository.save(user);
    }


    public List<VisualizeMessageDTO> getAllMessages(long forumID, Optional<LocalDate> date, Long userID) {
        Forum forum = forumRepository.findById(forumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!user.getSubscribedForums().contains(forum)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (date.isPresent()) {
            return forumMessageRepository.findForumMessagesByForum(forum.getForumID(), date.get().atStartOfDay(), userID);
        } else {
            return forumMessageRepository.findForumMessagesByForum(forum.getForumID(), LocalDateTime.now().minusDays(7), userID);
        }

    }
    public void sendMessageToForum(Long userID, long forumID, ForumMessageDTO forumMessageDTO) {

        Forum forum = forumRepository.findById(forumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!user.getSubscribedForums().contains(forum)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        ForumMessage message = new ForumMessage(forumMessageDTO.getBody(), user, forum);
        forumMessageRepository.save(message);
    }
    public List<VisualizeCollectionUserDTO> getAllSubscribersFromForum(long forumId, Long userId) {
        if (!userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getSubscribedForums().contains(forumRepository.findById(forumId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        List<VisualizeCollectionUserDTO> visualizeList = userRepository.findUsersSubscribedToForum(forumId);
        if (visualizeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return visualizeList;
    }
    public List<VisualizeForumDTO> getSubscribedForum(Long userID) {
        return forumRepository.findForumsByUserId(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void updateReadMessage(Long userID, long messageID) {
        CollectionUser user = userRepository.findById(userID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        PrivateMessage privateMessage = privateMessageRepository.findById(messageID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!user.getId().equals(privateMessage.getReceiver().getId()) || privateMessage.isRead()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        ForumMessage message = forumMessageRepository.findById(messageID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        message.addRead(user);
    }


    public void sendDirectMessage(Long senderID, long forumID, PrivateMessageDTO privateMessageDTO, boolean oneTimeMessage, Optional<Long> replayMessageID) {
        CollectionUser sender = userRepository.findById(senderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        CollectionUser receiver = userRepository.findByEmail(privateMessageDTO.getReceiverEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        if (sender.getSubscribedForums().stream()
                .noneMatch(forum -> forum.getForumID() == forumID) || receiver.getSubscribedForums().stream()
                .noneMatch(forum -> forum.getForumID() == forumID) || userRepository.isUserBlockedByOther(senderID, receiver.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        PrivateMessage message;
        if (replayMessageID.isPresent()) {
            message = new PrivateMessage(forumRepository.findById(forumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), privateMessageDTO.getBody(), receiver, sender, oneTimeMessage, privateMessageRepository.findById(replayMessageID.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Original message not found")));
        } else {
            message = new PrivateMessage(forumRepository.findById(forumID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), privateMessageDTO.getBody(), receiver, sender, oneTimeMessage);
        }
        privateMessageRepository.save(message);
    }

    public void replyDirectMessage(Long senderID, long forumId, Long messageId, PrivateMessageDTO privateMessageDTO) {
        sendDirectMessage(senderID, forumId, privateMessageDTO, false, Optional.ofNullable(messageId));
    }


    public List<VisualizeMessageDTO> getAllMessagesByDate(LocalDate date, Long userId) {
        return forumMessageRepository.findMessageAfterDate(date.atStartOfDay(), userId);
    }

    public List<VisualizeForumDTO> getAllForumsOrderedByMessages() {
        return forumRepository.findAllForumsOrderedByMessageCount().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void updatePrivateReadMessage(Long userID, long forumMessageID) {
        PrivateMessage privateMessage = privateMessageRepository.findById(forumMessageID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (privateMessage.getReceiver().getId() != userID) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (privateMessage.isOneTimeMessage()) {
            privateMessageRepository.delete(privateMessage);
        } else {
            privateMessage.setRead(true);
        }
    }

}