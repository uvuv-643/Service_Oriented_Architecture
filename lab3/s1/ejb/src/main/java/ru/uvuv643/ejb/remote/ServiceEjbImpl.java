package ru.uvuv643.ejb.remote;

import jakarta.ejb.Stateless;
import ru.uvuv643.ejb.remote.dto.enums.*;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;
import ru.uvuv643.ejb.remote.dto.human.response.StatisticResponseDto;
import ru.uvuv643.ejb.remote.repositories.HumanBeingRepository;

import java.sql.*;
import java.util.*;

@Stateless
public class ServiceEjbImpl implements RemoteServiceEjb {

    private final HumanBeingRepository humanBeingRepository = new HumanBeingRepository();

    @Override
    public AllResponseDto getAllHumanBeing(Integer idGte, Integer idLte, List<String> nameIn, Integer coordinateXGte, Integer coordinateXLte, Long coordinateYGte, Long coordinateYLte, String creationDateGte, String creationDateLte, Boolean realHero, Boolean hasToothpick, Double impactSpeedGte, Double impactSpeedLte, Double minutesOfWaitingGte, Double minutesOfWaitingLte, Boolean coolCar, List<String> moodIn, List<String> weaponTypeIn, Integer page, Integer size, List<String> sortFields, List<SortOrderDto> sortDirections) {
        try {
            return humanBeingRepository.getAllHumanBeing(idGte,
                    idLte,
                    nameIn,
                    coordinateXGte,
                    coordinateXLte,
                    coordinateYGte,
                    coordinateYLte,
                    creationDateGte,
                    creationDateLte,
                    realHero,
                    hasToothpick,
                    impactSpeedGte,
                    impactSpeedLte,
                    minutesOfWaitingGte,
                    minutesOfWaitingLte,
                    coolCar,
                    moodIn,
                    weaponTypeIn,
                    page,
                    size,
                    sortFields,
                    sortDirections
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void createHumanBeing(CreateHumanBeingRequest request) {
        try {
            humanBeingRepository.createHumanBeing(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HumanBeingDto getHumanBeingById(int id) {
        try {
            return humanBeingRepository.getHumanBeingById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void modifyHumanBeing(String id, ModifyHumanBeingRequest request) {
        try {
            humanBeingRepository.modifyHumanBeingById(Integer.parseInt(id), request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StatisticResponseDto getHumanStats(StatisticFieldDto field, StatisticOperationDto operation) {
        if (field == null || operation == null) {
            return null;
        }
        try {
            return new StatisticResponseDto(humanBeingRepository.getHumanStats(field, operation));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteByParams(Integer id, Boolean carCool, Double impactSpeed, Integer limit) {
        try {
            humanBeingRepository.deleteByParams(id, carCool, impactSpeed, limit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
