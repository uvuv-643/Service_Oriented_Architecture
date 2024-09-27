package ru.uvuv643.soa.api.v1.dto.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Weapon type enum")
enum class WeaponTypeDto {
    HAMMER, AXE, PISTOL, KNIFE, BAT
}