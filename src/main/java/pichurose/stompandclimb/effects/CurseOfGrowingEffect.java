package pichurose.stompandclimb.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Random;

public class CurseOfGrowingEffect extends MobEffect {
    public CurseOfGrowingEffect() {
        super(MobEffectCategory.HARMFUL, 0x010c41 );
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
        if(r.nextFloat() >= 0.99833334){
            float mult = r.nextFloat(1.0f,1.5f);
            ResizingUtils.resizeOneSecond(entity, mult);
        }
    }




}