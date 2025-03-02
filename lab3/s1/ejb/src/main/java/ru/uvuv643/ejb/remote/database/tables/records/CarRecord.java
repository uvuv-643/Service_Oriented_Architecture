/*
 * This file is generated by jOOQ.
 */
package ru.uvuv643.ejb.remote.database.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import ru.uvuv643.ejb.remote.database.tables.Car;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CarRecord extends UpdatableRecordImpl<CarRecord> implements Record2<Integer, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.car.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.car.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.car.cool</code>.
     */
    public void setCool(Boolean value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.car.cool</code>.
     */
    public Boolean getCool() {
        return (Boolean) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, Boolean> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, Boolean> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Car.CAR.ID;
    }

    @Override
    public Field<Boolean> field2() {
        return Car.CAR.COOL;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Boolean component2() {
        return getCool();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Boolean value2() {
        return getCool();
    }

    @Override
    public CarRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public CarRecord value2(Boolean value) {
        setCool(value);
        return this;
    }

    @Override
    public CarRecord values(Integer value1, Boolean value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CarRecord
     */
    public CarRecord() {
        super(Car.CAR);
    }

    /**
     * Create a detached, initialised CarRecord
     */
    public CarRecord(Integer id, Boolean cool) {
        super(Car.CAR);

        setId(id);
        setCool(cool);
    }
}
