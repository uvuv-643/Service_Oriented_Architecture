package ru.uvuv643.ejb.remote;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.meta.derby.sys.Sys;
import ru.uvuv643.ejb.remote.database.tables.Car;
import ru.uvuv643.ejb.remote.database.tables.Coordinates;
import ru.uvuv643.ejb.remote.database.tables.Humanbeing;
import ru.uvuv643.ejb.remote.dto.enums.SortOrderDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticFieldDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticOperationDto;
import ru.uvuv643.ejb.remote.dto.human.CarDto;
import ru.uvuv643.ejb.remote.dto.human.CoordinatesDto;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;
import ru.uvuv643.ejb.remote.dto.human.response.StatisticResponseDto;

import java.lang.Record;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class ServiceEjbImpl implements RemoteServiceEjb {

    private DSLContext context;

    public ServiceEjbImpl() {
        try {
            String userName = "your_user";
            String password = "your_password";
            String url = "jdbc:postgresql://postgres:5432/your_database";
            Connection conn = DriverManager.getConnection(url, userName, password);
            this.context = DSL.using(conn, SQLDialect.POSTGRES);

        } catch (SQLException e) {}
    }

    @Override
    public AllResponseDto getAllHumanBeing(Integer idGte, Integer idLte, List<String> nameIn, Integer coordinateXGte, Integer coordinateXLte, Double coordinateYGte, Double coordinateYLte, String creationDateGte, String creationDateLte, Boolean realHero, Boolean hasToothpick, Float impactSpeedGte, Float impactSpeedLte, Long minutesOfWaitingGte, Long minutesOfWaitingLte, Boolean coolCar, List<String> moodIn, List<String> weaponTypeIn, Integer page, Integer size, List<String> sortFields, List<SortOrderDto> sortDirections) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            if (creationDateGte != null) sdf.parse(creationDateGte);
            if (creationDateLte != null) sdf.parse(creationDateLte);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        try (Connection connection = DatabaseConfig.getConnection()) {
            List<Object> params = new ArrayList<>();
            List<String> whereClauses = new ArrayList<>();
            List<String> orderClauses = new ArrayList<>();

            StringBuilder baseSQL = new StringBuilder(
                    "SELECT hb.id, hb.name, hb.realHero, hb.hasToothpick, hb.impactSpeed, hb.minutesOfWaiting, " +
                            "hb.weaponType, hb.mood, hb.creationDate, " +
                            "c.x AS coord_x, c.y AS coord_y, " +
                            "car.cool AS car_cool " +
                            "FROM HumanBeing hb " +
                            "JOIN Coordinates c ON hb.coordinates_id = c.id " +
                            "JOIN Car car ON hb.car_id = car.id "
            );

            if (idGte != null) {
                whereClauses.add("hb.id >= ?");
                params.add(idGte);
            }
            if (idLte != null) {
                whereClauses.add("hb.id <= ?");
                params.add(idLte);
            }
            if (nameIn != null && !nameIn.isEmpty()) {
                whereClauses.add("hb.name IN (" + String.join(",", Collections.nCopies(nameIn.size(), "?")) + ")");
                params.addAll(nameIn);
            }
            if (coordinateXGte != null) {
                whereClauses.add("c.x >= ?");
                params.add(coordinateXGte);
            }
            if (coordinateXLte != null) {
                whereClauses.add("c.x <= ?");
                params.add(coordinateXLte);
            }
            if (coordinateYGte != null) {
                whereClauses.add("c.y >= ?");
                params.add(coordinateYGte);
            }
            if (coordinateYLte != null) {
                whereClauses.add("c.y <= ?");
                params.add(coordinateYLte);
            }
            if (creationDateGte != null) {
                whereClauses.add("hb.creationDate >= CAST(? AS DATE)");
                params.add(creationDateGte);
            }
            if (creationDateLte != null) {
                whereClauses.add("hb.creationDate <= CAST(? AS DATE)");
                params.add(creationDateLte);
            }
            if (realHero != null) {
                whereClauses.add("hb.realHero = ?");
                params.add(realHero);
            }
            if (hasToothpick != null) {
                whereClauses.add("hb.hasToothpick = ?");
                params.add(hasToothpick);
            }
            if (impactSpeedGte != null) {
                whereClauses.add("hb.impactSpeed >= ?");
                params.add(impactSpeedGte);
            }
            if (impactSpeedLte != null) {
                whereClauses.add("hb.impactSpeed <= ?");
                params.add(impactSpeedLte);
            }
            if (minutesOfWaitingGte != null) {
                whereClauses.add("hb.minutesOfWaiting >= ?");
                params.add(minutesOfWaitingGte);
            }
            if (minutesOfWaitingLte != null) {
                whereClauses.add("hb.minutesOfWaiting <= ?");
                params.add(minutesOfWaitingLte);
            }
            if (coolCar != null) {
                whereClauses.add("car.cool = ?");
                params.add(coolCar);
            }
            if (moodIn != null && !moodIn.isEmpty()) {
                String placeholders = String.join(",", Collections.nCopies(moodIn.size(), "?::mood"));
                whereClauses.add("hb.mood IN (" + placeholders + ")");
                params.addAll(moodIn);
            }
            if (weaponTypeIn != null && !weaponTypeIn.isEmpty()) {
                String placeholders = String.join(",", Collections.nCopies(weaponTypeIn.size(), "?::weapon_type"));
                whereClauses.add("hb.weaponType IN (" + placeholders + ")");
                params.addAll(weaponTypeIn);
            }

            if (sortFields != null) {
                List<SortOrderDto> directions = sortDirections != null ? sortDirections :
                        Collections.nCopies(sortFields.size(), SortOrderDto.ASC);
                for (int i = 0; i < sortFields.size(); i++) {
                    String field = sortFields.get(i);
                    SortOrderDto direction = directions.size() > i ? directions.get(i) : SortOrderDto.ASC;
                    String sanitizedField = switch (field) {
                        case "id", "name", "realHero", "hasToothpick", "impactSpeed", "minutesOfWaiting", "weaponType", "mood", "creationDate" -> "hb." + field;
                        case "coordinateX" -> "c.x";
                        case "coordinateY" -> "c.y";
                        case "carCool" -> "car.cool";
                        default -> throw new IllegalArgumentException("Invalid sort field: " + field);
                    };
                    orderClauses.add(sanitizedField + " " + direction.name());
                }
            }

            if (!whereClauses.isEmpty()) {
                baseSQL.append(" WHERE ").append(String.join(" AND ", whereClauses));
            }
            if (!orderClauses.isEmpty()) {
                baseSQL.append(" ORDER BY ").append(String.join(", ", orderClauses));
            }
            if (page != null && size != null) {
                baseSQL.append(" LIMIT ? OFFSET ?");
                params.add(size);
                params.add((page - 1) * size);
            }

            System.out.println(baseSQL);

            try (PreparedStatement stmt = connection.prepareStatement(baseSQL.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                ResultSet rs = stmt.executeQuery();
                List<HumanBeingDto> humanBeings = new ArrayList<>();
                while (rs.next()) {
                    HumanBeingDto humanBeing = new HumanBeingDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            new CoordinatesDto(
                                    rs.getInt("coord_x"),
                                    rs.getLong("coord_y")
                            ),
                            rs.getDate("creationDate"),
                            rs.getBoolean("realHero"),
                            rs.getBoolean("hasToothpick"),
                            rs.getFloat("impactSpeed"),
                            rs.getDouble("minutesOfWaiting"),
                            rs.getString("weaponType"),
                            rs.getString("mood"),
                            new CarDto(
                                    rs.getBoolean("car_cool")
                            )
                    );
                    humanBeings.add(humanBeing);
                }
                return new AllResponseDto(humanBeings);
            } catch (Exception ex) {
                throw new EJBException(ex);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }

    }

    @Override
    public void createHumanBeing(CreateHumanBeingRequest request) {

        System.out.println("createHumanBeing1" + request.toString());
        HumanBeingDto humanBeing = new HumanBeingDto(
                1,
                request.getName().toString(),
                request.getCoordinates(),
                new Date(System.currentTimeMillis()),
                Boolean.TRUE.equals(request.getRealHero()),
                Boolean.TRUE.equals(request.getHasToothpick()),
                request.getImpactSpeed() != null ? request.getImpactSpeed().floatValue() : null,
                request.getMinutesOfWaiting() != null ? request.getMinutesOfWaiting().doubleValue() : null,
                request.getWeaponType().toString(),
                request.getMood() != null ? request.getMood().toString() : null,
                (CarDto) request.getCar()
        );

        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("createHumanBeing2" + request.toString());
            insertHumanBeing(connection, humanBeing);
            System.out.println("createHumanBeing3" + request.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public HumanBeingDto getHumanBeingById(int id) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);

            try {
                // Use fetchOne() with indexes to access the fields
                ResultSet rs = create.select(
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
                        .fetchResultSet(); // Use fetchResultSet to handle manually.

                if (rs.next()) {
                    HumanBeingDto humanBeing = new HumanBeingDto(
                            rs.getInt(1),
                            rs.getString(2),
                            new CoordinatesDto(
                                    rs.getInt(10),
                                    rs.getLong(11)
                            ),
                            rs.getDate(7),
                            rs.getBoolean(3),
                            rs.getBoolean(4),
                            rs.getFloat(5),
                            rs.getDouble(6),
                            rs.getString(8),
                            rs.getString(9),
                            new CarDto(
                                    rs.getBoolean(12)
                            )
                    );
                    return humanBeing;
                } else {
                    return null;
                }
            } catch (SQLException ex) {
            }
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public void modifyHumanBeing(String id, ModifyHumanBeingRequest request) throws Exception {
        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try {
                StringBuilder updateSQL = new StringBuilder("UPDATE HumanBeing SET ");
                List<Object> params = new ArrayList<>();
                List<String> columns = new ArrayList<>();

                if (request.getName() != null) {
                    columns.add("name = ?");
                    params.add(request.getName());
                }

                if (request.getRealHero() != null) {
                    columns.add("realHero = ?");
                    params.add(request.getRealHero());
                }

                if (request.getHasToothpick() != null) {
                    columns.add("hasToothpick = ?");
                    params.add(request.getHasToothpick());
                }

                if (request.getImpactSpeed() != null) {
                    columns.add("impactSpeed = ?");
                    params.add(request.getImpactSpeed());
                }

                if (request.getMinutesOfWaiting() != null) {
                    columns.add("minutesOfWaiting = ?");
                    params.add(request.getMinutesOfWaiting());
                }

                if (request.getWeaponType() != null) {
                    columns.add("weaponType = ?::weapon_type");
                    params.add(request.getWeaponType().name());
                }

                if (request.getMood() != null) {
                    columns.add("mood = ?::mood");
                    params.add(request.getMood().name());
                }

                if (request.getCoordinates() != null) {
                    int coordinatesId = getCoordinatesIdByHumanId(connection, Integer.parseInt(id));
                    if (request.getCoordinates().getX() != null) {
                        updateCoordinateField(connection, coordinatesId, "x", request.getCoordinates().getX());
                    }
                    if (request.getCoordinates().getY() != null) {
                        updateCoordinateField(connection, coordinatesId, "y", request.getCoordinates().getY());
                    }
                }

                if (request.getCar() != null) {
                    int carId = getCarIdByHumanId(connection, Integer.parseInt(id));
                    if (request.getCar().getCool() != null) {
                        updateCarField(connection, carId, "cool", request.getCar().getCool());
                    }
                }

                if (!columns.isEmpty()) {
                    updateSQL.append(String.join(", ", columns));
                    updateSQL.append(" WHERE id = ?");
                    params.add(Integer.parseInt(id));

                    try (PreparedStatement stmt = connection.prepareStatement(updateSQL.toString())) {
                        for (int i = 0; i < params.size(); i++) {
                            stmt.setObject(i + 1, params.get(i));
                        }
                        stmt.executeUpdate();
                    }
                }

                connection.commit();

            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public StatisticResponseDto getHumanStats(StatisticFieldDto field, StatisticOperationDto operation) {
        if (field == null) {
            throw new BadRequestException("field parameter is required (IMPACT_SPEED / MINUTES_OF_WAITING)");
        }

        if (operation == null) {
            throw new BadRequestException("operation parameter is required (MEAN / MAX / MIN)");
        }

        Field<? extends Number> selectedField = switch (field) {
            case IMPACT_SPEED -> Humanbeing.HUMANBEING.IMPACTSPEED;
            case MINUTES_OF_WAITING -> Humanbeing.HUMANBEING.MINUTESOFWAITING;
            case AVERAGE_SPEED -> Humanbeing.HUMANBEING.IMPACTSPEED;
            default -> throw new IllegalArgumentException("Invalid field option");
        };

        try {
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

            return new StatisticResponseDto(result.doubleValue());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @Override
    public void deleteByParams(Boolean carCool, Float impactSpeed, Integer limit) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Object> params = new ArrayList<>();
                StringBuilder deleteSQL = new StringBuilder();
                deleteSQL.append("DELETE FROM HumanBeing WHERE id IN (");
                deleteSQL.append("SELECT HumanBeing.id FROM HumanBeing");

                if (carCool != null) {
                    deleteSQL.append(" JOIN Car ON HumanBeing.car_id = Car.id");
                }

                List<String> whereClauses = new ArrayList<>();

                if (carCool != null) {
                    whereClauses.add("Car.cool = ?");
                    params.add(carCool);
                }

                if (impactSpeed != null) {
                    whereClauses.add("HumanBeing.impactSpeed = ?");
                    params.add(impactSpeed);
                }

                if (!whereClauses.isEmpty()) {
                    deleteSQL.append(" WHERE ");
                    deleteSQL.append(String.join(" AND ", whereClauses));
                }

                if (limit != null) {
                    deleteSQL.append(" LIMIT ?");
                    params.add(limit);
                }

                deleteSQL.append(")");

                System.out.println(deleteSQL);

                try (PreparedStatement stmt = connection.prepareStatement(deleteSQL.toString())) {
                    for (int i = 0; i < params.size(); i++) {
                        stmt.setObject(i + 1, params.get(i));
                    }
                    int rowsAffected = stmt.executeUpdate();
                    System.out.println("Deleted Rows: " + rowsAffected);
                }
                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
                ex.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    void insertHumanBeing(Connection connection, HumanBeingDto human) {
        try {
            connection.setAutoCommit(false);

            Integer coordinatesId = (human.getCoordinates() != null) ? insertCoordinates(connection, human.getCoordinates()) : null;
            int carId = insertCar(connection, human.getCar());

            String insertHumanSQL = """
            INSERT INTO HumanBeing(
                name, coordinates_id, creationDate, realHero, hasToothpick, impactSpeed, minutesOfWaiting, weaponType, mood, car_id
            ) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?)
        """;

            try (PreparedStatement stmt = connection.prepareStatement(insertHumanSQL)) {
                stmt.setString(1, human.getName());
                if (coordinatesId != null) {
                    stmt.setInt(2, coordinatesId);
                } else {
                    stmt.setNull(2, Types.INTEGER);
                }
                stmt.setBoolean(3, human.getRealHero());
                stmt.setBoolean(4, human.getHasToothpick());

                if (human.getImpactSpeed() != null) {
                    stmt.setDouble(5, human.getImpactSpeed().doubleValue());
                } else {
                    stmt.setNull(5, Types.DOUBLE);
                }

                if (human.getMinutesOfWaiting() != null) {
                    stmt.setDouble(6, human.getMinutesOfWaiting());
                } else {
                    stmt.setNull(6, Types.DOUBLE);
                }

                if (human.getWeaponType() != null) {
                    stmt.setObject(7, human.getWeaponType().toString(), Types.OTHER);
                } else {
                    stmt.setNull(7, Types.OTHER);
                }

                stmt.setObject(8, (human.getMood() != null ? human.getMood().toString() : null), Types.OTHER);
                stmt.setInt(9, carId);

                stmt.executeUpdate();
            }

            connection.commit();
            System.out.println("HumanBeing record successfully inserted.");
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    int insertCoordinates(Connection conn, CoordinatesDto coordinates) throws SQLException {
        String insertCoordinatesSQL = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id";
        PreparedStatement stmt = conn.prepareStatement(insertCoordinatesSQL);
            stmt.setInt(1, coordinates.getX());
            stmt.setLong(2, coordinates.getY());

        ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Failed to retrieve coordinates ID.");
                }

    }


    int insertCar(Connection conn, CarDto car) throws SQLException {
        String insertCarSQL = "INSERT INTO Car (cool) VALUES (?) RETURNING id";
        PreparedStatement stmt = conn.prepareStatement(insertCarSQL);
        if (car.getCool() != null) {
            stmt.setBoolean(1, car.getCool());
        } else {
            stmt.setNull(1, Types.BOOLEAN);
        }

        ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Failed to retrieve car ID.");
            }


    }

    private void updateCoordinateField(Connection conn, int coordinatesId, String field, Object value) throws SQLException {
        String sql = "UPDATE Coordinates SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            stmt.setInt(2, coordinatesId);
            stmt.executeUpdate();
        }
    }

    private void updateCarField(Connection conn, int carId, String field, Object value) throws SQLException {
        String sql = "UPDATE Car SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            stmt.setInt(2, carId);
            stmt.executeUpdate();
        }
    }

    private int getCoordinatesIdByHumanId(Connection conn, int humanId) throws Exception {
        String sql = "SELECT coordinates_id FROM HumanBeing WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, humanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("coordinates_id");
                } else {
                    throw new Exception("Unable to find coordinates_id for HumanBeing with id = " + humanId);
                }
            }
        }
    }

    private int getCarIdByHumanId(Connection conn, int humanId) throws Exception {
        String sql = "SELECT car_id FROM HumanBeing WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, humanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("car_id");
                } else {
                    throw new Exception("Unable to find car_id for HumanBeing with id = " + humanId);
                }
            }
        }
    }


}
