package com.labproject.persistence;

import com.labproject.application.dto.output.VisualizeStickerDTO;
import com.labproject.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {

    @Query("SELECT new com.labproject.application.dto.output.VisualizeStickerDTO(" +
            "s.numberInAlbum, s.name, s.description, s.imageURL, s.typeOfSticker) " +
            "FROM Album a " +
            "JOIN a.sections sec " +
            "JOIN sec.stickers s " +
            "WHERE a.albumId = :albumId AND s.numberInAlbum = :number")
    Optional<VisualizeStickerDTO> findVisualizeStickerByAlbumAndNumber(@Param("albumId") Long albumId, @Param("number") Integer number);

}
