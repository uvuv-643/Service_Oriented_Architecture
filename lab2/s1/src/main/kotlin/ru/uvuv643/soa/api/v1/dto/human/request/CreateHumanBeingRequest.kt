package ru.uvuv643.soa.api.v1.dto.human.request

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
data class CreateHumanBeingRequest constructor(
        @XmlElement(name = "name")
        val name: String,

        @XmlElement(name = "coordinates")
        val coordinates: CoordinatesDto,

        @XmlAttribute(name = "realHero")
        val realHero: Boolean,

        @XmlAttribute(name = "hasToothpick")
        val hasToothpick: Boolean,

        @XmlElement(name = "impactSpeed")
        val impactSpeed: Float,

        @XmlElement(name = "minutesOfWaiting")
        val minutesOfWaiting: Long,

        @XmlElement(name = "car")
        val car: CarDto,

        @XmlElement(name = "mood")
        val mood: MoodDto?,

        @XmlElement(name = "weaponType")
        val weaponType: WeaponTypeDto?
)
{
        constructor() : this(
                name = "null",
                coordinates = CoordinatesDto(
                    x = 1,
                    y = 0.5f
                ),
                realHero = true,
                hasToothpick = false,
                impactSpeed = 656f,
                minutesOfWaiting = 6,
                car = CarDto(
                    cool = true
                ),
                mood = MoodDto.CALM,
                weaponType = WeaponTypeDto.BAT
        )
}