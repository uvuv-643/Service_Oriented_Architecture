package ru.uvuv643.soa.api.v1.dto.human.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema

@JacksonXmlRootElement
@Schema(title = "statisticResult", description = "Result of statistic request")
data class StatisticResponseDto constructor(

        @field:Schema(
                description = "Statistic Result",
        )
        @param:JacksonXmlProperty(namespace = "result")
        val result: Double?,

        )
