package cz.jbradle.school.smap.laser;

import cz.jbradle.school.smap.laser.configuration.Configurable;
import cz.jbradle.school.smap.laser.configuration.Configuration;
import org.zu.ardulink.Link;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.protocol.IProtocol;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Laser pin switch controller
 *
 * Created by George on 29.12.2015.
 */
public class LaserSwitchController  extends JPanel implements Linkable, Configurable {

    private int laserDiodeIndex;

    private JToggleButton switchToggleButton;

    private Link link = Link.getDefaultInstance();

    public LaserSwitchController() {
        reloadWithConfiguration();
    }

    @Override
    public void reloadWithConfiguration() {
        removeAll();
        setPreferredSize(new Dimension(125, 75));
        setLayout(null);
        JLabel indexLabel = new JLabel(String.valueOf(Configuration.getInstance().getLaserDiodePin(laserDiodeIndex)));
        indexLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        indexLabel.setBounds(66, 11, 47, 22);
        add(indexLabel);

        JLabel label = new JLabel("Laser Pin:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        label.setBounds(10, 15, 59, 14);
        add(label);

        switchToggleButton = new JToggleButton("Off");
        switchToggleButton.addItemListener(e -> {
            int pin = Configuration.getInstance().getLaserDiodePin(laserDiodeIndex);
            if(e.getStateChange() == ItemEvent.SELECTED) {
                switchToggleButton.setText("On");
                link.sendPowerPinSwitch(pin, IProtocol.POWER_HIGH);
            } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                switchToggleButton.setText("Off");
                link.sendPowerPinSwitch(pin, IProtocol.POWER_LOW);
            }
        });
        switchToggleButton.setBounds(10, 38, 103, 23);
        add(switchToggleButton);
        repaint();
    }

    public void setLaserDiodeIndex(int laserDiodeIndex) {
        this.laserDiodeIndex = laserDiodeIndex;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public ReplyMessageCallback getReplyMessageCallback() {
        throw new RuntimeException("Not developed yet");
    }

    public void setReplyMessageCallback(ReplyMessageCallback replyMessageCallback) {
        throw new RuntimeException("Not developed yet");
    }


}
