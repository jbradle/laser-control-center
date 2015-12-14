package cz.jbradle.school.smap.laser.ui.configuration;

import cz.jbradle.school.smap.laser.Configuration;
import org.zu.ardulink.gui.facility.IntMinMaxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Configuration panel
 *
 * Created by George on 30.11.2015.
 */
public class PhotoDiodeConfigurationPanel extends JPanel {


    @SuppressWarnings("unchecked")
    public PhotoDiodeConfigurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Photo Diode Pin: ");
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        add(label);

        IntMinMaxModel pinComboBoxModel = new IntMinMaxModel(0, 4).withSelectedItem(
                Configuration.getInstance().getPhotoDiodePin());
        JComboBox pinComboBox = new JComboBox(pinComboBoxModel);
        pinComboBox.addItemListener(itemEvent -> {
            if(itemEvent.getStateChange() == ItemEvent.SELECTED){
                Configuration.getInstance().setPhotoDiodePin((Integer) itemEvent.getItem());
            }
        });

        add(pinComboBox);
    }
}
