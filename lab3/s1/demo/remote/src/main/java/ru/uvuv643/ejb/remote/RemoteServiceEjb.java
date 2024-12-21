package ru.uvuv643.ejb.remote;

import jakarta.ejb.Remote;
import jakarta.ws.rs.core.Response;
import ru.uvuv643.ejb.remote.dto.enums.SortOrderDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticFieldDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticOperationDto;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;
import ru.uvuv643.ejb.remote.dto.human.response.StatisticResponseDto;

import java.util.List;
import java.util.Optional;

@Remote
public interface RemoteServiceEjb {

    AllResponseDto getAllHumanBeing(
            Integer idGte,
            Integer idLte,
            List<String> nameIn,
            Integer coordinateXGte,
            Integer coordinateXLte,
            Double coordinateYGte,
            Double coordinateYLte,
            String creationDateGte,
            String creationDateLte,
            Boolean realHero,
            Boolean hasToothpick,
            Float impactSpeedGte,
            Float impactSpeedLte,
            Long minutesOfWaitingGte,
            Long minutesOfWaitingLte,
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

    void modifyHumanBeing(String id, ModifyHumanBeingRequest request) throws Exception;

    StatisticResponseDto getHumanStats(StatisticFieldDto field, StatisticOperationDto operation);

    void deleteByParams(Boolean carCool, Float impactSpeed, Integer limit);


}
