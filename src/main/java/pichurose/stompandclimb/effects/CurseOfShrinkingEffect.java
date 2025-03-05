package pichurose.stompandclimb.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Random;

public class CurseOfShrinkingEffect extends MobEffect {
    public CurseOfShrinkingEffect() {
        super(MobEffectCategory.HARMFUL, 0x410c01 );
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.level().isClientSide()){
            return;
        }
        Random r = new Random();
        if(r.nextFloat() >= 0.9999){
            float mult = r.nextFloat(.1f,1f);
            ResizingUtils.resizeOneSecond(entity, mult);
        }
    }




}