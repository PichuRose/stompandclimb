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

public class HardHatMaterial implements ArmorMaterial {

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return 69420;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return ArmorMaterials.DIAMOND.getDefenseForType(ArmorItem.Type.HELMET);
    }

    @Override
    public int getEnchantmentValue() {
        return ArmorMaterials.GOLD.getEnchantmentValue();
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.IRON.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.COPPER_INGOT);
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
