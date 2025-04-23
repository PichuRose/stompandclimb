package pichurose.stompandclimb.mixins;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.interfaces.ClientInformationInterface;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;

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

    @Unique
    boolean isAllowedToClimbOld = true;


    @Shadow @Final private MinecraftServer server;

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void injected(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        ClientInformationInterface clientInformationInterface = (ClientInformationInterface)player;
        double d = clampHorizontal(packet.getX(this.player.getX()));
        double e = clampVertical(packet.getY(this.player.getY()));
        double f = clampHorizontal(packet.getZ(this.player.getZ()));
        double l = d - lastGoodX;
        double m = e - lastGoodY;
        double n = f - lastGoodZ;

        clientInformationInterface.stompandclimb_updateCache(new Vec3(l, m, n));


        ResourceLocation perm = FlanUtils.CLIMBING;
        //boolean isAllowedToClimb = ClaimHandler.canInteract(player, new BlockPos((int)packet.getX(this.player.getX()), (int)packet.getY(this.player.getY()), (int)packet.getZ(this.player.getZ())), perm);
        if(StompAndClimb.hasFlan && this.player.isAlive()) {
            boolean isAllowedToClimb = FlanUtils.canInteract(player, new BlockPos((int)packet.getX(this.player.getX()), (int)packet.getY(this.player.getY()), (int)packet.getZ(this.player.getZ())), perm);
            if(isAllowedToClimb != isAllowedToClimbOld){
                this.server.sendSystemMessage(Component.literal("isAllowedToClimb: " + isAllowedToClimb));
                isAllowedToClimbOld = isAllowedToClimb;
                clientInformationInterface.stompandclimb_updateIsAllowedToClimb(isAllowedToClimb);

                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(isAllowedToClimb);
                ServerPlayNetworking.send(player, StompAndClimbNetworkingConstants.UPDATE_IS_ALLOWED_TO_CLIMB_CLIENT_PACKET, buf);
        }
        }



    }
}
//PlayerMoveC2SPacket


