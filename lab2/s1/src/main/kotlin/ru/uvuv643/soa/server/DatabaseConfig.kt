package ru.uvuv643.soa.server

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

object DatabaseConfig {
    private val url: String
    private val user: String
    private val password: String

    init {

        try {
            Class.forName("org.postgresql.Driver") // Ensure the driver is loaded
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("PostgreSQL JDBC Driver not found. Include it in your library path.", e)
        }

        val props = Properties()
        val resource = this::class.java.classLoader.getResource("application.properties")
            ?: throw IllegalStateException("Could not locate application.properties in resources folder")

        props.load(resource.openStream())

        url = props.getProperty("db.url")
        user = props.getProperty("db.user")
        password = props.getProperty("db.password")
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
}