package ru.uvuv643.ejb.remote;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.uvuv643.ejb.remote.dto.enums.SortOrderDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticFieldDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticOperationDto;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;
import ru.uvuv643.ejb.remote.dto.human.response.StatisticResponseDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class ServiceEjbImpl implements RemoteServiceEjb {

//    private DSLContext context;
//
//    public ServiceEjbImpl() {
//        try {
//            String userName = "your_user";
//            String password = "your_password";
//            String url = "jdbc:postgresql://postgres:5432/your_database";
//            Connection conn = DriverManager.getConnection(url, userName, password);
//            this.context = DSL.using(conn, SQLDialect.POSTGRES);
//
//        } catch (SQLException e) {}
//    }

    @Override
    public AllResponseDto getAllHumanBeing(Integer idGte, Integer idLte, List<String> nameIn, Integer coordinateXGte, Integer coordinateXLte, Double coordinateYGte, Double coordinateYLte, String creationDateGte, String creationDateLte, Boolean realHero, Boolean hasToothpick, Float impactSpeedGte, Float impactSpeedLte, Long minutesOfWaitingGte, Long minutesOfWaitingLte, Boolean coolCar, List<String> moodIn, List<String> weaponTypeIn, Integer page, Integer size, List<String> sortFields, List<SortOrderDto> sortDirections) {
        var x = new AllResponseDto();
        x.setHumanBeingDto(new ArrayList<>());
        return x;
    }

    @Override
    public void createHumanBeing(CreateHumanBeingRequest request) {
        return;
    }

    @Override
    public HumanBeingDto getHumanBeingById(int id) {
        return new HumanBeingDto();
    }

    @Override
    public void modifyHumanBeing(String id, ModifyHumanBeingRequest request) {
        return;
    }

    public StatisticResponseDto getHumanStats(StatisticFieldDto field, StatisticOperationDto operation) {
        if (field == null) {
            throw new BadRequestException("field parameter is required (IMPACT_SPEED / MINUTES_OF_WAITING)");
        }

        if (operation == null) {
            throw new BadRequestException("operation parameter is required (MEAN / MAX / MIN)");
        }

        String fieldColumn;
        switch (field) {
            case IMPACT_SPEED:
                fieldColumn = "impactSpeed";
                break;
            case MINUTES_OF_WAITING:
                fieldColumn = "minutesOfWaiting";
                break;
            default:
                throw new IllegalArgumentException("Invalid field option");
        }

        String sqlFunction;
        switch (operation) {
            case AVERAGE:
                sqlFunction = "AVG";
                break;
            case MIN:
                sqlFunction = "MIN";
                break;
            case MAX:
                sqlFunction = "MAX";
                break;
            default:
                throw new IllegalArgumentException("Invalid operation option");
        }

        String sql = "SELECT " + sqlFunction + "(" + fieldColumn + ") as result FROM HumanBeing";

        try (Connection connection = ru.uvuv643.soa.server.DatabaseConfig.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        double result = rs.getDouble("result");
                        return new StatisticResponseDto(result);
                    } else {
                        new StatisticResponseDto(null);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException(ex.getMessage());
        }
        return null;
    }


    @Override
    public void deleteByParams(Boolean carCool, Float impactSpeed, Integer limit) {
        return;
    }
}
