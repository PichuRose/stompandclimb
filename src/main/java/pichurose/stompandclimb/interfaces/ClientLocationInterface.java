package pichurose.stompandclimb.interfaces;


import net.minecraft.world.phys.Vec3;

public interface ClientLocationInterface {

    void stompandclimb_updateCache(Vec3 vec);
    void stompandclimb_updateIsAllowedToClimb(boolean isAllowedToClimb);
}
