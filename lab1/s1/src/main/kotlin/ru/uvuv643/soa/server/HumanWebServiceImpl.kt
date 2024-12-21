package ru.uvuv643.soa.server

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ResponseBody
import ru.uvuv643.soa.api.v1.HumanWebService
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.request.CreateHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.DeleteHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.ModifyHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.response.DeleteResponseDto
import ru.uvuv643.soa.api.v1.dto.human.response.StatisticResponseDto
import java.util.*

@Controller
class HumanWebServiceImpl : HumanWebService {
    override fun getAllHumanBeing(
        idGte: Optional<Int>,
        idLte: Optional<Int>,
        nameIn: Optional<List<String>>,
        coordinateXGte: Optional<Int>,
        coordinateXLte: Optional<Int>,
        coordinateYGte: Optional<Double>,
        coordinateYLte: Optional<Double>,
        creationDateGte: Optional<Date>,
        creationDateLte: Optional<Date>,
        realHero: Optional<Boolean>,
        hasToothpick: Optional<Boolean>,
        impactSpeedGte: Optional<Float>,
        impactSpeedLte: Optional<Float>,
        minutesOfWaitingGte: Optional<Long>,
        minutesOfWaitingLte: Optional<Long>,
        coolCar: Optional<Boolean>,
        moodIn: Optional<List<MoodDto>>,
        weaponTypeIn: Optional<List<WeaponTypeDto>>,
        page: Optional<Int>,
        size: Optional<Int>,
        sortDirections: List<String>,
        sortFields: List<String>
    ): ListHumanBeingDto {
        TODO("Not yet implemented")
    }

    override fun createHumanBeing(request: CreateHumanBeingRequest): HumanBeingDto {
        TODO("Not yet implemented")
    }

    override fun getHumanBeingById(id: Int): HumanBeingDto {
        TODO("Not yet implemented")
    }

    override fun modifyHumanBeing(id: String, request: ModifyHumanBeingRequest): HumanBeingDto {
        TODO("Not yet implemented")
    }


    override fun getHumanStats(field: Optional<StatisticFieldDto>, operation: Optional<StatisticOperationDto>): StatisticResponseDto {
        TODO("Not yet implemented")
    }

    override fun deleteByParams(carCool: Boolean?, impactSpeed: Float?, limit: Int?): ResponseBody {
        TODO("Not yet implemented")
    }


}
