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
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.HashMap;
import java.util.UUID;

public class OmniCollarItem extends Item {

    private final HashMap<UUID, Float> sizeMap = new HashMap<>();
    public OmniCollarItem(Item.Properties settings) {
        super(settings);
    }

    public void setSIZE(UUID uuid, float SIZE) {
        sizeMap.put(uuid, SIZE);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return new ItemStack(StompAndClimb.OMNICOLLAR);
    }

    public float getSIZE(UUID uuid){
        return sizeMap.getOrDefault(uuid,1f);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if(user.level().isClientSide) { return super.interactLivingEntity(stack, user, entity, hand); }
        //if (!ClaimHandler.canInteract((ServerPlayer) user, entity.blockPosition(), ResourceLocation.of("stompandclimb:sizechanging", ':'))) { return super.interactLivingEntity(stack, user, entity, hand); }
        if (!FlanUtils.canInteract(user, entity, FlanUtils.SIZE_CHANGING)) { return super.interactLivingEntity(stack, user, entity, hand); }

        if(user.getCooldowns().isOnCooldown(this)){
            return super.interactLivingEntity(stack, user, entity, hand);
        }
        UUID uuid = user.getUUID();
        if(ResizingUtils.getSize(entity) != getSIZE(uuid)){
            ResizingUtils.setSize(entity, getSIZE(uuid));
            if(entity instanceof Player){
                user.getCooldowns().addCooldown(this, 200);
            }
            else{
                user.getCooldowns().addCooldown(this, 20);
            }
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(entity.getId());
            buf.writeFloat(getSIZE(uuid));
            if (user instanceof ServerPlayer) {
                ServerPlayNetworking.send((ServerPlayer) user, StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, buf);
            }
        }


        return super.interactLivingEntity(stack, user, entity, hand);
    }
}
