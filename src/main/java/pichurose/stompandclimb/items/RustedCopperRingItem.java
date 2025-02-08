package pichurose.stompandclimb.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.effects.CurseOfShrinkingEffect;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Random;

public class RustedCopperRingItem extends CopperRingItem {
    public RustedCopperRingItem(Settings settings) {
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 0.91700404320467123174354159479414f);
        user.addStatusEffect(new StatusEffectInstance(StompAndClimb.CURSE_OF_SHRINKING, 1200, 0, true, false, false));
        user.getItemCooldownManager().set(this, 20);
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
