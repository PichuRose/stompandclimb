package pichurose.stompandclimb.items;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pichurose.stompandclimb.utils.ResizingUtils;

public class CollarItem extends Item {

    private float SIZE = 1;

    public CollarItem(Item.Properties settings, float size) {
        super(settings);
        SIZE = size;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }
        ResizingUtils.setSize(entity, SIZE);
        if(entity instanceof Player){
            user.getCooldowns().addCooldown(this, 200);
        }
        else{
            user.getCooldowns().addCooldown(this, 20);
        }


        return super.interactLivingEntity(stack, user, entity, hand);
    }


}
