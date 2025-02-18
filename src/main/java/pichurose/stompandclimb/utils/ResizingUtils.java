package pichurose.stompandclimb.utils;


import net.minecraft.world.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ResizingUtils {

    public static void resize(Entity entity, float mult) {
        if (entity == null) return;
        float newSize = getSize(entity) * mult;



        PehkuiSupport.SACScaleType.get().getScaleData(entity).setScaleTickDelay(200);
        PehkuiSupport.SACScaleType.get().getScaleData(entity).setTargetScale(newSize);
    }

    public static void resizeInstant(Entity entity, float mult) {
        if (entity == null) return;
        float newSize = getSize(entity) * mult;



        PehkuiSupport.SACScaleType.get().getScaleData(entity).setScale(newSize);
    }

    public static void resizeOneSecond(Entity entity, float mult){
        if (entity == null) return;
        float entitySize = getSize(entity);
        if(entitySize > 1024 || entitySize < .001){
            return;
        }
        float newSize = getSize(entity) * mult;
        if(newSize > 1024f){
            newSize = 1024f;
        }
        if(newSize < .001){
            newSize = .001f;
        }

        PehkuiSupport.SACScaleType.get().getScaleData(entity).setScaleTickDelay(20);
        PehkuiSupport.SACScaleType.get().getScaleData(entity).setTargetScale(newSize);
    }



    public static void setSize(Entity entity, float size) {
        if (entity == null) return;
        if(size == 1){
            PehkuiSupport.SACScaleType.get().getScaleData(entity).resetScale();
        }
        else{
            float newSize = size;
            PehkuiSupport.SACScaleType.get().getScaleData(entity).setScale(newSize);
        }

    }

    public static float getSize(Entity entity) {
        if (entity == null) return 1;
        AtomicReference<Float> size = new AtomicReference<>(1f);
        size.set(size.get() * PehkuiSupport.SACScaleType.get().getScaleData(entity).getTargetScale());
        return size.get();
    }

    public static float getActualSize(Entity entity) {
        if (entity == null) return 1;
        AtomicReference<Float> size = new AtomicReference<>(1f);
        size.set(size.get() * ScaleUtils.getBoundingBoxHeightScale(entity));
        return size.get();
    }
}
