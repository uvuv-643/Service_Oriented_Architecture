package ru.uvuv643.soa.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.request.CreateHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.DeleteHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.response.DeleteResponseDto
import ru.uvuv643.soa.api.v1.dto.human.response.StatisticResponseDto
import java.util.*

@Tag(name = "Person Resource", description = "API to manage Person resources")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
interface HumanWebService {

    @GET
    @Path("/human-being")
    @Produces(MediaType.APPLICATION_XML)
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
                )
            ]
    )
    fun getAllHumanBeing(
            @QueryParam("idGte") idGte: Optional<Int>?,
            @QueryParam("idLte") idLte: Optional<Int>?,
            @QueryParam("nameIn") nameIn: Optional<List<String>>?,
            @QueryParam("coordinateXGte") coordinateXGte: Optional<Int>?,
            @QueryParam("coordinateXLte") coordinateXLte: Optional<Int>?,
            @QueryParam("coordinateYGte") coordinateYGte: Optional<Double>?,
            @QueryParam("coordinateYLte") coordinateYLte: Optional<Double>?,
            @QueryParam("creationDateGte") creationDateGte: Optional<Date>?,
            @QueryParam("creationDateLte") creationDateLte: Optional<Date>?,
            @QueryParam("realHero") realHero: Optional<Boolean>?,
            @QueryParam("hasToothpick") hasToothpick: Optional<Boolean>?,
            @QueryParam("impactSpeedGte") impactSpeedGte: Optional<Float>?,
            @QueryParam("impactSpeedLte") impactSpeedLte: Optional<Float>?,
            @QueryParam("minutesOfWaitingGte") minutesOfWaitingGte: Optional<Long>?,
            @QueryParam("minutesOfWaitingLte") minutesOfWaitingLte: Optional<Long>?,
            @QueryParam("coolCar") coolCar: Optional<Boolean>?,
            @QueryParam("moodIn") moodIn: Optional<List<MoodDto>>?,
            @QueryParam("weaponTypeIn") weaponTypeIn: Optional<List<WeaponTypeDto>>?,
            @QueryParam("page") page: Optional<Int>?,
            @QueryParam("size") size: Optional<Int>?,
            @QueryParam("sortDirection") sortDirection: Optional<SortOrderDto>?,
            @QueryParam("sortField") sortField: Optional<String>?
    ): ListHumanBeingDto

    @POST
    @Path("/human-being")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
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
                )
            ]
    )
    fun createHumanBeing(@Valid request: CreateHumanBeingRequest): HumanBeingDto

    @GET
    @Path("/human-being/{id}")
    @Produces(MediaType.APPLICATION_XML)
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
                )
            ]
    )
    fun getHumanBeingById(@PathParam("id") id: Int): HumanBeingDto

    @PUT
    @Path("/human-being/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
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
                )
            ]
    )
    fun modifyHumanBeing(@PathParam("id") id: String, @Valid request: CreateHumanBeingRequest): HumanBeingDto

    @GET
    @Path("/human-being/stats")
    @Produces(MediaType.APPLICATION_XML)
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
                )
            ]
    )
    fun getHumanStats(
            @QueryParam("field") field: Optional<StatisticFieldDto>?,
            @QueryParam("operation") operation: Optional<StatisticOperationDto>?
    ): StatisticResponseDto

    @DELETE
    @Path("/human-being/")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
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
                )
            ]
    )
    fun deleteByParams(
            @QueryParam("limit") limit: Optional<Int>?,
            @Valid request: DeleteHumanBeingRequest
    ): DeleteResponseDto
}
