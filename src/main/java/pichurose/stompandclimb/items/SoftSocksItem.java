package pichurose.stompandclimb.items;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


import org.jetbrains.annotations.Nullable;
import pichurose.stompandclimb.materials.SoftSocksMaterial;


public class SoftSocksItem extends ArmorItem {
    public SoftSocksItem(Properties settings) {
        super(new SoftSocksMaterial(), Type.BOOTS, settings);
    }
}