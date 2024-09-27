package ru.uvuv643.soa.api.v1.dto.human.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import ru.uvuv643.soa.api.v1.dto.human.CarDto

@JacksonXmlRootElement
data class DeleteHumanBeingRequest constructor(

        @field:Valid
        @field:Schema(
                description = "Car",
                required = false,
        )
        @param:JacksonXmlProperty(namespace = "car")
        val car: CarDto?,

        @field:Valid
        @field:Schema(
                description = "Impact Speed",
                required = false,
        )
        @param:JacksonXmlProperty(namespace = "impactSpeed")
        val impactSpeed: Float?,

        )