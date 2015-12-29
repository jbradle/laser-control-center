package cz.jbradle.school.smap.laser.configuration;

import org.zu.ardulink.gui.facility.IntMinMaxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Laser diode configuration panel
 *
 * Created by George on 30.11.2015.
 */
public class LaserDiodeConfigurationPanel extends JPanel {

    private List<Configurable> configurables = new ArrayList<>();

    public LaserDiodeConfigurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Laser Diode Pin:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        add(label);
        Configuration configuration = Configuration.getInstance();
        for(int i = 0; i < configuration.getLaserDiodeCount(); i ++){
            addLaserPinComboBox(i, configuration);
        }
    }

    @SuppressWarnings("unchecked")
    private void addLaserPinComboBox(int laserIndex, Configuration configuration) {
        JLabel label = new JLabel(laserIndex + 1 + ":");
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(label);

        IntMinMaxModel pinComboBoxModel = new IntMinMaxModel(0, 13).withSelectedItem(
                configuration.getLaserDiodePin(laserIndex));
        JComboBox pinComboBox = new JComboBox(pinComboBoxModel);
        pinComboBox.addItemListener(itemEvent -> {
            if(itemEvent.getStateChange() == ItemEvent.SELECTED){
                configuration.setLaserDiodePin(laserIndex, (Integer) itemEvent.getItem());
                configurables.forEach(Configurable::reloadWithConfiguration);
            }
        });

        add(pinComboBox);
    }

    public void addConfigurable(Configurable configurable){
        configurables.add(configurable);
    }
}
