package ru.uvuv643.soa.api.v1

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Tag(name = "Static Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface StaticService {

    @GET
    @Path("/openapi.json")
    @Produces(MediaType.APPLICATION_JSON)
    fun getOpenApiJson(): Response

}
