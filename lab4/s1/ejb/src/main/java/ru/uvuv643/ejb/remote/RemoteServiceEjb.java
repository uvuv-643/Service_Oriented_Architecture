package ru.uvuv643.ejb.remote;

import jakarta.ejb.Remote;
import ru.uvuv643.ejb.remote.dto.enums.SortOrderDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticFieldDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticOperationDto;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;
import ru.uvuv643.ejb.remote.dto.human.response.StatisticResponseDto;

import java.util.List;

@Remote
public interface RemoteServiceEjb {

    AllResponseDto getAllHumanBeing(
            Integer idGte,
            Integer idLte,
            List<String> nameIn,
            Integer coordinateXGte,
            Integer coordinateXLte,
            Long coordinateYGte,
            Long coordinateYLte,
            String creationDateGte,
            String creationDateLte,
            Boolean realHero,
            Boolean hasToothpick,
            Double impactSpeedGte,
            Double impactSpeedLte,
            Double minutesOfWaitingGte,
            Double minutesOfWaitingLte,
            Boolean coolCar,
            List<String> moodIn,
            List<String> weaponTypeIn,
            Integer page,
            Integer size,
            List<String> sortFields,
            List<SortOrderDto> sortDirections
    );

    void createHumanBeing(CreateHumanBeingRequest request);

    HumanBeingDto getHumanBeingById(int id);

    void modifyHumanBeing(String id, ModifyHumanBeingRequest request);

    StatisticResponseDto getHumanStats(StatisticFieldDto field, StatisticOperationDto operation);

    void deleteByParams(Integer id, Boolean carCool, Double impactSpeed, Integer limit);

}
