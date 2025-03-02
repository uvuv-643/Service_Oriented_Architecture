package ru.uvuv643.soa

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import jakarta.xml.bind.annotation.*
import org.glassfish.jersey.server.ParamException
import org.glassfish.jersey.server.ResourceConfig
import ru.uvuv643.soa.server.HumanWebServiceImpl
import ru.uvuv643.soa.server.StaticServiceImpl
import ru.uvuv643.soa.server.ExampleResource
import kotlin.collections.ArrayList

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class ConstraintError (

    @XmlElementWrapper(name = "errors")
    @XmlElement(name="error")
    val error: List<String>? = null,

    )

@Provider
class ConstraintViolationExceptionMapper : ExceptionMapper<ConstraintViolationException> {
    override fun toResponse(exception: ConstraintViolationException): Response {
        val errors = ArrayList<String>()
        for (error in exception.constraintViolations) {
            var last = ""
            for (x in error.propertyPath) {
                last = x.name
            }
            errors.add(last + " " + error.message)
        }
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(ConstraintError(errors))
            .type(MediaType.APPLICATION_XML)
            .build()
    }
}

@Provider
class AnyMapper : ExceptionMapper<Throwable> {
    override fun toResponse(exception: Throwable): Response {
        exception.printStackTrace()
        return Response.serverError().entity(exception.stackTrace.contentToString()).build()
    }
}

@Provider
class QueryParamMapper : ExceptionMapper<ParamException.QueryParamException> {
    override fun toResponse(exception: ParamException.QueryParamException): Response {
        exception.printStackTrace()
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(ConstraintError(listOf(exception.parameterName + " must be valid parameter")))
            .type(MediaType.APPLICATION_XML)
            .build()
    }
}

@Provider
class BadRequestMapper : ExceptionMapper<BadRequestException> {
    override fun toResponse(exception: BadRequestException): Response {
        exception.printStackTrace()
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(ConstraintError(listOf(exception.message.toString())))
            .type(MediaType.APPLICATION_XML)
            .build()
    }
}

@Provider
class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {
    override fun toResponse(exception: IllegalArgumentException): Response {
        exception.printStackTrace()
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(ConstraintError(listOf(exception.message.toString())))
            .type(MediaType.APPLICATION_XML)
            .build()
    }
}




@ApplicationPath("/api")
class MyApplication : ResourceConfig() {
    init {
        register(HumanWebServiceImpl::class.java)
        register(StaticServiceImpl::class.java)
        register(ExampleResource::class.java)
        register(ConstraintViolationExceptionMapper::class.java)
        register(QueryParamMapper::class.java)
        register(BadRequestMapper::class.java)
        register(IllegalArgumentExceptionMapper::class.java)
        register(AnyMapper::class.java)
    }
}
