package pichurose.stompandclimb.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import pichurose.stompandclimb.utils.ResizingUtils;

public class GrowEffect extends MobEffect {

    private int tick = 0;

    public GrowEffect() {
        super(MobEffectCategory.HARMFUL, 0xff005d  );
    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if(amplifier>10){
            amplifier = 10;
        }
        int ticksPerGrow = 40;

        if(tick >= ticksPerGrow){
            tick = 0;
            ResizingUtils.resizeOneSecond(entity,1.01f+(.01f*amplifier));
        }
        else{
            tick++;
        }

    }
}