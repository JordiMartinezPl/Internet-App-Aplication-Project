package com.labproject.application.dto.output;

import java.time.LocalDate;
import java.util.List;

public class VisualizeAlbumDTO {

    private String name;

    private LocalDate beginDate;

    private LocalDate endingDate;
    ;

    private List<Object> visualizeSectionDTOList;

    public VisualizeAlbumDTO() {
    }

    public VisualizeAlbumDTO(String name, LocalDate beginDate, LocalDate endingDate, List<Object> visualizeSectionDTOList) {
        this.name = name;
        this.beginDate = beginDate;
        this.endingDate = endingDate;
        this.visualizeSectionDTOList = visualizeSectionDTOList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Object> getVisualizeSectionDTOList() {
        return visualizeSectionDTOList;
    }

    public void setVisualizeSectionDTOList(List<Object> visualizeSectionDTOList) {
        this.visualizeSectionDTOList = visualizeSectionDTOList;
    }
}
