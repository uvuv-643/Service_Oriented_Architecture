package ru.uvuv643.soa.api.v1.dto.human.request

import EnumValue
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import jakarta.xml.bind.annotation.*
import ru.uvuv643.soa.api.v1.dto.enums.MoodDto
import ru.uvuv643.soa.api.v1.dto.enums.WeaponTypeDto
import ru.uvuv643.soa.api.v1.dto.human.CarDto
import ru.uvuv643.soa.api.v1.dto.human.CoordinatesDto



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class ModifyHumanBeingRequest (

        @XmlElement(name = "name")
        @field:Nullable
        @field:Size(min = 1, max = 1024)
        val name: String?,

        @XmlElement(name = "coordinates")
        @field:Valid
        val coordinates: CoordinatesUpdateDto?,

        @XmlAttribute(name = "realHero")
        val realHero: Boolean?,

        @XmlAttribute(name = "hasToothpick")
        val hasToothpick: Boolean?,

        @XmlElement(name = "impactSpeed")
        @field:Max(676)
        val impactSpeed: Float?,

        @XmlElement(name = "minutesOfWaiting")
        @field:PositiveOrZero
        val minutesOfWaiting: Double?,

        @XmlElement(name = "car")
        @field:Valid
        val car: CarDto?,

        @XmlElement(name = "mood")
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
                realHero = null,
                hasToothpick = null,
                impactSpeed = null,
                minutesOfWaiting = null,
                car = null,
                mood = null,
                weaponType = null
        )
}