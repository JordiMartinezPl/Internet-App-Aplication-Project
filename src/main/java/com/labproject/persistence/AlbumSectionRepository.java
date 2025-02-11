package com.labproject.persistence;

import com.labproject.domain.AlbumSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumSectionRepository extends JpaRepository<AlbumSection, Long> {

}
