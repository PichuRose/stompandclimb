package pichurose.stompandclimb.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pichurose.stompandclimb.utils.ResizingUtils;

public class OmniRingItem extends Item{
    private float SIZE = 1;

    public OmniRingItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        if(ResizingUtils.getSize(user) != SIZE){
            ResizingUtils.setSize(user, SIZE);
            user.getCooldowns().addCooldown(this, 20);
        }
        return super.use(world, user, hand);
    }
}
