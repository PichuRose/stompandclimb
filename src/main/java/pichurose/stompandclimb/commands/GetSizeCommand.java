package pichurose.stompandclimb.commands;


import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import pichurose.stompandclimb.utils.ResizingUtils;

public class GetSizeCommand {
    public static int executeCommand(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayer();
        if(player == null){
            return 0;
        }
        float sacSize = ResizingUtils.getSize(player);
        float realSize = ResizingUtils.getActualSize(player);
        player.displayClientMessage(Component.literal(sacSize+"x (Stomp & Climb) / "+realSize+"x (Real Scale)"), false);
        return 1;
    }
}
