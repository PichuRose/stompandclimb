package pichurose.stompandclimb;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static pichurose.stompandclimb.StompAndClimb.MODID;

public class StompAndClimbPotions {
    //public static final Potion GROW_POTION = Registry.register(Registries.POTION, new Identifier(StompAndClimb.MODID, "grow_potion"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 3600, 0)));
    public static final Potion GROW_POTION = Registry.register(Registries.POTION, new Identifier("grow_potion"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 6000, 0)));
    public static final Potion GROW_POTION_2 = Registry.register(Registries.POTION, new Identifier("grow_potion_2"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 5400, 1)));
    public static final Potion GROW_POTION_3 = Registry.register(Registries.POTION, new Identifier("grow_potion_3"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 4800, 2)));
    public static final Potion GROW_POTION_4 = Registry.register(Registries.POTION, new Identifier("grow_potion_4"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 4200, 3)));
    public static final Potion GROW_POTION_5 = Registry.register(Registries.POTION, new Identifier("grow_potion_5"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 3600, 4)));
    public static final Potion GROW_POTION_6 = Registry.register(Registries.POTION, new Identifier("grow_potion_6"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 3000, 5)));
    public static final Potion GROW_POTION_7 = Registry.register(Registries.POTION, new Identifier("grow_potion_7"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 2400, 6)));
    public static final Potion GROW_POTION_8 = Registry.register(Registries.POTION, new Identifier("grow_potion_8"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 1800, 7)));
    public static final Potion GROW_POTION_9 = Registry.register(Registries.POTION, new Identifier("grow_potion_9"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 1200, 8)));
    public static final Potion GROW_POTION_10 = Registry.register(Registries.POTION, new Identifier("grow_potion_10"), new Potion(new StatusEffectInstance(StompAndClimb.GROW_EFFECT, 600, 9)));
    public static final Potion SHRINK_POTION = Registry.register(Registries.POTION, new Identifier("shrink_potion"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 6000, 0)));
    public static final Potion SHRINK_POTION_2 = Registry.register(Registries.POTION, new Identifier("shrink_potion_2"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 5400, 1)));
    public static final Potion SHRINK_POTION_3 = Registry.register(Registries.POTION, new Identifier("shrink_potion_3"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 4800, 2)));
    public static final Potion SHRINK_POTION_4 = Registry.register(Registries.POTION, new Identifier("shrink_potion_4"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 4200, 3)));
    public static final Potion SHRINK_POTION_5 = Registry.register(Registries.POTION, new Identifier("shrink_potion_5"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 3600, 4)));
    public static final Potion SHRINK_POTION_6 = Registry.register(Registries.POTION, new Identifier("shrink_potion_6"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 3000, 5)));
    public static final Potion SHRINK_POTION_7 = Registry.register(Registries.POTION, new Identifier("shrink_potion_7"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 2400, 6)));
    public static final Potion SHRINK_POTION_8 = Registry.register(Registries.POTION, new Identifier("shrink_potion_8"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 1800, 7)));
    public static final Potion SHRINK_POTION_9 = Registry.register(Registries.POTION, new Identifier("shrink_potion_9"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 1200, 8)));
    public static final Potion SHRINK_POTION_10 = Registry.register(Registries.POTION, new Identifier("shrink_potion_10"), new Potion(new StatusEffectInstance(StompAndClimb.SHRINK_EFFECT, 600, 9)));

    /*private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack((ItemConvertible) SHRINK_POTION))
            .displayName(Text.translatable("itemGroup.stompandclimb.potions"))
            .entries((context, entries) -> {
                entries.add((ItemConvertible) GROW_POTION);
                entries.add((ItemConvertible) GROW_POTION_2);
                entries.add((ItemConvertible) GROW_POTION_3);
                entries.add((ItemConvertible) GROW_POTION_4);
                entries.add((ItemConvertible) GROW_POTION_5);
                entries.add((ItemConvertible) GROW_POTION_6);
                entries.add((ItemConvertible) GROW_POTION_7);
                entries.add((ItemConvertible) GROW_POTION_8);
                entries.add((ItemConvertible) GROW_POTION_9);
                entries.add((ItemConvertible) GROW_POTION_10);
                entries.add((ItemConvertible) SHRINK_POTION);
                entries.add((ItemConvertible) SHRINK_POTION_2);
                entries.add((ItemConvertible) SHRINK_POTION_3);
                entries.add((ItemConvertible) SHRINK_POTION_4);
                entries.add((ItemConvertible) SHRINK_POTION_5);
                entries.add((ItemConvertible) SHRINK_POTION_6);
                entries.add((ItemConvertible) SHRINK_POTION_7);
                entries.add((ItemConvertible) SHRINK_POTION_8);
                entries.add((ItemConvertible) SHRINK_POTION_9);
                entries.add((ItemConvertible) SHRINK_POTION_10);
            })
            .build();*/
    public static void register() {
        //Registry.register(Registries.ITEM_GROUP, Identifier.of(MODID, "potions"), ITEM_GROUP);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.WATER, Items.BROWN_MUSHROOM, SHRINK_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION, Items.GLOWSTONE_DUST, SHRINK_POTION_2);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_2, Items.GLOWSTONE_DUST, SHRINK_POTION_3);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_3, Items.GLOWSTONE_DUST, SHRINK_POTION_4);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_4, Items.GLOWSTONE_DUST, SHRINK_POTION_5);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_5, Items.GLOWSTONE_DUST, SHRINK_POTION_6);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_6, Items.GLOWSTONE_DUST, SHRINK_POTION_7);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_7, Items.GLOWSTONE_DUST, SHRINK_POTION_8);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_8, Items.GLOWSTONE_DUST, SHRINK_POTION_9);
        BrewingRecipeRegistry.registerPotionRecipe(SHRINK_POTION_9, Items.GLOWSTONE_DUST, SHRINK_POTION_10);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.WATER, Items.RED_MUSHROOM, GROW_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION, Items.GLOWSTONE_DUST, GROW_POTION_2);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_2, Items.GLOWSTONE_DUST, GROW_POTION_3);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_3, Items.GLOWSTONE_DUST, GROW_POTION_4);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_4, Items.GLOWSTONE_DUST, GROW_POTION_5);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_5, Items.GLOWSTONE_DUST, GROW_POTION_6);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_6, Items.GLOWSTONE_DUST, GROW_POTION_7);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_7, Items.GLOWSTONE_DUST, GROW_POTION_8);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_8, Items.GLOWSTONE_DUST, GROW_POTION_9);
        BrewingRecipeRegistry.registerPotionRecipe(GROW_POTION_9, Items.GLOWSTONE_DUST, GROW_POTION_10);
    }

}
