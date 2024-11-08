package ru.uvuv643.soa.api.v1.dto.human.request

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class CoordinatesUpdateDto(

        @field:Valid
        @field:Max(9)
        @XmlElement(name = "x")
        val x: Int?,

        @field:Valid
        @XmlElement(name = "y")
        val y: Long?,

        ) {
        constructor() : this(null, null)
}