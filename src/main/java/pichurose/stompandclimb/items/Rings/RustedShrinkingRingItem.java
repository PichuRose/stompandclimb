package pichurose.stompandclimb.items.Rings;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

public class RustedShrinkingRingItem extends ShrinkingRingItem {
    public RustedShrinkingRingItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity instanceof LivingEntity){
            ((LivingEntity)entity).addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 20, 0, true, false, false));
        }
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        if (!FlanUtils.canInteract(user, user, FlanUtils.SIZE_CHANGING)) { return super.use(world, user, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 0.91700404320467123174354159479414f);
        user.addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 12000, 0, true, false, false));
        user.getCooldowns().addCooldown(this, 20);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(user.getId());
        buf.writeFloat(0.91700404320467123174354159479414f);
        ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, buf);
        return super.use(world, user, hand);
    }

}
