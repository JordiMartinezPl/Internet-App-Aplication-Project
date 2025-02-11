package com.labproject.application.dto.input;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class AlbumDTO {


    private boolean publicAvailability = true;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private LocalDate beginDate;
    @Future
    private LocalDate endingDate;

    @NotNull
    private List<AlbumSectionDTO> sections = null;

    public AlbumDTO() {
    }

    public <E> AlbumDTO(String albumTitle, String albumDescription, LocalDate now, LocalDate localDate, boolean b, List<AlbumSectionDTO> section1) {
        this.title = albumTitle;
        this.description = albumDescription;
        this.beginDate = now;
        this.endingDate = localDate;
        this.publicAvailability = b;
        this.sections = section1;
    }


    public boolean isPublicAvailability() {
        return publicAvailability;
    }

    public void setPublicAvailability(boolean publicAvailability) {
        this.publicAvailability = publicAvailability;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }


    public List<AlbumSectionDTO> getSections() {
        return sections;
    }

    public void setSections(List<AlbumSectionDTO> sections) {
        this.sections = sections;
    }


}
