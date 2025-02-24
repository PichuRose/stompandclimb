package pichurose.stompandclimb.mixins;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.claim.Claim;
import io.github.flemmli97.flan.claim.ClaimStorage;
import io.github.flemmli97.flan.event.EntityInteractEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
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

    @Shadow public abstract EntityDimensions getDimensions(Pose pose);

    @Shadow private Optional<BlockPos> lastClimbablePos;
    @Unique
    public Vec3 playerVec = Vec3.ZERO;

    @Shadow protected abstract void actuallyHurt(DamageSource damageSource, float damageAmount);


    @Override
    public void stompandclimb_updateCache(Vec3 vec) {
        playerVec = vec;
    }




    @Inject(method = "doPush", at = @At("HEAD"), cancellable = true)
    private void injected(Entity entity, CallbackInfo ci) {
        if (entity instanceof Minecart) {
            //StompAndClimbForge.log("push called by minecart");
            return;
        }
        if (((Entity)(Object) this).getPassengers().contains(entity)) {
            //StompAndClimbForge.log("push called by passenger");
            return;
        }

        boolean softSocks = false;
        boolean hardHat = false;

        if (entity.getPassengers().contains(((Entity)(Object) this))) {
            //StompAndClimbForge.log("push called by passenger");
            softSocks = true;
        }

        float bootsArmor = 0;
        Vec3 velocity = new Vec3(0, 0, 0);
        Vec3 nonPlayerVelocity = new Vec3(0, 0, 0);
        boolean tryStomp = false;
        if (((Object) this) instanceof Player player) {
            for (ItemStack armorItem : player.getArmorSlots()) {
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
                    bootsArmor = armorItemInstance.getDefense();
                }
            }


            //StompAndClimbForge.log("push called by player");
            velocity = playerVec;
            if (velocity.x != 0 || velocity.y != 0 || velocity.z != 0) {
                tryStomp = true;
            }
            if (player.isShiftKeyDown()) {
                tryStomp = false;
                double entityHeightDimensions = entity.getDimensions(Pose.STANDING).height;
                double thisHeightDimensions = getDimensions(Pose.STANDING).height;
                double heightDiffDimensions = entityHeightDimensions / thisHeightDimensions;
                if (heightDiffDimensions <= 1) {
                    //StompAndClimbForge.log("height difference not great enough");
                    return;
                }
            }

        } else {
            nonPlayerVelocity = ((Entity) (Object) this).getDeltaMovement();
            if (nonPlayerVelocity.x != 0 || nonPlayerVelocity.z != 0) {
                tryStomp = true;
            }
        }

        if (tryStomp) {
            //StompAndClimbForge.log("tryStomp called");
            double entityHeightDimensions = entity.getDimensions(Pose.STANDING).height;
            double thisHeightDimensions = getDimensions(Pose.STANDING).height;
            double thisYPos = ((Entity) (Object) this).position().y;
            double theirYPos = entity.position().y + entityHeightDimensions;
            double YPosDifference = (Math.abs(thisYPos - theirYPos));
            boolean YPosCloseEnough = YPosDifference <= (thisHeightDimensions / 3);
            double heightDiffDimensions = thisHeightDimensions / entityHeightDimensions;
            if (entity instanceof LivingEntity && entity.isAlive() && heightDiffDimensions >= 4 && YPosCloseEnough) {
                if (!(entity instanceof Player) || ((entity instanceof Player) && ((Player) entity).canBeSeenAsEnemy())) {
                    for (ItemStack armorItem : entity.getArmorSlots()) {
                        if (armorItem.isEmpty())
                            continue;
                        else if (armorItem.getItem().equals(StompAndClimb.HARD_HAT)) {
                            hardHat = true;
                        }
                    }
                    //StompAndClimbForge.log("stomp called");
                    DamageSource damageSource;
                    if(((Object) this) instanceof Player player) {
                        damageSource = ((LivingEntity) (Object) this).damageSources().playerAttack(player);

                        if (!ClaimHandler.canInteract((ServerPlayer) player, entity.blockPosition(), ResourceLocation.of("flan:stomp", ':'))) {
                            ci.cancel();
                            return;
                        }
                    }
                    else{
                        damageSource = ((LivingEntity) (Object) this).damageSources().mobAttack((LivingEntity)entity);
                        boolean stompAllowedInFlan = false;
                        Claim claim;
                        try{
                            claim = ClaimStorage.get(entity.getServer().getLevel(entity.level().dimension())).getClaimAt(entity.blockPosition());
                        } catch(NullPointerException e){
                            claim = null;
                        }

                        if(claim != null){
                            ResourceLocation perm = new ResourceLocation("flan","stomp");

                            if (claim.parentClaim() == null) {
                                stompAllowedInFlan = claim.permEnabled(perm) == 1;
                            } else if (claim.permEnabled(perm) == -1) {
                                stompAllowedInFlan = claim.parentClaim().permEnabled(perm) == 1;
                            } else {
                                stompAllowedInFlan = claim.permEnabled(perm) == 1;
                            }

                            if(!stompAllowedInFlan){
                                ci.cancel();
                                return;
                            }
                        }
                    }

                    //DamageSource damageSource = ((LivingEntity) (Object) this).damageSources().cramming();
                    //((LivingEntity) (Object) this).doHurtTarget(entity);
                    //DamageSource damageSource = DamageSource.GENERIC;

                    //if(EntityInteractEvents.preventDamage(entity, damageSource)){

                    damageSource = ((LivingEntity) (Object) this).damageSources().generic();
                    if (hardHat && softSocks) {
                        entity.hurt(damageSource, 0);
                    } else if (hardHat) {
                        entity.hurt(damageSource, 1);
                    } else if (softSocks) {
                        entity.hurt(damageSource, 1);
                    } else {
                        //StompAndClimbForge.log("stomp damage");
                        int armorpoints = ((LivingEntity) entity).getArmorValue();
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

                        Vec3 speedMultVelocity = new Vec3(0, 0, 0);

                        if (((Object) this) instanceof Player) {
                            speedMultVelocity = new Vec3(velocity.x, velocity.y, velocity.z);
                        }
                        else {
                            speedMultVelocity = new Vec3(nonPlayerVelocity.x, nonPlayerVelocity.y, nonPlayerVelocity.z);
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
                        entity.hurt(damageSource, damageFinal);
                    }
                }
            }
        }
        ci.cancel();
    }

    @Inject(at = @At("TAIL"), method = "onClimbable", cancellable = true)
    public void postCheckClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof Player) {
            for (ItemStack armorItem : ((Player) ((Object) this)).getArmorSlots()) {
                if (armorItem.isEmpty())
                    continue;
                else if (armorItem.getItem().equals(StompAndClimb.HOVER_BOOTS)) {
                    lastClimbablePos = Optional.of(((Player) ((Object) this)).blockPosition());
                    cir.setReturnValue(true);
                    return;
                }
            }
        }


        LivingEntity entity = ((LivingEntity) (Object) this);
        BlockPos pos = entity.blockPosition();
        Vec3 exactPos = entity.position();
        BlockPos eastPos = pos.east();
        BlockPos westPos = pos.west();
        BlockPos northPos = pos.north();
        BlockPos southPos = pos.south();
        boolean posIsShovelable = entity.level().getBlockState(pos).getDestroySpeed(entity.level(), pos) > 0 && entity.level().getBlockState(pos).getDestroySpeed(entity.level(), pos) <= 1f;
        boolean eastPosIsShovelable = entity.level().getBlockState(eastPos).getDestroySpeed(entity.level(), eastPos) > 0 && entity.level().getBlockState(eastPos).getDestroySpeed(entity.level(), eastPos) <= 1f;
        boolean westPosIsShovelable = entity.level().getBlockState(westPos).getDestroySpeed(entity.level(), westPos) > 0 && entity.level().getBlockState(westPos).getDestroySpeed(entity.level(), westPos) <= 1f;
        boolean northPosIsShovelable = entity.level().getBlockState(northPos).getDestroySpeed(entity.level(), northPos) > 0 && entity.level().getBlockState(northPos).getDestroySpeed(entity.level(), northPos) <= 1f;
        boolean southPosIsShovelable = entity.level().getBlockState(southPos).getDestroySpeed(entity.level(), southPos) > 0 && entity.level().getBlockState(southPos).getDestroySpeed(entity.level(), southPos) <= 1f;
        boolean posIsClimbable = !(entity.level().getBlockState(pos).isAir()||(entity.level().getBlockState(pos).is(Blocks.LIGHT)));
        boolean eastIsClimbable = !(entity.level().getBlockState(eastPos).isAir()||(entity.level().getBlockState(eastPos).is(Blocks.LIGHT)));
        boolean westIsClimbable = !(entity.level().getBlockState(westPos).isAir()||(entity.level().getBlockState(westPos).is(Blocks.LIGHT)));
        boolean northIsClimbable = !(entity.level().getBlockState(northPos).isAir()||(entity.level().getBlockState(northPos).is(Blocks.LIGHT)));
        boolean southIsClimbable = !(entity.level().getBlockState(southPos).isAir()||(entity.level().getBlockState(southPos).is(Blocks.LIGHT)));
        if (eastIsClimbable) {
            double eastPosWall = eastPos.getCenter().x - 0.5;
            double distToEastPos = Math.abs(exactPos.x - eastPosWall);
            if (distToEastPos > 0.0625){
                eastIsClimbable = false;
                eastPosIsShovelable = false;
            }

        }

        if (westIsClimbable) {
            double westPosWall = westPos.getCenter().x + 0.5;
            double distToWestPos = Math.abs(exactPos.x - westPosWall);
            if (distToWestPos > 0.0625){
                westIsClimbable = false;
                westPosIsShovelable = false;
            }

        }

        if (northIsClimbable) {
            double northPosWall = northPos.getCenter().z + 0.5;
            double distToNorthPos = Math.abs(exactPos.z - northPosWall);
            if (distToNorthPos > 0.0625){
                northIsClimbable = false;
                northPosIsShovelable = false;
            }

        }

        if (southIsClimbable) {
            double southPosWall = southPos.getCenter().z - 0.5;
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
        //BlockState state = entity.level().getBlockState(pos);
        /*if (state.getBlock() instanceof IContextAware ladderBlock) {
            if (ladderBlock.isLadder(
                    state, entity.level(),
                    pos, entity
            )) {
                climbingPos = Optional.of(pos);
                cir.setReturnValue(true);
            }
        }*/
        if (((Object) this) instanceof Player) {
            for (ItemStack item : ((Player) ((Object) this)).getHandSlots()) { // || item.of(Items.)
                boolean isSlime = item.is(Items.SLIME_BALL) || item.is(Items.SLIME_BLOCK) || item.is(Items.CHAIN) || item.is(Items.TWISTING_VINES) || item.is(Items.WEEPING_VINES) || item.is(Items.VINE) || item.is(Items.COBWEB) || item.is(Items.STRING) || item.is(Items.LADDER) || item.is(Items.TRIPWIRE_HOOK) || item.is(Items.HONEY_BLOCK) || item.is(Items.HONEYCOMB) || item.is(Items.HONEYCOMB_BLOCK) || item.is(Items.STICKY_PISTON) || item.is(Items.FISHING_ROD) || item.is(Items.PHANTOM_MEMBRANE);
                if (((Player) ((Object) this)).getDimensions(Pose.STANDING).height <= .5 && (eastPosIsShovelable || westPosIsShovelable || northPosIsShovelable || southPosIsShovelable)) {
                    lastClimbablePos = Optional.of(pos);
                    cir.setReturnValue(true);
                }
                else if(isSlime){
                    lastClimbablePos = Optional.of(pos);
                    cir.setReturnValue(true);
                }
            }
        }

    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void immuneToPoisonIfBig(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if ((((Object) this) instanceof Player) && effect.getEffect() == MobEffects.POISON && effect.getAmplifier() == 0 && ResizingUtils.getSize((Entity)(Object)this) >= 8) {
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
