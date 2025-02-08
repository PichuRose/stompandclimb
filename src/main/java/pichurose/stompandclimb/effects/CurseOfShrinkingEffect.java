package pichurose.stompandclimb.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Random;

public class CurseOfShrinkingEffect extends StatusEffect {
    public CurseOfShrinkingEffect() {
        super(StatusEffectCategory.HARMFUL, 0x410c01 );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }


    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.getWorld().isClient){
            return;
        }
        Random r = new Random();
        if(r.nextFloat() >= 0.9999){
            float mult = r.nextFloat(.1f,1f);
            ResizingUtils.resize(entity, mult);
        }
    }




}