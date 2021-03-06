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

import cz.jbradle.school.smap.laser.configuration.Configurable;
import cz.jbradle.school.smap.laser.configuration.Configuration;
import org.zu.ardulink.Link;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.gui.PWMController;
import org.zu.ardulink.gui.event.PWMChangeEvent;
import org.zu.ardulink.gui.event.PWMControllerListener;
import org.zu.ardulink.gui.facility.IntMinMaxModel;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class LaserPWMController extends PWMController implements Linkable, Configurable {

    private static final long serialVersionUID = 7927439571760351922L;

    private JSlider powerSlider;
    private JComboBox valueComboBox;
    private IntMinMaxModel valueComboBoxModel;
    private JLabel voltValueLbl;
    private JCheckBox chckbxContChange;
    private JProgressBar progressBar;
    private IntMinMaxModel maxValueComboBoxModel;
    private IntMinMaxModel minValueComboBoxModel;
    private JLabel lblPowerPinController;

    private List<PWMControllerListener> pwmControllerListeners = new LinkedList<>();

    private Link link = Link.getDefaultInstance();
    private int laserDiodeIndex;

    /**
     * Create the panel.
     */
    public LaserPWMController() {
        reloadWithConfiguration();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reloadWithConfiguration() {
        removeAll();
        setPreferredSize(new Dimension(195, 260));
        setLayout(null);

        powerSlider = new JSlider();
        powerSlider.setFont(new Font("SansSerif", Font.PLAIN, 11));
        powerSlider.setMajorTickSpacing(15);
        powerSlider.setPaintLabels(true);
        powerSlider.setPaintTicks(true);
        powerSlider.setMaximum(255);
        powerSlider.setValue(0);
        powerSlider.setOrientation(SwingConstants.VERTICAL);
        powerSlider.setBounds(126, 38, 59, 199);
        add(powerSlider);

        JLabel lblPowerPin = new JLabel("Laser Pin:");
        lblPowerPin.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblPowerPin.setBounds(10, 40, 59, 14);
        add(lblPowerPin);

        JLabel pinLabel = new JLabel(String.valueOf(Configuration.getInstance().getLaserDiodePin(laserDiodeIndex)));
        pinLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        pinLabel.setBounds(65, 36, 55, 22);
        add(pinLabel);

        maxValueComboBoxModel = new IntMinMaxModel(0, 255).withLastItemSelected();
        JComboBox maxValueComboBox = new JComboBox(maxValueComboBoxModel);
        maxValueComboBox.setBounds(65, 65, 55, 22);
        add(maxValueComboBox);

        minValueComboBoxModel = new IntMinMaxModel(0, 255).withFirstItemSelected();
        JComboBox minValueComboBox = new JComboBox(minValueComboBoxModel);
        minValueComboBox.setBounds(65, 217, 55, 22);
        add(minValueComboBox);

        JLabel lblMaxValue = new JLabel("Max Value:");
        lblMaxValue.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblMaxValue.setBounds(10, 69, 59, 14);
        add(lblMaxValue);

        JLabel lblMinValue = new JLabel("Min Value:");
        lblMinValue.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblMinValue.setBounds(10, 221, 59, 14);
        add(lblMinValue);

        progressBar = new JProgressBar();
        progressBar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        progressBar.setStringPainted(true);
        progressBar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        progressBar.setOrientation(SwingConstants.VERTICAL);
        progressBar.setBounds(96, 98, 16, 108);
        add(progressBar);

        lblPowerPinController = new JLabel("Laser " + (laserDiodeIndex + 1) + " Controller");
        lblPowerPinController.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPowerPinController.setToolTipText("Power With Modulation");
        lblPowerPinController.setHorizontalAlignment(SwingConstants.CENTER);
        lblPowerPinController.setBounds(10, 11, 175, 14);
        add(lblPowerPinController);

        JLabel lblVoltOutput = new JLabel("Volt Output:");
        lblVoltOutput.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblVoltOutput.setBounds(10, 143, 59, 14);
        add(lblVoltOutput);

        voltValueLbl = new JLabel("0V");
        voltValueLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        voltValueLbl.setBounds(10, 157, 76, 14);
        add(voltValueLbl);

        JLabel lblCurrentValue = new JLabel("Current Value:");
        lblCurrentValue.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCurrentValue.setBounds(10, 98, 76, 14);
        add(lblCurrentValue);

        valueComboBoxModel = new IntMinMaxModel(0, 255).withFirstItemSelected();
        valueComboBox = new JComboBox(valueComboBoxModel);
        valueComboBox.addActionListener(e -> {
            int comboBoxCurrentValue = valueComboBoxModel.getSelectedItem();
            int powerSliderCurrentValue = powerSlider.getValue();
            if (comboBoxCurrentValue != powerSliderCurrentValue) {
                powerSlider.setValue(comboBoxCurrentValue);
            }
        });
        valueComboBox.setBounds(10, 112, 55, 22);
        add(valueComboBox);

        JLabel lblContinuousChange = new JLabel("Cont. Change:");
        lblContinuousChange.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblContinuousChange.setToolTipText("Continuous Change");
        lblContinuousChange.setBounds(10, 176, 73, 14);
        add(lblContinuousChange);

        chckbxContChange = new JCheckBox("");
        chckbxContChange.setRequestFocusEnabled(false);
        chckbxContChange.setRolloverEnabled(true);
        chckbxContChange.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        chckbxContChange.setSelected(true);
        chckbxContChange.setBounds(6, 188, 21, 22);
        add(chckbxContChange);

        powerSlider.addChangeListener(e -> {
            if (!powerSlider.getValueIsAdjusting() || chckbxContChange.isSelected()) {
                int powerValue = powerSlider.getValue();
                valueComboBoxModel.setSelectedItem(powerValue);
                float volt = powerValue * 2.15f / 255.0f;
                voltValueLbl.setText(String.valueOf(volt) + "V");
                float progress = (powerValue - powerSlider.getMinimum()) * 100.0f
                        / ((float) powerSlider.getMaximum() - (float) powerSlider.getMinimum());
                progressBar.setValue((int) progress);

                notifyListeners(powerValue);

                int pin = Configuration.getInstance().getLaserDiodePin(laserDiodeIndex);
                link.sendPowerPinIntensity(pin, powerValue);
            }
        });

        minValueComboBox.addActionListener(e -> {
            int maximum = maxValueComboBoxModel.getSelectedItem();
            int minimum = minValueComboBoxModel.getSelectedItem();

            if (minimum > maximum) {
                minValueComboBoxModel.setSelectedItem(maximum);
            }

            valueComboBoxModel = new IntMinMaxModel(minimum, maximum);
            valueComboBox.setModel(valueComboBoxModel);
            powerSlider.setMinimum(minimum);
        });

        maxValueComboBox.addActionListener(e -> {
            int maximum = maxValueComboBoxModel.getSelectedItem();
            int minimum = minValueComboBoxModel.getSelectedItem();

            if (minimum > maximum) {
                maxValueComboBoxModel.setSelectedItem(minimum);
            }

            valueComboBoxModel = new IntMinMaxModel(minimum, maximum);
            valueComboBox.setModel(valueComboBoxModel);
            powerSlider.setMaximum(maximum);
        });
        repaint();
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

    public void setTitle(String title) {
        lblPowerPinController.setText(title);
    }

    public boolean addPWMControllerListener(PWMControllerListener listener) {
        return pwmControllerListeners.add(listener);
    }

    public boolean removePWMControllerListener(PWMControllerListener listener) {
        return pwmControllerListeners.remove(listener);
    }

    private void notifyListeners(int powerValue) {
        PWMChangeEvent event = new PWMChangeEvent(this, powerValue);
        for (PWMControllerListener pwmControllerListener : pwmControllerListeners) {
            pwmControllerListener.pwmChanged(event);
        }
    }

    public int getValue() {
        return valueComboBoxModel.getSelectedItem();
    }

    public void setValue(int value) {
        int maximum = maxValueComboBoxModel.getSelectedItem();
        int minimum = minValueComboBoxModel.getSelectedItem();
        valueComboBoxModel.setSelectedItem(max(min(value, maximum), minimum));
    }


    public void setLaserDiodeIndex(int laserDiodeIndex) {
        this.laserDiodeIndex = laserDiodeIndex;
    }
}
