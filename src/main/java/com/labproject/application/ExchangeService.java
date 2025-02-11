package com.labproject.application;

import com.labproject.application.dto.output.VisualizeExchangeSticker;
import com.labproject.application.dto.output.VisualizeSaleStickerDTO;
import com.labproject.application.dto.output.VisualizeUserWithStickersDTO;
import com.labproject.domain.*;
import com.labproject.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeService {


    private final SaleStickerRepository saleStickerRepository;

    private final CollectionAlbumRepository collectionAlbumRepository;

    private final CollectionStickerRepository collectionStickerRepository;

    private final UserRepository userRepository;

    private final StickerRepository stickerRepository;

    private final AlbumService albumService;
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeService(SaleStickerRepository saleStickerRepository, CollectionAlbumRepository collectionAlbumRepository, CollectionStickerRepository collectionStickerRepository, UserRepository userRepository, AlbumService albumService, StickerRepository stickerRepository, ExchangeRepository exchangeRepository) {
        this.saleStickerRepository = saleStickerRepository;
        this.collectionAlbumRepository = collectionAlbumRepository;
        this.collectionStickerRepository = collectionStickerRepository;
        this.userRepository = userRepository;
        this.albumService = albumService;
        this.stickerRepository = stickerRepository;
        this.exchangeRepository = exchangeRepository;
    }


    public void putOnSaleSticker(Long userId, long stickerId, long price) {
        if (price <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        CollectionUser user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        saleStickerRepository.save(new SaleSticker(user, price, collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(userId, stickerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }

    @Transactional
    public void buySaleSticker(Long userId, long stickerId) {
        SaleSticker saleSticker = saleStickerRepository.findById(stickerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionSticker sellerCollectionSticker = saleSticker.getCollectionSticker();
        CollectionAlbum sellerCollectionAlbum = collectionAlbumRepository.findByUserAndSticker(saleSticker.getSeller(), sellerCollectionSticker.getSticker()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (sellerCollectionSticker.getQuantity() == 0 || saleSticker.isSold()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        albumService.markStickerAsObtained(userId, sellerCollectionAlbum.getCollectionAlbumId(), saleSticker.getCollectionSticker().getSticker().getStickerID());
        if (sellerCollectionSticker.getQuantity() == 1) {
            sellerCollectionAlbum.removeSticker(sellerCollectionSticker);
            collectionStickerRepository.delete(sellerCollectionSticker);
        } else {
            sellerCollectionSticker.reduceQuantity();
        }
        saleSticker.setSold(true);
    }


    public List<VisualizeSaleStickerDTO> getAllSaleStickerByAlbumId(long id) {
        return saleStickerRepository.findSaleStickersByAlbumId(id);
    }

    @Transactional
    public void giveStickerToUser(long stickerId, String receiverEmail, Long userId) {
        CollectionSticker collectionSticker = collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(userId, stickerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionAlbum collectionAlbumSender = collectionAlbumRepository.findByUserAndSticker(userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), collectionSticker.getSticker()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        albumService.markStickerAsObtained(userRepository.findByEmail(receiverEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getId(), collectionAlbumSender.getCollectionAlbumId(), collectionSticker.getSticker().getStickerID());
        if (collectionSticker.getQuantity() == 1) {
            collectionAlbumSender.removeSticker(collectionSticker);
            collectionStickerRepository.delete(collectionSticker);
        } else {
            collectionSticker.reduceQuantity();
        }

    }

    public List<VisualizeUserWithStickersDTO> getInterestingProposals(Long userId, long stickerId) {
        List<VisualizeUserWithStickersDTO> proposals = userRepository.findUsersWithDuplicateStickersExcludingUser(stickerId, userId);
        for (VisualizeUserWithStickersDTO u : proposals) {
            u.setVisualizeCollectionSticker(collectionStickerRepository.findStickersForFirstUserButNotSecond(userId, userRepository.findByEmail(u.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getId()));
        }
        return proposals;
    }

    public void sendProposal(Long userId, String interestedEmail, long stickerId) {
        ExchangeSticker exchangeSticker = new ExchangeSticker(
                stickerRepository.findById(stickerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
                userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
                userRepository.findByEmail(interestedEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
        exchangeRepository.save(exchangeSticker);
    }

    @Transactional
    public void acceptProposal(long exchangeId, long exchangeStickerId) {
        ExchangeSticker exchangeSticker = exchangeRepository.findById(exchangeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (collectionStickerRepository.findCollectionStickerByUserIdAndStickerId(exchangeSticker.getOwner().getId(), exchangeSticker.getInterestingSticker().getStickerID()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getQuantity() <= 1 || collectionStickerRepository.existsStickerForUser(exchangeSticker.getInterested().getId(), exchangeSticker.getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }
        giveStickerToUser(exchangeStickerId, exchangeSticker.getOwner().getEmail(), exchangeSticker.getInterested().getId());
        giveStickerToUser(exchangeSticker.getInterestingSticker().getStickerID(), exchangeSticker.getInterested().getEmail(), exchangeSticker.getOwner().getId());
        exchangeSticker.setStatus(ExchangeStatus.COMPLETED);
        exchangeSticker.setExchangeDate(LocalDateTime.now());
        exchangeSticker.setStickerForOwner(stickerRepository.findById(exchangeStickerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public void sendMassiveExchangeProposal(Long userId, String stickerOwnerEmail) {
        CollectionUser interestedUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser ownerUser = userRepository.findByEmail(stickerOwnerEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Sticker> interestedMissingStickers = collectionStickerRepository.findAllStickersAvailableForFirstUserButNotSecond(ownerUser.getId(), interestedUser.getId());
        List<Sticker> ownerMissingStickers = collectionStickerRepository.findAllStickersAvailableForFirstUserButNotSecond(interestedUser.getId(), ownerUser.getId());
        interestedMissingStickers=collectionStickerRepository.findAvailableFromList(interestedMissingStickers).orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT));
        ownerMissingStickers=collectionStickerRepository.findAvailableFromList((ownerMissingStickers)).orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT));

        if (interestedMissingStickers.isEmpty() || ownerMissingStickers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        int deleteCount = 0;
        if (interestedUser.getRole().getEnumID() == 3 || ownerUser.getRole().getEnumID() == 3) {
            while (interestedMissingStickers.size() > 2) {
                interestedMissingStickers.removeLast();
            }
            while (ownerMissingStickers.size() > 2) {
                ownerMissingStickers.removeLast();
            }
        }

        deleteCount = interestedMissingStickers.size() - ownerMissingStickers.size();

        if (deleteCount > 0) {
            while (deleteCount > 0) {
                interestedMissingStickers.removeLast();
                deleteCount--;
            }
        } else if (deleteCount < 0) {
            while (deleteCount < 0) {
                ownerMissingStickers.removeLast();
                deleteCount++;
            }
        }

        ExchangeSticker exchangeSticker = new ExchangeSticker(interestedMissingStickers, ownerUser, interestedUser);
        List<CollectionSticker> offeredStickers = collectionStickerRepository
                .findCollectionStickerListByOwnerAndStickerList(interestedUser.getId(), ownerMissingStickers)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        for (CollectionSticker cs : offeredStickers) {
            cs.increaseBlockedCopies();
        }
        collectionStickerRepository.saveAll(offeredStickers);
        exchangeSticker.setStickerForOwner(ownerMissingStickers);
        exchangeSticker.setProposalInitiationDate(LocalDateTime.now());
        exchangeRepository.save(exchangeSticker);
    }

    public VisualizeExchangeSticker openMassiveExchange(Long userId, Long exchangeId) {
        ExchangeSticker order = exchangeRepository.findById(exchangeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!userId.equals(order.getOwner().getId())&&!userId.equals(order.getInterested().getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return new VisualizeExchangeSticker(order.getId(), order.getInterestingStickerList(), order.getStickerListForOwner(), order.getInterested().getName(), order.getProposalInitiationDate());
    }

    @Transactional
    public void acceptMassiveExchangeProposal(long exchangeID, Long ownerID) {
        ExchangeSticker exchangeProposal = exchangeRepository.findById(exchangeID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!exchangeProposal.getOwner().getId().equals(ownerID)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        CollectionUser interestedUser = userRepository.findById(exchangeProposal.getInterested().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CollectionUser owner = userRepository.findById(ownerID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<CollectionSticker> ownerNewCollectionStickers = collectionStickerRepository.findCollectionStickerListByOwnerAndStickerList(interestedUser.getId(), exchangeProposal.getStickerListForOwner())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<CollectionSticker> interestedNewCollectionStickers = collectionStickerRepository.findCollectionStickerListByOwnerAndStickerList(ownerID, exchangeProposal.getInterestingStickerList())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        for (CollectionSticker cs : ownerNewCollectionStickers) {
            if (cs.getQuantity() > 1) {
                CollectionAlbum collectionAlbumSender = collectionAlbumRepository.findByUserAndSticker(interestedUser, cs.getSticker()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                albumService.markStickerAsObtained(ownerID, collectionAlbumSender.getCollectionAlbumId(), cs.getSticker().getStickerID());
                cs.reduceBlockedCopies();
                cs.reduceQuantity();
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        for (CollectionSticker cs : interestedNewCollectionStickers) {
            if (cs.getQuantity() > 1) {
                CollectionAlbum collectionAlbumSender = collectionAlbumRepository.findByUserAndSticker(owner, cs.getSticker()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                albumService.markStickerAsObtained(interestedUser.getId(), collectionAlbumSender.getCollectionAlbumId(), cs.getSticker().getStickerID());
                cs.reduceBlockedCopies();
                cs.reduceQuantity();
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        exchangeProposal.setExchangeDate(LocalDateTime.now());
        exchangeProposal.setStatus(ExchangeStatus.COMPLETED);
        exchangeRepository.save(exchangeProposal);
    }

    public void rejectMassiveExchangeProposal(long exchangeID, Long ownerID) {
        List<ExchangeSticker> stickerExchange = new ArrayList<>();
        stickerExchange.add(exchangeRepository.findById(exchangeID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        if (!stickerExchange.getFirst().getOwner().getId().equals(ownerID)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if(stickerExchange.getFirst().getStatus().equals(ExchangeStatus.REJECT)){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        rejectByTime(stickerExchange);
    }


    @Scheduled(fixedRate = 150000)
    public void updateProposals() {
        List<ExchangeSticker> timeRejectedExchanges = exchangeRepository.findAllPendingAndBeforeDate(LocalDateTime.now().minusMinutes(5));
        rejectByTime(timeRejectedExchanges);
    }

    private void rejectByTime(List<ExchangeSticker> exchangeStickers) {
        for (ExchangeSticker exchangeSticker : exchangeStickers) {
            List<CollectionSticker> ownerNewCollectionStickers = collectionStickerRepository
                    .findCollectionStickerListByOwnerAndStickerList(exchangeSticker.getInterested().getId(), exchangeSticker.getStickerListForOwner())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            for (CollectionSticker collectionSticker : ownerNewCollectionStickers) {
                collectionSticker.reduceBlockedCopies();
                collectionSticker.incrementQuantity();
                collectionStickerRepository.save(collectionSticker);
            }
            exchangeSticker.setStatus(ExchangeStatus.REJECT);
            exchangeRepository.save(exchangeSticker);
        }
    }


}




