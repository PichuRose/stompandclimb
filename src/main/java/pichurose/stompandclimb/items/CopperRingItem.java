package pichurose.stompandclimb.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;

public class CopperRingItem extends Item {
    public CopperRingItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 0.91700404320467123174354159479414f);
        user.getItemCooldownManager().set(this, 20);
        return super.use(world, user, hand);
    }
}
