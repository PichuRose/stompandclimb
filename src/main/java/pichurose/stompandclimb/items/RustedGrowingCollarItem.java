package pichurose.stompandclimb.items;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;


public class RustedGrowingCollarItem extends Item {

    public RustedGrowingCollarItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity instanceof LivingEntity){
            ((LivingEntity)entity).addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_GROWING, 20, 0, true, false, false));
        }
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.level().isClientSide) { return super.interactLivingEntity(stack, user, entity, hand); }
        if (!FlanUtils.canInteract(user, entity, FlanUtils.SIZE_CHANGING)) { return super.interactLivingEntity(stack, user, entity, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }
        ResizingUtils.resizeInstant(entity, 1.0905077326652576592070106557607f);
        entity.addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_GROWING, 12000, 0, true, false, false));
        user.getCooldowns().addCooldown(this, 20);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entity.getId());
        buf.writeFloat(1.0905077326652576592070106557607f);
        if (user instanceof ServerPlayer) {
            ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, buf);
        }
        return super.interactLivingEntity(stack, user, entity, hand);
    }

}
