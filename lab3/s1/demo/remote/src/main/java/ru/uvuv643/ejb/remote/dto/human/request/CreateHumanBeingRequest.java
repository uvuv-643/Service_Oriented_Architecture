package ru.uvuv643.ejb.remote.dto.human.request;

import jakarta.validation.Valid;
import ru.uvuv643.ejb.remote.dto.enums.MoodDto;
import ru.uvuv643.ejb.remote.dto.enums.WeaponTypeDto;
import ru.uvuv643.ejb.remote.dto.human.CarDto;
import ru.uvuv643.ejb.remote.dto.human.CoordinatesDto;

import jakarta.validation.constraints.*;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlAttribute;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "CreateHumanBeingRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateHumanBeingRequest implements Serializable {

        @XmlElement(name = "name")
        @NotBlank
        private String name;

        @XmlElement(name = "coordinates")
        @NotNull
        @Valid
        private CoordinatesDto coordinates;

        @XmlAttribute(name = "realHero")
        @NotNull
        private Boolean realHero;

        @XmlAttribute(name = "hasToothpick")
        @NotNull
        private Boolean hasToothpick;

        @XmlElement(name = "impactSpeed")
        @NotNull(message = "is not correct value")
        @Max(676)
        private Float impactSpeed;

        @XmlElement(name = "minutesOfWaiting")
        @NotNull(message = "is not correct value")
        @PositiveOrZero
        private Double minutesOfWaiting;

        @XmlElement(name = "car")
        @Valid
        @NotNull
        private CarDto car;

        @XmlElement(name = "mood")
        @NotNull
        @Valid
        private MoodDto mood;

        @XmlElement(name = "weaponType")
        @Valid
        private WeaponTypeDto weaponType;

        public CreateHumanBeingRequest() {
                this(null, null, false, false, null, null, null, null, null);
        }

        public CreateHumanBeingRequest(String name, CoordinatesDto coordinates, Boolean realHero, Boolean hasToothpick,
                                       Float impactSpeed, Double minutesOfWaiting, CarDto car, MoodDto mood,
                                       WeaponTypeDto weaponType) {
                this.name = name;
                this.coordinates = coordinates;
                this.realHero = realHero;
                this.hasToothpick = hasToothpick;
                this.impactSpeed = impactSpeed;
                this.minutesOfWaiting = minutesOfWaiting;
                this.car = car;
                this.mood = mood;
                this.weaponType = weaponType;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public CoordinatesDto getCoordinates() {
                return coordinates;
        }

        public void setCoordinates(CoordinatesDto coordinates) {
                this.coordinates = coordinates;
        }

        public Boolean getRealHero() {
                return realHero;
        }

        public void setRealHero(Boolean realHero) {
                this.realHero = realHero;
        }

        public Boolean getHasToothpick() {
                return hasToothpick;
        }

        public void setHasToothpick(Boolean hasToothpick) {
                this.hasToothpick = hasToothpick;
        }

        public Float getImpactSpeed() {
                return impactSpeed;
        }

        public void setImpactSpeed(Float impactSpeed) {
                this.impactSpeed = impactSpeed;
        }

        public Double getMinutesOfWaiting() {
                return minutesOfWaiting;
        }

        public void setMinutesOfWaiting(Double minutesOfWaiting) {
                this.minutesOfWaiting = minutesOfWaiting;
        }

        public CarDto getCar() {
                return car;
        }

        public void setCar(CarDto car) {
                this.car = car;
        }

        public MoodDto getMood() {
                return mood;
        }

        public void setMood(MoodDto mood) {
                this.mood = mood;
        }

        public WeaponTypeDto getWeaponType() {
                return weaponType;
        }

        public void setWeaponType(WeaponTypeDto weaponType) {
                this.weaponType = weaponType;
        }
}