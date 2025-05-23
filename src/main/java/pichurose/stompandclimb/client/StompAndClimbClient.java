package pichurose.stompandclimb.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import pichurose.stompandclimb.interfaces.ClientInformationInterface;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Objects;
import java.util.UUID;

public class StompAndClimbClient implements ClientModInitializer {
    private static KeyMapping pickupPlacedown;
    private static KeyMapping collectItemsWhileBig;

    private boolean allowToCollectWhileBig = true;

    @Override
    public void onInitializeClient() {
        //PehkuiSupport.setup();
        pickupPlacedown = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.stompandclimb.pickupplacedown", // The translation key of the KeyMapping's name
                InputConstants.Type.KEYSYM, // The type of the KeyMapping, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_END, // The keycode of the key
                "key.categories.stompandclimb" // The translation key of the KeyMapping's category.
        ));
        collectItemsWhileBig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.stompandclimb.collectitemswhilebig", // The translation key of the KeyMapping's name
                InputConstants.Type.KEYSYM, // The type of the KeyMapping, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_INSERT, // The keycode of the key
                "key.categories.stompandclimb" // The translation key of the KeyMapping's category.
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (pickupPlacedown.consumeClick()) {
                //client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
                if (client.player != null) {
                    StompAndClimbClient.handlePickUpPlaceDownKeyPressClient(client.player);
                }
            }
            while (collectItemsWhileBig.consumeClick()) {
                //client.player.sendMessage(Text.literal("Key 2 was pressed!"), false);
                if (client.player != null) {
                    StompAndClimbClient.handleCollectItemsWhileBigKeyPressClient(client.player);
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            boolean holdOutHand = buf.readBoolean();
            boolean followBodyAngle = buf.readBoolean();
            boolean invisiblePassengers = buf.readBoolean();
            client.execute(() -> {
                CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface) (client.player);
                Objects.requireNonNull(customCarryOffsetInterface).stompandclimb_updateCustomCarryCache(x, y, z, holdOutHand, followBodyAngle, invisiblePassengers);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_SENDALL_PACKET, (client, handler, buf, responseSender) -> {
            String uuid = buf.readUtf();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            boolean holdOutHand = buf.readBoolean();
            boolean followBodyAngle = buf.readBoolean();
            boolean invisiblePassengers = buf.readBoolean();
            client.execute(() -> {
                assert client.level != null;
                CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface) (client.level.getPlayerByUUID(UUID.fromString(uuid)));
                Objects.requireNonNull(customCarryOffsetInterface).stompandclimb_updateCustomCarryCache(x, y, z, holdOutHand, followBodyAngle, invisiblePassengers);
            });
        });


        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.SIZE_CHANGE_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            int targetId = buf.readInt();
            float size = buf.readFloat();

            if (client.player != null) {
                //final Entity target = targetId != -1 ? client.player.getServer().getLevel(client.player.level().dimension()).getEntity(targetId) : null;
                final Minecraft minecraftInstance = Minecraft.getInstance();
                final var singleplayerServer = minecraftInstance.getSingleplayerServer();
                if (singleplayerServer != null) {
                    final var level = singleplayerServer.getLevel(client.player.level().dimension());
                    if (level == null){
                        final Entity target = targetId != -1 ? level.getEntity(targetId) : null;
                        client.execute(() -> {
                            if(target != null){
                                ResizingUtils.setSize(target, size);
                            }
                        });
                    }
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.SIZE_RESIZE_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            int targetId = buf.readInt();
            float size = buf.readFloat();

            if (client.player != null) {
                //final Entity target = targetId != -1 ? client.player.getServer().getLevel(client.player.level().dimension()).getEntity(targetId) : null;
                final Minecraft minecraftInstance = Minecraft.getInstance();
                final var singleplayerServer = minecraftInstance.getSingleplayerServer();
                if (singleplayerServer != null) {
                    final var level = singleplayerServer.getLevel(client.player.level().dimension());
                    if (level == null){
                        final Entity target = targetId != -1 ? level.getEntity(targetId) : null;
                        client.execute(() -> {
                            if(target != null){
                                ResizingUtils.setSize(target, size);
                            }
                        });
                    }
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.UPDATE_IS_ALLOWED_TO_CLIMB_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            boolean isAllowedToClimb = buf.readBoolean();

            if (client.player != null) {
                //final Entity target = targetId != -1 ? client.player.getServer().getLevel(client.player.level().dimension()).getEntity(targetId) : null;
                client.execute(() -> {
                    if(client.player != null){
                        ClientInformationInterface clientInformationInterface = (ClientInformationInterface)client.player;
                        clientInformationInterface.stompandclimb_updateIsAllowedToClimb(isAllowedToClimb);
                    }
                });
            }
        });
    }

    public static void handlePickUpPlaceDownKeyPressClient(LocalPlayer player) {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        int hitResultType;
        if (hitResult == null)
            hitResultType = 0;
        else
            hitResultType = hitResult.getType() == HitResult.Type.ENTITY ? 1 : 0;
        Entity target = null;
        boolean smallEnough = false;
        if (hitResultType == 1) {
            target = ((EntityHitResult) hitResult).getEntity();
            smallEnough = (player.getDimensions(Pose.STANDING).height / target.getDimensions(Pose.STANDING).height) >= 3;
        }
        Vec3 hitPos = null;
        if (hitResult != null)
            hitPos = hitResult.getLocation();

        if (hitResultType == 1) {
            if (smallEnough) {
                if (target.getVehicle() == null) {
                    target.startRiding(player, true);
                }
            }
        } else if (hitPos != null) {
            for (Entity passenger : player.getPassengers()) {
                passenger.stopRiding();
                passenger.teleportTo(hitPos.x, hitPos.y, hitPos.z);
                passenger.setPose(Pose.STANDING);
            }
        }

        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(hitResultType);
        buf.writeInt(target != null ? target.getId() : -1);
        buf.writeBoolean(smallEnough);
        buf.writeDouble(Objects.requireNonNull(hitPos).x);
        buf.writeDouble(hitPos.y);
        buf.writeDouble(hitPos.z);
        ClientPlayNetworking.send(StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET, buf);
    }

    public static void handleCollectItemsWhileBigKeyPressClient(LocalPlayer player) {
        ClientInformationInterface clientInfoInterface = ((ClientInformationInterface)player);
        boolean isAllowedToCollect = clientInfoInterface.stompandclimb_getIsAllowedToCollect();
        clientInfoInterface.stompandclimb_updateIsAllowedToCollect(!isAllowedToCollect);
        player.displayClientMessage(Component.literal("isAllowedToCollect = "+!isAllowedToCollect), true);

        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(!isAllowedToCollect);
        ClientPlayNetworking.send(StompAndClimbNetworkingConstants.UPDATE_IS_ALLOWED_TO_COLLECT_ITEMS_WHILE_BIG_PACKET, buf);
    }




}
