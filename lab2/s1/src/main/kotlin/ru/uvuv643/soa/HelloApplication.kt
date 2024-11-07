package ru.uvuv643.soa

import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider


@ApplicationPath("/api")
class MyApplication : Application() {
}

@Provider
class DebugMapper : ExceptionMapper<Throwable> {
    override fun toResponse(t: Throwable): Response {
        t.printStackTrace()
        return Response.serverError()
            .entity(t.stackTraceToString())
            .build()
    }
}