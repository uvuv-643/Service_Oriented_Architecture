package ru.uvuv643.web;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.glassfish.jersey.server.ParamException;
import org.glassfish.jersey.server.ResourceConfig;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ConstraintError {
    @XmlElementWrapper(name = "errors")
    @XmlElement(name = "error")
    private List<String> error;

    public ConstraintError() {}

    public ConstraintError(List<String> error) {
        this.error = error;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}

@Provider
class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    public ConstraintViolationExceptionMapper() {}
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>();
        exception.getConstraintViolations().forEach(error -> {
            String last = "";
            for (var x : error.getPropertyPath()) {
                last = x.getName();
            }
            errors.add(last + " " + error.getMessage());
        });
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ConstraintError(errors))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}

@Provider
class AnyMapper implements ExceptionMapper<Throwable> {

    public AnyMapper() {}
    @Override
    public Response toResponse(Throwable exception) {
        exception.printStackTrace();
        return Response.serverError().entity(exception.getStackTrace().toString()).build();
    }
}

@Provider
class QueryParamMapper implements ExceptionMapper<ParamException.QueryParamException> {
    public QueryParamMapper() {
    }

    @Override
    public Response toResponse(ParamException.QueryParamException exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ConstraintError(List.of(exception.getParameterName() + " must be valid parameter")))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}

@Provider
class BadRequestMapper implements ExceptionMapper<BadRequestException> {
    public BadRequestMapper() {
    }

    @Override
    public Response toResponse(BadRequestException exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ConstraintError(List.of(exception.getMessage())))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}

@Provider
class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    public IllegalArgumentExceptionMapper() {
    }

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ConstraintError(List.of(exception.getMessage())))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {
    public HelloApplication() {
        register(HumanWebServiceImpl.class);
        register(ConstraintViolationExceptionMapper.class);

        register(QueryParamMapper.class);
        register(BadRequestMapper.class);
        register(IllegalArgumentExceptionMapper.class);
        register(AnyMapper.class);
    }
}
