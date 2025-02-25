package pichurose.stompandclimb.items;

import io.github.flemmli97.flan.api.ClaimHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.ResizingUtils;

public class RustedGrowingRingItem extends ShrinkingRingItem {
    public RustedGrowingRingItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity != null && entity instanceof LivingEntity){
            ((LivingEntity)entity).addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_GROWING, 20, 0, true, false, false));
        }
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        if (!ClaimHandler.canInteract((ServerPlayer) user, user.blockPosition(), ResourceLocation.of("flan:sizechanging", ':'))) { return super.use(world, user, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 1.0905077326652576592070106557607f);
        user.addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_GROWING, 12000, 0, true, false, false));
        user.getCooldowns().addCooldown(this, 20);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(user.getId());
        buf.writeFloat(1.0905077326652576592070106557607f);
        ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, buf);
        return super.use(world, user, hand);
    }

    /*
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        resize_self = false;
        entity.addStatusEffect(new StatusEffectInstance(new CurseOfShrinkingEffect(),12000,0, true, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 1200, 0, true, false, false));
        return super.useOnEntity(stack, user, entity, hand);

    }*/
}
