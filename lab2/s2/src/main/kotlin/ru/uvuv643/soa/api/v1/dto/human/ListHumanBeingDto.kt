package ru.uvuv643.soa.api.v1.dto.human

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema

@JacksonXmlRootElement
@Schema(description = "List of human being")
data class ListHumanBeingDto constructor(

        @Schema(
                description = "Result human being",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @param:JacksonXmlProperty(namespace = "objects")
        val humanBeing : List<HumanBeingDto>,

)