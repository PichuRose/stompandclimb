package pichurose.stompandclimb.utils;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ResizingUtils {
    private static final UUID uuidHeight = UUID.fromString("5440b01a-974f-4495-bb9a-c7c87424bca4");
    private static final UUID uuidWidth = UUID.fromString("3949d2ed-b6cc-4330-9c13-98777f48ea51");
    private static final float MIN_SIZE = 0.00390625f;
    private static final float MAX_SIZE = 8;
    //private static final double DOWNSCALE_RATE = 0.5;
    //private static final double UPSCALE_RATE = 0.5;

    public static void resize(Entity entity, float mult) {
        if (entity == null) return;
        float newSize = getSize(entity) * mult;
        if(newSize < MIN_SIZE){
            newSize = MIN_SIZE;
        }
        if(newSize > MAX_SIZE){
            newSize = MAX_SIZE;
        }


        PehkuiSupport.SACScaleType.get().getScaleData(entity).setScaleTickDelay(200);
        PehkuiSupport.SACScaleType.get().getScaleData(entity).setTargetScale(newSize);
    }

    public static void resizeInstant(Entity entity, float mult) {
        if (entity == null) return;
        float newSize = getSize(entity) * mult;
        if(newSize < MIN_SIZE){
            newSize = MIN_SIZE;
        }
        if(newSize > MAX_SIZE){
            newSize = MAX_SIZE;
        }



        PehkuiSupport.SACScaleType.get().getScaleData(entity).setScale(newSize);
    }



    public static void setSize(Entity entity, float size) {
        if (entity == null) return;
        if(size == 1){
            PehkuiSupport.SACScaleType.get().getScaleData(entity).resetScale();
        }
        else{
            float newSize = size;
            if(newSize < MIN_SIZE){
                newSize = MIN_SIZE;
            }
            if(newSize > MAX_SIZE){
                newSize = MAX_SIZE;
            }
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
