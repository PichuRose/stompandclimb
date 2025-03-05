package pichurose.stompandclimb.utils;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.claim.Claim;
import io.github.flemmli97.flan.claim.ClaimStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import pichurose.stompandclimb.StompAndClimb;

public class FlanUtils {
    public static final ResourceLocation SIZE_CHANGING = ResourceLocation.of("stompandclimb:sizechanging", ':');
    public static final ResourceLocation STOMP_PLAYER = ResourceLocation.of("stompandclimb:stompplayer", ':');
    public static final ResourceLocation STOMP_MOB = ResourceLocation.of("stompandclimb:stompmob", ':');
    public static final ResourceLocation CLIMBING = ResourceLocation.of("stompandclimb:climbing", ':');

    public static boolean canInteract(Player player, Entity entity, ResourceLocation perm) {
        if(!StompAndClimb.hasFlan) { return true; }
        return ClaimHandler.canInteract((ServerPlayer) player, entity.blockPosition(), perm);
    }
    public static boolean canInteract(ServerPlayer player, Entity entity, ResourceLocation perm) {
        if(!StompAndClimb.hasFlan) { return true; }
        return ClaimHandler.canInteract(player, entity.blockPosition(), perm);
    }
    public static boolean canInteract(Player player, BlockPos pos, ResourceLocation perm) {
        if(!StompAndClimb.hasFlan) { return true; }
        return ClaimHandler.canInteract((ServerPlayer) player, pos, perm);
    }
    public static boolean canInteract(ServerPlayer player, BlockPos pos, ResourceLocation perm) {
        if(!StompAndClimb.hasFlan) { return true; }
        return ClaimHandler.canInteract(player, pos, perm);
    }

    private static Claim getClaim(Entity entity){
        if(!StompAndClimb.hasFlan) { return null; }
        if (!entity.level().isClientSide() && entity.level() instanceof ServerLevel serverLevel){
            return ClaimStorage.get(serverLevel).getClaimAt(entity.blockPosition());
        }
        return null;
    }
    private static Claim getClaim(ServerLevel serverLevel, BlockPos pos){
        if(!StompAndClimb.hasFlan) { return null; }
        return ClaimStorage.get(serverLevel).getClaimAt(pos);
    }

    public static boolean permEnabled(Entity entity, ResourceLocation perm){
        if(!StompAndClimb.hasFlan) { return true; }
        Claim claim = getClaim(entity);
        if(claim == null) return true;
        int status = claim.permEnabled(perm);
        if (status != 1 && claim.parentClaim() != null) status = claim.parentClaim().permEnabled(perm);
        return status == 1;
    }
    @SuppressWarnings("unused")
    public static boolean permEnabled(ServerLevel serverLevel, BlockPos pos, ResourceLocation perm){
        if(!StompAndClimb.hasFlan) { return true; }
        Claim claim = getClaim(serverLevel, pos);
        if(claim == null) return true;
        int status = claim.permEnabled(perm);
        if (status != 1 && claim.parentClaim() != null) status = claim.parentClaim().permEnabled(perm);
        return status == 1;
    }


    /*
    if(claim != null) {
                ResourceLocation perm = CLIMBING;
                int status = claim.permEnabled(perm);
                if (status != 1 && claim.parentClaim() != null) status = claim.parentClaim().permEnabled(perm);
                isAllowedToClimb = status == 1;
            }
     */
}
