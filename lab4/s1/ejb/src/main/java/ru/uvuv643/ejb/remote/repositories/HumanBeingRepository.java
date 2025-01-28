package ru.uvuv643.ejb.remote.repositories;

import org.jooq.*;
import org.jooq.impl.DSL;
import ru.uvuv643.ejb.remote.DatabaseConfig;
import ru.uvuv643.ejb.remote.database.tables.Car;
import ru.uvuv643.ejb.remote.database.tables.Coordinates;
import ru.uvuv643.ejb.remote.database.tables.Humanbeing;
import ru.uvuv643.ejb.remote.database.tables.records.HumanbeingRecord;
import ru.uvuv643.ejb.remote.dto.enums.*;
import ru.uvuv643.ejb.remote.dto.human.CarDto;
import ru.uvuv643.ejb.remote.dto.human.CoordinatesDto;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HumanBeingRepository {

    public HumanBeingDto getHumanBeingById(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);
            try (ResultSet rs = create.select(
                            Humanbeing.HUMANBEING.ID,
                            Humanbeing.HUMANBEING.NAME,
                            Humanbeing.HUMANBEING.REALHERO,
                            Humanbeing.HUMANBEING.HASTOOTHPICK,
                            Humanbeing.HUMANBEING.IMPACTSPEED,
                            Humanbeing.HUMANBEING.MINUTESOFWAITING,
                            Humanbeing.HUMANBEING.CREATIONDATE,
                            Humanbeing.HUMANBEING.WEAPONTYPE,
                            Humanbeing.HUMANBEING.MOOD,
                            Coordinates.COORDINATES.X,
                            Coordinates.COORDINATES.Y,
                            Car.CAR.COOL
                    )
                    .from(Humanbeing.HUMANBEING)
                    .join(Coordinates.COORDINATES).on(Humanbeing.HUMANBEING.COORDINATES_ID.eq(Coordinates.COORDINATES.ID))
                    .join(Car.CAR).on(Humanbeing.HUMANBEING.CAR_ID.eq(Car.CAR.ID))
                    .where(Humanbeing.HUMANBEING.ID.eq((long) id))
                    .fetchResultSet()) {
                if (rs.next()) {
                    return new HumanBeingDto(
                            rs.getInt(1),
                            rs.getString(2),
                            new CoordinatesDto(
                                    rs.getInt(10),
                                    rs.getLong(11)
                            ),
                            rs.getDate(7),
                            rs.getBoolean(3),
                            rs.getBoolean(4),
                            rs.getDouble(5),
                            rs.getDouble(6),
                            WeaponTypeDto.fromString(rs.getString(8)),
                            MoodDto.fromString(rs.getString(9)),
                            new CarDto(
                                    rs.getBoolean(12)
                            )
                    );
                }
            }
        }
        return null;
    }

    public Double getHumanStats(StatisticFieldDto field, StatisticOperationDto operation) throws IllegalArgumentException, SQLException {

        Field<? extends Number> selectedField = switch (field) {
            case IMPACT_SPEED -> Humanbeing.HUMANBEING.IMPACTSPEED;
            case MINUTES_OF_WAITING -> Humanbeing.HUMANBEING.MINUTESOFWAITING;
        };

        DSLContext create = DSL.using(DatabaseConfig.getConnection(), SQLDialect.POSTGRES);
        Number result;

        if (operation == StatisticOperationDto.AVERAGE) {
            result = create.select(DSL.avg(selectedField)).from(Humanbeing.HUMANBEING).fetchOneInto(BigDecimal.class);
        } else {
            AggregateFunction<? extends Number> function = switch (operation) {
                case MIN -> DSL.min(selectedField);
                case MAX -> DSL.max(selectedField);
                default -> throw new IllegalArgumentException("Invalid operation option");
            };
            result = create.select(function).from(Humanbeing.HUMANBEING).fetchOne(0, Double.class);
        }
        return result != null ? result.doubleValue() : 0.0;

    }

    public void modifyHumanBeingById(int id, ModifyHumanBeingRequest humanBeingDto) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);

            if (humanBeingDto.getCoordinates() != null) {
                create.update(Coordinates.COORDINATES)
                        .set(Coordinates.COORDINATES.X, humanBeingDto.getCoordinates().getX())
                        .set(Coordinates.COORDINATES.Y, humanBeingDto.getCoordinates().getY())
                        .where(Coordinates.COORDINATES.ID.eq(
                                create.select(Humanbeing.HUMANBEING.COORDINATES_ID)
                                        .from(Humanbeing.HUMANBEING)
                                        .where(Humanbeing.HUMANBEING.ID.eq((long) id))
                        ))
                        .execute();
            }

            if (humanBeingDto.getCar() != null) {
                create.update(Car.CAR)
                        .set(Car.CAR.COOL, humanBeingDto.getCar().getCool())
                        .where(Car.CAR.ID.eq(
                                create.select(Humanbeing.HUMANBEING.CAR_ID)
                                        .from(Humanbeing.HUMANBEING)
                                        .where(Humanbeing.HUMANBEING.ID.eq((long) id))
                        ))
                        .execute();
            }

            UpdateSetStep<HumanbeingRecord> updateStep = create.update(Humanbeing.HUMANBEING);

            if (humanBeingDto.getName() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.NAME, humanBeingDto.getName());
            }
            if (humanBeingDto.getRealHero() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.REALHERO, humanBeingDto.getRealHero());
            }
            if (humanBeingDto.getHasToothpick() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.HASTOOTHPICK, humanBeingDto.getHasToothpick());
            }
            if (humanBeingDto.getImpactSpeed() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.IMPACTSPEED, humanBeingDto.getImpactSpeed());
            }
            if (humanBeingDto.getMinutesOfWaiting() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.MINUTESOFWAITING, humanBeingDto.getMinutesOfWaiting());
            }
            if (humanBeingDto.getWeaponType() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.WEAPONTYPE, WeaponTypeDto.toDatabase(humanBeingDto.getWeaponType()));
            }
            if (humanBeingDto.getMood() != null) {
                updateStep = updateStep.set(Humanbeing.HUMANBEING.MOOD, MoodDto.toDatabase(humanBeingDto.getMood()));
            }

            ((UpdateSetMoreStep<HumanbeingRecord>) updateStep)
                    .where(Humanbeing.HUMANBEING.ID.eq((long) id))
                    .execute();

        }
    }

    public void createHumanBeing(CreateHumanBeingRequest humanBeingDto) throws SQLException {

        try (Connection connection = DatabaseConfig.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);

            int coordinatesId = Objects.requireNonNull(create.insertInto(Coordinates.COORDINATES)
                                .columns(Coordinates.COORDINATES.X, Coordinates.COORDINATES.Y)
                                .values(humanBeingDto.getCoordinates().getX(), humanBeingDto.getCoordinates().getY())
                                .returning(Coordinates.COORDINATES.ID)
                                .fetchOne())
                        .getValue(Coordinates.COORDINATES.ID);


            int carId = Objects.requireNonNull(create.insertInto(Car.CAR)
                            .columns(Car.CAR.COOL)
                            .values(humanBeingDto.getCar().getCool())
                            .returning(Car.CAR.ID)
                            .fetchOne())
                    .getValue(Car.CAR.ID);

            create.insertInto(Humanbeing.HUMANBEING)
                    .columns(
                            Humanbeing.HUMANBEING.NAME,
                            Humanbeing.HUMANBEING.REALHERO,
                            Humanbeing.HUMANBEING.HASTOOTHPICK,
                            Humanbeing.HUMANBEING.IMPACTSPEED,
                            Humanbeing.HUMANBEING.MINUTESOFWAITING,
                            Humanbeing.HUMANBEING.CREATIONDATE,
                            Humanbeing.HUMANBEING.WEAPONTYPE,
                            Humanbeing.HUMANBEING.MOOD,
                            Humanbeing.HUMANBEING.COORDINATES_ID,
                            Humanbeing.HUMANBEING.CAR_ID
                    )
                    .values(
                            humanBeingDto.getName(),
                            humanBeingDto.getRealHero(),
                            humanBeingDto.getHasToothpick(),
                            humanBeingDto.getImpactSpeed(),
                            humanBeingDto.getMinutesOfWaiting(),
                            LocalDate.now(),
                            WeaponTypeDto.toDatabase(humanBeingDto.getWeaponType()),
                            MoodDto.toDatabase(humanBeingDto.getMood()),
                            coordinatesId,
                            carId
                    )
                    .execute();

        }
    }

    public AllResponseDto getAllHumanBeing(Integer idGte, Integer idLte, List<String> nameIn, Integer coordinateXGte, Integer coordinateXLte, Long coordinateYGte, Long coordinateYLte, String creationDateGte, String creationDateLte, Boolean realHero, Boolean hasToothpick, Double impactSpeedGte, Double impactSpeedLte, Double minutesOfWaitingGte, Double minutesOfWaitingLte, Boolean coolCar, List<String> moodIn, List<String> weaponTypeIn, Integer page, Integer size, List<String> sortFields, List<SortOrderDto> sortDirections) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);

            SelectOnConditionStep<?> query = create.select(
                            Humanbeing.HUMANBEING.ID,
                            Humanbeing.HUMANBEING.NAME,
                            Humanbeing.HUMANBEING.REALHERO,
                            Humanbeing.HUMANBEING.HASTOOTHPICK,
                            Humanbeing.HUMANBEING.IMPACTSPEED,
                            Humanbeing.HUMANBEING.MINUTESOFWAITING,
                            Humanbeing.HUMANBEING.CREATIONDATE,
                            Humanbeing.HUMANBEING.WEAPONTYPE,
                            Humanbeing.HUMANBEING.MOOD,
                            Coordinates.COORDINATES.X,
                            Coordinates.COORDINATES.Y,
                            Car.CAR.COOL
                    )
                    .from(Humanbeing.HUMANBEING)
                    .join(Coordinates.COORDINATES).on(Humanbeing.HUMANBEING.COORDINATES_ID.eq(Coordinates.COORDINATES.ID))
                    .join(Car.CAR).on(Humanbeing.HUMANBEING.CAR_ID.eq(Car.CAR.ID));

            if (idGte != null) query = query.and(Humanbeing.HUMANBEING.ID.ge(Long.valueOf(idGte)));
            if (idLte != null) query = query.and(Humanbeing.HUMANBEING.ID.le(Long.valueOf(idLte)));
            if (nameIn != null && !nameIn.isEmpty()) query = query.and(Humanbeing.HUMANBEING.NAME.in(nameIn));
            if (coordinateXGte != null) query = query.and(Coordinates.COORDINATES.X.ge(coordinateXGte));
            if (coordinateXLte != null) query = query.and(Coordinates.COORDINATES.X.le(coordinateXLte));
            if (coordinateYGte != null) query = query.and(Coordinates.COORDINATES.Y.ge(coordinateYGte));
            if (coordinateYLte != null) query = query.and(Coordinates.COORDINATES.Y.le(coordinateYLte));
            if (creationDateGte != null) query = query.and(Humanbeing.HUMANBEING.CREATIONDATE.ge(LocalDate.parse(creationDateGte, DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            if (creationDateLte != null) query = query.and(Humanbeing.HUMANBEING.CREATIONDATE.le(LocalDate.parse(creationDateLte, DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            if (realHero != null) query = query.and(Humanbeing.HUMANBEING.REALHERO.eq(realHero));
            if (hasToothpick != null) query = query.and(Humanbeing.HUMANBEING.HASTOOTHPICK.eq(hasToothpick));
            if (impactSpeedGte != null) query = query.and(Humanbeing.HUMANBEING.IMPACTSPEED.ge(impactSpeedGte));
            if (impactSpeedLte != null) query = query.and(Humanbeing.HUMANBEING.IMPACTSPEED.le(impactSpeedLte));
            if (minutesOfWaitingGte != null) query = query.and(Humanbeing.HUMANBEING.MINUTESOFWAITING.ge(minutesOfWaitingGte));
            if (minutesOfWaitingLte != null) query = query.and(Humanbeing.HUMANBEING.MINUTESOFWAITING.le(minutesOfWaitingLte));
            if (coolCar != null) query = query.and(Car.CAR.COOL.eq(coolCar));
            if (moodIn != null && !moodIn.isEmpty()) query = query.and(Humanbeing.HUMANBEING.MOOD.in(moodIn));
            if (weaponTypeIn != null && !weaponTypeIn.isEmpty()) query = query.and(Humanbeing.HUMANBEING.WEAPONTYPE.in(weaponTypeIn));

            SelectLimitStep<?> sortQuery = query;
            if (sortFields != null && sortDirections != null && sortFields.size() == sortDirections.size()) {
                for (int i = 0; i < sortFields.size(); i++) {
                    String sortField = sortFields.get(i);
                    SortOrderDto sortDirection = sortDirections.get(i);
                    sortQuery = applySort((SelectOnConditionStep<?>) sortQuery, sortField, sortDirection);
                }
            }

            if (page != null && size != null) {
                sortQuery = (SelectLimitStep<?>) sortQuery.limit(size).offset((page - 1) * size);
            }

            try (ResultSet rs = sortQuery.fetchResultSet()) {
                List<HumanBeingDto> humanBeings = new ArrayList<>();
                while (rs.next()) {
                    humanBeings.add(new HumanBeingDto(
                            rs.getInt(1),
                            rs.getString(2),
                            new CoordinatesDto(rs.getInt(10), rs.getLong(11)),
                            rs.getDate(7),
                            rs.getBoolean(3),
                            rs.getBoolean(4),
                            rs.getDouble(5),
                            rs.getDouble(6),
                            WeaponTypeDto.fromString(rs.getString(8)),
                            MoodDto.fromString(rs.getString(9)),
                            new CarDto(rs.getBoolean(12))
                    ));
                }

                return new AllResponseDto(humanBeings);
            }
        }
    }
    private <T> SelectLimitStep<?> applySort(SelectOnConditionStep<?> query, Field<T> field, SortOrderDto sortDirection) {
        if (sortDirection == SortOrderDto.ASC) {
            return query.orderBy(field.asc());
        } else {
            return query.orderBy(field.desc());
        }
    }

    private SelectLimitStep<?> applySort(SelectOnConditionStep<?> query, String sortField, SortOrderDto sortDirection) {
        return switch (sortField) {
            case "id" -> applySort(query, Humanbeing.HUMANBEING.ID, sortDirection);
            case "name" -> applySort(query, Humanbeing.HUMANBEING.NAME, sortDirection);
            case "realHero" -> applySort(query, Humanbeing.HUMANBEING.REALHERO, sortDirection);
            case "hasToothpick" -> applySort(query, Humanbeing.HUMANBEING.HASTOOTHPICK, sortDirection);
            case "impactSpeed" -> applySort(query, Humanbeing.HUMANBEING.IMPACTSPEED, sortDirection);
            case "minutesOfWaiting" -> applySort(query, Humanbeing.HUMANBEING.MINUTESOFWAITING, sortDirection);
            case "creationDate" -> applySort(query, Humanbeing.HUMANBEING.CREATIONDATE, sortDirection);
            case "weaponType" -> applySort(query, Humanbeing.HUMANBEING.WEAPONTYPE, sortDirection);
            case "mood" -> applySort(query, Humanbeing.HUMANBEING.MOOD, sortDirection);
            case "coordinateX" -> applySort(query, Coordinates.COORDINATES.X, sortDirection);
            case "coordinateY" -> applySort(query, Coordinates.COORDINATES.Y, sortDirection);
            case "coolCar" -> applySort(query, Car.CAR.COOL, sortDirection);
            default -> query;
        };
    }

    public void deleteByParams(Integer id, Boolean carCool, Double impactSpeed, Integer limit) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);
            DeleteConditionStep<?> delete = create.deleteFrom(Humanbeing.HUMANBEING).where(DSL.trueCondition());
            if (carCool != null) {
                delete = delete.and(Humanbeing.HUMANBEING.CAR_ID.in(
                        create.select(Car.CAR.ID).from(Car.CAR).where(Car.CAR.COOL.eq(carCool))
                ));
            }
            if (impactSpeed != null) {
                delete = delete.and(Humanbeing.HUMANBEING.IMPACTSPEED.eq(impactSpeed));
            }
            if (id != null) {
                delete = delete.and(Humanbeing.HUMANBEING.ID.eq(Long.valueOf(id)));
            }
            if (limit != null) {
                delete = (DeleteConditionStep<?>) delete.limit(limit);
            }
            delete.execute();
        }
    }

}
