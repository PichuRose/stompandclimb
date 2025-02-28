package pichurose.stompandclimb.items;

import io.github.flemmli97.flan.api.ClaimHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.ResizingUtils;

public class GrowingRingItem extends Item {
    public GrowingRingItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        if (!ClaimHandler.canInteract((ServerPlayer) user, user.blockPosition(), ResourceLocation.of("stompandclimb:sizechanging", ':'))) { return super.use(world, user, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 1.0905077326652576592070106557607f);
        user.getCooldowns().addCooldown(this, 20);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(user.getId());
        buf.writeFloat(1.0905077326652576592070106557607f);
        ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, buf);
        return super.use(world, user, hand);
    }
}
