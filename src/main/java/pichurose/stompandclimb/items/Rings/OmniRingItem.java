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
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.HashMap;
import java.util.UUID;

public class OmniRingItem extends Item{
    //private float SIZE = 1;
    private final HashMap<UUID, Float> sizeMap = new HashMap<>();
    public OmniRingItem(Item.Properties settings) {
        super(settings);
    }

    public void setSIZE(UUID uuid, float SIZE) {
        sizeMap.put(uuid, SIZE);
    }
    public float getSIZE(UUID uuid){
        return sizeMap.getOrDefault(uuid,1f);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return new ItemStack(StompAndClimb.OMNIRING);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(world.isClientSide) { return super.use(world, user, hand); }
        if (!FlanUtils.canInteract(user, user, FlanUtils.SIZE_CHANGING)) { return super.use(world, user, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.use(world, user, hand);
        }
        UUID uuid = user.getUUID();
        //LOGGER.debug("RingItem used on " + (world.isClientSide ? "client" : "server"));
        if(ResizingUtils.getSize(user) != getSIZE(uuid)){
            ResizingUtils.setSize(user, getSIZE(uuid));
            user.getCooldowns().addCooldown(this, 20);
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(user.getId());
            buf.writeFloat(getSIZE(uuid));
            ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, buf);
        }
        return super.use(world, user, hand);
    }
}
