package pichurose.stompandclimb.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import pichurose.stompandclimb.utils.ResizingUtils;

public class ShrinkEffect extends StatusEffect {
    private int tick = 0;

    public ShrinkEffect() {
        super(StatusEffectCategory.HARMFUL, 0x9400ff  );
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
        if(amplifier>10){
            amplifier = 10;
        }
        int ticksPerGrow = 200 - (amplifier*20);

        if(tick >= ticksPerGrow){
            tick = 0;
            ResizingUtils.resize(entity,.99f-(.01f*amplifier));
        }
        else{
            tick++;
        }

    }
}