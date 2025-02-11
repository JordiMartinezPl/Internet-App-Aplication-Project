package com.labproject.api;

import com.labproject.application.AlbumService;
import com.labproject.application.ExchangeService;
import com.labproject.application.dto.input.AlbumDTO;
import com.labproject.application.dto.output.*;
import com.labproject.security.authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/albums")
public class AlbumRest {

    private final AlbumService albumService;
    private final ExchangeService exchangeService;

    @Autowired
    public AlbumRest(AlbumService albumService, ExchangeService exchangeService) {
        this.albumService = albumService;
        this.exchangeService = exchangeService;
    }

    //SPRINT 1 STUDENT 2 (1) AND SPRINT 1 STUDENT 3 (1)
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAlbum(@RequestBody @Valid AlbumDTO album) {
        Long userID = AuthenticationService.getIDFromToken();
        albumService.createAlbum(userID, album);
    }


    //SPRINT 1 STUDENT 2 (2)
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeAlbumDTO> getInfoAllAlbums() {
        return albumService.getInfoAllAlbums();
    }

    //SPRINT 1 STUDENT 2 (3)
    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeAlbumDTO> getAllAvailableAlbums() {
        return albumService.getAvailableAlbums();
    }

    //SPRINT 2 STUDENT 1 (1)
    @PostMapping("/{id}/publicAvailability")
    @ResponseStatus(HttpStatus.OK)
    public void changePublicAvailability(@PathVariable long id) {
        albumService.changePublicAvailability(id);
    }


    //SPRINT 2 STUDENT 1 (2)
    @GetMapping("/{albumID}/number/{number}")
    @ResponseStatus(HttpStatus.OK)
    public VisualizeStickerDTO getStickerByAlbumI(@PathVariable long albumID, @PathVariable int number) {
        return albumService.getStickerByAlbumAndNumber(albumID, number);
    }

    //SPRINT 2 STUDENT 1 (3)
    @PostMapping("/{albumID}/collectionAlbum")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCollectionAlbum(@PathVariable long albumID) {
        Long userID = AuthenticationService.getIDFromToken();
        albumService.addCollectionAlbum(userID, albumID);
    }


    //SPRINT 2 STUDENT 1 (4)
    @GetMapping("/{id}/users")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeCollectionUserDTO> getUsersCollectingAlbum(@PathVariable long id) {
        return albumService.getUsersCollectingAlbum(id);
    }


    //SPRINT 2 STUDENT 1 (5)
    @GetMapping("/{albumID}/details")
    @ResponseStatus(HttpStatus.OK)
    public VisualizeAlbumDTO getAlbumDetails(@PathVariable long albumID) {
        return albumService.getAlbumDetails(albumID);
    }

    //SPRINT 2 STUDENT 1 (6)
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeAlbumDTO> getMyActiveAlbums() {
        Long userId = AuthenticationService.getIDFromToken();
        return albumService.getMyActiveAlbums(userId);
    }


    //SPRINT 2 STUDENT 1 (7)
    @GetMapping("/ended")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeCollectionAlbumDTO> getMyEndedAlbums() {
        Long userID = AuthenticationService.getIDFromToken();
        return albumService.getMyEndedAlbums(userID);
    }

    //SPRINT 2 STUDENT 2 (1)
    @PutMapping("/{albumID}/sticker/{stickerID}")
    @ResponseStatus(HttpStatus.OK)
    public void obtainStickerByID(@PathVariable long albumID, @PathVariable long stickerID) {
        Long userID = AuthenticationService.getIDFromToken();
        albumService.obtainSticker(userID, albumID, stickerID);
    }

    //SPRINT 2 STUDENT 2 (2)
    @GetMapping("/{id}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<HasStickerDTO> checkStickersByAlbum(@PathVariable long id) {
        Long userID = AuthenticationService.getIDFromToken();
        return albumService.checkStickers(userID, id);
    }

    //SPRINT 2 STUDENT 2 (3)
    @GetMapping("/{id}/missingStickers")
    @ResponseStatus(HttpStatus.OK)
    public List<HasStickerDTO> getMissingStickers(@PathVariable long id) {
        Long userID = AuthenticationService.getIDFromToken();
        return albumService.getMissingStickers(userID, id);
    }

    //SPRINT 2 STUDENT 2 (2)
    @GetMapping("/{id}/collectedStickers")
    @ResponseStatus(HttpStatus.OK)
    public List<HasStickerDTO> getCollectedStickers(@PathVariable long id) {
        Long userID = AuthenticationService.getIDFromToken();
        return albumService.getCollectedStickers(userID, id);
    }

    //SPRINT 3 STUDENT 3 (1)
    @PostMapping("/sticker/{stickerId}/sale")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void putOnSaleSticker(@PathVariable long stickerId, @RequestParam long price) {
        Long userId = AuthenticationService.getIDFromToken();
        exchangeService.putOnSaleSticker(userId, stickerId, price);
    }

    //SPRINT 3 STUDENT (2)
    @PostMapping("saleSticker/{saleStickerId}/buy")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void buySaleSticker(@PathVariable long saleStickerId) {
        Long userId = AuthenticationService.getIDFromToken();
        exchangeService.buySaleSticker(userId, saleStickerId);
    }

    //SPRINT 3 STUDENT 3 (3)
    @GetMapping("/{id}/saleStickers")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeSaleStickerDTO> getAllSaleStickerByAlbumID(@PathVariable long id) {
        return exchangeService.getAllSaleStickerByAlbumId(id);
    }

    //SPRINT 4 STUDENT 1 (1)
    @PostMapping("/sticker/{stickerId}/gift")
    @ResponseStatus(HttpStatus.OK)
    public void giveStickerToUser(@PathVariable long stickerId, @RequestParam String receiverEmail) {
        Long senderId = AuthenticationService.getIDFromToken();
        exchangeService.giveStickerToUser(stickerId, receiverEmail, senderId);

    }

    //SPRINT 4 STUDENT 1 (2)
    @GetMapping("/sticker/{stickerId}/users")
    @ResponseStatus(HttpStatus.OK)
    public List<VisualizeUserWithStickersDTO> getInterestingProposals(@PathVariable long stickerId) {
        Long userID = AuthenticationService.getIDFromToken();
        return exchangeService.getInterestingProposals(userID, stickerId);
    }

    //SPRINT 4 STUDENT 1 (3)
    @PostMapping("/sticker/{stickerId}/interested")
    public void sendProposal(@PathVariable long stickerId, @RequestParam String receiverEmail) {
        Long userID = AuthenticationService.getIDFromToken();
        exchangeService.sendProposal(userID, receiverEmail, stickerId);
    }

    //SPRINT 4 STUDENT 1 (4)
    @PostMapping("/exchange/{exchangeId}")
    public void acceptProposal(@PathVariable long exchangeId, @RequestParam long exchangeSticker) {
        exchangeService.acceptProposal(exchangeId, exchangeSticker);
    }

    //SPRINT 4 STUDENT 3 (2)
    @PostMapping("exchange/fullProposal/send")
    public void sendMassiveProposal(@RequestParam String stickerOwnerEmail) {
        Long userID = AuthenticationService.getIDFromToken();
        exchangeService.sendMassiveExchangeProposal(userID, stickerOwnerEmail);

    }

    //SPRINT 4 STUDENT 3 (3)

    @GetMapping("/exchange/fullProposal/{exchangeId}")
    public VisualizeExchangeSticker openMassiveProposal(@PathVariable long exchangeId) {    //see MassiveProposal
        Long userID = AuthenticationService.getIDFromToken();
        return exchangeService.openMassiveExchange(userID, exchangeId);
    }

    @PostMapping("/exchange/fullProposal/accept/{exchangeID}")
    public void acceptMassiveProposal(@PathVariable long exchangeID) {
        Long userID = AuthenticationService.getIDFromToken();
        exchangeService.acceptMassiveExchangeProposal(exchangeID, userID);
    }

    @PostMapping("/exchange/fullProposal/reject/{exchangeID}")
    public void rejectMassiveProposal(@PathVariable long exchangeID) {
        Long userID = AuthenticationService.getIDFromToken();
        exchangeService.rejectMassiveExchangeProposal(exchangeID, userID);
    }


}
