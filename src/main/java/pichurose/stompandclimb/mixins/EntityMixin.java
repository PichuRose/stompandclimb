package pichurose.stompandclimb.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.utils.ResizingUtils;


import java.util.List;
import java.util.logging.Level;

@Mixin(Entity.class )
public abstract class EntityMixin implements CustomCarryOffsetInterface {

    @Shadow
    public abstract boolean hasPassenger(Entity passenger);

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    private EntityDimensions dimensions;


    @Unique
    public double forwardBackOffset = 0.5;
    @Unique
    public double upDownOffset = -1;
    @Unique
    public double leftRightOffset = -0.5;

    @Unique
    boolean holdOutHand = true;

    @Override
    public void stompandclimb_updateCustomCarryCache(double x, double y, double z) {
        forwardBackOffset = x;
        upDownOffset = y;
        leftRightOffset = z;
    }

    @Override
    public void stompandclimb_updateCustomCarryCache(String string) {
        switch(string){
            case "hand":
                holdOutHand = true;
                forwardBackOffset = 0.5;
                upDownOffset = -1;
                leftRightOffset = -0.5;
                break;
            case "head":
                holdOutHand = false;
                forwardBackOffset = 0;
                upDownOffset = -0.25;
                leftRightOffset = 0;
                break;
        }
    }


    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"), cancellable = true)
    protected void injected(Entity passenger, Entity.MoveFunction positionUpdater, CallbackInfo ci) {
        if (!this.hasPassenger(passenger)) {
            return;
        }

        /*if(((Object)this) instanceof PlayerEntity) {
            double d = this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() - 0.5;
            //positionUpdater.accept(passenger, this.getX(), d, this.getZ());


            double offsetX = Math.cos(Math.toRadians(this.getYaw() + 90));
            double offsetZ = Math.sin(Math.toRadians(this.getYaw() + 90));
            positionUpdater.accept(passenger, this.getX() + offsetX, d, this.getZ() + offsetZ);
            ci.cancel();
        }*/
        if(((Object)this) instanceof Player player) {


            //forwardbackoffset = -0.08 - .08
            //updownoffset = -0.45 - 0.05 = hit
            //leftrightoffset = -.08 - .08

            //0.4 -1.45 -0.4

//            if (forwardBackOffset > -0.08 && forwardBackOffset < 0.08) {
//                forwardBackOffset = forwardBackOffset < 0 ? -0.08 : 0.08;
//            }
//            if (upDownOffset > -0.45 && upDownOffset < 0.05) {
//                upDownOffset = upDownOffset < -0.2 ? -0.45 : 0.05;
//            }
//            if (leftRightOffset > -0.08 && leftRightOffset < 0.08) {
//                leftRightOffset = leftRightOffset < 0 ? -0.08 : 0.08;
//            }

            float scale = ResizingUtils.getActualSize(player);
            float passengerHeight = passenger.getDimensions(Pose.STANDING).height;

            float angle = player.yBodyRotO;

            double offsetX = Math.cos(Math.toRadians(angle + 90)) * (forwardBackOffset * scale) + Math.cos(Math.toRadians(angle)) * (leftRightOffset * scale);
            double offsetY = this.getY() + this.dimensions.height + passenger.getMyRidingOffset() + ((upDownOffset) * scale);
            double offsetZ = Math.sin(Math.toRadians(angle + 90)) * (forwardBackOffset * scale) + Math.sin(Math.toRadians(angle)) * (leftRightOffset * scale);


            if (holdOutHand) {
                player.swingTime = 0;
                player.swing(InteractionHand.MAIN_HAND);
            }

            positionUpdater.accept(passenger, this.getX() + offsetX, offsetY, this.getZ() + offsetZ);


            //((PlayerEntity)(Object)this).sendMessage(Text.literal(((Entity)(Object)this).getWorld().isClient + "Offsets - Left/Right: " + leftRightOffset + ", Up/Down: " + upDownOffset + ", Forward/Back: " + forwardBackOffset));

            ci.cancel();
        }
    }



    /*@ModifyArgs(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/lang/Iterable;D)D"))
    private static void injected(Args args) {
        //(Direction.Axis axis, Box box, Iterable<VoxelShape> shapes, double maxDist

        Direction.Axis axis = args.get(0);
        Box box = args.get(1);
        List<VoxelShape> shapes = args.get(2);
        double maxDist = args.get(3);

        double signNum = Math.signum(maxDist);

        switch (axis) {
            case X ->
                    box = box.contract(-signNum * box.getXLength(), 0, 0);
            case Y ->
                    box = box.contract(0, -signNum * box.getYLength(), 0);
            case Z ->
                    box = box.contract(0, 0, -signNum * box.getZLength());
        }



        args.set(1, box);
    }*/
}
