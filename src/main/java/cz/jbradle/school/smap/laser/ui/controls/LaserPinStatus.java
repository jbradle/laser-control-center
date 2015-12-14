package cz.jbradle.school.smap.laser.ui.controls;

import cz.jbradle.school.smap.laser.Configuration;
import org.zu.ardulink.Link;
import org.zu.ardulink.event.DigitalReadChangeEvent;
import org.zu.ardulink.event.DigitalReadChangeListener;
import org.zu.ardulink.gui.DigitalPinStatus;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Laser pin status
 *
 * Created by George on 2.12.2015.
 */
public class LaserPinStatus extends JPanel implements DigitalReadChangeListener, Linkable {

    private static final long serialVersionUID = -7773514191770737230L;

    private JLabel lblStateLabel;
    private JToggleButton toggleButton;

    private Link link = Link.getDefaultInstance();


    private static final String HIGH_ICON_NAME = "icons/blue-on-32.png";
    private static final String LOW_ICON_NAME = "icons/blue-off-32.png";

    private static final ImageIcon HIGH_ICON = new ImageIcon(DigitalPinStatus.class.getResource(HIGH_ICON_NAME));
    private static final ImageIcon LOW_ICON = new ImageIcon(DigitalPinStatus.class.getResource(LOW_ICON_NAME));


    /**
     * Create the panel.
     */
    public LaserPinStatus() {
        setLayout(new GridLayout(3, 1, 0, 0));

        lblStateLabel = new JLabel("Laser Diode");
        lblStateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblStateLabel.setIcon(LOW_ICON);
        lblStateLabel.setEnabled(false);
        add(lblStateLabel);

        JPanel comboPanel = new JPanel();
        add(comboPanel);


        toggleButton = new JToggleButton("Sensor off");
        toggleButton.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                link.addDigitalReadChangeListener((DigitalReadChangeListener) toggleButton.getParent());

                toggleButton.setText("Sensor on");
                lblStateLabel.setEnabled(true);


            } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                link.removeDigitalReadChangeListener((DigitalReadChangeListener) toggleButton.getParent());

                toggleButton.setText("Sensor off");
                lblStateLabel.setEnabled(false);
            }
        });
        add(toggleButton);
    }

    @Override
    public void stateChanged(DigitalReadChangeEvent e) {
        int value = e.getValue();
        if(value == DigitalReadChangeEvent.POWER_HIGH) {
            lblStateLabel.setIcon(HIGH_ICON);
        } else if(value == DigitalReadChangeEvent.POWER_LOW) {
            lblStateLabel.setIcon(LOW_ICON);
        }
    }

    @Override
    public int getPinListening() {
        return Configuration.getInstance().getLaserDiodeSensorPin();
    }

    public void setLink(Link link) {
        if(this.link != null) {
            this.link.removeDigitalReadChangeListener((DigitalReadChangeListener) toggleButton.getParent());
        }
        toggleButton.setText("Sensor off");
        lblStateLabel.setEnabled(false);
        this.link = link;
    }

    public ReplyMessageCallback getReplyMessageCallback() {
        throw new RuntimeException("Not developed yet");
    }

    public void setReplyMessageCallback(ReplyMessageCallback replyMessageCallback) {
        throw new RuntimeException("Not developed yet");
    }
}
