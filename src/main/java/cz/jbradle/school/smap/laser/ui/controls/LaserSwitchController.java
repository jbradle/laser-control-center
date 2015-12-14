package cz.jbradle.school.smap.laser.ui.controls;

import cz.jbradle.school.smap.laser.Configuration;
import org.zu.ardulink.Link;
import org.zu.ardulink.event.DigitalReadChangeEvent;
import org.zu.ardulink.event.DigitalReadChangeListener;
import org.zu.ardulink.gui.DigitalPinStatus;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.gui.facility.IntMinMaxModel;
import org.zu.ardulink.protocol.IProtocol;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

public class LaserSwitchController extends JPanel implements Linkable {

    private Link link = Link.getDefaultInstance();

    public LaserSwitchController() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Power Laser");
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        add(label);

        JLabel lblStateLabel = new JLabel();
        lblStateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblStateLabel);

        JToggleButton switchToggleButton = new JToggleButton("Off");
        switchToggleButton.addItemListener(e -> {
            int pin = Configuration.getInstance().getLaserDiodePin();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                switchToggleButton.setText("On");
                link.sendPowerPinSwitch(pin, IProtocol.POWER_HIGH);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                switchToggleButton.setText("Off");
                link.sendPowerPinSwitch(pin, IProtocol.POWER_LOW);
            }
        });
        add(switchToggleButton);
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
