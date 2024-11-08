package ru.uvuv643.soa.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Produces(MediaType.APPLICATION_XML)
interface TeamWebService {

    @GET
    @Path("/")
    @Operation(
        summary = "Get heroes in the team",
        tags = ["team"],
        description = "Get heroes in the team",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Human Being were added to the team. List of the team provided.",
                content = [Content(schema = Schema(implementation = ListHumanBeingDto::class))]
            ),
            ApiResponse(
                responseCode = "415",
                description = "Content-Type header must be specified as XML in request",
                content = [Content()]
            )
        ]
    )
    fun getHeroes(): Response?

    @POST
    @Path("/{team-id}/add/{hero-id}")
    @Operation(
        summary = "Add hero to the team",
        tags = ["team"],
        description = "Create a relation between some human-being and some team",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Human Being were added to the team. List of the team provided.",
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
    fun addHeroToTheTeam(
        @PathParam("hero-id") heroId: Int,
        @PathParam("team-id") teamId: Int
    ): Response?

    @POST
    @Path("/{team-id}/make-depressive")
    @Operation(
        summary = "Make all heroes in the team depressive",
        tags = ["team"],
        description = "Make all heroes in the team depressive",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "All Human Being were depressed to the team. List of the team provided.",
                content = [Content(schema = Schema(implementation = ListHumanBeingDto::class))]
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
    fun makeTeamDepressive(
        @PathParam("team-id") teamId: Int
    ): Response?
}