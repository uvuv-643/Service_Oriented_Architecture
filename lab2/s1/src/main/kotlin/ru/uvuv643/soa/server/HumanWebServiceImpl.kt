package ru.uvuv643.soa.server

import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.uvuv643.soa.api.v1.HumanWebService
import ru.uvuv643.soa.api.v1.dto.enums.*
import ru.uvuv643.soa.api.v1.dto.human.CarDto
import ru.uvuv643.soa.api.v1.dto.human.CoordinatesDto
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto
import ru.uvuv643.soa.api.v1.dto.human.request.CreateHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.request.ModifyHumanBeingRequest
import ru.uvuv643.soa.api.v1.dto.human.response.AllResponseDto
import ru.uvuv643.soa.api.v1.dto.human.response.StatisticResponseDto
import java.sql.Connection
import java.sql.Types
import java.text.ParseException
import java.text.SimpleDateFormat
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

    @GET
    @Path("/human-being")
    override fun getAllHumanBeing(
        @QueryParam("idGte") idGte: Int?,
        @QueryParam("idLte") idLte: Int?,
        @QueryParam("nameIn") nameIn: List<String>?,
        @QueryParam("coordinateXGte") coordinateXGte: Int?,
        @QueryParam("coordinateXLte") coordinateXLte: Int?,
        @QueryParam("coordinateYGte") coordinateYGte: Double?,
        @QueryParam("coordinateYLte") coordinateYLte: Double?,
        @QueryParam("creationDateGte") creationDateGte: String?,
        @QueryParam("creationDateLte") creationDateLte: String?,
        @QueryParam("realHero") realHero: Boolean?,
        @QueryParam("hasToothpick") hasToothpick: Boolean?,
        @QueryParam("impactSpeedGte") impactSpeedGte: Float?,
        @QueryParam("impactSpeedLte") impactSpeedLte: Float?,
        @QueryParam("minutesOfWaitingGte") minutesOfWaitingGte: Long?,
        @QueryParam("minutesOfWaitingLte") minutesOfWaitingLte: Long?,
        @QueryParam("coolCar") coolCar: Boolean?,
        @QueryParam("moodIn") moodIn: List<String>?,
        @QueryParam("weaponTypeIn") weaponTypeIn: List<String>?,
        @QueryParam("page") page: Int?,
        @QueryParam("size") size: Int?,
        @QueryParam("sortFields") sortFields: List<String>?,
        @QueryParam("sortDirections") sortDirections: List<SortOrderDto>?
    ): Response? {

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        try {
            if (creationDateGte != null) sdf.parse(creationDateGte)
            if (creationDateLte != null) sdf.parse(creationDateLte)
        } catch (e: ParseException) {
            e.printStackTrace()
            return Response.status(400).entity("yyyy-MM-dd - корректный формат для даты").type(MediaType.APPLICATION_XML_TYPE).build()
        }

        DatabaseConfig.getConnection().use { connection ->
            val params = mutableListOf<Any?>()
            val whereClauses = mutableListOf<String>()
            val orderClauses = mutableListOf<String>()

            val baseSQL = StringBuilder("""
            SELECT hb.id, hb.name, hb.realHero, hb.hasToothpick, hb.impactSpeed, hb.minutesOfWaiting,
                   hb.weaponType, hb.mood, hb.creationDate,
                   c.x AS coord_x, c.y AS coord_y,
                   car.cool AS car_cool
            FROM HumanBeing hb
            JOIN Coordinates c ON hb.coordinates_id = c.id
            JOIN Car car ON hb.car_id = car.id
        """.trimIndent())

            // Building WHERE clauses based on provided filters
            idGte?.let {
                whereClauses.add("hb.id >= ?")
                params.add(it)
            }
            idLte?.let {
                whereClauses.add("hb.id <= ?")
                params.add(it)
            }
            nameIn?.takeIf { it.isNotEmpty() }?.let {
                whereClauses.add("hb.name IN (${it.joinToString(",") { "?" }})")
                params.addAll(it)
            }
            coordinateXGte?.let {
                whereClauses.add("c.x >= ?")
                params.add(it)
            }
            coordinateXLte?.let {
                whereClauses.add("c.x <= ?")
                params.add(it)
            }
            coordinateYGte?.let {
                whereClauses.add("c.y >= ?")
                params.add(it)
            }
            coordinateYLte?.let {
                whereClauses.add("c.y <= ?")
                params.add(it)
            }
            creationDateGte?.let {
                whereClauses.add("hb.creationDate >= CAST(? AS DATE)")
                params.add(it)
            }
            creationDateLte?.let {
                whereClauses.add("hb.creationDate <= CAST(? AS DATE)")
                params.add(it)
            }
            realHero?.let {
                whereClauses.add("hb.realHero = ?")
                params.add(it)
            }
            hasToothpick?.let {
                whereClauses.add("hb.hasToothpick = ?")
                params.add(it)
            }
            impactSpeedGte?.let {
                whereClauses.add("hb.impactSpeed >= ?")
                params.add(it)
            }
            impactSpeedLte?.let {
                whereClauses.add("hb.impactSpeed <= ?")
                params.add(it)
            }
            minutesOfWaitingGte?.let {
                whereClauses.add("hb.minutesOfWaiting >= ?")
                params.add(it)
            }
            minutesOfWaitingLte?.let {
                whereClauses.add("hb.minutesOfWaiting <= ?")
                params.add(it)
            }
            coolCar?.let {
                whereClauses.add("car.cool = ?")
                params.add(it)
            }
            moodIn?.takeIf { it.isNotEmpty() }?.let { moods ->
                val placeholders = moods.joinToString(",") { "?::mood" } // Explicit cast to mood enum
                whereClauses.add("hb.mood IN ($placeholders)")
                params.addAll(moods)
            }
            weaponTypeIn?.takeIf { it.isNotEmpty() }?.let { weaponTypes ->
                val placeholders = weaponTypes.joinToString(",") { "?::weapon_type" } // Explicit cast to weapon_type enum
                whereClauses.add("hb.weaponType IN ($placeholders)")
                params.addAll(weaponTypes)
            }

            // Building ORDER BY clause
            sortFields?.let { fields ->
                val directions = sortDirections ?: List(fields.size) { SortOrderDto.ASC }
                fields.forEachIndexed { index, field ->
                    val direction = directions.getOrNull(index) ?: SortOrderDto.ASC
                    // Sanitize the field name to prevent SQL injection
                    val sanitizedField = when (field) {
                        "id", "name", "realHero", "hasToothpick", "impactSpeed", "minutesOfWaiting", "weaponType", "mood", "creationDate" -> "hb.$field"
                        "coordinateX" -> "c.x"
                        "coordinateY" -> "c.y"
                        "carCool" -> "car.cool"
                        else -> throw IllegalArgumentException("Invalid sort field: $field")
                    }
                    orderClauses.add("$sanitizedField ${direction.name}")
                }
            }

            // Assemble the final SQL query
            if (whereClauses.isNotEmpty()) {
                baseSQL.append(" WHERE ")
                baseSQL.append(whereClauses.joinToString(" AND "))
            }

            if (orderClauses.isNotEmpty()) {
                baseSQL.append(" ORDER BY ")
                baseSQL.append(orderClauses.joinToString(", "))
            }

            // Pagination
            page?.let { p ->
                size?.let { s ->
                    baseSQL.append(" LIMIT ? OFFSET ?")
                    params.add(s)
                    params.add((p - 1) * s)
                }
            }

            println(baseSQL)

            try {
                connection.prepareStatement(baseSQL.toString()).use { stmt ->
                    params.forEachIndexed { index, param ->
                        stmt.setObject(index + 1, param)
                    }

                    val rs = stmt.executeQuery()
                    val humanBeings = mutableListOf<HumanBeingDto>()

                    while (rs.next()) {
                        val humanBeing = HumanBeingDto(
                            id = rs.getInt("id"),
                            name = rs.getString("name"),
                            realHero = rs.getBoolean("realHero"),
                            hasToothpick = rs.getBoolean("hasToothpick"),
                            impactSpeed = rs.getFloat("impactSpeed"),
                            minutesOfWaiting = rs.getDouble("minutesOfWaiting"),
                            weaponType = rs.getString("weaponType"),
                            mood = rs.getString("mood"),
                            creationDate = rs.getDate("creationDate"),
                            coordinates = CoordinatesDto(
                                x = rs.getInt("coord_x"),
                                y = rs.getLong("coord_y")
                            ),
                            car = CarDto(
                                cool = rs.getBoolean("car_cool")
                            )
                        )
                        humanBeings.add(humanBeing)
                    }

                    return Response.ok(AllResponseDto(humanBeings)).type(MediaType.APPLICATION_XML_TYPE).build()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return Response.status(500).entity(ex.message).build()
            }
        }

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


    override fun getHumanBeingById(id: Int): Response? {

        DatabaseConfig.getConnection().use { connection ->
            val sql = """
            SELECT hb.id, hb.name, hb.realHero, hb.hasToothpick, hb.impactSpeed, hb.minutesOfWaiting, hb.creationdate,
                   hb.weaponType, hb.mood,
                   c.x AS coord_x, c.y AS coord_y,
                   car.cool AS car_cool
            FROM HumanBeing hb
            JOIN Coordinates c ON hb.coordinates_id = c.id
            JOIN Car car ON hb.car_id = car.id
            WHERE hb.id = ?
        """.trimIndent()

            try {
                connection.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        val humanBeing = HumanBeingDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            CoordinatesDto(
                                x = rs.getInt("coord_x"),
                                y = rs.getLong("coord_y")
                            ),
                            rs.getDate("creationdate"),
                            rs.getBoolean("realHero"),
                            rs.getBoolean("hasToothpick"),
                            rs.getFloat("impactSpeed"),
                            rs.getDouble("minutesOfWaiting"),
                            rs.getString("weaponType"),
                            rs.getString("mood"),
                            CarDto(
                                cool = rs.getBoolean("car_cool")
                            )
                        )
                        return Response.ok(humanBeing).build()
                    } else {
                        return Response.status(404).entity("HumanBeing with id $id not found").build()
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return Response.status(500).entity(ex.message).build()
            }
        }


    }

    override fun modifyHumanBeing(id: String, @Valid request: ModifyHumanBeingRequest): Response? {
        DatabaseConfig.getConnection().use { connection ->
            connection.autoCommit = false
            try {
                val updateSQL = StringBuilder("UPDATE HumanBeing SET ")

                val params = mutableListOf<Any?>()
                val columns = mutableListOf<String>()

                request.name?.let {
                    columns.add("name = ?")
                    params.add(it)
                }

                request.realHero?.let {
                    columns.add("realHero = ?")
                    params.add(it)
                }

                request.hasToothpick?.let {
                    columns.add("hasToothpick = ?")
                    params.add(it)
                }

                request.impactSpeed?.let {
                    columns.add("impactSpeed = ?")
                    params.add(it)
                }

                request.minutesOfWaiting?.let {
                    columns.add("minutesOfWaiting = ?")
                    params.add(it)
                }

                request.weaponType?.let {
                    columns.add("weaponType = ?::weapon_type")
                    params.add(it)
                }

                request.mood?.let {
                    columns.add("mood = ?::mood")
                    params.add(it)
                }

                request.coordinates?.let { coordinatesUpdate ->
                    val coordinatesId = getCoordinatesIdByHumanId(connection, id.toInt())

                    coordinatesUpdate.x?.let { x ->
                        updateCoordinateField(connection, coordinatesId, "x", x)
                    }
                    coordinatesUpdate.y?.let { y ->
                        updateCoordinateField(connection, coordinatesId, "y", y)
                    }
                }

                request.car?.let { carDto ->
                    val carId = getCarIdByHumanId(connection, id.toInt())

                    carDto.cool?.let { cool ->
                        updateCarField(connection, carId, "cool", cool)
                    }
                }

                if (columns.isNotEmpty()) {
                    updateSQL.append(columns.joinToString(", "))
                    updateSQL.append(" WHERE id = ?")
                    params.add(id.toInt())

                    connection.prepareStatement(updateSQL.toString()).use { stmt ->
                        params.forEachIndexed { index, param ->
                            stmt.setObject(index + 1, param)
                        }
                        stmt.executeUpdate()
                    }
                }

                println(updateSQL)

                connection.commit()
                return Response.ok().build()
            } catch (ex: Exception) {
                connection.rollback()
                ex.printStackTrace()
                return Response.status(404).entity(ex.message).build()
            } finally {
                connection.autoCommit = true
            }
        }
    }

    // Функция для обновления отдельного поля Coordinates
    private fun updateCoordinateField(conn: Connection, coordinatesId: Int, field: String, value: Any) {
        val sql = "UPDATE Coordinates SET $field = ? WHERE id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setObject(1, value)
            stmt.setInt(2, coordinatesId)
            stmt.executeUpdate()
        }
    }

    private fun updateCarField(conn: Connection, carId: Int, field: String, value: Any) {
        val sql = "UPDATE Car SET $field = ? WHERE id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setObject(1, value)
            stmt.setInt(2, carId)
            stmt.executeUpdate()
        }
    }

    private fun getCoordinatesIdByHumanId(conn: Connection, humanId: Int): Int {
        val sql = "SELECT coordinates_id FROM HumanBeing WHERE id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setInt(1, humanId)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return rs.getInt("coordinates_id")
            } else {
                throw Exception("Не удалось найти coordinates_id для HumanBeing с id = $humanId")
            }
        }
    }

    private fun getCarIdByHumanId(conn: Connection, humanId: Int): Int {
        val sql = "SELECT car_id FROM HumanBeing WHERE id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setInt(1, humanId)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return rs.getInt("car_id")
            } else {
                throw Exception("Не удалось найти car_id для HumanBeing с id = $humanId")
            }
        }
    }

    override fun getHumanStats(field: Optional<StatisticFieldDto>?, operation: Optional<StatisticOperationDto>?): Response? {

        val selectedField = field?.orElse(null) ?: throw BadRequestException("field parameter is required (IMPACT_SPEED / MINUTES_OF_WAITING)")
        val selectedOperation = operation?.orElse(null) ?: throw BadRequestException("operation parameter is required (MEAN / MAX / MIN)")

        val fieldColumn = when (selectedField) {
            StatisticFieldDto.IMPACT_SPEED -> "impactSpeed"
            StatisticFieldDto.MINUTES_OF_WAITING -> "minutesOfWaiting"
        }

        val sqlFunction = when (selectedOperation) {
            StatisticOperationDto.MEAN -> "AVG"
            StatisticOperationDto.MIN -> "MIN"
            StatisticOperationDto.MAX -> "MAX"
        }

        val sql = "SELECT $sqlFunction($fieldColumn) as result FROM HumanBeing"

        DatabaseConfig.getConnection().use { connection ->
            try {
                connection.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        val result = rs.getDouble("result")
                        return Response.ok().entity(StatisticResponseDto(result = result)).type(MediaType.APPLICATION_XML_TYPE).build()
                    } else {
                        return Response.ok().entity(StatisticResponseDto(result = null)).type(MediaType.APPLICATION_XML_TYPE).build()
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw InternalServerErrorException(ex.message)
            }
        }

    }

    @Produces(MediaType.APPLICATION_XML)
    @DELETE
    @Path("/human-being")
    override fun deleteByParams(
        @QueryParam("carCool") carCool: Boolean?,
        @QueryParam("impactSpeed") impactSpeed: Float?,
        @QueryParam("limit") @PositiveOrZero @Valid limit: Int?
    ): Response? {

        DatabaseConfig.getConnection().use { connection ->
            connection.autoCommit = false
            try {
                val params = mutableListOf<Any?>()
                val deleteSQL = StringBuilder()
                deleteSQL.append("DELETE FROM HumanBeing WHERE id IN (")
                deleteSQL.append("SELECT HumanBeing.id FROM HumanBeing")

                if (carCool != null) {
                    deleteSQL.append(" JOIN Car ON HumanBeing.car_id = Car.id")
                }

                val whereClauses = mutableListOf<String>()

                if (carCool != null) {
                    whereClauses.add("Car.cool = ?")
                    params.add(carCool)
                }

                if (impactSpeed != null) {
                    whereClauses.add("HumanBeing.impactSpeed = ?")
                    params.add(impactSpeed)
                }

                if (whereClauses.isNotEmpty()) {
                    deleteSQL.append(" WHERE ")
                    deleteSQL.append(whereClauses.joinToString(" AND "))
                }

                if (limit != null) {
                    deleteSQL.append(" LIMIT ?")
                    params.add(limit)
                }

                deleteSQL.append(")")

                println(deleteSQL)

                connection.prepareStatement(deleteSQL.toString()).use { stmt ->
                    params.forEachIndexed { index, param ->
                        stmt.setObject(index + 1, param)
                    }
                    val rowsAffected = stmt.executeUpdate()
                    println("Deleted Rows: $rowsAffected")
                }

                connection.commit()
                return Response.ok().build()
            } catch (ex: Exception) {
                connection.rollback()
                ex.printStackTrace()
                return Response.status(500).entity(ex.message).build()
            } finally {
                connection.autoCommit = true
            }
        }

    }

}
