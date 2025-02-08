package pichurose.stompandclimb.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Optional;

public class HoverBootsItem extends ArmorItem {

    public HoverBootsItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }
}
