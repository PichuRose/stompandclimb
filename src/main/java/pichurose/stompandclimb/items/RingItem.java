package pichurose.stompandclimb.items;

import jdk.jshell.Snippet;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
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

    public RingItem(Settings settings, float size) {
        super(settings);
        SIZE = size;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)){
            return super.use(world, user, hand);
        }
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        ResizingUtils.setSize(user, SIZE);
        user.getItemCooldownManager().set(this, 20);
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
