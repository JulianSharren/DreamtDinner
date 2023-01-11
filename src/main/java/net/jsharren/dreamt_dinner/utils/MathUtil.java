package net.jsharren.dreamt_dinner.utils;

import net.minecraft.util.math.Vec3i;

public class MathUtil {
    public static long initClock(Vec3i v) {
        return 23L * v.getX() + 27L * v.getY() - 29L * v.getZ(); 
    }
}
