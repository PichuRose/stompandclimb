package pichurose.stompandclimb.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;

public class StompAndClimbCustomCarryCommand {
    public static int executeCommandWithArg(CommandContext<ServerCommandSource> context) {
        //int value = IntegerArgumentType.getInteger(context, "value");
        //context.getSource().sendFeedback(() -> Text.literal("Called /command_with_arg with value = %s".formatted(value)), false);
        double x = DoubleArgumentType.getDouble(context, "x");
        double y = DoubleArgumentType.getDouble(context, "y");
        double z = DoubleArgumentType.getDouble(context, "z");

        context.getSource().sendFeedback(() -> Text.literal("Called /custom_carry with x = %s, y = %s, z = %s".formatted(x, y, z)), false);

        CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface)(context.getSource().getPlayer());
        customCarryOffsetInterface.stompandclimb_updateCustomCarryCache(x, y, z);


        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        assert StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET != null;
        ServerPlayNetworking.send(context.getSource().getPlayer(), StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET, buf);

        return 1;
    }
}
