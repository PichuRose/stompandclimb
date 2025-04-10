package pichurose.stompandclimb.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import pichurose.stompandclimb.items.Collars.OmniCollarItem;
import pichurose.stompandclimb.items.Rings.OmniRingItem;

import java.util.UUID;

public class OmniSizeSetCommand {
    public static int executeCommandWithArg(CommandContext<CommandSourceStack> context) {
        float size = FloatArgumentType.getFloat(context, "size");
        Player player = context.getSource().getPlayer();
        if(player == null){
            return 0;
        }
        UUID uuid = player.getUUID();
        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof OmniRingItem omniring){
            omniring.setSIZE(uuid, size);
            player.displayClientMessage(Component.literal("Setting size of Omniring to "+size), true);
        }
        else if(heldItem.getItem() instanceof OmniCollarItem omnicollar){
            omnicollar.setSIZE(uuid, size);
            player.displayClientMessage(Component.literal("Setting size of Omnicollar to "+size), true);
        }
        return 1;
    }
}
