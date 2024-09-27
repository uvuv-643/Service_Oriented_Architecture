package ru.uvuv643.soa.api.v1.dto.human.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@JacksonXmlRootElement
data class DeleteHumanBeingByImpactSpeedRequest constructor(

        @field:Valid
        @field:NotNull
        @field:Schema(
                description = "Impact Speed",
                required = true,
        )
        @param:JacksonXmlProperty(namespace = "impactSpeed")
        val impactSpeed: Float?,

        )