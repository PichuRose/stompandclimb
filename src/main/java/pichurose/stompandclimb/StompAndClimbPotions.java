package pichurose.stompandclimb;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import pichurose.stompandclimb.effects.CurseOfShrinkingEffect;

import static pichurose.stompandclimb.StompAndClimb.MODID;

public class StompAndClimbPotions {
    public static final Potion GROW_POTION = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 6000, 0)));
    public static final Potion GROW_POTION_2 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_2"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 5400, 1)));
    public static final Potion GROW_POTION_3 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_3"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 4800, 2)));
    public static final Potion GROW_POTION_4 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_4"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 4200, 3)));
    public static final Potion GROW_POTION_5 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_5"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 3600, 4)));
    public static final Potion GROW_POTION_6 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_6"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 3000, 5)));
    public static final Potion GROW_POTION_7 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_7"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 2400, 6)));
    public static final Potion GROW_POTION_8 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_8"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 1800, 7)));
    public static final Potion GROW_POTION_9 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_9"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 1200, 8)));
    public static final Potion GROW_POTION_10 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("grow_potion_10"), new Potion(new MobEffectInstance(StompAndClimb.GROW_EFFECT, 600, 9)));
    public static final Potion SHRINK_POTION = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 6000, 0)));
    public static final Potion SHRINK_POTION_2 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_2"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 5400, 1)));
    public static final Potion SHRINK_POTION_3 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_3"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 4800, 2)));
    public static final Potion SHRINK_POTION_4 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_4"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 4200, 3)));
    public static final Potion SHRINK_POTION_5 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_5"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 3600, 4)));
    public static final Potion SHRINK_POTION_6 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_6"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 3000, 5)));
    public static final Potion SHRINK_POTION_7 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_7"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 2400, 6)));
    public static final Potion SHRINK_POTION_8 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_8"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 1800, 7)));
    public static final Potion SHRINK_POTION_9 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_9"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 1200, 8)));
    public static final Potion SHRINK_POTION_10 = (Potion)Registry.register(BuiltInRegistries.POTION, new ResourceLocation("shrink_potion_10"), new Potion(new MobEffectInstance(StompAndClimb.SHRINK_EFFECT, 600, 9)));

    public static void register() {
        //Registry.register(Registries.ITEM_GROUP, Identifier.of(MODID, "potions"), ITEM_GROUP);
        PotionBrewing.addMix(Potions.WATER, Items.BROWN_MUSHROOM, SHRINK_POTION);
        PotionBrewing.addMix(SHRINK_POTION, Items.GLOWSTONE_DUST, SHRINK_POTION_2);
        PotionBrewing.addMix(SHRINK_POTION_2, Items.GLOWSTONE_DUST, SHRINK_POTION_3);
        PotionBrewing.addMix(SHRINK_POTION_3, Items.GLOWSTONE_DUST, SHRINK_POTION_4);
        PotionBrewing.addMix(SHRINK_POTION_4, Items.GLOWSTONE_DUST, SHRINK_POTION_5);
        PotionBrewing.addMix(SHRINK_POTION_5, Items.GLOWSTONE_DUST, SHRINK_POTION_6);
        PotionBrewing.addMix(SHRINK_POTION_6, Items.GLOWSTONE_DUST, SHRINK_POTION_7);
        PotionBrewing.addMix(SHRINK_POTION_7, Items.GLOWSTONE_DUST, SHRINK_POTION_8);
        PotionBrewing.addMix(SHRINK_POTION_8, Items.GLOWSTONE_DUST, SHRINK_POTION_9);
        PotionBrewing.addMix(SHRINK_POTION_9, Items.GLOWSTONE_DUST, SHRINK_POTION_10);
        PotionBrewing.addMix(Potions.WATER, Items.RED_MUSHROOM, GROW_POTION);
        PotionBrewing.addMix(GROW_POTION, Items.GLOWSTONE_DUST, GROW_POTION_2);
        PotionBrewing.addMix(GROW_POTION_2, Items.GLOWSTONE_DUST, GROW_POTION_3);
        PotionBrewing.addMix(GROW_POTION_3, Items.GLOWSTONE_DUST, GROW_POTION_4);
        PotionBrewing.addMix(GROW_POTION_4, Items.GLOWSTONE_DUST, GROW_POTION_5);
        PotionBrewing.addMix(GROW_POTION_5, Items.GLOWSTONE_DUST, GROW_POTION_6);
        PotionBrewing.addMix(GROW_POTION_6, Items.GLOWSTONE_DUST, GROW_POTION_7);
        PotionBrewing.addMix(GROW_POTION_7, Items.GLOWSTONE_DUST, GROW_POTION_8);
        PotionBrewing.addMix(GROW_POTION_8, Items.GLOWSTONE_DUST, GROW_POTION_9);
        PotionBrewing.addMix(GROW_POTION_9, Items.GLOWSTONE_DUST, GROW_POTION_10);
    }

}
