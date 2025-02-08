package pichurose.stompandclimb.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.PehkuiSupport;

public class StompAndClimbClient implements ClientModInitializer {
    private static KeyBinding pickupPlacedown;
    @Override
    public void onInitializeClient() {
        //PehkuiSupport.setup();
        pickupPlacedown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.stompandclimbforge.pickupplacedown", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_END, // The keycode of the key
                "key.categories.stompandclimbforge" // The translation key of the keybinding's category.
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (pickupPlacedown.wasPressed()) {
                //client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
                StompAndClimb.handleKeyPressClient(client.player);
            }
        });
    }


}
