package ru.uvuv643.soa.api.v1.dto.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Statistic field DTO")
enum class StatisticFieldDto {
    IMPACT_SPEED, MINUTES_OF_WAITING
}