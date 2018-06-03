package com.mygdx.game.constant;

import java.util.HashMap;

/**
 * @author xiezd 2018-06-01 7:51
 */
public class PlayerConstants {
    public static final Integer STEP_LEVEL_LOW = 1;
    public static final Float STEP_LEVEL_LOW_STEP = 0.08F;
    public static final Integer STEP_LEVEL_MID = 2;
    public static final Float STEP_LEVEL_MID_STEP = 0.12F;
    public static final Integer STEP_LEVEL_HIGH = 3;
    public static final Float STEP_LEVEL_HIGH_STEP = 0.16F;
    public static HashMap<Integer, Float> SPEED_MAP;
    
    public static final float PLAYER_SIZE_WIDTH = 1.2f;
    public static final float PLAYER_SIZE_HEIGHT = 1.2f;
    static {
    	SPEED_MAP = new HashMap<Integer, Float>();
    	SPEED_MAP.put(STEP_LEVEL_LOW, STEP_LEVEL_LOW_STEP);
    	SPEED_MAP.put(STEP_LEVEL_MID, STEP_LEVEL_MID_STEP);
    	SPEED_MAP.put(STEP_LEVEL_HIGH, STEP_LEVEL_HIGH_STEP);
    }
}
