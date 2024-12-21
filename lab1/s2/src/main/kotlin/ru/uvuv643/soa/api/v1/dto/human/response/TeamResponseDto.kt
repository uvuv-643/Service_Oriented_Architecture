package ru.uvuv643.soa.api.v1.dto.human.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema

@JacksonXmlRootElement
@Schema(title = "addingHeroResponse", description = "Result of add-hero request")
data class TeamResponseDto constructor(

    @field:Schema(
        description = "New Team List",
    )
    @param:JacksonXmlProperty(namespace = "teamList")
    val teamList: ListHumanBeingDto?,

    )
