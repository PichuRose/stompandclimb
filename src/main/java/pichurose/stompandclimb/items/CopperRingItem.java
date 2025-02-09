package pichurose.stompandclimb.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;

public class CopperRingItem extends Item {
    public CopperRingItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 0.91700404320467123174354159479414f);
        user.getCooldowns().addCooldown(this, 20);
        return super.use(world, user, hand);
    }
}
