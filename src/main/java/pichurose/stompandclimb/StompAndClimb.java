package pichurose.stompandclimb;

import com.ibm.icu.text.DisplayContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.Locals;
import oshi.hardware.Display;
import pichurose.stompandclimb.commands.StompAndClimbCustomCarryCommand;
import pichurose.stompandclimb.effects.CurseOfShrinkingEffect;
import pichurose.stompandclimb.effects.GrowEffect;
import pichurose.stompandclimb.effects.ShrinkEffect;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.items.*;
import pichurose.stompandclimb.materials.HardHatMaterial;
import pichurose.stompandclimb.materials.HoverBootsMaterial;
import pichurose.stompandclimb.materials.SoftSocksMaterial;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.PehkuiSupport;

import java.awt.*;
import java.util.function.Supplier;

public class StompAndClimb implements ModInitializer {
    public static final String MODID = "stompandclimb";


    /*public static final Item HARD_HAT = new ArmorItem(HARD_HAT_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item SOFT_SOCKS = new ArmorItem(SOFT_SOCKS_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings());
    public static final Item HOVER_BOOTS = new HoverBootsItem(HOVER_BOOTS_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings());

    public static final Item AMETHYST_RING = new RingItem(new Item.Settings(), 8);
    public static final Item DIAMOND_RING = new RingItem(new Item.Settings(), 4);
    public static final Item EMERALD_RING = new RingItem(new Item.Settings(), 2);
    public static final Item QUARTZ_RING = new RingItem(new Item.Settings(), 1);
    public static final Item SEA_RING = new RingItem(new Item.Settings(), .25f);
    public static final Item REDSTONE_RING = new RingItem(new Item.Settings(), .125f);
    public static final Item SPAWNER_RING = new RingItem(new Item.Settings(), .0625f);

    public static final Item COPPER_RING = new CopperRingItem(new Item.Settings());
    public static final Item RUSTED_RING = new RustedCopperRingItem(new Item.Settings());

    public static final Item DINI = new Item(new Item.Settings().food(FoodComponents.COOKIE));
    public static final Item GOLDEN_DINI = new Item(new Item.Settings().food(FoodComponents.CARROT));

    public static final StatusEffect CURSE_OF_SHRINKING = new CurseOfShrinkingEffect();
    public static final StatusEffect GROW_EFFECT = new GrowEffect();
    public static final StatusEffect SHRINK_EFFECT = new ShrinkEffect();

    public static final Potion GROW_POTION =
            Registry.register(Registries.POTION, new Identifier(StompAndClimb.MODID, "grow_potion"),
                    new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 3600, 0)));

    public static final Potion SHRINK_POTION =
            Registry.register(Registries.POTION, new Identifier(StompAndClimb.MODID, "shrink_potion"),
                    new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 3600, 0)));
    */


    public static final Item HARD_HAT = new ArmorItem(new HardHatMaterial(), ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item SOFT_SOCKS = new SoftSocksItem(new Item.Settings());
    public static final Item HOVER_BOOTS = new HoverBootsItem(new HoverBootsMaterial(), ArmorItem.Type.BOOTS, new Item.Settings());

    public static final Item NETHERITE_RING = new RingItem(new Item.Settings(),16);
    public static final Item AMETHYST_RING = new RingItem(new Item.Settings(),8);
    public static final Item DIAMOND_RING = new RingItem(new Item.Settings(),4);
    public static final Item EMERALD_RING = new RingItem(new Item.Settings(),2);
    public static final Item QUARTZ_RING = new RingItem(new Item.Settings(),1);
    public static final Item NETHERBRICK_RING = new RingItem(new Item.Settings(),.5f);
    public static final Item SEA_RING = new RingItem(new Item.Settings(),.25f);
    public static final Item REDSTONE_RING = new RingItem(new Item.Settings(),.125f);
    public static final Item SPAWNER_RING = new RingItem(new Item.Settings(),.0625f);
    public static final Item COPPER_RING = new CopperRingItem(new Item.Settings());
    public static final Item RUSTED_RING = new RustedCopperRingItem(new Item.Settings());

    public static final Item NETHERITE_COLLAR = new CollarItem(new Item.Settings(), 16);
    public static final Item AMETHYST_COLLAR = new CollarItem(new Item.Settings(), 8);
    public static final Item DIAMOND_COLLAR = new CollarItem(new Item.Settings(), 4);
    public static final Item EMERALD_COLLAR = new CollarItem(new Item.Settings(), 2);
    public static final Item QUARTZ_COLLAR = new CollarItem(new Item.Settings(), 1);
    public static final Item NETHERBRICK_COLLAR = new CollarItem(new Item.Settings(), .5f);
    public static final Item SEA_COLLAR = new CollarItem(new Item.Settings(), .25f);
    public static final Item REDSTONE_COLLAR = new CollarItem(new Item.Settings(), .125f);
    public static final Item SPAWNER_COLLAR = new CollarItem(new Item.Settings(), .0625f);
    public static final Item COPPER_COLLAR = new CopperCollarItem(new Item.Settings());
    public static final Item RUSTED_COLLAR = new RustedCopperCollarItem(new Item.Settings());





    public static final StatusEffect CURSE_OF_SHRINKING = new CurseOfShrinkingEffect();
    public static final StatusEffect GROW_EFFECT = new GrowEffect();
    public static final StatusEffect SHRINK_EFFECT = new ShrinkEffect();

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SPAWNER_RING))
            .displayName(Text.translatable("itemGroup.stompandclimb.ringsandgear"))
            .entries((context, entries) -> {
                entries.add(HARD_HAT);
                entries.add(SOFT_SOCKS);
                entries.add(HOVER_BOOTS);
                entries.add(NETHERITE_RING);
                entries.add(AMETHYST_RING);
                entries.add(DIAMOND_RING);
                entries.add(EMERALD_RING);
                entries.add(QUARTZ_RING);
                entries.add(NETHERBRICK_RING);
                entries.add(SEA_RING);
                entries.add(REDSTONE_RING);
                entries.add(SPAWNER_RING);
                entries.add(COPPER_RING);
                entries.add(RUSTED_RING);
                entries.add(NETHERITE_COLLAR);
                entries.add(AMETHYST_COLLAR);
                entries.add(DIAMOND_COLLAR);
                entries.add(EMERALD_COLLAR);
                entries.add(QUARTZ_COLLAR);
                entries.add(NETHERBRICK_COLLAR);
                entries.add(SEA_COLLAR);
                entries.add(REDSTONE_COLLAR);
                entries.add(SPAWNER_COLLAR);
                entries.add(COPPER_COLLAR);
                entries.add(RUSTED_COLLAR);
            })
            .build();

    public StompAndClimb() {
        registerSetup(this::setup);
    }

    //@Override
    public void registerSetup(Runnable common) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            ClientLifecycleEvents.CLIENT_STARTED.register((a) -> common.run());
        else ServerLifecycleEvents.SERVER_STARTED.register((a) -> common.run());


    }

    public static void handleKeyPressServer(PlayerEntity player, int hitResultType, @Nullable Entity target, boolean smallEnough, Vec3d hitPos){
        if (hitResultType == 1) {
            if (smallEnough) {
                assert target != null;
                if (target.getVehicle() == null) {
                    target.startRiding(player, true);

                }
            }
        } else if (hitResultType == 0) {
            for (Entity passenger : player.getPassengerList()) {
                passenger.stopRiding();
                passenger.teleport(hitPos.x, hitPos.y, hitPos.z);
                passenger.setPose(EntityPose.STANDING);
            }
        }
    }


    public static void handleKeyPressClient(PlayerEntity player) {
        HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
        int hitResultType = -1;
        if(hitResult == null)
            hitResultType = 0;
        else
            hitResultType = hitResult.getType() == HitResult.Type.ENTITY ? 1 : 0;
        Entity target = null;
        boolean smallEnough = false;
        if (hitResultType == 1) {
            target = ((EntityHitResult) hitResult).getEntity();
            smallEnough = (player.getDimensions(EntityPose.STANDING).height / target.getDimensions(EntityPose.STANDING).height) >= 3;

        }
        Vec3d hitPos = null;
        if(hitResult != null)
            hitPos = hitResult.getPos();

        if (hitResultType == 1) {
            if (smallEnough) {
                if (target.getVehicle() == null) {
                    target.startRiding(player, true);
                    //target.setPosition(player.getX(), player.getY()-1, player.getZ());
                }
            }
        } else if (hitPos != null) {
            for (Entity passenger : player.getPassengerList()) {
                passenger.stopRiding();
                passenger.teleport(hitPos.x, hitPos.y, hitPos.z);
                passenger.setPose(EntityPose.STANDING);
            }
        }


        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(hitResultType);
        buf.writeInt(target != null ? target.getId() : -1);
        buf.writeBoolean(smallEnough);
        buf.writeDouble(hitPos.x);
        buf.writeDouble(hitPos.y);
        buf.writeDouble(hitPos.z);
        //StompAndClimbForge.CHANNEL.sendToServer(new PickupTeleportPacket(buf));
        assert StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET != null;
        ClientPlayNetworking.send(StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET, buf);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier(MODID, "hard_hat"), HARD_HAT);
        Registry.register(Registries.ITEM, new Identifier(MODID, "soft_socks"), SOFT_SOCKS);
        Registry.register(Registries.ITEM, new Identifier(MODID, "hover_boots"), HOVER_BOOTS);
        Registry.register(Registries.ITEM, new Identifier(MODID, "netherite_ring"), NETHERITE_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "amethyst_ring"), AMETHYST_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "diamond_ring"), DIAMOND_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "emerald_ring"), EMERALD_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "quartz_ring"), QUARTZ_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "netherbrick_ring"), NETHERBRICK_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "sea_ring"), SEA_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "redstone_ring"), REDSTONE_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "spawner_ring"), SPAWNER_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "copper_ring"), COPPER_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "rusted_ring"), RUSTED_RING);
        Registry.register(Registries.ITEM, new Identifier(MODID, "netherite_collar"), NETHERITE_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "amethyst_collar"), AMETHYST_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "diamond_collar"), DIAMOND_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "emerald_collar"), EMERALD_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "quartz_collar"), QUARTZ_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "netherbrick_collar"), NETHERBRICK_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "sea_collar"), SEA_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "redstone_collar"), REDSTONE_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "spawner_collar"), SPAWNER_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "copper_collar"), COPPER_COLLAR);
        Registry.register(Registries.ITEM, new Identifier(MODID, "rusted_collar"), RUSTED_COLLAR);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MODID, "curse_of_shrinking"), CURSE_OF_SHRINKING);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MODID, "grow_effect"), GROW_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MODID, "shrink_effect"), SHRINK_EFFECT);

        //StompAndClimbPotions.registerPotions();

        /*ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.add(new ItemStack((ItemConvertible) GROW_POTION));
        });*/

        //brown shrinks, red grows



        Registry.register(Registries.ITEM_GROUP, Identifier.of(MODID, "ringsandgear"), ITEM_GROUP);
        StompAndClimbPotions.register();

        assert StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET != null;
        ServerPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.PICKUP_TELEPORT_PACKET, (server, player, handler, buf, responseSender) -> {
            int hitResultType = buf.readInt();
            int targetId = buf.readInt();
            boolean smallEnough = buf.readBoolean();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            Entity target = targetId != -1 ? player.getServerWorld().getEntityById(targetId) : null;
            Vec3d hitPos = new Vec3d(x, y, z);
            server.execute(() -> handleKeyPressServer(player, hitResultType, target, smallEnough, hitPos));
        });

        assert StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET != null;
        ClientPlayNetworking.registerGlobalReceiver(StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            // Read packet data on the event loop
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            client.execute(() -> {
                CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface)(client.player);
                customCarryOffsetInterface.stompandclimb_updateCustomCarryCache(x, y, z);
            });
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("sac_cc")
                    .then(CommandManager.argument("x", DoubleArgumentType.doubleArg(-1, 1))
                            .then(CommandManager.argument("y", DoubleArgumentType.doubleArg(-2, 1))
                                    .then(CommandManager.argument("z", DoubleArgumentType.doubleArg(-1, 1))
                    .executes(StompAndClimbCustomCarryCommand::executeCommandWithArg)))));
        });
    }

    private void setup() {
        PehkuiSupport.setup();

    }
}
