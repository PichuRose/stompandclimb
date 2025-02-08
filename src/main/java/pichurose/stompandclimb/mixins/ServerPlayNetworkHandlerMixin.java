package pichurose.stompandclimb.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Shadow
    protected static double clampHorizontal(double d) {
        return 0;
    }

    @Shadow
    protected static double clampVertical(double d) {
        return 0;
    }

    @Shadow private double lastTickX;

    @Shadow private double lastTickY;

    @Shadow private double lastTickZ;

    @Shadow private double updatedZ;

    @Shadow private double updatedY;

    @Shadow private double updatedX;

    @Inject(method = "onPlayerMove", at = @At("HEAD"))
    private void injected(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        ClientLocationInterface clientLocationInterface = (ClientLocationInterface)player;
        double d = clampHorizontal(packet.getX(this.player.getX()));
        double e = clampVertical(packet.getY(this.player.getY()));
        double f = clampHorizontal(packet.getZ(this.player.getZ()));
        double l = d - updatedX;
        double m = e - updatedY;
        double n = f - updatedZ;
        clientLocationInterface.stompandclimb_updateCache(new Vec3d(l, m, n));
    }
}
//PlayerMoveC2SPacket


