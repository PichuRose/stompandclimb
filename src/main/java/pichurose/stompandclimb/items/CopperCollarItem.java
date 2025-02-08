package pichurose.stompandclimb.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import pichurose.stompandclimb.utils.ResizingUtils;

public class CopperCollarItem extends Item {

    public CopperCollarItem(Settings settings) {
        super(settings);
    }



    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)){
            return super.useOnEntity(stack, user, entity, hand);
        }
        ResizingUtils.resizeInstant(entity, 0.91700404320467123174354159479414f);
        user.getItemCooldownManager().set(this, 20);
        return super.useOnEntity(stack, user, entity, hand);
    }


}
