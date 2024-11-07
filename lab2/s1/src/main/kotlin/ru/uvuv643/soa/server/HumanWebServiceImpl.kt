package ru.uvuv643.soa.server

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
import java.util.*

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
    override fun createHumanBeing(request: CreateHumanBeingRequest): HumanBeingDto {
        TODO("Not yet implemented")
//        val connection: Connection = DatabaseConfig.getConnection()
//        val insertSql = """
//            INSERT INTO human_being (name, coordinates_id, creation_date, real_hero, has_toothpick,
//                                     impact_speed, minutes_of_waiting, weapon_type, mood, car_id)
//            VALUES (?, ?, NOW(), ?, ?, ?, ?, ?::weapon_type_enum, ?::mood_enum, ?)
//            RETURNING id
//        """.trimIndent()
//
//        try {
//            connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
//                statement.setString(1, request.name)
//                statement.setInt(2, saveCoordinates(request.coordinates, connection)) // Save coordinates separately
//                statement.setBoolean(3, request.realHero ?: false)
//                statement.setBoolean(4, request.hasToothpick ?: false)
//                statement.setObject(5, request.impactSpeed)
//                statement.setLong(6, request.minutesOfWaiting ?: 0)
//                statement.setString(7, request.weaponType?.name)
//                statement.setString(8, request.mood?.name)
//                statement.setObject(9, saveCar(request.car, connection)) // Save car separately
//
//                val affectedRows = statement.executeUpdate()
//                if (affectedRows == 0) throw SQLException("Creating Human Being failed, no rows affected.")
//
//                statement.generatedKeys.use { keys ->
//                    if (keys.next()) {
//                        val newId = keys.getInt(1)
//                        val humanBeing = HumanBeingDto(
//                            id = newId,
//                            name = request.name ?: "",
//                            coordinates = request.coordinates,
//                            creationDate = java.util.Date(),
//                            realHero = request.realHero ?: false,
//                            hasToothpick = request.hasToothpick ?: false,
//                            impactSpeed = request.impactSpeed ?: 0.0F,
//                            minutesOfWaiting = request.minutesOfWaiting ?: 0L,
//                            weaponType = request.weaponType,
//                            mood = request.mood,
//                            car = request.car
//                        )
//                        return humanBeing
//                    } else {
//                        throw SQLException("Creating Human Being failed, no ID obtained.")
//                    }
//                }
//            }
//        } catch (ex: SQLException) {
//            throw WebApplicationException("Database error: ${ex.message}", Response.Status.INTERNAL_SERVER_ERROR)
//        } finally {
//            connection.close()
//        }
    }

    private fun saveCoordinates(coordinates: CoordinatesDto, connection: Connection): Int {
        val coordinatesSql = """
            INSERT INTO coordinates (x, y)
            VALUES (?, ?)
            RETURNING id
        """.trimIndent()

        connection.prepareStatement(coordinatesSql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setObject(1, coordinates.x)
            statement.setFloat(2, coordinates.y ?: throw IllegalArgumentException("Y coordinate is required"))

            statement.executeUpdate()
            statement.generatedKeys.use { keys ->
                if (keys.next()) return keys.getInt(1)
                else throw SQLException("Creating Coordinates failed, no ID obtained.")
            }
        }
    }

    private fun saveCar(car: CarDto?, connection: Connection): Int? {
        if (car == null) return null

        val carSql = """
            INSERT INTO car (cool)
            VALUES (?)
            RETURNING id
        """.trimIndent()

        connection.prepareStatement(carSql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
            statement.setObject(1, car.cool)

            statement.executeUpdate()
            statement.generatedKeys.use { keys ->
                if (keys.next()) return keys.getInt(1)
                else throw SQLException("Creating Car failed, no ID obtained.")
            }
        }
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
