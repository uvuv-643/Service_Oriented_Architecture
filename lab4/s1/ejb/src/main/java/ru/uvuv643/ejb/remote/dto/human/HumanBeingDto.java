package ru.uvuv643.ejb.remote.dto.human;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlAttribute;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import ru.uvuv643.ejb.remote.dto.enums.MoodDto;
import ru.uvuv643.ejb.remote.dto.enums.WeaponTypeDto;

import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "HumanBeingDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class HumanBeingDto implements Serializable {

        @XmlElement(name = "id")
        @NotNull
        @Min(0)
        private Integer id;

        @XmlElement(name = "name")
        @NotNull
        @Size(min = 1)
        private String name;

        @XmlElement(name = "coordinates")
        @NotNull
        @Valid
        private CoordinatesDto coordinates;

        @XmlElement(name = "creationDate")
        @NotNull
        private Date creationDate;

        @XmlAttribute(name = "realHero")
        @NotNull
        private Boolean realHero;

        @XmlAttribute(name = "hasToothpick")
        @NotNull
        private Boolean hasToothpick;

        @XmlElement(name = "impactSpeed")
        @NotNull
        private Double impactSpeed;

        @XmlElement(name = "minutesOfWaiting")
        @NotNull
        @Min(0)
        private Double minutesOfWaiting;

        @XmlElement(name = "weaponType")
        @Valid
        private WeaponTypeDto weaponType;

        @XmlElement(name = "mood")
        @NotNull
        @Valid
        private MoodDto mood;

        @XmlElement(name = "car")
        @NotNull
        @Valid
        private CarDto car;

        public HumanBeingDto() {
                this(1, "null", null, new Date(), true, false, 1.0d, 1.0, WeaponTypeDto.HAMMER, MoodDto.FRENZY, new CarDto(true));
        }

        public HumanBeingDto(Integer id, String name, CoordinatesDto coordinates, Date creationDate, Boolean realHero, Boolean hasToothpick, Double impactSpeed, Double minutesOfWaiting, WeaponTypeDto weaponType, MoodDto mood, CarDto car) {
                this.id = id;
                this.name = name;
                this.coordinates = coordinates;
                this.creationDate = creationDate;
                this.realHero = realHero;
                this.hasToothpick = hasToothpick;
                this.impactSpeed = impactSpeed;
                this.minutesOfWaiting = minutesOfWaiting;
                this.weaponType = weaponType;
                this.mood = mood;
                this.car = car;
        }

        // Getter and Setter methods for all fields

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
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

        public Date getCreationDate() {
                return creationDate;
        }

        public void setCreationDate(Date creationDate) {
                this.creationDate = creationDate;
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

        public WeaponTypeDto getWeaponType() {
                return weaponType;
        }

        public void setWeaponType(WeaponTypeDto weaponType) {
                this.weaponType = weaponType;
        }

        public MoodDto getMood() {
                return mood;
        }

        public void setMood(MoodDto mood) {
                this.mood = mood;
        }

        public CarDto getCar() {
                return car;


        }

        public void setCar(CarDto car) {
                this.car = car;
        }

        @Override
        public String toString() {
                return "HumanBeingDto{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", coordinates=" + coordinates +
                        ", creationDate=" + creationDate +
                        ", realHero=" + realHero +
                        ", hasToothpick=" + hasToothpick +
                        ", impactSpeed=" + impactSpeed +
                        ", minutesOfWaiting=" + minutesOfWaiting +
                        ", weaponType=" + weaponType +
                        ", mood=" + mood +
                        ", car=" + car +
                        '}';
        }

}
