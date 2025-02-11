package com.labproject.persistence;


import com.labproject.domain.ExchangeSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeSticker, Long> {

    @Query("""

                SELECT es FROM ExchangeSticker es WHERE es.status= 'PENDING' AND  es.proposalInitiationDate <=:dateTime
            """)
    List<ExchangeSticker> findAllPendingAndBeforeDate(@Param("dateTime") LocalDateTime dateTime);

    @Query("""
                    SELECT es FROM ExchangeSticker es JOIN es.stickersForOwner WHERE es.interested.collectionUserID=:interestedId
            """)
    List<ExchangeSticker> findAllByInterestedId(@Param("interestedId") Long interestedId);
}
