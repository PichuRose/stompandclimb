package pichurose.stompandclimb.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pichurose.stompandclimb.StompAndClimb;
import pichurose.stompandclimb.interfaces.ClientLocationInterface;
import pichurose.stompandclimb.items.Armor.SoftSocksItem;
import pichurose.stompandclimb.utils.FlanUtils;
import pichurose.stompandclimb.utils.ResizingUtils;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ClientLocationInterface {

    @Shadow public abstract EntityDimensions getDimensions(Pose pose);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Shadow private Optional<BlockPos> lastClimbablePos;

    @Unique
    public Vec3 playerVec = Vec3.ZERO;


    @Override
    public void stompandclimb_updateCache(Vec3 vec) {
        playerVec = vec;
    }

    @Override
    public void stompandclimb_updateIsAllowedToClimb(boolean isAllowedToClimb) {
        this.isAllowedToClimb = isAllowedToClimb;
    }

    @Unique
    public boolean isAllowedToClimb = true;




    @Inject(method = "doPush", at = @At("HEAD"), cancellable = true)
    private void doPushButStompHead(Entity entity, CallbackInfo ci) {
        double thisHeight = ((Entity)(Object)this).getDimensions(Pose.STANDING).height;
        double entityHeight = entity.getDimensions(Pose.STANDING).height;
        boolean thisEntity = false;
        boolean entityThis = false;
        if(thisHeight>=entityHeight){
            //noinspection DataFlowIssue
            thisEntity = doStomp((Entity)(Object)this, entity);
        }
        if(entityHeight>=thisHeight){
            //noinspection DataFlowIssue
            entityThis = doStomp(entity, (Entity)(Object)this);
        }

        if(!entityThis && !thisEntity){
            ci.cancel();
        }
        if(!entityThis){
            StompAndClimb.threadLocal0.set(true);
        }
        if(!thisEntity){
            StompAndClimb.threadLocal1.set(true);
        }
    }

    @Inject(method = "doPush", at = @At("TAIL"))
    private void doPushButStompTail(Entity entity, CallbackInfo ci) {
        StompAndClimb.threadLocal0.remove();
        StompAndClimb.threadLocal1.remove();
    }

    @Unique
    private boolean doStomp(Entity collider, Entity collidee) {
        if (collidee instanceof Minecart) {return true;}
        if (collidee.isPassenger()) { return false; }
        if (collider.isPassenger()) { return false; }

        if(collidee instanceof Endermite || (collidee instanceof AgeableMob possibleBaby && possibleBaby.isBaby())){
            return false;
        }

        boolean softSocks = false, hardHat = false;

        float bootsArmor = 0;
        Vec3 velocity = new Vec3(0, 0, 0);
        Vec3 nonPlayerVelocity = new Vec3(0, 0, 0);
        boolean tryStomp = false;
        if (collider instanceof Player player) {
            for (ItemStack armorItem : player.getArmorSlots()) {
                if (armorItem.isEmpty())
                    //noinspection UnnecessaryContinue
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
                double collideeHeightDimensions = collidee.getDimensions(Pose.STANDING).height;
                double colliderHeightDimensions = getDimensions(Pose.STANDING).height;
                double heightDiffDimensions = collideeHeightDimensions / colliderHeightDimensions;
                if (heightDiffDimensions <= 1) {
                    //StompAndClimbForge.log("height difference not great enough");
                    return true;
                }
            }

        } else {
            nonPlayerVelocity = collider.getDeltaMovement();
            if (nonPlayerVelocity.x != 0 || nonPlayerVelocity.z != 0) {
                tryStomp = true;
            }
        }

        if (tryStomp) {
            //StompAndClimbForge.log("tryStomp called");
            double collideeHeightDimensions = collidee.getDimensions(Pose.STANDING).height;
            double colliderHeightDimensions = getDimensions(Pose.STANDING).height;
            double colliderYPos = collider.position().y;
            double collideeYPos = collidee.position().y + collideeHeightDimensions;
            double YPosDifference = (Math.abs(colliderYPos - collideeYPos));
            boolean YPosCloseEnough = YPosDifference <= (colliderHeightDimensions / 3);
            double heightDiffDimensions = colliderHeightDimensions / collideeHeightDimensions;
            if (collidee instanceof LivingEntity && collidee.isAlive() && heightDiffDimensions >= 4 && YPosCloseEnough) {
                if (!(collidee instanceof Player) || ((collidee instanceof Player) && ((Player) collidee).canBeSeenAsEnemy())) {
                    for (ItemStack armorItem : collidee.getArmorSlots()) {
                        if (armorItem.isEmpty())
                            //noinspection UnnecessaryContinue
                            continue;
                        else if (armorItem.getItem().equals(StompAndClimb.HARD_HAT)) {
                            hardHat = true;
                        }
                    }
                    //StompAndClimbForge.log("stomp called");
                    DamageSource damageSource;


                    if(StompAndClimb.hasFlan){
                        ResourceLocation perm = collidee instanceof Player ? FlanUtils.STOMP_PLAYER : FlanUtils.STOMP_MOB;

                        if(collider instanceof Player player) {
                            if (!FlanUtils.canInteract(player, collidee.blockPosition(), perm)) {
                                return false;
                            }
                        }
                        else{
                            if(!FlanUtils.permEnabled(collidee, perm)){
                                return false;
                            }
                        }
                    }

                    damageSource = collider.damageSources().generic();
                    if (hardHat && softSocks) {
                        collidee.hurt(damageSource, 0);
                    } else if (hardHat) {
                        collidee.hurt(damageSource, 1);
                    } else if (softSocks) {
                        collidee.hurt(damageSource, 1);
                    } else {
                        //StompAndClimbForge.log("stomp damage");
                        int armorpoints = ((LivingEntity) collidee).getArmorValue();
                        if (armorpoints <= 0) {
                            armorpoints = 1;
                        }
                        if (bootsArmor <= 0){
                            bootsArmor = 1;
                        }
                        double heightDiffPow2 = Math.pow(heightDiffDimensions, 2);
                        double armorPtsSqrt = Math.sqrt(armorpoints);
                        float damageFull = (float) (heightDiffPow2 / armorPtsSqrt) * bootsArmor / 2f;

                        float speedDamageMultiplier;

                        Vec3 speedMultVelocity;

                        if (collider instanceof Player) {
                            speedMultVelocity = new Vec3(velocity.x, velocity.y, velocity.z);
                        }
                        else {
                            speedMultVelocity = new Vec3(nonPlayerVelocity.x, nonPlayerVelocity.y, nonPlayerVelocity.z);
                        }



                        float colliderActualSize = ResizingUtils.getActualSize(collider);
                        double velocityX = (Math.abs(speedMultVelocity.x) / colliderActualSize)+1;
                        double velocityY = (Math.abs(speedMultVelocity.y)*2 / colliderActualSize)+1;
                        double velocityZ = (Math.abs(speedMultVelocity.z) / colliderActualSize)+1;

                        speedDamageMultiplier = (float) (Math.pow(Math.max(velocityX, velocityZ),3) * velocityY);

                        float damageFinal = damageFull * speedDamageMultiplier;

                        collidee.hurt(damageSource, damageFinal);
                    }
                }
            }
        }
        return false;
    }

    @Inject(at = @At("TAIL"), method = "onClimbable", cancellable = true)
    public void postCheckClimbable(CallbackInfoReturnable<Boolean> cir) {
        //noinspection DuplicateCondition, ConstantValue
        if(!(((Object)this) instanceof Player)){
            return;
        }

        if(!isAllowedToClimb){
            return;
        }

        //noinspection DuplicateCondition
        if (((Object) this) instanceof Player) {
            for (ItemStack armorItem : ((Player) ((Object) this)).getArmorSlots()) {
                if (armorItem.isEmpty())
                    //noinspection UnnecessaryContinue
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
        //boolean posIsShovelable = entity.level().getBlockState(pos).getDestroySpeed(entity.level(), pos) > 0 && entity.level().getBlockState(pos).getDestroySpeed(entity.level(), pos) <= 1f;
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
        //noinspection ConstantValue
        if ((((Object) this) instanceof Player) && effect.getEffect() == MobEffects.POISON && effect.getAmplifier() == 0 && ResizingUtils.getSize((Entity)(Object)this) >= 8) {
            cir.setReturnValue(false);
        }
    }

    //scale down knockback based on size if bigger than 1
    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void modifyKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        float entitySize = ResizingUtils.getActualSize(entity);
        if(entitySize <= 1f){
            return;
        }
        strength /= entitySize;
        strength *= (double)1.0F - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (!(strength <= (double)0.0F)) {
            entity.hasImpulse = true;
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec32 = (new Vec3(x, 0.0F, z)).normalize().scale(strength);
            entity.setDeltaMovement(vec3.x / (double)2.0F - vec32.x, entity.onGround() ? Math.min(0.4, vec3.y / (double)2.0F + strength) : vec3.y, vec3.z / (double)2.0F - vec32.z);
        }
        ci.cancel();
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void disableScreenMovement(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity entity = (LivingEntity) (Object) this;
        if(!(entity instanceof Player)){ return; }
        DamageType type = source.type();

        String[] allowedDamageTypes = {
                "MOB", "MOB_ATTACK_NO_AGGRO", "MOB_PROJECTILE",
                "HOTFLOOR", "CACTUS", "LAVA", "INFIRE", "SWEETBERRYBUSH",
                "THORNS", "TRIDENT", "ARROW", "FIREBALL", "CAMPFIRE"
        };
        boolean damageTypeNotAllowed = true;
        for(String allowedDamageType : allowedDamageTypes){
            //.substring(46,allowedDamageType.length()-1)
            String allowedDamageTypeCut = allowedDamageType.toLowerCase();
            if(type.toString().toLowerCase().contains(allowedDamageTypeCut)){
                damageTypeNotAllowed = false;
                ((Player)entity).displayClientMessage(Component.literal("Allowed! Type: " + type.toString()), true);
                break;
            }
            //((Player)entity).displayClientMessage(Component.literal("disallowed Type: "+allowedDamageTypeCut + " Type: " + type.toString()), false);
        }
        if (damageTypeNotAllowed) { ((Player)entity).displayClientMessage(Component.literal(type.toString()), false); return; }
        float entitySize = ResizingUtils.getActualSize(entity);
        if(entitySize < 8f){ return; }

        //no hurt if amount is less than 0.5 damage(1/4th heart) after dividing by size
        if (amount/entitySize <= 0.5 && entity instanceof Player) { cir.setReturnValue(false); }
    }
}
