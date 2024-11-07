package ru.uvuv643.soa.server

import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import ru.uvuv643.soa.api.v1.StaticService

@Path("/v1/")
class StaticServiceImpl : StaticService {
    override fun getOpenApiJson(): Response {
        println("getJsonFile")
        val jsonStream = javaClass.classLoader.getResourceAsStream("openapi.json")
            ?: return Response.status(Response.Status.NOT_FOUND).entity("File not found").build()
        return Response.status(200)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Credentials", "true")
            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
            .header("Access-Control-Max-Age", "1209600")
            .entity(jsonStream).build()
    }
}