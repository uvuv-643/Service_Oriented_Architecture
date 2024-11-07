package ru.uvuv643.soa.server

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.sql.ResultSet

@Path("/example")
class ExampleResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getExampleData(): List<Map<String, Any>> {
        val resultList = mutableListOf<Map<String, Any>>()
        DatabaseConfig.getConnection().use { connection ->
            val stmt = connection.prepareStatement("SELECT * FROM your_table")
            val rs: ResultSet = stmt.executeQuery()
            while (rs.next()) {
                resultList.add(mapOf("column1" to rs.getObject("column1"), "column2" to rs.getObject("column2")))
            }
        }
        return resultList
    }
}