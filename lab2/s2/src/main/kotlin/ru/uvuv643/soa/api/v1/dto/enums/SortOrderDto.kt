package ru.uvuv643.soa.api.v1.dto.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Sort order")
enum class SortOrderDto {
    ASC, DESC
}