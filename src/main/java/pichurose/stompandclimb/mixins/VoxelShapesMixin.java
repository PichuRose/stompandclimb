package pichurose.stompandclimb.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShapes.class )
public abstract class VoxelShapesMixin {

    /*
    Direction.Axis ySwivel = Direction.Axis.X;

        if (movement.x != 0) ySwivel = Direction.Axis.X;
        else if (movement.y != 0) ySwivel = Direction.Axis.Y;
        else if (movement.z != 0) ySwivel = Direction.Axis.Z;


        double pDesiredOffset = 0;
        double signNum = Math.signum(pDesiredOffset);

        switch (ySwivel) {
            case X ->
                    box = box.expand(pDesiredOffset, 0, 0).contract(-signNum * pCollisionBox.getXsize(), 0, 0);
            case Y ->
                    box = box.expand(0, pDesiredOffset, 0).contract(0, -signNum * pCollisionBox.getYsize(), 0);
            case Z ->
                    box = box.expand(0, 0, pDesiredOffset).contract(0, 0, -signNum * pCollisionBox.getZsize());
        }
     */
}