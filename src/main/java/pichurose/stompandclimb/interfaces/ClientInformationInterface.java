package pichurose.stompandclimb.interfaces;


import net.minecraft.world.phys.Vec3;

public interface ClientInformationInterface {

    void stompandclimb_updateCache(Vec3 vec);
    void stompandclimb_updateIsAllowedToClimb(boolean isAllowedToClimb);
    void stompandclimb_updateIsAllowedToCollect(boolean isAllowedToCollect);

    boolean stompandclimb_getIsAllowedToCollect();
}
