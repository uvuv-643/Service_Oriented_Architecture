package ru.uvuv643.soa.api.v1.dto.human

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class CoordinatesDto(

        @field:Valid
        @field:NotNull(message = "is not correct value")
        @field:Max(9)
        @XmlElement(name = "x")
        val x: Int? = null,

        @field:Valid
        @field:NotNull(message = "is not correct value")
        @XmlElement(name = "y")
        val y: Float? = null,

        )