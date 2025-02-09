package pichurose.stompandclimb.items;

import jdk.jshell.Snippet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.List;

public class RingItem extends Item {
    /*
        private float SIZE = 1;
    private static final Logger LOGGER = LogManager.getLogger();

    public RingItem(Properties properties, float size) {
        super(properties);
        SIZE = size;
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
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        ResizingUtils.setSize(user, SIZE);
        user.getCooldowns().addCooldown(this, 20);
        return super.use(world, user, hand);
    }
     */
    private float SIZE = 1;

    public RingItem(Properties settings, float size) {
        super(settings);
        SIZE = size;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        ResizingUtils.setSize(user, SIZE);
        user.getCooldowns().addCooldown(this, 20);
        return super.use(world, user, hand);
    }

    /*@Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.hasStatusEffect(StatusEffects.LUCK)){
            resize_self = false;
            ResizingUtils.setSize(entity, SIZE);
            if(entity instanceof PlayerEntity) {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 1200, 0, true, false, false));
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }*/
}
