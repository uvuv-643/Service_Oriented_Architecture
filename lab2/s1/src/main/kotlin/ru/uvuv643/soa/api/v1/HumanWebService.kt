package ru.uvuv643.soa.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.request.CreateHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.DeleteHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.ModifyHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.response.DeleteResponseDto
import ru.uvuv643.soa.api.v1.dto.human.response.StatisticResponseDto
import java.time.Instant
import java.time.OffsetDateTime
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
        @QueryParam("idGte") idGte: Int?,
        @QueryParam("idLte") idLte: Int?,
        @QueryParam("nameIn") nameIn: List<String>?,
        @QueryParam("coordinateXGte") coordinateXGte: Int?,
        @QueryParam("coordinateXLte") coordinateXLte: Int?,
        @QueryParam("coordinateYGte") coordinateYGte: Double?,
        @QueryParam("coordinateYLte") coordinateYLte: Double?,
        @QueryParam("creationDateGte") creationDateGte: String?,
        @QueryParam("creationDateLte") creationDateLte: String?,
        @QueryParam("realHero") realHero: Boolean?,
        @QueryParam("hasToothpick") hasToothpick: Boolean?,
        @QueryParam("impactSpeedGte") impactSpeedGte: Float?,
        @QueryParam("impactSpeedLte") impactSpeedLte: Float?,
        @QueryParam("minutesOfWaitingGte") minutesOfWaitingGte: Long?,
        @QueryParam("minutesOfWaitingLte") minutesOfWaitingLte: Long?,
        @QueryParam("coolCar") coolCar: Boolean?,
        @QueryParam("moodIn") moodIn: List<String>?,
        @QueryParam("weaponTypeIn") weaponTypeIn: List<String>?,
        @QueryParam("page") page: Int?,
        @QueryParam("size") size: Int?,
        @QueryParam("sortFields") sortFields: List<String>?,
        @QueryParam("sortDirections") sortDirections: List<SortOrderDto>?
    ): Response?

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
    fun createHumanBeing(@Valid request: CreateHumanBeingRequest): Response?

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
    fun getHumanBeingById(@PathParam("id") id: Int): Response?

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
    fun modifyHumanBeing(@PathParam("id") id: String, @Valid request: ModifyHumanBeingRequest): Response?

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
    ): Response?

    @DELETE
    @Path("/human-being")
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
        @QueryParam("carCool") carCool: Boolean?,
        @QueryParam("impactSpeed") impactSpeed: Float?,
        @QueryParam("limit") @PositiveOrZero @Valid limit: Int?
    ): Response?
}
