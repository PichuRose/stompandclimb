package pichurose.stompandclimb.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StompAndClimbNetworkingConstants {
    public static final ResourceLocation PICKUP_TELEPORT_PACKET = ResourceLocation.of("stompandclimb", 'a');
    public static final ResourceLocation CUSTOM_CARRY_POS_CLIENT_PACKET = ResourceLocation.of("stompandclimb", 'b');
    public static final ResourceLocation SIZE_CHANGE_CLIENT_PACKET = ResourceLocation.of("stompandclimb", 'c');
    public static final ResourceLocation SIZE_RESIZE_CLIENT_PACKET = ResourceLocation.of("stompandclimb", 'd');
}
