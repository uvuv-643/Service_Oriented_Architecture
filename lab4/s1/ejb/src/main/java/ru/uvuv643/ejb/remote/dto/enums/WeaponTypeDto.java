package ru.uvuv643.ejb.remote.dto.enums;

import ru.uvuv643.ejb.remote.database.enums.WeaponType;

import java.io.Serializable;

public enum WeaponTypeDto  implements Serializable {
    HAMMER, AXE, PISTOL, KNIFE, BAT;

    public static WeaponType toDatabase(WeaponTypeDto value) {
        switch (value) {
            case HAMMER -> {
                return WeaponType.HAMMER;
            }
            case AXE -> {
                return WeaponType.AXE;
            }
            case PISTOL -> {
                return WeaponType.PISTOL;
            }
            case KNIFE -> {
                return WeaponType.KNIFE;
            }
            case BAT -> {
                return WeaponType.BAT;
            }
        }
        return null;
    }

    public static WeaponTypeDto fromString(String value) {
        switch (value) {
            case "HAMMER" -> {
                return HAMMER;
            }
            case "AXE" -> {
                return AXE;
            }
            case "PISTOL" -> {
                return PISTOL;
            }
            case "KNIFE" -> {
                return KNIFE;
            }
            case "BAT" -> {
                return BAT;
            }
        }
        return null;
    }
}