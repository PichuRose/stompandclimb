package pichurose.stompandclimb.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pichurose.stompandclimb.utils.ResizingUtils;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin {

    @Environment(EnvType.CLIENT)
    @ModifyVariable(method = "shouldRenderAtSqrDistance", at = @At("STORE"), ordinal = 1)
    private double scaledDistance(double distance){
            Entity entity = (Entity) (Object) this;
            float scale = ResizingUtils.getActualSize(entity);
            float clientscale = ResizingUtils.getActualSize(Minecraft.getInstance().player);
            if(scale < 1.0f){
                return 8192.0d / scale;
            }
            else if (clientscale > 1.0f){
                return 8192.0d * clientscale;
            }
            else{
                return 8192;
            }
    }
}
