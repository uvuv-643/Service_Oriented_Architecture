package ru.uvuv643.ejb.remote.dto.enums;

import ru.uvuv643.ejb.remote.database.enums.Mood;
import ru.uvuv643.ejb.remote.database.enums.WeaponType;

import java.io.Serializable;

public enum MoodDto implements Serializable {
    SORROW, APATHY, CALM, RAGE, FRENZY;

    public static Mood toDatabase(MoodDto value) {
        switch (value) {
            case SORROW -> {
                return Mood.SORROW;
            }
            case APATHY -> {
                return Mood.APATHY;
            }
            case CALM -> {
                return Mood.CALM;
            }
            case RAGE -> {
                return Mood.RAGE;
            }
            case FRENZY -> {
                return Mood.FRENZY;
            }
        }
        return null;
    }

    public static MoodDto fromString(String value) {
        switch (value) {
            case "SORROW" -> {
                return SORROW;
            }
            case "APATHY" -> {
                return APATHY;
            }
            case "CALM" -> {
                return CALM;
            }
            case "RAGE" -> {
                return RAGE;
            }
            case "FRENZY" -> {
                return FRENZY;
            }
        }
        return null;
    }
}