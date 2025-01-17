package ru.uvuv643.ejb.remote.dto.human;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "CoordinatesDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CoordinatesDto implements Serializable {

        @Valid
        @NotNull(message = "is not correct value")
        @Max(9)
        @XmlElement(name = "x")
        private Integer x;

        @Valid
        @NotNull(message = "is not correct value")
        @XmlElement(name = "y")
        private Long y;

        public CoordinatesDto() {
                this(null, null);
        }

        public CoordinatesDto(Integer x, Long y) {
                this.x = x;
                this.y = y;
        }

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

        @Override
        public String toString() {
                return "Coordinates{" +
                        "x=" + x +
                        ", y=" + y  + "}";
        }

}