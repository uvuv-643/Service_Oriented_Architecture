package ru.uvuv643.soa.api.v1.dto.human

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.xml.bind.annotation.*

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class CarDto constructor(

        @field:Valid
        @field:Nullable
        @XmlAttribute(name = "cool")
        val cool: Boolean? = null,

        )