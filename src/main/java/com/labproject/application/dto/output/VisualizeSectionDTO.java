package com.labproject.application.dto.output;

public class VisualizeSectionDTO {

    private String sectionName;

    private int numberOfStickers;


    public VisualizeSectionDTO() {
    }

    public VisualizeSectionDTO(String sectionName, int numberOfStickers) {
        this.sectionName = sectionName;
        this.numberOfStickers = numberOfStickers;
    }

    public int getNumberOfStickers() {
        return numberOfStickers;
    }

    public void setNumberOfStickers(int numberOfStickers) {
        this.numberOfStickers = numberOfStickers;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
