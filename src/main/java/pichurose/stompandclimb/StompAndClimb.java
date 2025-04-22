package pichurose.stompandclimb;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import pichurose.stompandclimb.commands.GetSizeCommand;
import pichurose.stompandclimb.commands.OmniSizeSetCommand;
import pichurose.stompandclimb.commands.StompAndClimbCustomCarryCommand;
import pichurose.stompandclimb.effects.CurseOfGrowingEffect;
import pichurose.stompandclimb.effects.CurseOfShrinkingEffect;
import pichurose.stompandclimb.effects.GrowEffect;
import pichurose.stompandclimb.effects.ShrinkEffect;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.items.Armor.HoverBootsItem;
import pichurose.stompandclimb.items.Armor.SoftSocksItem;
import pichurose.stompandclimb.items.Collars.*;
import pichurose.stompandclimb.items.Rings.*;
import pichurose.stompandclimb.materials.HardHatMaterial;
import pichurose.stompandclimb.materials.HoverBootsMaterial;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;
import pichurose.stompandclimb.utils.PehkuiSupport;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StompAndClimb implements ModInitializer {

    public static final String MODID = "stompandclimb";
    public static boolean hasFlan = false;

    public static final ThreadLocal<Boolean> threadLocal0 = new ThreadLocal<>();
    public static final ThreadLocal<Boolean> threadLocal1 = new ThreadLocal<>();









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

    public static final Item NETHERITE_COLLAR = registerItem("netherite_collar", new CollarItem(new Item.Properties(), 16));
    public static final Item AMETHYST_COLLAR = registerItem("amethyst_collar", new CollarItem(new Item.Properties(), 8));
    public static final Item DIAMOND_COLLAR = registerItem("diamond_collar", new CollarItem(new Item.Properties(), 4));
    public static final Item EMERALD_COLLAR = registerItem("emerald_collar", new CollarItem(new Item.Properties(), 2));
    public static final Item QUARTZ_COLLAR = registerItem("quartz_collar", new CollarItem(new Item.Properties(), 1));
    public static final Item NETHERBRICK_COLLAR = registerItem("netherbrick_collar", new CollarItem(new Item.Properties(), .5f));
    public static final Item SEA_COLLAR = registerItem("sea_collar", new CollarItem(new Item.Properties(), .25f));
    public static final Item REDSTONE_COLLAR = registerItem("redstone_collar", new CollarItem(new Item.Properties(), .125f));
    public static final Item SPAWNER_COLLAR = registerItem("spawner_collar", new CollarItem(new Item.Properties(), .0625f));


    public static final Item SHRINKING_RING = registerItem("copper_ring", new ShrinkingRingItem(new Item.Properties()));
    public static final Item RUSTED_SHRINKING_RING = registerItem("rusted_ring", new RustedShrinkingRingItem(new Item.Properties()));
    public static final Item SHRINKING_COLLAR = registerItem("copper_collar", new ShrinkingCollarItem(new Item.Properties()));
    public static final Item RUSTED_SHRINKING_COLLAR = registerItem("rusted_collar", new RustedShrinkingCollarItem(new Item.Properties()));

    public static final Item GROWING_RING = registerItem("copper_ring_red", new GrowingRingItem(new Item.Properties()));
    public static final Item RUSTED_GROWING_RING = registerItem("rusted_ring_red", new RustedGrowingRingItem(new Item.Properties()));
    public static final Item GROWING_COLLAR = registerItem("copper_collar_red", new GrowingCollarItem(new Item.Properties()));
    public static final Item RUSTED_GROWING_COLLAR = registerItem("rusted_collar_red", new RustedGrowingCollarItem(new Item.Properties()));

    public static final Item EMPOWERED_SHRINKING_RING = registerItem("empowered_copper_ring", new EmpoweredShrinkingRingItem(new Item.Properties()));
    public static final Item EMPOWERED_SHRINKING_COLLAR = registerItem("empowered_copper_collar", new EmpoweredShrinkingCollarItem(new Item.Properties()));
    public static final Item EMPOWERED_GROWING_RING = registerItem("empowered_copper_ring_red", new EmpoweredGrowingRingItem(new Item.Properties()));
    public static final Item EMPOWERED_GROWING_COLLAR = registerItem("empowered_copper_collar_red", new EmpoweredGrowingCollarItem(new Item.Properties()));


    public static final Item OMNIRING = registerItem("omniring", new OmniRingItem(new Item.Properties()));
    public static final Item OMNICOLLAR = registerItem("omnicollar", new OmniCollarItem(new Item.Properties()));


    public static final MobEffect CURSE_OF_GROWING = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "curse_of_growing"), new CurseOfGrowingEffect());
    public static final MobEffect CURSE_OF_SHRINKING = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "curse_of_shrinking"), new CurseOfShrinkingEffect());
    public static final MobEffect GROW_EFFECT = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "grow_effect"), new GrowEffect());
    public static final MobEffect SHRINK_EFFECT = Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(MODID, "shrink_effect"), new ShrinkEffect());

    //wool, carpet, moss, moss carpet, sand, soul sand, red sand, mud, clay, snow, snow block, leaves, pink petals, bamboo, sugar cane, slime block, honey block, cobweb, lightning rod, lever, tripwire hook, string, wooden shovel, golden sword, brush, lead, snowball, stick, bone, feather, wheat, slime_ball, scute, clay ball, honey comb, blaze rod, paper, magma cream, phantom membrane, soft socks
    public static final List<Item> softItems = Arrays.asList(Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL, Items.WHITE_CARPET, Items.ORANGE_CARPET, Items.MAGENTA_CARPET, Items.LIGHT_BLUE_CARPET, Items.YELLOW_CARPET, Items.LIME_CARPET, Items.PINK_CARPET, Items.GRAY_CARPET, Items.LIGHT_GRAY_CARPET, Items.CYAN_CARPET, Items.PURPLE_CARPET, Items.BLUE_CARPET, Items.BROWN_CARPET, Items.GREEN_CARPET, Items.RED_CARPET, Items.BLACK_CARPET, Items.MOSS_BLOCK, Items.MOSS_CARPET, Items.SAND, Items.SOUL_SAND, Items.RED_SAND, Items.DIRT, Items.CLAY, Items.SNOW, Items.SNOW_BLOCK, Items.OAK_LEAVES, Items.SPRUCE_LEAVES, Items.BIRCH_LEAVES, Items.JUNGLE_LEAVES, Items.ACACIA_LEAVES, Items.DARK_OAK_LEAVES, Items.MANGROVE_LEAVES, Items.PINK_TULIP, Items.BAMBOO, Items.SUGAR_CANE, Items.SLIME_BLOCK, Items.HONEY_BLOCK, Items.COBWEB, Items.LIGHTNING_ROD, Items.LEVER, Items.TRIPWIRE_HOOK, Items.STRING, Items.WOODEN_SHOVEL, Items.GOLDEN_SWORD, Items.BRUSH, Items.LEAD, Items.SNOWBALL, Items.STICK, Items.BONE, Items.FEATHER, Items.WHEAT, Items.SLIME_BALL, Items.SCUTE, Items.CLAY_BALL, Items.HONEYCOMB, Items.BLAZE_ROD, Items.PAPER, Items.MAGMA_CREAM, Items.PHANTOM_MEMBRANE, SOFT_SOCKS);
    //public static final List<Item> stickyItems = Arrays.asList();

    @SuppressWarnings("unused")
    private static final CreativeModeTab ITEM_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(MODID, "ringsandgear"), FabricItemGroup.builder()
            .icon(() -> new ItemStack(SPAWNER_RING))
            .title(Component.translatable("itemGroup.stompandclimb.ringsandgear"))
            .displayItems((context, entries) -> {
                entries.accept(HARD_HAT);
                entries.accept(SOFT_SOCKS);
                entries.accept(HOVER_BOOTS);

                entries.accept(OMNIRING);
                entries.accept(NETHERITE_RING);
                entries.accept(AMETHYST_RING);
                entries.accept(DIAMOND_RING);
                entries.accept(EMERALD_RING);
                entries.accept(QUARTZ_RING);
                entries.accept(NETHERBRICK_RING);
                entries.accept(SEA_RING);
                entries.accept(REDSTONE_RING);
                entries.accept(SPAWNER_RING);

                entries.accept(OMNICOLLAR);
                entries.accept(NETHERITE_COLLAR);
                entries.accept(AMETHYST_COLLAR);
                entries.accept(DIAMOND_COLLAR);
                entries.accept(EMERALD_COLLAR);
                entries.accept(QUARTZ_COLLAR);
                entries.accept(NETHERBRICK_COLLAR);
                entries.accept(SEA_COLLAR);
                entries.accept(REDSTONE_COLLAR);
                entries.accept(SPAWNER_COLLAR);


                entries.accept(GROWING_RING);
                entries.accept(GROWING_COLLAR);
                entries.accept(RUSTED_GROWING_RING);
                entries.accept(RUSTED_GROWING_COLLAR);
                entries.accept(EMPOWERED_GROWING_RING);
                entries.accept(EMPOWERED_GROWING_COLLAR);

                entries.accept(SHRINKING_RING);
                entries.accept(SHRINKING_COLLAR);
                entries.accept(RUSTED_SHRINKING_RING);
                entries.accept(RUSTED_SHRINKING_COLLAR);
                entries.accept(EMPOWERED_SHRINKING_RING);
                entries.accept(EMPOWERED_SHRINKING_COLLAR);



            })
            .build());

    public StompAndClimb() {
        registerSetup(this::setup);
        hasFlan = isLoaded("flan");
    }

    public static boolean isLoaded(String mod) {
        return FabricLoader.getInstance().isModLoaded(mod);
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
            Entity target = targetId != -1 ? Objects.requireNonNull(Objects.requireNonNull(player.getServer()).getLevel(player.level().dimension())).getEntity(targetId) : null;
            Vec3 hitPos = new Vec3(x, y, z);
            server.execute(() -> handleKeyPressServer(player, hitResultType, target, smallEnough, hitPos));
        });




        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("customcarry")
                    .then(Commands.argument("x", DoubleArgumentType.doubleArg(-1, 1))
                    .then(Commands.argument("y", DoubleArgumentType.doubleArg(-4, 0))
                    .then(Commands.argument("z", DoubleArgumentType.doubleArg(-1, 1))
                    .then(Commands.argument("holdOutHand", BoolArgumentType.bool())
                    .then(Commands.argument("followHead", BoolArgumentType.bool())
                    .then(Commands.argument("invisiblePassengers", BoolArgumentType.bool())
                        .executes(StompAndClimbCustomCarryCommand::executeCommandWithArg))))))));
        });
        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("customcarry")
                    .then(Commands.argument("place", StringArgumentType.string())
                                            .executes(StompAndClimbCustomCarryCommand::executeCommandWithStringArg)));
        });
        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("omnisize")
                    .then(Commands.argument("size", FloatArgumentType.floatArg(Float.MIN_VALUE, 1024))
                            .executes(OmniSizeSetCommand::executeCommandWithArg)));
        });
        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("getsize").executes(GetSizeCommand::executeCommand));
        });
    }

    private void setup() {
        PehkuiSupport.setup();
    }
}