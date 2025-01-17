package ru.uvuv643.ejb.remote.dto.human.request;

import ru.uvuv643.ejb.remote.dto.enums.MoodDto;
import ru.uvuv643.ejb.remote.dto.enums.WeaponTypeDto;
import ru.uvuv643.ejb.remote.dto.human.CarDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlAttribute;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "ModifyHumanBeingRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModifyHumanBeingRequest implements Serializable {

        @XmlElement(name = "name")
        @Size(min = 1, max = 1024)
        private String name;

        @XmlElement(name = "coordinates")
        @Valid
        private CoordinatesUpdateDto coordinates;

        @XmlAttribute(name = "realHero")
        private Boolean realHero;

        @XmlAttribute(name = "hasToothpick")
        private Boolean hasToothpick;

        @XmlElement(name = "impactSpeed")
        @Max(676)
        private Double impactSpeed;

        @XmlElement(name = "minutesOfWaiting")
        @PositiveOrZero
        private Double minutesOfWaiting;

        @XmlElement(name = "car")
        @Valid
        private CarDto car;

        @XmlElement(name = "mood")
        @Valid
        private MoodDto mood;

        @XmlElement(name = "weaponType")
        @Valid
        private WeaponTypeDto weaponType;

        public ModifyHumanBeingRequest() {
                this(null, null, null, null, null, null, null, null, null);
        }

        public ModifyHumanBeingRequest(String name, CoordinatesUpdateDto coordinates, Boolean realHero, Boolean hasToothpick,
                                       Double impactSpeed, Double minutesOfWaiting, CarDto car, MoodDto mood,
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

        public CoordinatesUpdateDto getCoordinates() {
                return coordinates;
        }

        public void setCoordinates(CoordinatesUpdateDto coordinates) {
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

        public Double getImpactSpeed() {
                return impactSpeed;
        }

        public void setImpactSpeed(Double impactSpeed) {
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