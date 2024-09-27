package ru.uvuv643.soa.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.request.CreateHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.DeleteHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.response.DeleteResponseDto
import ru.uvuv643.soa.api.v1.dto.human.response.StatisticResponseDto
import java.util.*


@RequestMapping("/v1/")
@Validated
interface HumanWebService {

    @GetMapping("/human-being", produces = ["application/xml"])
    @Operation(
            summary = "Get all human-being",
            tags = ["human-being"],
            description = "Get all human-being instances in collection",
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "Human Being results from datasource",
                        content = [Content(schema = Schema(implementation = ListHumanBeingDto::class))]
                ),
                ApiResponse(
                        responseCode = "400",
                        description = "Operations parameters are invalid",
                        content = [Content()]
                ),
            ]
    )
    fun getAllHumanBeing(
            @RequestParam(name = "idGte", required = false) idGte : Optional<Int>,
            @RequestParam(name = "idLte", required = false) idLte : Optional<Int>,
            @RequestParam(name = "nameIn", required = false) nameIn : Optional<List<String>>,
            @RequestParam(name = "coordinateXGte", required = false) coordinateXGte : Optional<Int>,
            @RequestParam(name = "coordinateXLte", required = false) coordinateXLte : Optional<Int>,
            @RequestParam(name = "coordinateYGte", required = false) coordinateYGte : Optional<Double>,
            @RequestParam(name = "coordinateYLte", required = false) coordinateYLte : Optional<Double>,
            @RequestParam(name = "creationDateGte", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") creationDateGte : Optional<Date>,
            @RequestParam(name = "creationDateLte", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") creationDateLte : Optional<Date>,
            @RequestParam(name = "realHero", required = false) realHero : Optional<Boolean>,
            @RequestParam(name = "hasToothpick", required = false) hasToothpick : Optional<Boolean>,
            @RequestParam(name = "impactSpeedGte", required = false) impactSpeedGte : Optional<Float>,
            @RequestParam(name = "impactSpeedLte", required = false) impactSpeedLte : Optional<Float>,
            @RequestParam(name = "minutesOfWaitingGte", required = false) minutesOfWaitingGte : Optional<Long>,
            @RequestParam(name = "minutesOfWaitingLte", required = false) minutesOfWaitingLte : Optional<Long>,
            @RequestParam(name = "coolCar", required = false) coolCar : Optional<Boolean>,
            @RequestParam(name = "moodIn", required = false) moodIn : Optional<List<MoodDto>>,
            @RequestParam(name = "weaponTypeIn", required = false) weaponTypeIn : Optional<List<WeaponTypeDto>>,
            @RequestParam(name = "page", required = false) page : Optional<Int>,
            @RequestParam(name = "size", required = false) size : Optional<Int>,
            @RequestParam(name = "sortDirection", required = false) sortDirection : Optional<SortOrderDto>,
            @RequestParam(name = "sortField", required = false) sortField : Optional<String>,
    ) : ListHumanBeingDto

    @PostMapping("/human-being", consumes = ["application/xml"], produces = ["application/xml"])
    @Operation(
        summary = "Create new Human Being",
        tags = ["human-being"],
        description = "Create new Human Being in collection",
        responses = [
            ApiResponse(
                    responseCode = "200",
                    description = "Human Being has been created",
                    content = [Content(schema = Schema(implementation = HumanBeingDto::class))]
            ),
            ApiResponse(
                    responseCode = "400",
                    description = "Operations parameters are invalid",
                    content = [Content()]
            ),
            ApiResponse(
                    responseCode = "415",
                    description = "Content-Type header must be specified as XML in request",
                    content = [Content()]
            ),
        ]
    )
    fun createHumanBeing(
            @Valid @RequestBody request: CreateHumanBeingRequest,
    ) : HumanBeingDto

    @GetMapping("/human-being/{id}", produces = ["application/xml"])
    @Operation(
            summary = "Get Human Being",
            tags = ["human-being"],
            description = "Get Human Being by id from collection",
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "Human Being instance retrieved",
                        content = [Content(schema = Schema(implementation = HumanBeingDto::class))]
                ),
                ApiResponse(
                        responseCode = "400",
                        description = "Operations parameters are invalid",
                        content = [Content()]
                ),
            ]
    )
    fun getHumanBeingById(
            @PathVariable id: Int,
    ) : HumanBeingDto

    @PutMapping("/human-being/{id}", consumes = ["application/xml"], produces = ["application/xml"])
    @Operation(
            summary = "Modify Human Being",
            tags = ["human-being"],
            description = "Modify existing Human Being instance in collection",
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "Human Being has been modified",
                        content = [Content(schema = Schema(implementation = HumanBeingDto::class))]
                ),
                ApiResponse(
                        responseCode = "400",
                        description = "Operations parameters are invalid",
                        content = [Content()]
                ),
                ApiResponse(
                        responseCode = "415",
                        description = "Content-Type header must be specified as XML in request",
                        content = [Content()]
                ),
            ]
    )
    fun modifyHumanBeing(
            @PathVariable id: String,
            @Valid @RequestBody request: CreateHumanBeingRequest,
    ) : HumanBeingDto

    @GetMapping("/human-being/stats", produces = ["application/xml"])
    @Operation(
            summary = "Get human-being statistic",
            tags = ["human-being"],
            description = "Get human-being statistic",
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "Human Being results from datasource",
                        content = [Content(schema = Schema(implementation = StatisticResponseDto::class))]
                ),
                ApiResponse(
                        responseCode = "400",
                        description = "Operations parameters are invalid",
                        content = [Content()]
                ),
            ]
    )
    fun getHumanStats(
            @RequestParam(name = "field", required = false) field : Optional<StatisticFieldDto>,
            @RequestParam(name = "operation", required = false) operation : Optional<StatisticOperationDto>,
    ) : StatisticResponseDto

    @DeleteMapping("/human-being/", consumes = ["application/xml"], produces = ["application/xml"])
    @Operation(
            summary = "Delete human being with given params and limit",
            tags = ["human-being"],
            description = "Delete human being by given car or impact speed, with limit",
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "Result of delete operation",
                        content = [Content(schema = Schema(implementation = DeleteResponseDto::class))]
                ),
                ApiResponse(
                        responseCode = "400",
                        description = "Operations parameters are invalid",
                        content = [Content()]
                ),
            ]
    )
    fun deleteByParams(
            @RequestParam(name = "limit", required = false) field : Optional<Int>,
            @Valid @RequestBody request: DeleteHumanBeingRequest,
    ) : DeleteResponseDto

}
