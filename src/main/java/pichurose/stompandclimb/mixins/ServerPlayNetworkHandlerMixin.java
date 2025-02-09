package pichurose.stompandclimb.mixins;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayer player;

    @Shadow
    private static double clampHorizontal(double d) {
        return 0;
    }

    @Shadow
    private static double clampVertical(double d) {
        return 0;
    }

    @Shadow private double lastGoodX;

    @Shadow private double lastGoodY;

    @Shadow private double lastGoodZ;

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void injected(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        ClientLocationInterface clientLocationInterface = (ClientLocationInterface)player;
        double d = clampHorizontal(packet.getX(this.player.getX()));
        double e = clampVertical(packet.getY(this.player.getY()));
        double f = clampHorizontal(packet.getZ(this.player.getZ()));
        double l = d - lastGoodX;
        double m = e - lastGoodY;
        double n = f - lastGoodZ;
        clientLocationInterface.stompandclimb_updateCache(new Vec3(l, m, n));
    }
}
//PlayerMoveC2SPacket


