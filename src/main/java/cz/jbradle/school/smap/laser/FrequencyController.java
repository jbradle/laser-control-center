/**
 * Copyright 2013 Luciano Zu project Ardulink http://www.ardulink.org/
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Luciano Zu
 */

package cz.jbradle.school.smap.laser;

import org.zu.ardulink.Link;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.gui.facility.IntMinMaxModel;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class FrequencyController extends JPanel implements Linkable {

    private static final long serialVersionUID = 1754259889096759199L;

    private Link link = Link.getDefaultInstance();
    private ReplyMessageCallback replyMessageCallback = null;

    private JSpinner frequencySpinner;
    private JToggleButton toneButton;
    private JLabel durationLabel;
    private JSpinner durationSpinner;

    private JCheckBox durationCheckBox;


    /**
     * Create the valuePanelOff.
     */
    @SuppressWarnings("unchecked")
    public FrequencyController(int pinNumber) {
        setLayout(new BorderLayout(0, 0));

        JPanel configPanel = new JPanel();
        add(configPanel, BorderLayout.NORTH);
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));

        JPanel pinPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) pinPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        configPanel.add(pinPanel);

        JLabel pinLabel = new JLabel("Pin:");
        pinPanel.add(pinLabel);
        pinLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel pinNumLabel = new JLabel(String.valueOf(pinNumber));
        pinNumLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        pinPanel.add(pinNumLabel);

        JPanel frequencyPanel = new JPanel();
        configPanel.add(frequencyPanel);
        frequencyPanel.setLayout(new BoxLayout(frequencyPanel, BoxLayout.X_AXIS));

        JLabel frequencyLabel = new JLabel("Frequency (Hz):");
        frequencyLabel.setToolTipText("the frequency of the tone in hertz");
        frequencyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        frequencyPanel.add(frequencyLabel);

        frequencySpinner = new JSpinner();
        frequencySpinner.setModel(new SpinnerNumberModel(31, 1, 65535, 1));
        frequencyPanel.add(frequencySpinner);

        JPanel durationPanel = new JPanel();
        configPanel.add(durationPanel);
        durationPanel.setLayout(new BoxLayout(durationPanel, BoxLayout.X_AXIS));

        durationLabel = new JLabel("Duration (ms):");
        durationLabel.setToolTipText("the duration of the tone in milliseconds (optional)");
        durationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        durationLabel.setEnabled(false);
        durationPanel.add(durationLabel);

        durationSpinner = new JSpinner();
        durationSpinner.setPreferredSize(new Dimension(105, 28));
        durationSpinner.setModel(new SpinnerNumberModel(1000, 1, null, 1));
        durationSpinner.setEnabled(false);
        durationPanel.add(durationSpinner);

        durationCheckBox = new JCheckBox("Duration enabled");
        durationCheckBox.setSelected(false);
        durationCheckBox.addActionListener(e -> {
            if (durationCheckBox.isSelected()) {
                durationLabel.setEnabled(true);
                durationSpinner.setEnabled(true);
            } else {
                durationLabel.setEnabled(false);
                durationSpinner.setEnabled(false);
            }
        });
        durationPanel.add(durationCheckBox);


        toneButton = new JToggleButton("Off");
        toneButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Integer value = (Integer) frequencySpinner.getValue();
                if (durationCheckBox.isSelected()) {
                    link.sendToneMessage(pinNumber, value,
                            (Integer) durationSpinner.getValue());
                } else {
                    link.sendToneMessage(pinNumber, value);
                }
                updateToneButtonText();
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                link.sendNoToneMessage(pinNumber);
                updateToneButtonText();
            }
        });

        add(toneButton);

    }

    @Override
    public void setLink(Link link) {
        this.link = link;
    }


    private void updateToneButtonText() {
        if (toneButton.isSelected()) {
            String toneButtonOnText = "On";
            toneButton.setText(toneButtonOnText);
        } else {
            String toneButtonOffText = "Off";
            toneButton.setText(toneButtonOffText);
        }
    }

    public ReplyMessageCallback getReplyMessageCallback() {
        return replyMessageCallback;
    }

    public void setReplyMessageCallback(ReplyMessageCallback replyMessageCallback) {
        this.replyMessageCallback = replyMessageCallback;
    }


    public void setForeground(Color fg) {
        if (toneButton != null) {
            toneButton.setForeground(fg);
        }
    }

    public void setBackground(Color bg) {
        if (toneButton != null) {
            toneButton.setBackground(bg);
        }
    }

}
