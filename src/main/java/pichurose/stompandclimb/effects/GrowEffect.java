package pichurose.stompandclimb.effects;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Random;

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
        if(entity.level().isClientSide()){
            return;
        }
        if(amplifier>10){
            amplifier = 10;
        }
        int ticksPerGrow = 200 - (amplifier*20);

        if(tick >= ticksPerGrow){
            tick = 0;
            ResizingUtils.resize(entity,1.01f+(.01f*amplifier));
        }
        else{
            tick++;
        }

    }
}