package ru.uvuv643.soa.api.v1.dto.human

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.xml.bind.annotation.XmlAttribute

@JacksonXmlRootElement
data class CarDto constructor(

        @field:Valid
        @field:Nullable
        @field:Schema(
                description = "Is car really cool?",
                required = false,
        )
        @XmlAttribute
        @param:JacksonXmlProperty(isAttribute = true, namespace = "cool")
        val cool: Boolean?,

)