package pichurose.stompandclimb.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pichurose.stompandclimb.utils.ResizingUtils;

public class GrowingCollarItem extends Item {

    public GrowingCollarItem(Properties settings) {
        super(settings);
    }



    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }
        ResizingUtils.resizeInstant(entity, 1.0905077326652576592070106557607f);
        user.getCooldowns().addCooldown(this, 20);
        return super.interactLivingEntity(stack, user, entity, hand);
    }


}
