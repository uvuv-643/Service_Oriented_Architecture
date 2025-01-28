package ru.uvuv643.dto.human;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "listHumanBeingDto") // Root XML tag
public class ListHumanBeingDto {

        @JacksonXmlElementWrapper(localName = "pairs") // Wrapper element for the list
        @JacksonXmlProperty(localName = "pairTeamHumanDto") // Inner tag for each object in the list
        private List<PairTeamHumanDto> humanBeingPair = new ArrayList<>();

        // Default Constructor
        public ListHumanBeingDto() {
        }

        // Constructor
        public ListHumanBeingDto(List<PairTeamHumanDto> humanBeingPair) {
                this.humanBeingPair = humanBeingPair;
        }

        // Getters and Setters
        public List<PairTeamHumanDto> getHumanBeingPair() {
                return humanBeingPair;
        }

        public void setHumanBeingPair(List<PairTeamHumanDto> humanBeingPair) {
                this.humanBeingPair = humanBeingPair;
        }
}