package pichurose.stompandclimb.mixins;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pichurose.stompandclimb.interfaces.ClientInformationInterface;
import pichurose.stompandclimb.utils.ResizingUtils;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void preventItemCollection(Player player, CallbackInfo ci) {
        // Add your condition to stop item collection
        boolean allowItemCollection = ((ClientInformationInterface)player).stompandclimb_getIsAllowedToCollect(); // Set this dynamically as needed
        boolean bigEnoughToBeUnableToCollect = ResizingUtils.getActualSize(player) >= 4;
        if (bigEnoughToBeUnableToCollect && !allowItemCollection) {
            ci.cancel(); // Prevent the item from being collected
            return;
        }
    }
}