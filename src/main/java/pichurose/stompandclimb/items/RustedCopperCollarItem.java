package pichurose.stompandclimb.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;



public class RustedCopperCollarItem extends Item {

    public RustedCopperCollarItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity != null && entity instanceof LivingEntity){
            ((LivingEntity)entity).addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 20, 0, true, false, false));
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }
        ResizingUtils.resizeInstant(entity, 0.91700404320467123174354159479414f);
        entity.addEffect(new MobEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 12000, 0, true, false, false));
        user.getCooldowns().addCooldown(this, 20);
        return super.interactLivingEntity(stack, user, entity, hand);
    }


}
