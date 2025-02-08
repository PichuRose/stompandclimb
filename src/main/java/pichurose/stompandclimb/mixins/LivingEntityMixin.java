package pichurose.stompandclimb.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.items.SoftSocksItem;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ClientLocationInterface {
    @Shadow
    public abstract void kill();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    private @Nullable DamageSource lastDamageSource;

    @Shadow
    public abstract @Nullable DamageSource getRecentDamageSource();

    @Shadow
    public abstract Box getBoundingBox(EntityPose pose);

    @Shadow
    public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public float forwardSpeed;

    @Shadow
    private Optional<BlockPos> climbingPos;

    @Shadow
    protected abstract float getEyeHeight(EntityPose pose, EntityDimensions dimensions);

    @Shadow protected abstract boolean shouldAlwaysDropXp();

    public Vec3d playerVec = Vec3d.ZERO;



    @Override
    public void stompandclimb_updateCache(Vec3d vec) {
        playerVec = vec;
    }




    @Inject(method = "pushAway", at = @At("HEAD"), cancellable = true)
    private void injected(Entity entity, CallbackInfo ci) {
        if (entity instanceof MinecartEntity) {
            //StompAndClimbForge.log("push called by minecart");
            return;
        }
        if (((Entity)(Object) this).getPassengerList().contains(entity)) {
            //StompAndClimbForge.log("push called by passenger");
            return;
        }

        boolean softSocks = false;
        boolean hardHat = false;

        if (entity.getPassengerList().contains(((Entity)(Object) this))) {
            //StompAndClimbForge.log("push called by passenger");
            softSocks = true;
        }

        float bootsArmor = 0;
        Vec3d velocity = new Vec3d(0, 0, 0);
        Vec3d nonPlayerVelocity = new Vec3d(0, 0, 0);
        boolean tryStomp = false;
        if (((Object) this) instanceof PlayerEntity player) {
            for (ItemStack armorItem : player.getArmorItems()) {
                if (armorItem.isEmpty())
                    continue;
                else if (armorItem.getItem() instanceof SoftSocksItem){
                    softSocks = true;
                }

                /*else if (armorItem.getItem().getEquipmentSlot(armorItem) == EquipmentSlot.FEET){
                    StompAndClimbForge.log("armor item is " + armorItem.getItem().toString() + " and the defense is " + ((ArmorItem)armorItem.getItem()).getDefense());
                    bootsArmor = ((ArmorItem)armorItem.getItem()).getDefense();
                }*/
                else if (armorItem.getItem() instanceof ArmorItem armorItemInstance && armorItemInstance.getType() == ArmorItem.Type.BOOTS){
                    //StompAndClimbForge.log("armor item is " + armorItem.getItem().toString() + " and the defense is " + armorItemInstance.getDefense());
                    bootsArmor = armorItemInstance.getProtection();
                }
            }


            //StompAndClimbForge.log("push called by player");
            velocity = playerVec;
            if (velocity.x != 0 || velocity.y != 0 || velocity.z != 0) {
                tryStomp = true;
            }
            if (player.isSneaking()) {
                tryStomp = false;
                double entityHeightDimensions = entity.getDimensions(EntityPose.STANDING).height;
                double thisHeightDimensions = getDimensions(EntityPose.STANDING).height;
                double heightDiffDimensions = entityHeightDimensions / thisHeightDimensions;
                if (heightDiffDimensions <= 1) {
                    //StompAndClimbForge.log("height difference not great enough");
                    return;
                }
            }

        } else {
            nonPlayerVelocity = ((Entity) (Object) this).getVelocity();
            if (nonPlayerVelocity.x != 0 || nonPlayerVelocity.z != 0) {
                tryStomp = true;
            }
        }

        if (tryStomp) {
            //StompAndClimbForge.log("tryStomp called");
            double entityHeightDimensions = entity.getDimensions(EntityPose.STANDING).height;
            double thisHeightDimensions = getDimensions(EntityPose.STANDING).height;
            double thisYPos = ((Entity) (Object) this).getPos().y;
            double theirYPos = entity.getPos().y + entityHeightDimensions;
            double YPosDifference = (Math.abs(thisYPos - theirYPos));
            boolean YPosCloseEnough = YPosDifference <= (thisHeightDimensions / 3);
            double heightDiffDimensions = thisHeightDimensions / entityHeightDimensions;
            if (entity instanceof LivingEntity && entity.isAlive() && heightDiffDimensions >= 4 && YPosCloseEnough) {
                if (!(entity instanceof PlayerEntity) || ((entity instanceof PlayerEntity) && ((PlayerEntity) entity).canTakeDamage())) {
                    for (ItemStack armorItem : entity.getArmorItems()) {
                        if (armorItem.isEmpty())
                            continue;
                        else if (armorItem.getItem().equals(StompAndClimb.HARD_HAT)) {
                            hardHat = true;
                        }
                    }
                    //StompAndClimbForge.log("stomp called");
                    DamageSource damageSource = ((LivingEntity) (Object) this).getDamageSources().genericKill();
                    //DamageSource damageSource = DamageSource.GENERIC;

                    if (hardHat && softSocks) {
                        entity.damage(damageSource, 0);
                    } else if (hardHat) {
                        entity.damage(damageSource, 1);
                    } else if (softSocks) {
                        entity.damage(damageSource, 1);
                    } else {
                        //StompAndClimbForge.log("stomp damage");
                        int armorpoints = ((LivingEntity) entity).getArmor();
                        if (armorpoints <= 0) {
                            armorpoints = 1;
                        }
                        if (bootsArmor <= 0){
                            bootsArmor = 1;
                        }
                        double heightDiffPow2 = Math.pow(heightDiffDimensions, 2);
                        double armorPtsSqrt = Math.sqrt(armorpoints);
                        float damageFull = (float) (heightDiffPow2 / armorPtsSqrt) * bootsArmor / 2f;

                        double velocityX = 0;
                        double velocityY = 0;
                        double velocityZ = 0;
                        float speedDamageMultiplier = 1;

                        Vec3d speedMultVelocity = new Vec3d(0, 0, 0);

                        if (((Object) this) instanceof PlayerEntity) {
                            speedMultVelocity = new Vec3d(velocity.x, velocity.y, velocity.z);
                        }
                        else {
                            speedMultVelocity = new Vec3d(nonPlayerVelocity.x, nonPlayerVelocity.y, nonPlayerVelocity.z);
                        }



                        float thisActualSize = ResizingUtils.getActualSize((Entity)(Object)this);
                        velocityX = (Math.abs(speedMultVelocity.x) / thisActualSize)+1;
                        velocityY = (Math.abs(speedMultVelocity.y)*2 / thisActualSize)+1;
                        velocityZ = (Math.abs(speedMultVelocity.z) / thisActualSize)+1;

                        speedDamageMultiplier = (float) (Math.pow(Math.max(velocityX, velocityZ),3) * velocityY);

                        float damageFinal = damageFull * speedDamageMultiplier;

                        //StompAndClimbForge.log("push called by player with velocity " + velocityX + " " + velocityY + " " + velocityZ);
                        //StompAndClimbForge.log("velocity multiplied together is " + speedDamageMultiplier);
                        //StompAndClimb.log("damage: " + damageFull + "/" + damageFinal);
                        entity.damage(damageSource, damageFinal);
                    }
                }
            }
        }
        ci.cancel();
    }

    @Inject(at = @At("TAIL"), method = "isClimbing", cancellable = true)
    public void postCheckClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof PlayerEntity) {
            for (ItemStack armorItem : ((PlayerEntity) ((Object) this)).getArmorItems()) {
                if (armorItem.isEmpty())
                    continue;
                else if (armorItem.getItem().equals(StompAndClimb.HOVER_BOOTS)) {
                    climbingPos = Optional.of(((PlayerEntity) ((Object) this)).getBlockPos());
                    cir.setReturnValue(true);
                    return;
                }
            }
        }


        LivingEntity entity = ((LivingEntity) (Object) this);
        BlockPos pos = entity.getBlockPos();
        Vec3d exactPos = entity.getPos();
        BlockPos eastPos = pos.east();
        BlockPos westPos = pos.west();
        BlockPos northPos = pos.north();
        BlockPos southPos = pos.south();
        boolean posIsShovelable = entity.getWorld().getBlockState(pos).getHardness(entity.getWorld(), pos) > 0 && entity.getWorld().getBlockState(pos).getHardness(entity.getWorld(), pos) <= 1f;
        boolean eastPosIsShovelable = entity.getWorld().getBlockState(eastPos).getHardness(entity.getWorld(), eastPos) > 0 && entity.getWorld().getBlockState(eastPos).getHardness(entity.getWorld(), eastPos) <= 1f;
        boolean westPosIsShovelable = entity.getWorld().getBlockState(westPos).getHardness(entity.getWorld(), westPos) > 0 && entity.getWorld().getBlockState(westPos).getHardness(entity.getWorld(), westPos) <= 1f;
        boolean northPosIsShovelable = entity.getWorld().getBlockState(northPos).getHardness(entity.getWorld(), northPos) > 0 && entity.getWorld().getBlockState(northPos).getHardness(entity.getWorld(), northPos) <= 1f;
        boolean southPosIsShovelable = entity.getWorld().getBlockState(southPos).getHardness(entity.getWorld(), southPos) > 0 && entity.getWorld().getBlockState(southPos).getHardness(entity.getWorld(), southPos) <= 1f;
        boolean posIsClimbable = !(entity.getWorld().getBlockState(pos).isAir()||(entity.getWorld().getBlockState(pos).isOf(Blocks.LIGHT)));
        boolean eastIsClimbable = !(entity.getWorld().getBlockState(eastPos).isAir()||(entity.getWorld().getBlockState(eastPos).isOf(Blocks.LIGHT)));
        boolean westIsClimbable = !(entity.getWorld().getBlockState(westPos).isAir()||(entity.getWorld().getBlockState(westPos).isOf(Blocks.LIGHT)));
        boolean northIsClimbable = !(entity.getWorld().getBlockState(northPos).isAir()||(entity.getWorld().getBlockState(northPos).isOf(Blocks.LIGHT)));
        boolean southIsClimbable = !(entity.getWorld().getBlockState(southPos).isAir()||(entity.getWorld().getBlockState(southPos).isOf(Blocks.LIGHT)));
        if (eastIsClimbable) {
            double eastPosWall = eastPos.toCenterPos().x - 0.5;
            double distToEastPos = Math.abs(exactPos.x - eastPosWall);
            if (distToEastPos > 0.0625){
                eastIsClimbable = false;
                eastPosIsShovelable = false;
            }

        }

        if (westIsClimbable) {
            double westPosWall = westPos.toCenterPos().x + 0.5;
            double distToWestPos = Math.abs(exactPos.x - westPosWall);
            if (distToWestPos > 0.0625){
                westIsClimbable = false;
                westPosIsShovelable = false;
            }

        }

        if (northIsClimbable) {
            double northPosWall = northPos.toCenterPos().z + 0.5;
            double distToNorthPos = Math.abs(exactPos.z - northPosWall);
            if (distToNorthPos > 0.0625){
                northIsClimbable = false;
                northPosIsShovelable = false;
            }

        }

        if (southIsClimbable) {
            double southPosWall = southPos.toCenterPos().z - 0.5;
            double distToSouthPos = Math.abs(exactPos.z - southPosWall);
            if (distToSouthPos > 0.0625){
                southIsClimbable = false;
                southPosIsShovelable = false;
            }

        }

        if (!posIsClimbable && !eastIsClimbable && !westIsClimbable && !northIsClimbable && !southIsClimbable) {
            return;
        }

//BlockPos.isWithinDistance
        //BlockState state = entity.getWorld().getBlockState(pos);
        /*if (state.getBlock() instanceof IContextAware ladderBlock) {
            if (ladderBlock.isLadder(
                    state, entity.getWorld(),
                    pos, entity
            )) {
                climbingPos = Optional.of(pos);
                cir.setReturnValue(true);
            }
        }*/
        if (((Object) this) instanceof PlayerEntity) {
            for (ItemStack item : ((PlayerEntity) ((Object) this)).getHandItems()) { // || item.isOf(Items.)
                boolean isSlime = item.isOf(Items.SLIME_BALL) || item.isOf(Items.SLIME_BLOCK) || item.isOf(Items.CHAIN) || item.isOf(Items.TWISTING_VINES) || item.isOf(Items.WEEPING_VINES) || item.isOf(Items.VINE) || item.isOf(Items.COBWEB) || item.isOf(Items.STRING) || item.isOf(Items.LADDER) || item.isOf(Items.TRIPWIRE_HOOK) || item.isOf(Items.HONEY_BLOCK) || item.isOf(Items.HONEYCOMB) || item.isOf(Items.HONEYCOMB_BLOCK) || item.isOf(Items.STICKY_PISTON) || item.isOf(Items.FISHING_ROD) || item.isOf(Items.PHANTOM_MEMBRANE);
                if (((PlayerEntity) ((Object) this)).getDimensions(EntityPose.STANDING).height <= .5 && (eastPosIsShovelable || westPosIsShovelable || northPosIsShovelable || southPosIsShovelable)) {
                    climbingPos = Optional.of(pos);
                    cir.setReturnValue(true);
                }
                else if(isSlime){
                    climbingPos = Optional.of(pos);
                    cir.setReturnValue(true);
                }
            }
        }

    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void immuneToPoisonIfBig(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if ((((Object) this) instanceof PlayerEntity) && effect.getEffectType() == StatusEffects.POISON && effect.getAmplifier() == 0 && ResizingUtils.getSize((Entity)(Object)this) >= 8) {
            cir.setReturnValue(false);
        }
    }

    /*@Inject(method = "pushAwayFrom", at = @At("HEAD"))
    private void injected(Entity entity, CallbackInfo ci ){
        if(entity instanceof LivingEntity && entity.getDimensions(EntityPose.STANDING).height < getDimensions(EntityPose.STANDING).height){
            entity.kill();
        }
    }*/

    /*@Inject(method = "tickMovement", at = @At("HEAD"))
    private void injected(CallbackInfo ci ){

    }

    //pushAwayFrom
    //applyMovementInput
    @Inject(method = "isClimbing", at = @At("TAIL"))
    private void injected(CallbackInfoReturnable<Boolean> cir ){

    }*/

}
