package pichurose.stompandclimb.materials;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import pichurose.stompandclimb.StompAndClimb;

public class HoverBootsMaterial implements ArmorMaterial {

    @Override
    public int getDurability(ArmorItem.Type type) {
        //ArmorMaterials.IRON.getDurability(ArmorItem.Type.BOOTS);
        return 69420;
    }



    @Override
    public int getProtection(ArmorItem.Type type) {
        return ArmorMaterials.NETHERITE.getProtection(ArmorItem.Type.BOOTS);
    }

    @Override
    public int getEnchantability() {
        return ArmorMaterials.GOLD.getEnchantability();
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.LEATHER.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.NETHERITE_INGOT);
    }

    @Override
    public String getName() {
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
