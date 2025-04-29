package pichurose.stompandclimb.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import pichurose.stompandclimb.interfaces.CustomCarryOffsetInterface;
import pichurose.stompandclimb.network.StompAndClimbNetworkingConstants;

import java.util.Objects;

public class StompAndClimbCustomCarryCommand {

    private static final double[][] places = {
        //head
            {0,     0,      0},
        //right_hand
            {0.5,   -1,     -0.5},
        //left_hand
            {0.5,   -1,     0.5},
        //boob
            {0.27,   -1.1,   0},
        //mouth
            {0.25,  -0.8,   0},
        //left_shoulder
            {0,     -0.8,   0.33},
        //right_shoulder
            {0,     -0.8,   -0.33},
        //gut
            {0,     -1.3,   0},
        //belly_button
            {0.15,  -1.35,  0},
        //hip
            {0.15,  -1.5,   0},
        //butt
            {-0.15, -1.5,   0},
        //inshoe_left
            {0,     -2.2,   0.15},
        //inshoe_right
            {0,     -2.2,   -0.15},
        //ear_left
            {0,     -0.66,  0.28},
        //ear_right
            {0,     -0.66,  -0.28},
        //back
            {-0.14, -1.1,   0},
        //armpit_left
            {0,     -1.1,   0.27},
        //armpit_right
            {0,     -1.1,   -0.27},
        //elbow_left
            {-0.14, -1.2,   0.33},
        //elbow_right
            {-0.14, -1.2,   -0.33},
        //knee_left
            {-0.15, -1.8,   0.15},
        //knee_right
            {-0.15, -1.8,   -0.15},
        //ankle_left
            {0,     -2,     0.28},
        //ankle_right
            {0,     -2,     -0.28},
        //wrist_left
            {0.15,  -1.45,  0.37},
        //wrist_right
            {0.15,  -1.45,  -0.37},
        //thigh_left
            {0.15,  -1.6,   0.15},
        //thigh_right
            {0.15,  -1.6,   -0.15},
        //throat
            {0,     -0.8,   0},
        //between_legs
            {0,     -1.62,  0},
    };

    public static int convertStringToPlaceLocation(String place, Player player){
        return switch (place.toLowerCase().strip()) {
            case "head", "default", "hat", "hair", "above", "noggin", "cranium", "skull", "heads", "hats", "hairs" -> 0;
            case "hand", "hands", "arm", "uppies", "pickup", "grab", "yoink", "mine", "palm" -> getDominantHand(Objects.requireNonNull(player));
            case "right_hand", "righthand", "right", "righty", "right-hand", "right_hands", "righthands", "right-hands" -> 1;
            case "left_hand", "lefthand", "left", "lefty", "left-hand", "left_hands", "lefthands", "left-hands" -> 2;
            case "chest", "boob", "boobs", "bust", "breast", "breasts", "booba", "tits", "tit", "booby", "boobies", "badonkadonks", "boobas", "chests", "busts" -> 3;
            case "mouth", "lips", "teeth", "tongue", "bite", "chew", "chewtoy", "maw", "snack", "food", "mouths", "tongues", "bites", "chews", "snacks", "foods" -> 4;
            case "left_shoulder", "leftshoulder", "left_shoulderpad", "leftshoulderpad", "left-shoulder", "left-shoulderpad", "left_shoulders", "leftshoulders", "left-shoulders" -> 5;
            case "right_shoulder", "shoulder", "rightshoulder", "right_shoulderpad", "rightshoulderpad", "right-shoulder", "right-shoulderpad", "right_shoulders", "rightshoulders", "right-shoulders" -> 6;
            case "stomach", "gut", "tummy", "belly", "abdomen", "vore", "inside", "guts", "stomachs", "tummies", "bellies", "abdomens" -> 7;
            case "belly_button", "navel", "bellybutton", "tummy_button", "tummybutton", "midriff", "belly-buttons", "bellybuttons", "navels", "tummy-buttons", "tummybuttons", "midriffs" -> 8;
            case "hip", "waist", "pocket", "pockets", "waistline", "hips", "waists", "waistlines" -> 9;
            case "butt", "booty", "ass", "buttocks", "bottom", "backside", "rear", "rump", "derriere", "buttcheek", "buttcheeks", "bootylicious", "butts", "asses", "booties", "bottoms", "backsides", "rears", "rumps", "derrieres" -> 10;
            case "left_foot", "leftshoe", "left_shoe", "inshoe_left", "leftfoot", "inshoe-left", "left-shoe", "left-foot", "left_shoes", "leftshoes", "left_feet", "leftfeet" -> 11;
            case "right_foot", "foot", "rightshoe", "right_shoe", "inshoe_right", "rightfoot", "inshoe-right", "right-shoe", "right-foot", "right_shoes", "rightshoes", "right_feet", "rightfeet" -> 12;
            case "ear_left", "left_ear", "leftear", "left_hearing", "left-ear", "left_ears", "leftears", "left_hearings" -> 13;
            case "ear_right", "ear", "right_ear", "rightear", "right_hearing", "right-ear", "right_ears", "rightears", "right_hearings" -> 14;
            case "back", "spine", "shoulder_blade", "shoulderblade", "behind", "behind_back", "backs", "spines", "shoulder_blades", "shoulderblades" -> 15;
            case "armpit_left", "left_armpit", "leftarmpit", "left_underarm", "left-armpit", "left_underarms", "leftarmpits", "left-armpits" -> 16;
            case "armpit_right", "armpit", "right_armpit", "rightarmpit", "right_underarm", "right-armpit", "right_underarms", "rightarmpits", "right-armpits" -> 17;
            case "elbow_left", "left_elbow", "leftelbow", "left-elbow", "left_elbows", "leftelbows", "left-elbows" -> 18;
            case "elbow_right", "elbow", "right_elbow", "rightelbow", "right-elbow", "right_elbows", "rightelbows", "right-elbows" -> 19;
            case "knee_left", "left_knee", "leftknee", "left-knee", "left_knees", "leftknees", "left-knees" -> 20;
            case "knee_right", "knee", "right_knee", "rightknee", "right-knee", "right_knees", "rightknees", "right-knees" -> 21;
            case "ankle_left", "left_ankle", "leftankle", "left-ankle", "left_ankles", "leftankles", "left-ankles" -> 22;
            case "ankle_right", "ankle", "right_ankle", "rightankle", "right-ankle", "right_ankles", "rightankles", "right-ankles" -> 23;
            case "wrist_left", "left_wrist", "leftwrist", "left-wrist", "left_wrists", "leftwrists", "left-wrists" -> 24;
            case "wrist_right", "wrist", "right_wrist", "rightwrist", "right-wrist", "right_wrists", "rightwrists", "right-wrists" -> 25;
            case "thigh_left", "left_thigh", "leftthigh", "left-thigh", "left_thighs", "leftthighs", "left-thighs" -> 26;
            case "thigh_right", "thigh", "right_thigh", "rightthigh", "right-thigh", "right_thighs", "rightthighs", "right-thighs" -> 27;
            case "throat", "neck", "esophagus", "windpipe", "larynx", "pharynx", "glrk", "gulp", "swallow", "gullet", "throats", "necks", "esophaguses", "windpipes", "larynxes", "pharynxes", "gullets" -> 28;
            case "between_legs", "between_thighs", "betweenlegs", "between-thighs", "betweenthighs", "between-legs" -> 29;
            default -> 0;
        };
    }

    //TODO: Add a method to executeCommandWithStringAndXYZParameters to offset one of the default locations, and add a command for it as well.

    public static int executeCommandWithStringArg(CommandContext<CommandSourceStack> context) {
        String place = context.getArgument("place", String.class);
        int placeLocation = convertStringToPlaceLocation(place, context.getSource().getPlayer());
        //TODO: Add Lay Down Pose for Passenger as a Toggle, enable if inshoe, mouth, etc
        boolean holdOutHand = placeLocation == getDominantHand(Objects.requireNonNull(context.getSource().getPlayer()));
        boolean followBodyAngle = placeLocation != 4 && placeLocation != 13 && placeLocation != 14;
        boolean invisiblePassengers = placeLocation == 7 || placeLocation == 28;
        double[] placeLocArr = places[placeLocation];
        executeCarrying(placeLocArr, context.getSource().getPlayer(), holdOutHand, followBodyAngle, invisiblePassengers);
        context.getSource().sendSuccess(() -> Component.literal("Called /custom_carry with x = %s, y = %s, z = %s, holdOutHand = %s, followHead = %s, invisiblePassengers = %s".formatted(placeLocArr[0], placeLocArr[1], placeLocArr[2], holdOutHand, !followBodyAngle, invisiblePassengers)), false);
        return 1;
    }

    public static int getDominantHand(Player player){
        return player.getMainArm().equals(HumanoidArm.RIGHT) ? 1 : 2;
    }

    public static int executeCustomCarryWithXYZAndParameters(CommandContext<CommandSourceStack> context) {
        double x = DoubleArgumentType.getDouble(context, "x");
        double y = DoubleArgumentType.getDouble(context, "y");
        double z = DoubleArgumentType.getDouble(context, "z");
        boolean holdOutHand = BoolArgumentType.getBool(context, "holdOutHand");
        boolean followBodyAngle = !BoolArgumentType.getBool(context, "followHead");
        boolean invisiblePassengers = BoolArgumentType.getBool(context, "invisiblePassengers");
        executeCarrying(x, y, z, context.getSource().getPlayer(), holdOutHand, followBodyAngle, invisiblePassengers);
        context.getSource().sendSuccess(() -> Component.literal("Called /custom_carry with x = %s, y = %s, z = %s, holdOutHand = %s, followHead = %s, invisiblePassengers = %s".formatted(x, y, z, holdOutHand, !followBodyAngle, invisiblePassengers)), false);
        return 1;
    }

    public static int executeCustomCarryWithXYZ(CommandContext<CommandSourceStack> context) {
        double x = DoubleArgumentType.getDouble(context, "x");
        double y = DoubleArgumentType.getDouble(context, "y");
        double z = DoubleArgumentType.getDouble(context, "z");
        boolean holdOutHand = false;
        boolean followBodyAngle = true;
        boolean invisiblePassengers = false;
        executeCarrying(x, y, z, context.getSource().getPlayer(), holdOutHand, followBodyAngle, invisiblePassengers);
        context.getSource().sendSuccess(() -> Component.literal("Called /custom_carry with x = %s, y = %s, z = %s, holdOutHand = %s, followHead = %s, invisiblePassengers = %s".formatted(x, y, z, holdOutHand, !followBodyAngle, invisiblePassengers)), false);
        return 1;
    }


    public static void executeCarrying(double[] relativeLocation, ServerPlayer player, boolean holdOutHand, boolean followBodyAngle, boolean invisiblePassengers){
        executeCarrying(relativeLocation[0], relativeLocation[1], relativeLocation[2], player, holdOutHand, followBodyAngle, invisiblePassengers);
    }

    public static void executeCarrying(double x, double y, double z, ServerPlayer player, boolean holdOutHand, boolean followBodyAngle, boolean invisiblePassengers){

        CustomCarryOffsetInterface customCarryOffsetInterface = (CustomCarryOffsetInterface)(player);
        customCarryOffsetInterface.stompandclimb_updateCustomCarryCache(x, y, z, holdOutHand, followBodyAngle, invisiblePassengers);


        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeBoolean(holdOutHand);
        buf.writeBoolean(followBodyAngle);
        buf.writeBoolean(invisiblePassengers);
        ServerPlayNetworking.send(player, StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_CLIENT_PACKET, buf);

        for (ServerPlayer otherPlayer : Objects.requireNonNull(player.level().getServer()).getPlayerList().getPlayers()) {
            if (otherPlayer != player) {
                FriendlyByteBuf buf2 = PacketByteBufs.create();
                buf2.writeUtf(player.getUUID().toString());
                buf2.writeDouble(x);
                buf2.writeDouble(y);
                buf2.writeDouble(z);
                buf2.writeBoolean(holdOutHand);
                buf2.writeBoolean(followBodyAngle);
                buf2.writeBoolean(invisiblePassengers);
                ServerPlayNetworking.send(otherPlayer, StompAndClimbNetworkingConstants.CUSTOM_CARRY_POS_SENDALL_PACKET, buf2);
            }
        }

    }
}
