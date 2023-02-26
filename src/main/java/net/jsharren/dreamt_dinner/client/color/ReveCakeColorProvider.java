package net.jsharren.dreamt_dinner.client.color;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class ReveCakeColorProvider {
    public static int[] BASE_COLORS = {
        0xe365f8, 0x8043fa, 0x43fa80, 0x65e3f8
    };
    
    public static int getBlockColor(BlockState state, @Nullable BlockRenderView view, @Nullable BlockPos pos, int tintIndex) {
        if ( tintIndex < 0 || tintIndex > 3 ) return -1;
        
        int shift = ((pos.getX() & 1) | ((pos.getZ() & 1) << 1)) + (pos.getY() & 3) + tintIndex;
        return BASE_COLORS[shift & 3];
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if ( tintIndex < 0 || tintIndex > 3 ) return -1;
    
        return BASE_COLORS[tintIndex];
    }
}
