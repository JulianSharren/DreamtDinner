package net.jsharren.dreamt_dinner.utils;

import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public final class SerializeUtil {
    public static NbtList toNbt(Vec3d v) {
        NbtList nbt = new NbtList();
        nbt.add(NbtDouble.of(v.getX()));
        nbt.add(NbtDouble.of(v.getY()));
        nbt.add(NbtDouble.of(v.getZ()));
        return nbt;
    }
    
    public static NbtList toNbt(Vec3f v) {
        NbtList nbt = new NbtList();
        nbt.add(NbtFloat.of(v.getX()));
        nbt.add(NbtFloat.of(v.getY()));
        nbt.add(NbtFloat.of(v.getZ()));
        return nbt;
    }

    public static Vec3d toVec3d(NbtList nbt) {
        return new Vec3d(nbt.getDouble(0), nbt.getDouble(1), nbt.getDouble(2));
    }

    public static Vec3f toVec3f(NbtList nbt) {
        return new Vec3f(nbt.getFloat(0), nbt.getFloat(1), nbt.getFloat(2));
    }
}
