package cz.jbradle.school.smap.laser;

/**
 * Configuration class. Implemented as singleton.
 *
 * Created by George on 30.11.2015.
 */
public class Configuration {

    private static final int DEFAULT_LASER_DIODE_PIN = 9;
    private static final int DEFAULT_LASER_DIODE_SENSOR_PIN = 10;
    private static final int DEFAULT_PHOTO_DIODE_PIN = 0;

    /**
     * Number of pin, that opens transistor of laser diode circle
     */
    private Integer laserDiodePin;

    /**
     * Number of pin, that reads analog signal from photo diode
     */
    private Integer photoDiodePin;

    /**
     * Number of pin, that reads digital signal from laser diode
     */
    private Integer laserDiodeSensorPin;

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

    public void setLaserDiodePin(int laserDiodePin) {
        this.laserDiodePin = laserDiodePin;
    }

    public void setPhotoDiodePin(Integer photoDiodePin) {
        this.photoDiodePin = photoDiodePin;
    }

    public void setLaserDiodeSensorPin(Integer laserDiodeSensorPin) {
        this.laserDiodeSensorPin = laserDiodeSensorPin;
    }

    public int getLaserDiodePin() {
        if (laserDiodePin == null){
            return DEFAULT_LASER_DIODE_PIN;
        }
        return laserDiodePin;
    }

    public Integer getPhotoDiodePin() {
        if (photoDiodePin == null){
            return DEFAULT_PHOTO_DIODE_PIN;
        }
        return photoDiodePin;
    }

    public Integer getLaserDiodeSensorPin() {
        if (laserDiodeSensorPin == null){
            return DEFAULT_LASER_DIODE_SENSOR_PIN;
        }
        return laserDiodeSensorPin;
    }
}
