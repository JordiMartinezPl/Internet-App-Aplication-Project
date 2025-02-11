package com.labproject.application.dto.output;

import java.util.List;

public class VisualizeSectionWithStickerDTO {
    private String sectionName;

    private List<VisualizeStickerDTO> stickerDTOList;


    public VisualizeSectionWithStickerDTO() {
    }

    public VisualizeSectionWithStickerDTO(String sectionName, List<VisualizeStickerDTO> stickerDTOList) {
        this.sectionName = sectionName;
        this.stickerDTOList = stickerDTOList;
    }

    public List<VisualizeStickerDTO> getStickers() {
        return stickerDTOList;
    }

    public void setStickerDTOList(List<VisualizeStickerDTO> stickerDTOList) {
        this.stickerDTOList = stickerDTOList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
