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
public class LaserDiodeConfigurationPanel extends JPanel {

    @SuppressWarnings("unchecked")
    public LaserDiodeConfigurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Laser Diode Pin:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        add(label);

        IntMinMaxModel pinComboBoxModel = new IntMinMaxModel(0, 13).withSelectedItem(
                Configuration.getInstance().getLaserDiodePin());
        JComboBox pinComboBox = new JComboBox(pinComboBoxModel);
        pinComboBox.addItemListener(itemEvent -> {
            if(itemEvent.getStateChange() == ItemEvent.SELECTED){
                Configuration.getInstance().setLaserDiodePin((Integer) itemEvent.getItem());
            }
        });

        add(pinComboBox);
    }
}
