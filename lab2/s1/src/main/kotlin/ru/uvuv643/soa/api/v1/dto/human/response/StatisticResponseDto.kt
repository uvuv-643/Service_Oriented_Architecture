package ru.uvuv643.soa.api.v1.dto.human.response

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class StatisticResponseDto (

        @XmlElement(name = "result")
        val result: Double?,

        ) {
        constructor() : this(null)
}