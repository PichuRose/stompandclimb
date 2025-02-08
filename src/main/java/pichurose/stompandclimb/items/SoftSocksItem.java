package pichurose.stompandclimb.items;


import net.minecraft.client.render.VertexFormatElement;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;


import org.jetbrains.annotations.Nullable;
import pichurose.stompandclimb.materials.SoftSocksMaterial;


public class SoftSocksItem extends ArmorItem {
    public SoftSocksItem(Settings settings) {
        super(new SoftSocksMaterial(), Type.BOOTS, settings);
    }
}