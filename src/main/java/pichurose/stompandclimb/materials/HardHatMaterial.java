package pichurose.stompandclimb.materials;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import pichurose.stompandclimb.StompAndClimb;

public class HardHatMaterial implements ArmorMaterial {

    @Override
    public int getDurability(ArmorItem.Type type) {
        //ArmorMaterials.IRON.getDurability(ArmorItem.Type.BOOTS);
        return 69420;
    }



    @Override
    public int getProtection(ArmorItem.Type type) {
        return ArmorMaterials.DIAMOND.getProtection(ArmorItem.Type.HELMET);
    }

    @Override
    public int getEnchantability() {
        return ArmorMaterials.GOLD.getEnchantability();
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.IRON.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.COPPER_INGOT);
    }

    @Override
    public String getName() {
        return StompAndClimb.MODID + ":" +"hardhat";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
