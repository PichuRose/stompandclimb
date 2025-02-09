package pichurose.stompandclimb.materials;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
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
    public SoundEvent getEquipSound() {
        return ArmorMaterials.LEATHER.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.NETHERITE_INGOT);
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
