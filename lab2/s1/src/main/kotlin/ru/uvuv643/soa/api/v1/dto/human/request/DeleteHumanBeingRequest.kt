package ru.uvuv643.soa.api.v1.dto.human.request

import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import ru.uvuv643.soa.api.v1.dto.human.CarDto

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class DeleteHumanBeingRequest (

        @XmlElement(name = "limit")
        @field:Nullable
        @field:Valid
        val limit: Int?,

        @XmlElement(name = "car")
        @field:Nullable
        @field:Valid
        val car: CarDto?,

        @XmlElement(name = "impactSpeed")
        @field:Nullable
        val impactSpeed: Float?,

        ) {
        constructor() : this(null, null, null)
}