package pichurose.stompandclimb.mixins;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.utils.ResizingUtils;

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
    @Unique
    boolean followBodyAngle = true;
    @Unique
    boolean invisiblePassengers = false;

    @Override
    public void stompandclimb_updateCustomCarryCache(double x, double y, double z, boolean holdOutHand, boolean followBodyAngle, boolean invisiblePassengers) {
        forwardBackOffset = x;
        upDownOffset = y;
        leftRightOffset = z;
        this.holdOutHand = holdOutHand;
        this.followBodyAngle = followBodyAngle;
        this.invisiblePassengers = invisiblePassengers;
    }

/*
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"), cancellable = true)
    protected void injected(Entity passenger, Entity.MoveFunction positionUpdater, CallbackInfo ci) {
        if (!this.hasPassenger(passenger)) {
            return;
        }

        if(((Object)this) instanceof Player player) {


            //forwardbackoffset = -0.08 - .08
            //updownoffset = -0.45 - 0.05 = hit
            //leftrightoffset = -.08 - .08

            //0.4 -1.45 -0.4

            float scale = ResizingUtils.getActualSize(player);
            float angle;
            if(followBodyAngle)
                angle = player.yBodyRotO;
            else
                angle = player.yHeadRot;

            if (holdOutHand) {
                player.swingTime = 0;
                player.swing(InteractionHand.MAIN_HAND);
            }

            if (invisiblePassengers && passenger instanceof LivingEntity) {
                ((LivingEntity) passenger).addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 5, 0, true, false, false));
            }

            double offsetX = Math.cos(Math.toRadians(angle + 90)) * (forwardBackOffset * scale) + Math.cos(Math.toRadians(angle)) * (leftRightOffset * scale);
            double offsetY = this.getY() + this.dimensions.height + passenger.getMyRidingOffset() + ((upDownOffset) * scale);
            double offsetZ = Math.sin(Math.toRadians(angle + 90)) * (forwardBackOffset * scale) + Math.sin(Math.toRadians(angle)) * (leftRightOffset * scale);

            if (!followBodyAngle) {
                // Adjust offsetX, offsetY, and offsetZ based on the head's pitch and yaw

            }



            positionUpdater.accept(passenger, this.getX() + offsetX, offsetY, this.getZ() + offsetZ);

            //((Player)(Object)this).displayClientMessage(Component.literal(((Entity)(Object)this).level().isClientSide + "\tOffsets - Left/Right: " + leftRightOffset + ", Up/Down: " + upDownOffset + ", Forward/Back: " + forwardBackOffset),false);

            ci.cancel();
        }
    }
*/

    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"), cancellable = true)
    protected void injected(Entity passenger, Entity.MoveFunction positionUpdater, CallbackInfo ci) {
        if (!this.hasPassenger(passenger)) {
            return;
        }

        if (((Object) this) instanceof Player player) {
            float scale = ResizingUtils.getActualSize(player);
            float angle = followBodyAngle ? player.yBodyRotO : player.yHeadRot;

            if (holdOutHand) {
                player.swingTime = 0;
                player.swing(InteractionHand.MAIN_HAND);
            }

            if (invisiblePassengers && passenger instanceof LivingEntity) {
                ((LivingEntity) passenger).addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 5, 0, true, false, false));
            }

            double offsetX, offsetY, offsetZ;
            //
            if (!followBodyAngle) {
                // Calculate offsets based on head pitch and yaw
                float pitch = player.getXRot();
                float yaw = player.yHeadRot;

                // Adjust forward-backward offset based on pitch
                double pitchFactor = Math.cos(Math.toRadians(pitch));

                offsetX = Math.cos(Math.toRadians(yaw + 90)) * (forwardBackOffset * scale * pitchFactor)
                        + Math.cos(Math.toRadians(yaw)) * (leftRightOffset * scale);
                offsetY = this.getY() + this.dimensions.height + passenger.getMyRidingOffset()
                        - (Math.sin(Math.toRadians(pitch)) * (forwardBackOffset * scale)) // Negated the effect of pitch
                        + (upDownOffset * scale);
                offsetZ = Math.sin(Math.toRadians(yaw + 90)) * (forwardBackOffset * scale * pitchFactor)
                        + Math.sin(Math.toRadians(yaw)) * (leftRightOffset * scale);
            } else {
                // Default behavior
                offsetX = Math.cos(Math.toRadians(angle + 90)) * (forwardBackOffset * scale)
                        + Math.cos(Math.toRadians(angle)) * (leftRightOffset * scale);
                offsetY = this.getY() + this.dimensions.height + passenger.getMyRidingOffset()
                        + (upDownOffset * scale);
                offsetZ = Math.sin(Math.toRadians(angle + 90)) * (forwardBackOffset * scale)
                        + Math.sin(Math.toRadians(angle)) * (leftRightOffset * scale);
            }

            positionUpdater.accept(passenger, this.getX() + offsetX, offsetY, this.getZ() + offsetZ);
            ci.cancel();
        }
    }

    @Redirect(
            // the method this function is called in
            method = "push(Lnet/minecraft/world/entity/Entity;)V",
            // target the invocation of System.out.println
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;push(DDD)V",
                    ordinal = 0
            )
    )
    private void pushIfThreadLocalTrueOrdinal0(Entity instance, double x, double y, double z) {
        if(instance instanceof LivingEntity && StompAndClimb.threadLocal0.get()!=null) {
            instance.setDeltaMovement(instance.getDeltaMovement().add(x, y, z));
            instance.hasImpulse = true;
        }
    }

    @Redirect(
            // the method this function is called in
            method = "push(Lnet/minecraft/world/entity/Entity;)V",
            // target the invocation of System.out.println
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;push(DDD)V",
                    ordinal = 1
            )
    )
    private void pushIfThreadLocalTrueOrdinal1(Entity instance, double x, double y, double z) {
        if(instance instanceof LivingEntity && StompAndClimb.threadLocal1.get()!=null) {
            instance.setDeltaMovement(instance.getDeltaMovement().add(x, y, z));
            instance.hasImpulse = true;
        }
    }

    @Inject(method= "getDismountLocationForPassenger(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    public void getDismountLocationForPassenger(LivingEntity passenger, CallbackInfoReturnable<Vec3> cir) {
        if(!this.hasPassenger(passenger)) {
            return;
        }
        if(passenger instanceof Player && (Object)this instanceof Player) {
            cir.setReturnValue(new Vec3(passenger.getX(), passenger.getY(), passenger.getZ()));
        }

    }
}
