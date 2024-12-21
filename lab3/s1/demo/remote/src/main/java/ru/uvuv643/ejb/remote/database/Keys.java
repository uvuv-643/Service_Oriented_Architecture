/*
 * This file is generated by jOOQ.
 */
package ru.uvuv643.ejb.remote.database;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import ru.uvuv643.ejb.remote.database.tables.Car;
import ru.uvuv643.ejb.remote.database.tables.Coordinates;
import ru.uvuv643.ejb.remote.database.tables.Humanbeing;
import ru.uvuv643.ejb.remote.database.tables.TeamHeroes;
import ru.uvuv643.ejb.remote.database.tables.records.CarRecord;
import ru.uvuv643.ejb.remote.database.tables.records.CoordinatesRecord;
import ru.uvuv643.ejb.remote.database.tables.records.HumanbeingRecord;
import ru.uvuv643.ejb.remote.database.tables.records.TeamHeroesRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CarRecord> CAR_PKEY = Internal.createUniqueKey(Car.CAR, DSL.name("car_pkey"), new TableField[] { Car.CAR.ID }, true);
    public static final UniqueKey<CoordinatesRecord> COORDINATES_PKEY = Internal.createUniqueKey(Coordinates.COORDINATES, DSL.name("coordinates_pkey"), new TableField[] { Coordinates.COORDINATES.ID }, true);
    public static final UniqueKey<HumanbeingRecord> HUMANBEING_PKEY = Internal.createUniqueKey(Humanbeing.HUMANBEING, DSL.name("humanbeing_pkey"), new TableField[] { Humanbeing.HUMANBEING.ID }, true);
    public static final UniqueKey<TeamHeroesRecord> TEAM_HEROES_PKEY = Internal.createUniqueKey(TeamHeroes.TEAM_HEROES, DSL.name("team_heroes_pkey"), new TableField[] { TeamHeroes.TEAM_HEROES.TEAM_ID, TeamHeroes.TEAM_HEROES.HERO_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<HumanbeingRecord, CarRecord> HUMANBEING__HUMANBEING_CAR_ID_FKEY = Internal.createForeignKey(Humanbeing.HUMANBEING, DSL.name("humanbeing_car_id_fkey"), new TableField[] { Humanbeing.HUMANBEING.CAR_ID }, Keys.CAR_PKEY, new TableField[] { Car.CAR.ID }, true);
    public static final ForeignKey<HumanbeingRecord, CoordinatesRecord> HUMANBEING__HUMANBEING_COORDINATES_ID_FKEY = Internal.createForeignKey(Humanbeing.HUMANBEING, DSL.name("humanbeing_coordinates_id_fkey"), new TableField[] { Humanbeing.HUMANBEING.COORDINATES_ID }, Keys.COORDINATES_PKEY, new TableField[] { Coordinates.COORDINATES.ID }, true);
}
