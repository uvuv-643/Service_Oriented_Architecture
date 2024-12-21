package ru.uvuv643.ejb.remote.dto.human.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "DeleteHumanBeingByImpactSpeedRequest")
public class DeleteHumanBeingByImpactSpeedRequest implements Serializable {

        @Valid
        @NotNull(message = "Impact speed cannot be null")
        @JacksonXmlProperty(localName = "impactSpeed")
        private Float impactSpeed;

        // Default constructor
        public DeleteHumanBeingByImpactSpeedRequest() {
        }

        // Parameterized constructor
        public DeleteHumanBeingByImpactSpeedRequest(Float impactSpeed) {
                this.impactSpeed = impactSpeed;
        }

        // Getter and Setter
        public Float getImpactSpeed() {
                return impactSpeed;
        }

        public void setImpactSpeed(Float impactSpeed) {
                this.impactSpeed = impactSpeed;
        }
}