package ru.uvuv643.soa.server;

import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;


@Path("/v1/")
class CorsFilter {

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

}