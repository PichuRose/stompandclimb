package pichurose.stompandclimb;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.Commands;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import pichurose.stompandclimb.commands.StompAndClimbCustomCarryCommand;
import pichurose.stompandclimb.effects.CurseOfShrinkingEffect;
import pichurose.stompandclimb.effects.GrowEffect;
import pichurose.stompandclimb.effects.ShrinkEffect;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.items.*;
import pichurose.stompandclimb.materials.HardHatMaterial;
import pichurose.stompandclimb.materials.HoverBootsMaterial;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.PehkuiSupport;

public class StompAndClimb implements ModInitializer {
    public static final String MODID = "stompandclimb";

    public static final Item HARD_HAT = registerItem("hard_hat", new ArmorItem(new HardHatMaterial(), ArmorItem.Type.HELMET, new Item.Properties()));
    public static final Item SOFT_SOCKS = registerItem("soft_socks", new SoftSocksItem(new Item.Properties()));
    public static final Item HOVER_BOOTS = registerItem("hover_boots", new HoverBootsItem(new HoverBootsMaterial(), ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final Item NETHERITE_RING = registerItem("netherite_ring", new RingItem(new Item.Properties(), 16));
    public static final Item AMETHYST_RING = registerItem("amethyst_ring", new RingItem(new Item.Properties(), 8));
    public static final Item DIAMOND_RING = registerItem("diamond_ring", new RingItem(new Item.Properties(), 4));
    public static final Item EMERALD_RING = registerItem("emerald_ring", new RingItem(new Item.Properties(), 2));
    public static final Item QUARTZ_RING = registerItem("quartz_ring", new RingItem(new Item.Properties(), 1));
    public static final Item NETHERBRICK_RING = registerItem("netherbrick_ring", new RingItem(new Item.Properties(), .5f));
    public static final Item SEA_RING = registerItem("sea_ring", new RingItem(new Item.Properties(), .25f));
    public static final Item REDSTONE_RING = registerItem("redstone_ring", new RingItem(new Item.Properties(), .125f));
    public static final Item SPAWNER_RING = registerItem("spawner_ring", new RingItem(new Item.Properties(), .0625f));
    public static final Item COPPER_RING = registerItem("copper_ring", new CopperRingItem(new Item.Properties()));
    public static final Item RUSTED_RING = registerItem("rusted_ring", new RustedCopperRingItem(new Item.Properties()));

    public static final Item NETHERITE_COLLAR = registerItem("netherite_collar", new CollarItem(new Item.Properties(), 16));
    public static final Item AMETHYST_COLLAR = registerItem("amethyst_collar", new CollarItem(new Item.Properties(), 8));
    public static final Item DIAMOND_COLLAR = registerItem("diamond_collar", new CollarItem(new Item.Properties(), 4));
    public static final Item EMERALD_COLLAR = registerItem("emerald_collar", new CollarItem(new Item.Properties(), 2));
    public static final Item QUARTZ_COLLAR = registerItem("quartz_collar", new CollarItem(new Item.Properties(), 1));
    public static final Item NETHERBRICK_COLLAR = registerItem("netherbrick_collar", new CollarItem(new Item.Properties(), .5f));
    public static final Item SEA_COLLAR = registerItem("sea_collar", new CollarItem(new Item.Properties(), .25f));
    public static final Item REDSTONE_COLLAR = registerItem("redstone_collar", new CollarItem(new Item.Properties(), .125f));
    public static final Item SPAWNER_COLLAR = registerItem("spawner_collar", new CollarItem(new Item.Properties(), .0625f));
    public static final Item COPPER_COLLAR = registerItem("copper_collar", new CopperCollarItem(new Item.Properties()));
    public static final Item RUSTED_COLLAR = registerItem("rusted_collar", new RustedCopperCollarItem(new Item.Properties()));

    public static final MobEffect CURSE_OF_SHRINKING = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "curse_of_shrinking"), new CurseOfShrinkingEffect());
    public static final MobEffect GROW_EFFECT = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "grow_effect"), new GrowEffect());
    public static final MobEffect SHRINK_EFFECT = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "shrink_effect"), new CurseOfShrinkingEffect());

    private static final CreativeModeTab ITEM_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(MODID, "ringsandgear"), FabricItemGroup.builder()
            .icon(() -> new ItemStack(SPAWNER_RING))
            .title(Component.translatable("itemGroup.stompandclimb.ringsandgear"))
            .displayItems((context, entries) -> {
                entries.accept(HARD_HAT);
                entries.accept(SOFT_SOCKS);
                entries.accept(HOVER_BOOTS);
                entries.accept(NETHERITE_RING);
                entries.accept(AMETHYST_RING);
                entries.accept(DIAMOND_RING);
                entries.accept(EMERALD_RING);
                entries.accept(QUARTZ_RING);
                entries.accept(NETHERBRICK_RING);
                entries.accept(SEA_RING);
                entries.accept(REDSTONE_RING);
                entries.accept(SPAWNER_RING);
                entries.accept(COPPER_RING);
                entries.accept(RUSTED_RING);
                entries.accept(NETHERITE_COLLAR);
                entries.accept(AMETHYST_COLLAR);
                entries.accept(DIAMOND_COLLAR);
                entries.accept(EMERALD_COLLAR);
                entries.accept(QUARTZ_COLLAR);
                entries.accept(NETHERBRICK_COLLAR);
                entries.accept(SEA_COLLAR);
                entries.accept(REDSTONE_COLLAR);
                entries.accept(SPAWNER_COLLAR);
                entries.accept(COPPER_COLLAR);
                entries.accept(RUSTED_COLLAR);
            })
            .build());

    public StompAndClimb() {
        registerSetup(this::setup);
    }

    public void registerSetup(Runnable common) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            ClientLifecycleEvents.CLIENT_STARTED.register((a) -> common.run());
        else ServerLifecycleEvents.SERVER_STARTED.register((a) -> common.run());
    }

    public static void handleKeyPressServer(Player player, int hitResultType, @Nullable Entity target, boolean smallEnough, Vec3 hitPos) {
        if (hitResultType == 1) {
            if (smallEnough) {
                assert target != null;
                if (target.getVehicle() == null) {
                    target.startRiding(player, true);
                }
            }
        } else if (hitResultType == 0) {
            for (Entity passenger : player.getPassengers()) {
                passenger.stopRiding();
                passenger.teleportTo(hitPos.x, hitPos.y, hitPos.z);
                passenger.setPose(Pose.STANDING);
            }
        }
    }

    public static void handleKeyPressClient(LocalPlayer player) {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        int hitResultType = -1;
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
        buf.writeDouble(hitPos.x);
        buf.writeDouble(hitPos.y);
        buf.writeDouble(hitPos.z);
        ClientPlayNetworking.send(StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET, buf);
    }

    public static Item registerItem(String string, Item item) {
        return registerItem(new ResourceLocation(MODID, string), item);
    }

    public static Item registerItem(ResourceLocation resourceLocation, Item item) {
        return registerItem(ResourceKey.create(BuiltInRegistries.ITEM.key(), resourceLocation), item);
    }

    public static Item registerItem(ResourceKey<Item> resourceKey, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    @Override
    public void onInitialize() {
        StompAndClimbPotions.register();

        ServerPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET, (server, player, handler, buf, responseSender) -> {
            int hitResultType = buf.readInt();
            int targetId = buf.readInt();
            boolean smallEnough = buf.readBoolean();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            Entity target = targetId != -1 ? player.getServer().overworld().getEntity(targetId) : null;
            Vec3 hitPos = new Vec3(x, y, z);
            server.execute(() -> handleKeyPressServer(player, hitResultType, target, smallEnough, hitPos));
        });

        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            client.execute(() -> {
                CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface) (client.player);
                customCarryOffsetInterface.stompandclimb_updateCustomCarryCache(x, y, z);
            });
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("sac_cc")
                    .then(Commands.argument("x", DoubleArgumentType.doubleArg(-1, 1))
                            .then(Commands.argument("y", DoubleArgumentType.doubleArg(-2, 1))
                                    .then(Commands.argument("z", DoubleArgumentType.doubleArg(-1, 1))
                                            .executes(StompAndClimbCustomCarryCommand::executeCommandWithArg)))));
        });
    }

    private void setup() {
        PehkuiSupport.setup();
    }
}