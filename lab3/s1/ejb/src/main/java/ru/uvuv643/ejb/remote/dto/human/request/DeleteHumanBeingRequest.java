package ru.uvuv643.ejb.remote.dto.human.request;

import ru.uvuv643.ejb.remote.dto.human.CarDto;

import jakarta.validation.Valid;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "DeleteHumanBeingRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteHumanBeingRequest implements Serializable {

        @XmlElement(name = "limit")
        @Valid
        private Integer limit;

        @XmlElement(name = "car")
        @Valid
        private CarDto car;

        @XmlElement(name = "impactSpeed")
        private Float impactSpeed;

        public DeleteHumanBeingRequest() {
                this(null, null, null);
        }

        public DeleteHumanBeingRequest(Integer limit, CarDto car, Float impactSpeed) {
                this.limit = limit;
                this.car = car;
                this.impactSpeed = impactSpeed;
        }

        public Integer getLimit() {
                return limit;
        }

        public void setLimit(Integer limit) {
                this.limit = limit;
        }

        public CarDto getCar() {
                return car;
        }

        public void setCar(CarDto car) {
                this.car = car;
        }

        public Float getImpactSpeed() {
                return impactSpeed;
        }

        public void setImpactSpeed(Float impactSpeed) {
                this.impactSpeed = impactSpeed;
        }
}