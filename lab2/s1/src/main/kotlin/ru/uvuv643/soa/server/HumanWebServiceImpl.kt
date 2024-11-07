package ru.uvuv643.soa.server

import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.uvuv643.soa.api.v1.HumanWebService
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.CarDto
import ru.uvuv643.soa.api.v1.dto.human.CoordinatesDto
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.ListHumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.request.CreateHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.DeleteHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.response.DeleteResponseDto
import ru.uvuv643.soa.api.v1.dto.human.response.StatisticResponseDto
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Types
import java.util.*



fun insertHumanBeing(connection: Connection, human: HumanBeingDto) {
    connection.use { conn ->
        conn.autoCommit = false
        try {
            val coordinatesId = human.coordinates?.let { insertCoordinates(conn, it) }

            val carId = insertCar(conn, human.car)

            val insertHumanSQL = """
                INSERT INTO HumanBeing(
                    name, coordinates_id, creationDate, realHero, hasToothpick, impactSpeed, minutesOfWaiting, weaponType, mood, car_id
                ) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

            conn.prepareStatement(insertHumanSQL).use { stmt ->
                stmt.setString(1, human.name)
                if (coordinatesId != null) {
                    stmt.setInt(2, coordinatesId)
                }
                stmt.setBoolean(3, human.realHero)
                stmt.setBoolean(4, human.hasToothpick)
                human.impactSpeed.let {
                    stmt.setDouble(5, it.toDouble())
                }
                human.minutesOfWaiting.let {
                    stmt.setDouble(6, it)
                }
                human.weaponType?.let {
                    stmt.setObject(7, it, Types.OTHER)
                } ?: stmt.setNull(7, Types.OTHER)
                stmt.setObject(8, human.mood, Types.OTHER)
                stmt.setInt(9, carId)
                stmt.executeUpdate()
            }

            conn.commit()
            println("Запись HumanBeing успешно вставлена.")
        } catch (ex: Exception) {
            conn.rollback()
            ex.printStackTrace()
        } finally {
            conn.autoCommit = true
        }
    }
}

fun insertCoordinates(conn: Connection, coordinates: CoordinatesDto): Int {
    val insertCoordinatesSQL = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id"
    conn.prepareStatement(insertCoordinatesSQL).use { stmt ->
        stmt.setInt(1, coordinates.x as Int)
        stmt.setLong(2, coordinates.y as Long)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            return rs.getInt("id")
        } else {
            throw Exception("Не удалось получить ID координат.")
        }
    }
}

fun insertCar(conn: Connection, car: CarDto): Int {
    val insertCarSQL = "INSERT INTO Car (cool) VALUES (?) RETURNING id"
    conn.prepareStatement(insertCarSQL).use { stmt ->
        car.cool?.let {
            stmt.setBoolean(1, it)
        } ?: stmt.setNull(1, Types.BOOLEAN)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            return rs.getInt("id")
        } else {
            throw Exception("Не удалось получить ID автомобиля.")
        }


    }
}

@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Path("/v1/")
open class HumanWebServiceImpl : HumanWebService {

    override fun getAllHumanBeing(idGte: Optional<Int>?, idLte: Optional<Int>?, nameIn: Optional<List<String>>?, coordinateXGte: Optional<Int>?, coordinateXLte: Optional<Int>?, coordinateYGte: Optional<Double>?, coordinateYLte: Optional<Double>?, creationDateGte: Optional<Date>?, creationDateLte: Optional<Date>?, realHero: Optional<Boolean>?, hasToothpick: Optional<Boolean>?, impactSpeedGte: Optional<Float>?, impactSpeedLte: Optional<Float>?, minutesOfWaitingGte: Optional<Long>?, minutesOfWaitingLte: Optional<Long>?, coolCar: Optional<Boolean>?, moodIn: Optional<List<MoodDto>>?, weaponTypeIn: Optional<List<WeaponTypeDto>>?, page: Optional<Int>?, size: Optional<Int>?, sortDirection: Optional<SortOrderDto>?, sortField: Optional<String>?): ListHumanBeingDto {
        TODO("Not yet implemented")
    }

    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @POST
    @Path("/human-being")
    override fun createHumanBeing(@Valid request: CreateHumanBeingRequest): Response? {

        if (request.impactSpeed == null && request.minutesOfWaiting == null && request.car == null) {
            return Response.serverError().entity(request).build();
        }

        val humanBeing = HumanBeingDto(
            id = 1,
            name = request.name.toString(),
            coordinates = request.coordinates,
            creationDate = Date(System.currentTimeMillis()),
            realHero = request.realHero == true,
            hasToothpick = request.hasToothpick == true,
            impactSpeed = request.impactSpeed as Float,
            minutesOfWaiting = request.minutesOfWaiting as Double,
            weaponType = request.weaponType,
            mood = request.mood.toString(),
            car = request.car as CarDto
        )

        DatabaseConfig.getConnection().use { connection ->
            insertHumanBeing(connection, humanBeing)
        }

        return Response.ok().entity(humanBeing).type(MediaType.APPLICATION_XML_TYPE).build();

    }


    override fun getHumanBeingById(id: Int): HumanBeingDto {
        TODO("Not yet implemented")
    }

    override fun modifyHumanBeing(id: String, request: CreateHumanBeingRequest): HumanBeingDto {
        TODO("Not yet implemented")
    }

    override fun getHumanStats(field: Optional<StatisticFieldDto>?, operation: Optional<StatisticOperationDto>?): StatisticResponseDto {
        TODO("Not yet implemented")
    }

    override fun deleteByParams(limit: Optional<Int>?, request: DeleteHumanBeingRequest): DeleteResponseDto {
        TODO("Not yet implemented")
    }

}
