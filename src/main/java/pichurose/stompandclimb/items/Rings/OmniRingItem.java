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

public class OmniRingItem extends Item{
    private float SIZE = 1;

    public OmniRingItem(Item.Properties settings) {
        super(settings);
    }

    public void setSIZE(float SIZE) {
        this.SIZE = SIZE;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
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
