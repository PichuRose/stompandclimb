package pichurose.stompandclimb.mixins;

import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VoxelShape.class )
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