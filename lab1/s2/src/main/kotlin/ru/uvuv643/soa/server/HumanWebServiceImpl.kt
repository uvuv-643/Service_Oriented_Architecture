package ru.uvuv643.soa.server

import org.springframework.stereotype.Controller
import ru.uvuv643.soa.api.v1.TeamWebService
import ru.uvuv643.soa.api.v1.dto.human.response.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.response.TeamResponseDto

@Controller
class HumanWebServiceImpl : TeamWebService {
    override fun getHeroes(): ListHumanBeingDto {
        TODO("Not yet implemented")
    }

    override fun addHeroToTheTeam(heroId: Int, teamId: Int): TeamResponseDto {
        TODO("Not yet implemented")
    }

    override fun makeTeamDepressive(teamId: Int): TeamResponseDto {
        TODO("Not yet implemented")
    }


}
