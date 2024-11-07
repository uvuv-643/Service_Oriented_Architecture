package ru.uvuv643.soa.api.v1.dto.human

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@JacksonXmlRootElement
data class CoordinatesDto constructor(

        @field:Valid
        @field:Nullable
        @field:Schema(
                description = "X coordinate",
                required = false,
        )
        @param:JacksonXmlProperty(namespace = "x")
        val x: Int?,

        @field:Valid
        @field:NotNull
        @field:Schema(
                description = "Y coordinate",
                required = true,
        )
        @param:JacksonXmlProperty(namespace = "y")
        val y: Float?,

        )