package ru.uvuv643.soa

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@OpenAPIDefinition(
        info =
        Info(
                title = "Team manager API",
                version = "1.0",
        ),
        servers = [
            Server(
                    url = "http://localhost:8080",
                    description = "First Web Server",
            ),
            Server(
                    url = "http://localhost:8081",
                    description = "Second Web Server",
            ),
        ],
)
class SoaApplication

fun main(args: Array<String>) {
    runApplication<SoaApplication>(*args)
}

