package ru.uvuv643.soa.server

import jakarta.ws.rs.Path
import ru.uvuv643.soa.api.v1.TeamWebService
import ru.uvuv643.soa.api.v1.dto.human.response.TeamResponseDto

@Path("/v1/team/")
open class TeamWebServiceImpl : TeamWebService {
    override fun addHeroToTheTeam(heroId: Int, teamId: Int): TeamResponseDto {
        TODO("Not yet implemented")
    }

    override fun makeTeamDepressive(teamId: Int): TeamResponseDto {
        TODO("Not yet implemented")
    }
}