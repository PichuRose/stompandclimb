package pichurose.stompandclimb.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;

public class RustedCopperCollarItem extends Item {

    public RustedCopperCollarItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity != null && entity instanceof LivingEntity){
            ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 20, 0, true, false, false));
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)){
            return super.useOnEntity(stack, user, entity, hand);
        }
        ResizingUtils.resizeInstant(entity, 0.91700404320467123174354159479414f);
        entity.addStatusEffect(new StatusEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 12000, 0, true, false, false));
        user.getItemCooldownManager().set(this, 20);
        return super.useOnEntity(stack, user, entity, hand);
    }


}
