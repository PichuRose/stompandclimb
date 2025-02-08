package pichurose.stompandclimb.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import pichurose.stompandclimb.utils.ResizingUtils;

public class CollarItem extends Item {

    private float SIZE = 1;

    public CollarItem(Settings settings, float size) {
        super(settings);
        SIZE = size;
    }



    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)){
            return super.useOnEntity(stack, user, entity, hand);
        }
        ResizingUtils.setSize(entity, SIZE);
        if(entity instanceof PlayerEntity){
            user.getItemCooldownManager().set(this, 200);
        }
        else{
            user.getItemCooldownManager().set(this, 20);
        }


        return super.useOnEntity(stack, user, entity, hand);
    }


}
