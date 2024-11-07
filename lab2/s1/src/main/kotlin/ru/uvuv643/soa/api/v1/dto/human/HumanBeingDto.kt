package ru.uvuv643.soa.api.v1.dto.human

import jakarta.validation.constraints.NotNull
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import jakarta.xml.bind.annotation.XmlAttribute
import java.util.*


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class HumanBeingDto (

        @XmlElement(name = "id")
        @NotNull
        @Min(0)
        val id : Int,

        @XmlElement(name = "name")
        @NotNull
        @Size(min = 1)
        val name : String,

        @XmlElement(name = "coordinates")
        @NotNull
        @Valid
        val coordinates : CoordinatesDto?,

        @XmlElement(name = "creationDate")
        @NotNull
        val creationDate : Date,

        @XmlAttribute(name = "realHero")
        @NotNull
        val realHero : Boolean,

        @XmlAttribute(name = "hasToothpick")
        @NotNull
        val hasToothpick : Boolean,

        @XmlElement(name = "impactSpeed")
        @NotNull
        val impactSpeed : Float,

        @XmlElement(name = "minutesOfWaiting")
        @NotNull
        @Min(0)
        val minutesOfWaiting : Double,

        @XmlElement(name = "weaponType")
        @Valid
        val weaponType : String?,

        @XmlElement(name = "mood")
        @NotNull
        @Valid
        val mood : String,

        @XmlElement(name = "car")
        @Valid
        @NotNull
        val car : CarDto,

        ) {
        constructor() : this(
                id = 1,
                name = "null",
                coordinates = null,
                creationDate = Date(),
                realHero = true,
                hasToothpick = false,
                impactSpeed = 1.0f,
                minutesOfWaiting = 1.0,
                car = CarDto(
                        cool = true
                ),
                mood = "",
                weaponType = ""
        )
}