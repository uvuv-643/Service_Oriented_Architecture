package ru.uvuv643.soa

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import jakarta.xml.bind.annotation.*
import org.glassfish.jersey.server.ResourceConfig
import ru.uvuv643.soa.server.HumanWebServiceImpl

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

@ApplicationPath("/api")
class MyApplication : ResourceConfig() {
    init {
        register(HumanWebServiceImpl::class.java)
        register(ConstraintViolationExceptionMapper::class.java)
    }
}
