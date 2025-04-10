package pichurose.stompandclimb.mixins;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pichurose.stompandclimb.utils.ResizingUtils;

@Mixin(Player.class )
public class PlayerEntityMixin {

    @SuppressWarnings("UnusedAssignment")
    @ModifyVariable(method = "hurtArmor", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    public float modifyArmorDamage(float damageAmount, DamageSource damageSource) {
        if(damageAmount == 0) {
            return damageAmount;
        }
        LivingEntity entity = (LivingEntity) (Object) this;
        float entitySize = ResizingUtils.getActualSize(entity);
        Entity damageSourceEntity = damageSource.getEntity();
        Entity damageSourceDirectEntity = damageSource.getDirectEntity();
        float damageSourceEntitySize = -1;
        float damageSourceDirectEntitySize = -1;
        float sizeDifferenceThisOverThem = 1;

        if (damageSourceDirectEntity != null) {
            damageSourceDirectEntitySize = ResizingUtils.getActualSize(damageSourceDirectEntity);
            sizeDifferenceThisOverThem = entitySize / damageSourceDirectEntitySize;
        } else if (damageSourceEntity != null) {
            damageSourceEntitySize = ResizingUtils.getActualSize(damageSourceEntity);
            sizeDifferenceThisOverThem = entitySize / damageSourceEntitySize;
        } else {
            sizeDifferenceThisOverThem = entitySize;
        }

        if(sizeDifferenceThisOverThem <= 1){
            damageAmount *= sizeDifferenceThisOverThem;
        }
        return damageAmount;
    }

}
