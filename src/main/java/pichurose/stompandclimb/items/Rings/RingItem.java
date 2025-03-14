package pichurose.stompandclimb.items.Rings;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

public class RingItem extends Item {
    private final float SIZE;

    public RingItem(Properties settings, float size) {
        super(settings);
        SIZE = size;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        //if (!ClaimHandler.canInteract((ServerPlayer) user, user.blockPosition(), ResourceLocation.of("stompandclimb:sizechanging", ':'))) { return super.use(world, user, hand); }
        if (!FlanUtils.canInteract(user, user, FlanUtils.SIZE_CHANGING)) { return super.use(world, user, hand); }

        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        if(ResizingUtils.getSize(user) != SIZE){
            ResizingUtils.setSize(user, SIZE);
            user.getCooldowns().addCooldown(this, 20);
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(user.getId());
            buf.writeFloat(SIZE);
            ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, buf);
        }
        return super.use(world, user, hand);
    }
}
