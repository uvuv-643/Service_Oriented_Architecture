package ru.uvuv643.ejb.remote.dto.human;

import jakarta.validation.Valid;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlAttribute;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "CarDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarDto implements Serializable {

        @Valid
        @XmlAttribute(name = "cool")
        private Boolean cool;

        public CarDto() {
                this(null);
        }

        public CarDto(Boolean cool) {
                this.cool = cool;
        }

        public Boolean getCool() {
                return cool;
        }

        public void setCool(Boolean cool) {
                this.cool = cool;
        }

        @Override
        public String toString() {
                return "Car {" +
                        "cool='" + cool + "'}";
        }

}