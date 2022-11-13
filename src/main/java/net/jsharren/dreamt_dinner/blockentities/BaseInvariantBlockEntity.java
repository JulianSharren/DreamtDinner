package net.jsharren.dreamt_dinner.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BaseInvariantBlockEntity extends BlockEntity {

    public BaseInvariantBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void packNbt(NbtCompound nbt, Boolean toClient) {}
    
    public void unpackNbt(NbtCompound nbt) {}

    public void markChanges() {
        if ( hasWorld() ) world.markDirty(pos);
    }

    public void markSync() {
        if ( this.world instanceof ServerWorld serverWorld ) {
            serverWorld.getChunkManager().markForUpdate(pos);
        }
    }
    
    @Override
    public void setCachedState(BlockState state) {
        if ( getCachedState() != state ) {
            throw new IllegalStateException("You cannot use AbstractInvariantBlockEntity for a multi-state block!");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        packNbt(nbt, false);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        unpackNbt(nbt);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        packNbt(nbt, true);
        super.writeNbt(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
