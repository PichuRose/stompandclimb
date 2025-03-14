package pichurose.stompandclimb.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;

import java.util.Objects;

public class StompAndClimbCustomCarryCommand {

    private static final double[][] places = {
        {0,     0,      0},
        {0.5,   -1,     -0.5},
        {0.5,   -1,     0.5},
        {0.2,   -1.1,   0},
        {0.25,  -0.8,   0}
    };

    public static int getDominantHand(Player player){
        return player.getMainArm().equals(HumanoidArm.RIGHT) ? 1 : 2;
    }

    public static int executeCommandWithArg(CommandContext<CommandSourceStack> context) {
        double x = DoubleArgumentType.getDouble(context, "x");
        double y = DoubleArgumentType.getDouble(context, "y");
        double z = DoubleArgumentType.getDouble(context, "z");
        boolean holdOutHand = BoolArgumentType.getBool(context, "holdOutHand");
        boolean followBodyAngle = BoolArgumentType.getBool(context, "followBodyAngle");
        executeCarrying(x, y, z, context.getSource().getPlayer(), holdOutHand, followBodyAngle);
        context.getSource().sendSuccess(() -> Component.literal("Called /custom_carry with x = %s, y = %s, z = %s".formatted(x, y, z)), false);
        return 1;
    }

    public static int executeCommandWithStringArg(CommandContext<CommandSourceStack> context) {
        String place = context.getArgument("place", String.class);
        context.getSource().sendSuccess(() -> Component.literal("Called /command_with_string_arg with place = %s".formatted(place)), false);
        int placeLocation = switch (place.toLowerCase().strip()) {
            case "head", "hat", "hair", "default", "above" -> 0;
            case "right_hand", "right", "righty" -> 1;
            case "left_hand", "left", "lefty" -> 2;
            case "hand", "hands", "arm", "wrist", "uppies", "pickup", "grab", "yoink", "mine", "palm" -> getDominantHand(Objects.requireNonNull(context.getSource().getPlayer()));
            case "boob", "boobs", "chest", "bust", "booba", "tits", "tit", "breast", "breasts", "booby", "boobies", "badonkadonks" -> 3;
            case "mouth", "teeth", "lips", "tongue", "bite", "chew", "chewtoy", "maw", "snack", "food" -> 4;
            default -> 0;
        };

        boolean holdOutHand = placeLocation == getDominantHand(Objects.requireNonNull(context.getSource().getPlayer()));
        boolean followBodyAngle = placeLocation != 4;

        executeCarrying(places[placeLocation], context.getSource().getPlayer(), holdOutHand, followBodyAngle);
        return 1;
    }

    public static void executeCarrying(double[] relativeLocation, ServerPlayer player, boolean holdOutHand, boolean followBodyAngle){
        executeCarrying(relativeLocation[0], relativeLocation[1], relativeLocation[2], player, holdOutHand, followBodyAngle);
    }

    public static void executeCarrying(double x, double y, double z, ServerPlayer player, boolean holdOutHand, boolean followBodyAngle){

        CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface)(player);
        customCarryOffsetInterface.stompandclimb_updateCustomCarryCache(x, y, z, holdOutHand, followBodyAngle);


        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeBoolean(holdOutHand);
        buf.writeBoolean(followBodyAngle);
        ServerPlayNetworking.send(player, StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET, buf);

    }
}
