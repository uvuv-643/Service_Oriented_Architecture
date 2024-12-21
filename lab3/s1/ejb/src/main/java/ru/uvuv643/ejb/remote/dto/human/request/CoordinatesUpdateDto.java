package ru.uvuv643.ejb.remote.dto.human.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "CoordinatesUpdateDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CoordinatesUpdateDto implements Serializable {

        @Valid
        @Max(9)
        @XmlElement(name = "x")
        private Integer x;

        @Valid
        @XmlElement(name = "y")
        private Long y;

        // Default constructor
        public CoordinatesUpdateDto() {
                this(null, null);
        }

        // Parameterized constructor
        public CoordinatesUpdateDto(Integer x, Long y) {
                this.x = x;
                this.y = y;
        }

        // Getters and Setters
        public Integer getX() {
                return x;
        }

        public void setX(Integer x) {
                this.x = x;
        }

        public Long getY() {
                return y;
        }

        public void setY(Long y) {
                this.y = y;
        }
}