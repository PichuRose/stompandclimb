package pichurose.stompandclimb.items;


import net.minecraft.world.item.ArmorItem;


import pichurose.stompandclimb.materials.SoftSocksMaterial;


public class SoftSocksItem extends ArmorItem {
    public SoftSocksItem(Properties settings) {
        super(new SoftSocksMaterial(), Type.BOOTS, settings);
    }
}