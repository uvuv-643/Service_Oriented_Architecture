package ru.uvuv643.web;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.Produces;
import ru.uvuv643.ejb.remote.dto.enums.SortOrderDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticFieldDto;
import ru.uvuv643.ejb.remote.dto.enums.StatisticOperationDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;

@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public interface HumanWebService {

    @GET
    @Path("/human-being")
    @Produces(MediaType.APPLICATION_XML)
    Response getAllHumanBeing(
            @QueryParam("idGte") Integer idGte,
            @QueryParam("idLte") Integer idLte,
            @QueryParam("nameIn") List<String> nameIn,
            @QueryParam("coordinateXGte") Integer coordinateXGte,
            @QueryParam("coordinateXLte") Integer coordinateXLte,
            @QueryParam("coordinateYGte") Double coordinateYGte,
            @QueryParam("coordinateYLte") Double coordinateYLte,
            @QueryParam("creationDateGte") String creationDateGte,
            @QueryParam("creationDateLte") String creationDateLte,
            @QueryParam("realHero") Boolean realHero,
            @QueryParam("hasToothpick") Boolean hasToothpick,
            @QueryParam("impactSpeedGte") Float impactSpeedGte,
            @QueryParam("impactSpeedLte") Float impactSpeedLte,
            @QueryParam("minutesOfWaitingGte") Long minutesOfWaitingGte,
            @QueryParam("minutesOfWaitingLte") Long minutesOfWaitingLte,
            @QueryParam("coolCar") Boolean coolCar,
            @QueryParam("moodIn") List<String> moodIn,
            @QueryParam("weaponTypeIn") List<String> weaponTypeIn,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sortFields") List<String> sortFields,
            @QueryParam("sortDirections") List<SortOrderDto> sortDirections
    );

    @POST
    @Path("/human-being")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    Response createHumanBeing(@Valid CreateHumanBeingRequest request);

    @GET
    @Path("/human-being/{id}")
    @Produces(MediaType.APPLICATION_XML)
    Response getHumanBeingById(@PathParam("id") int id);

    @PUT
    @Path("/human-being/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    Response modifyHumanBeing(@PathParam("id") String id, @Valid ModifyHumanBeingRequest request);

    @GET
    @Path("/human-being/stats")
    @Produces(MediaType.APPLICATION_XML)
    Response getHumanStats(
            @QueryParam("field") StatisticFieldDto field,
            @QueryParam("operation") StatisticOperationDto operation
    );

    @DELETE
    @Path("/human-being")
    @Produces(MediaType.APPLICATION_XML)
    Response deleteByParams(
            @QueryParam("carCool") Boolean carCool,
            @QueryParam("impactSpeed") Float impactSpeed,
            @QueryParam("limit") @PositiveOrZero @Valid Integer limit
    );
}