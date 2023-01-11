package net.jsharren.dreamt_dinner.impl;

import net.jsharren.dreamt_dinner.api.IScheduler;
import net.minecraft.world.World;

public class TimeOfDayScheduler implements IScheduler<Integer> {
    public static final Integer MODULO = 192000;  // Period of moon phases

    private final Integer param;

    public TimeOfDayScheduler(Integer param) {
        this.param = param;
    }

    public static TimeOfDayScheduler init(World world) {
        Long scheduledTimeLong = world.getTimeOfDay();
        Integer scheduledTime = ((Long)(scheduledTimeLong % MODULO)).intValue();
        return new TimeOfDayScheduler(MODULO - scheduledTime);
    }

    @Override
    public Integer getElapsed(World world) {
        Long elapsedLong = world.getTimeOfDay() + param;
        Integer elapsed = ((Long)(elapsedLong % MODULO)).intValue();
        return (elapsed * 2 >= MODULO) ? elapsed - MODULO : elapsed;
    }

    @Override
    public Integer getParam() {
        return param;
    }

    
}
