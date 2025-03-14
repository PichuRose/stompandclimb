package pichurose.stompandclimb.items.Rings;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

public class GrowingRingItem extends Item {
    public GrowingRingItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        if (!FlanUtils.canInteract(user, user, FlanUtils.SIZE_CHANGING)) { return super.use(world, user, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        ResizingUtils.resizeInstant(user, 1.0442737824274138403219664787399f);
        user.getCooldowns().addCooldown(this, 20);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(user.getId());
        buf.writeFloat(1.0442737824274138403219664787399f);
        ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, buf);
        return super.use(world, user, hand);
    }
}
