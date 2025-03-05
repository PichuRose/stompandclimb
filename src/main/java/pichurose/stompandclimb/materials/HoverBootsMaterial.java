package pichurose.stompandclimb.materials;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import pichurose.stompandclimb.StompAndClimb;

public class HoverBootsMaterial implements ArmorMaterial {

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return 69420;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return ArmorMaterials.NETHERITE.getDefenseForType(ArmorItem.Type.HELMET);
    }

    @Override
    public int getEnchantmentValue() {
        return ArmorMaterials.GOLD.getEnchantmentValue();
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return ArmorMaterials.LEATHER.getEquipSound();
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }

    @Override
    public @NotNull String getName() {
        return StompAndClimb.MODID + ":" +"hoverboots";
    }

    @Override
    public float getToughness() {
        return ArmorMaterials.NETHERITE.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return ArmorMaterials.NETHERITE.getKnockbackResistance();
    }


}
