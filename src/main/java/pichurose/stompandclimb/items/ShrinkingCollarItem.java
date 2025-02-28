package pichurose.stompandclimb.items;

import io.github.flemmli97.flan.api.ClaimHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.ResizingUtils;

public class ShrinkingCollarItem extends Item {

    public ShrinkingCollarItem(Properties settings) {
        super(settings);
    }



    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.level().isClientSide) { return super.interactLivingEntity(stack, user, entity, hand); }
        if (!ClaimHandler.canInteract((ServerPlayer) user, entity.blockPosition(), ResourceLocation.of("stompandclimb:sizechanging", ':'))) { return super.interactLivingEntity(stack, user, entity, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }
        ResizingUtils.resizeInstant(entity, 0.91700404320467123174354159479414f);
        user.getCooldowns().addCooldown(this, 20);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entity.getId());
        buf.writeFloat(0.91700404320467123174354159479414f);
        if (user instanceof ServerPlayer) {
            ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, buf);
        }
        return super.interactLivingEntity(stack, user, entity, hand);
    }


}
