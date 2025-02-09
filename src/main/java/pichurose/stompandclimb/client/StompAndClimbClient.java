package pichurose.stompandclimb.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.PehkuiSupport;

public class StompAndClimbClient implements ClientModInitializer {
    private static KeyMapping pickupPlacedown;
    @Override
    public void onInitializeClient() {
        //PehkuiSupport.setup();
        pickupPlacedown = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.stompandclimbforge.pickupplacedown", // The translation key of the KeyMapping's name
                InputConstants.Type.KEYSYM, // The type of the KeyMapping, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_END, // The keycode of the key
                "key.categories.stompandclimbforge" // The translation key of the KeyMapping's category.
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (pickupPlacedown.consumeClick()) {
                //client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
                StompAndClimb.handleKeyPressClient(client.player);
            }
        });
    }


}
