package com.labproject.persistence;

import com.labproject.application.dto.output.VisualizeSaleStickerDTO;
import com.labproject.domain.SaleSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleStickerRepository extends JpaRepository<SaleSticker, Long> {


    @Query("""
            SELECT new com.labproject.application.dto.output.VisualizeSaleStickerDTO(
                ss.collectionSticker.sticker.stickerID,
                ss.collectionSticker.sticker.name,
                ss.seller.email,
                ss.price
            )
            FROM SaleSticker ss
            JOIN ss.collectionSticker cs
            JOIN cs.sticker s
            JOIN CollectionAlbum ca ON cs MEMBER OF ca.collectedStickers
            WHERE ca.album.albumId = :albumId
            """)
    List<VisualizeSaleStickerDTO> findSaleStickersByAlbumId(long albumId);


    @Query("SELECT s FROM SaleSticker s WHERE s.collectionSticker.sticker.stickerID = :stickerId AND s.seller.collectionUserID = :sellerId")
    Optional<SaleSticker> findByStickerIdAndSellerId(@Param("stickerId") Long stickerId, @Param("sellerId") Long sellerId);


}


