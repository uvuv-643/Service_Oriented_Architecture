package ru.uvuv643.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.uvuv643.ejb.remote.RemoteServiceEjb;
import ru.uvuv643.ejb.remote.dto.enums.SortOrderDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticFieldDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticOperationDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.web.config.ConsulConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Path("/v1/")
public class HumanWebServiceImpl implements HumanWebService {

    private final RemoteServiceEjb remoteServiceEjb;

    public HumanWebServiceImpl() throws NamingException {
        this.remoteServiceEjb = this.getFromEJBPool();
    }

    @Override
    @GET
    @Path("/human-being")
    public Response getAllHumanBeing(
            @QueryParam("idGte") Integer idGte,
            @QueryParam("idLte") Integer idLte,
            @QueryParam("nameIn") List<String> nameIn,
            @QueryParam("coordinateXGte") Integer coordinateXGte,
            @QueryParam("coordinateXLte") Integer coordinateXLte,
            @QueryParam("coordinateYGte") Long coordinateYGte,
            @QueryParam("coordinateYLte") Long coordinateYLte,
            @QueryParam("creationDateGte") String creationDateGte,
            @QueryParam("creationDateLte") String creationDateLte,
            @QueryParam("realHero") Boolean realHero,
            @QueryParam("hasToothpick") Boolean hasToothpick,
            @QueryParam("impactSpeedGte") Double impactSpeedGte,
            @QueryParam("impactSpeedLte") Double impactSpeedLte,
            @QueryParam("minutesOfWaitingGte") Double minutesOfWaitingGte,
            @QueryParam("minutesOfWaitingLte") Double minutesOfWaitingLte,
            @QueryParam("coolCar") Boolean coolCar,
            @QueryParam("moodIn") List<String> moodIn,
            @QueryParam("weaponTypeIn") List<String> weaponTypeIn,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sortFields") List<String> sortFields,
            @QueryParam("sortDirections") List<SortOrderDto> sortDirections) {

        return Response.ok(remoteServiceEjb.getAllHumanBeing(idGte, idLte, nameIn, coordinateXGte, coordinateXLte, coordinateYGte,
                coordinateYLte, creationDateGte, creationDateLte, realHero, hasToothpick, impactSpeedGte, impactSpeedLte,
                minutesOfWaitingGte, minutesOfWaitingLte, coolCar, moodIn, weaponTypeIn, page, size, sortFields, sortDirections)).header("X-Server", ConsulConfig.service).build();
    }

    @Override
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @POST
    @Path("/human-being")
    public Response createHumanBeing(@Valid CreateHumanBeingRequest request) {
        remoteServiceEjb.createHumanBeing(request);
        return Response.ok().header("X-Server", ConsulConfig.service).build();
    }

    @Override
    @GET
    @Path("/human-being/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getHumanBeingById(@PathParam("id") int id) {
        return Response.ok(remoteServiceEjb.getHumanBeingById(id)).header("X-Server", ConsulConfig.service).build();
    }

    @Override
    @PUT
    @Path("/human-being/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response modifyHumanBeing(@PathParam("id") String id, @Valid ModifyHumanBeingRequest request) {
        remoteServiceEjb.modifyHumanBeing(id, request);
        return Response.ok().header("X-Server", ConsulConfig.service).build();
    }

    @Override
    @GET
    @Path("/human-being/stats")
    @Produces(MediaType.APPLICATION_XML)
    public Response getHumanStats( @QueryParam("field") StatisticFieldDto field,
                                   @QueryParam("operation") StatisticOperationDto operation) {
        return Response.ok(remoteServiceEjb.getHumanStats(field, operation)).header("X-Server", ConsulConfig.service).build();
    }

    @DELETE
    @Path("/human-being")
    @Produces(MediaType.APPLICATION_XML)
    @Override
    public Response deleteByParams(
            @QueryParam("id") Integer id,
            @QueryParam("carCool") Boolean carCool,
                @QueryParam("impactSpeed") Double impactSpeed,
            @QueryParam("limit") @PositiveOrZero @Valid Integer limit
    ) {
        remoteServiceEjb.deleteByParams(id, carCool, impactSpeed, limit);
        return Response.ok().header("X-Server", ConsulConfig.service).build();
    }

    private RemoteServiceEjb getFromEJBPool() throws NamingException {
        return (RemoteServiceEjb) new InitialContext().lookup("java:global/remote-1.0-SNAPSHOT/ServiceEjbImpl!ru.uvuv643.ejb.remote.RemoteServiceEjb");
    }

}
