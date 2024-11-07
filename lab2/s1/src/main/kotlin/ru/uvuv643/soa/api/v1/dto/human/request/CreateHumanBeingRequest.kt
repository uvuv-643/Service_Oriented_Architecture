package ru.uvuv643.soa.api.v1.dto.human.request

import EnumValue
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlElement
import ru.uvuv643.soa.api.v1.dto.enums.MoodDto
import ru.uvuv643.soa.api.v1.dto.enums.WeaponTypeDto
import ru.uvuv643.soa.api.v1.dto.human.CarDto
import ru.uvuv643.soa.api.v1.dto.human.CoordinatesDto

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class CreateHumanBeingRequest (

        @XmlElement(name = "name")
        @field:NotBlank
        val name: String?,

        @XmlElement(name = "coordinates")
        @field:NotNull
        @field:Valid
        val coordinates: CoordinatesDto?,

        @XmlAttribute(name = "realHero")
        @field:NotNull
        val realHero: Boolean?,

        @XmlAttribute(name = "hasToothpick")
        @field:NotNull
        val hasToothpick: Boolean?,

        @XmlElement(name = "impactSpeed")
        @field:NotNull(message = "is not correct value")
        @field:Max(676)
        val impactSpeed: Float?,

        @XmlElement(name = "minutesOfWaiting")
        @field:NotNull(message = "is not correct value")
        @field:PositiveOrZero
        val minutesOfWaiting: Double?,

        @XmlElement(name = "car")
        @field:Valid
        @field:NotNull
        val car: CarDto?,

        @XmlElement(name = "mood")
        @field:NotNull
        @field:Valid
        @field:EnumValue(
                enumClass = MoodDto::class,
        )
        val mood: String?,

        @XmlElement(name = "weaponType")
        @field:Valid
        @field:EnumValue(
                enumClass = WeaponTypeDto::class,
        )
        val weaponType: String?
)
{
        constructor() : this(
                name = null,
                coordinates = null,
                realHero = false,
                hasToothpick = false,
                impactSpeed = null,
                minutesOfWaiting = null,
                car = null,
                mood = null,
                weaponType = null
        )
}