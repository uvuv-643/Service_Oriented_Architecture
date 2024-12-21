package ru.uvuv643.soa.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.response.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.response.TeamResponseDto
import java.util.*


@RequestMapping("/v1/team/")
@Validated
interface TeamWebService {

    @GetMapping("/",  produces = ["application/xml"])
    @Operation(
        summary = "Get heroes in the team",
        tags = ["team"],
        description = "Get heroes in the team in pairs",
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
    fun getHeroes(): ListHumanBeingDto

    @PostMapping("/{team-id}/add/{hero-id}", produces = ["application/xml"])
    @Operation(
        summary = "Add hero to the team",
        tags = ["team"],
        description = "Create a relation between some human-being and some team",
        responses = [
            ApiResponse(
                    responseCode = "200",
                    description = "Human Being were added to the team. List of the team provided.",
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
            ),
        ]
    )
    fun addHeroToTheTeam(
            @PathVariable("hero-id") heroId: Int,
            @PathVariable("team-id") teamId: Int
    ) : TeamResponseDto

    @PostMapping("/{team-id}/make-depressive", produces = ["application/xml"])
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
                ),
            ]
    )
    fun makeTeamDepressive(
            @PathVariable("team-id") teamId: Int
    ) : TeamResponseDto

}
