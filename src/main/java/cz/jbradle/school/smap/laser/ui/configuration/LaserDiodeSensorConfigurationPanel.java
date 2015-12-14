package cz.jbradle.school.smap.laser.ui.configuration;

import cz.jbradle.school.smap.laser.Configuration;
import org.zu.ardulink.gui.facility.IntMinMaxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Laser diode configuration panel
 *
 * Created by George on 30.11.2015.
 */
public class LaserDiodeSensorConfigurationPanel extends JPanel {

    @SuppressWarnings("unchecked")
    public LaserDiodeSensorConfigurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Laser Sensor Pin: ");
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        add(label);

        IntMinMaxModel pinComboBoxModel = new IntMinMaxModel(0, 13).withSelectedItem(
                Configuration.getInstance().getLaserDiodeSensorPin());
        JComboBox pinComboBox = new JComboBox(pinComboBoxModel);
        pinComboBox.addItemListener(itemEvent -> {
            if(itemEvent.getStateChange() == ItemEvent.SELECTED){
                Configuration.getInstance().setLaserDiodeSensorPin((Integer) itemEvent.getItem());
            }
        });

        add(pinComboBox);
    }
}
