package pichurose.stompandclimb.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pichurose.stompandclimb.StompAndClimb;
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
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 1.0905077326652576592070106557607f);
        user.addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_GROWING, 12000, 0, true, false, false));
        user.getCooldowns().addCooldown(this, 20);
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
