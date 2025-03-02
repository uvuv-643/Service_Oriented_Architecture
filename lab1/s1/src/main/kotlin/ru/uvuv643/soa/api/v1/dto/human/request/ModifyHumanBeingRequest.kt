package ru.uvuv643.soa.api.v1.dto.human.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import jakarta.xml.bind.annotation.XmlAttribute
import ru.uvuv643.soa.api.v1.dto.enums.MoodDto
import ru.uvuv643.soa.api.v1.dto.enums.WeaponTypeDto
import ru.uvuv643.soa.api.v1.dto.human.CarDto
import ru.uvuv643.soa.api.v1.dto.human.CoordinatesDto


@JacksonXmlRootElement(namespace = "modifyHumanBeingRequest")
@Schema(name = "modifyHumanBeingRequest")
data class ModifyHumanBeingRequest constructor(

        @field:Valid
        @field:Size(min = 1, message = "String must be non-empty")
        @field:Schema(
                description = "Name of Human Being",
                required = false,
        )
        @param:JacksonXmlProperty(namespace = "name")
        val name: String?,

        @field:Valid
        @param:JacksonXmlProperty(namespace = "coordinates")
        @field:Schema(
                description = "Coordinates information",
                required = false,
        )
        val coordinates: CoordinatesDto,

        @field:Valid
        @field:Schema(
                description = "Is real hero?",
                required = false,
        )
        @XmlAttribute
        @param:JacksonXmlProperty(isAttribute = true, namespace = "realHero")
        val realHero: Boolean?,

        @field:Valid
        @field:Schema(
                description = "Does hero have a toothpick?",
                required = false,
        )
        @XmlAttribute
        @param:JacksonXmlProperty(isAttribute = true, namespace = "hasToothpick")
        val hasToothpick: Boolean?,

        @field:Valid
        @field:Min(0, message = "Speed must be non-negative")
        @field:Max(676, message = "Maximum value of impact speed is 676")
        @field:Nullable
        @field:Schema(
                description = "Impact Speed",
                required = false,
        )
        @param:JacksonXmlProperty(namespace = "impactSpeed")
        val impactSpeed: Float?,

        @field:Valid
        @field:Min(0, message = "Time must be non-negative")
        @field:Schema(
                description = "Minutes of waiting",
                required = false,
        )
        @param:JacksonXmlProperty(namespace = "minutesOfWaiting")
        val minutesOfWaiting: Long?,

        @field:Valid
        @field:Nullable
        @param:JacksonXmlProperty(namespace = "car")
        @field:Schema(
                description = "Car information",
                required = false,
        )
        val car: CarDto,

        @field:Valid
        @field:Nullable
        @param:JacksonXmlProperty(namespace = "mood")
        @field:Schema(
                description = "Mood type",
                required = false,
        )
        val mood: MoodDto,

        @field:Valid
        @field:Nullable
        @param:JacksonXmlProperty(namespace = "weaponType")
        @field:Schema(
                description = "Weapon Type",
                required = false,
        )
        val weaponType: WeaponTypeDto,

        )

