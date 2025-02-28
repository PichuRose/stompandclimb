package pichurose.stompandclimb.mixins;

import io.github.flemmli97.flan.api.ClaimHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;

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
    boolean isAllowedToClimbOld = false;


    @Shadow @Final private MinecraftServer server;

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void injected(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        ClientLocationInterface clientLocationInterface = (ClientLocationInterface)player;
        double d = clampHorizontal(packet.getX(this.player.getX()));
        double e = clampVertical(packet.getY(this.player.getY()));
        double f = clampHorizontal(packet.getZ(this.player.getZ()));
        double l = d - lastGoodX;
        double m = e - lastGoodY;
        double n = f - lastGoodZ;




        ResourceLocation perm = ResourceLocation.of("stompandclimb:climbing", ':');
        boolean isAllowedToClimb = ClaimHandler.canInteract(player, new BlockPos((int)packet.getX(this.player.getX()), (int)packet.getY(this.player.getY()), (int)packet.getZ(this.player.getZ())), perm);

        if(isAllowedToClimb != isAllowedToClimbOld){
            this.server.sendSystemMessage(Component.literal("isAllowedToClimb: " + isAllowedToClimb));
            isAllowedToClimbOld = isAllowedToClimb;
            clientLocationInterface.stompandclimb_updateCache(new Vec3(l, m, n), isAllowedToClimb);

            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeDouble(l);
            buf.writeDouble(m);
            buf.writeDouble(n);
            buf.writeBoolean(isAllowedToClimb);
            ServerPlayNetworking.send(player, StompAndClimbNetworkingConstants.UPDATE_IS_ALLOWED_TO_CLIMB_CLIENT_PACKET, buf);

        }



    }
}
//PlayerMoveC2SPacket


