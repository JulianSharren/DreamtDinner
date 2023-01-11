package net.jsharren.dreamt_dinner.api;

import net.minecraft.world.World;

public interface IScheduler <N extends Number> {
    public N getParam();
    public N getElapsed(World world);
}
