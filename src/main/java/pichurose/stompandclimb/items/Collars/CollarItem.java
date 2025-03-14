package pichurose.stompandclimb.items.Collars;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

public class CollarItem extends Item {

    private final float SIZE;

    public CollarItem(Item.Properties settings, float size) {
        super(settings);
        SIZE = size;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.level().isClientSide) { return super.interactLivingEntity(stack, user, entity, hand); }

        if (!FlanUtils.canInteract(user, entity, FlanUtils.SIZE_CHANGING)) { return super.interactLivingEntity(stack, user, entity, hand); }
        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }

        if(ResizingUtils.getSize(entity) != SIZE){
            ResizingUtils.setSize(entity, SIZE);
            if(entity instanceof Player){
                user.getCooldowns().addCooldown(this, 200);
            }
            else{
                user.getCooldowns().addCooldown(this, 20);
            }


            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(entity.getId());
            buf.writeFloat(SIZE);
            if (user instanceof ServerPlayer) {
                ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, buf);
            }
            //ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, buf);
        }


        return super.interactLivingEntity(stack, user, entity, hand);
    }


}
