package ru.uvuv643.soa.api.v1.dto.human

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.xml.bind.annotation.XmlAttribute
import ru.uvuv643.soa.api.v1.dto.enums.MoodDto
import ru.uvuv643.soa.api.v1.dto.enums.WeaponTypeDto
import java.util.*


@Schema(title = "HumanBeing", description = "Main structural unit. Describes a person. Purpose of this object is to be a part of some collection.")
@JacksonXmlRootElement
data class HumanBeingDto constructor(

    @Schema(
        description = "Unique ID generated on server",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "id")
    val id : Int,

    @Schema(
        description = "Name of Human Being",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "name")
    val name : String,

    @Schema(
        description = "Coordinates",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "coordinates")
    val coordinates : CoordinatesDto,

    @Schema(
        description = "When object was created",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "creationDate")
    val creationDate : Date,

    @Schema(
        description = "Is human real hero?",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @XmlAttribute
    @param:JacksonXmlProperty(isAttribute = true,namespace = "realHero")
    val realHero : Boolean,

    @Schema(
        description = "Does human has toothpick?",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @XmlAttribute
    @param:JacksonXmlProperty(isAttribute = true, namespace = "hasToothpick")
    val hasToothpick : Boolean,

    @Schema(
        description = "Impact speed",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "impactSpeed")
    val impactSpeed : Float,

    @Schema(
        description = "Minutes of waiting",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "minutesOfWaiting")
    val minutesOfWaiting : Long,

    @Schema(
        description = "Weapon Type",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "weaponType")
    val weaponType : WeaponTypeDto,

    @Schema(
        description = "Mood",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "mood")
    val mood : MoodDto,

    @Schema(
        description = "Human Car",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @param:JacksonXmlProperty(namespace = "car")
    val car : CarDto,

)