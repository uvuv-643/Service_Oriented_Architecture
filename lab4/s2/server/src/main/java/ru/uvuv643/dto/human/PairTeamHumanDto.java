package ru.uvuv643.dto.human;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "pairTeamHumanDto") // Root XML tag for individual pair
public class PairTeamHumanDto {

        @JacksonXmlProperty(localName = "humanBeingId") // Tag for humanBeingId field
        private int humanBeingId;

        @JacksonXmlProperty(localName = "teamId") // Tag for teamId field
        private int teamId;

        // Default Constructor
        public PairTeamHumanDto() {
        }

        // Constructor
        public PairTeamHumanDto(int humanBeingId, int teamId) {
                this.humanBeingId = humanBeingId;
                this.teamId = teamId;
        }

        // Getters and Setters
        public int getHumanBeingId() {
                return humanBeingId;
        }

        public void setHumanBeingId(int humanBeingId) {
                this.humanBeingId = humanBeingId;
        }

        public int getTeamId() {
                return teamId;
        }

        public void setTeamId(int teamId) {
                this.teamId = teamId;
        }
}