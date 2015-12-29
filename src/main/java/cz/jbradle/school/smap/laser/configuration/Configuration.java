package cz.jbradle.school.smap.laser.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class. Implemented as singleton.
 *
 * Created by George on 30.11.2015.
 */
public class Configuration {

    private static final int DEFAULT_LASER_DIODE_PIN = 9;
    private static final int DEFAULT_LASER_DIODE_COUNT = 3;

    /**
     * Map of laser diodes pins
     */
    private Map<Integer, Integer> laserDiodePinMap = new HashMap<>(DEFAULT_LASER_DIODE_COUNT);

    /**
     * Laser diode count
     */
    private Integer laserDiodeCount;

    private static Configuration instance = null;

    protected Configuration() {
        // Exists only to defeat instantiation.
    }

    public static Configuration getInstance() {
        if(instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public void setLaserDiodePin(int laserIndex, int laserDiodePin) {
        laserDiodePinMap.put(laserIndex, laserDiodePin);
    }

    public int getLaserDiodePin(int laserIndex) {
        if (!laserDiodePinMap.containsKey(laserIndex)){
            return DEFAULT_LASER_DIODE_PIN + laserIndex;
        }
        return laserDiodePinMap.get(laserIndex);
    }

    public Integer getLaserDiodeCount() {
        if (laserDiodeCount == null){
            return DEFAULT_LASER_DIODE_COUNT;
        }
        return laserDiodeCount;
    }
}
