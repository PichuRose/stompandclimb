package pichurose.stompandclimb.items;

import io.github.flemmli97.flan.api.ClaimHandler;
import jdk.jshell.Snippet;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.List;

public class RingItem extends Item {
    private float SIZE = 1;

    public RingItem(Properties settings, float size) {
        super(settings);
        SIZE = size;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        if (!ClaimHandler.canInteract((ServerPlayer) user, user.blockPosition(), ResourceLocation.of("flan:sizechanging", ':'))) { return super.use(world, user, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        if(ResizingUtils.getSize(user) != SIZE){
            ResizingUtils.setSize(user, SIZE);
            user.getCooldowns().addCooldown(this, 20);
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(user.getId());
            buf.writeFloat(SIZE);
            ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, buf);
        }
        return super.use(world, user, hand);
    }
}
