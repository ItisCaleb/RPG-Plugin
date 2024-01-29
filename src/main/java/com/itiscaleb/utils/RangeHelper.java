package com.itiscaleb.utils;

import org.bukkit.Location;

public class RangeHelper {

    static public boolean checkWithinRange(Location middle, Location target ,RangeMode mode){

        return true;
    }

    public enum RangeMode{
        FrontSemiCircle,
        FrontRectangle
    }
}

