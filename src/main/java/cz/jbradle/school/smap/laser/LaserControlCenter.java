package cz.jbradle.school.smap.laser;

import cz.jbradle.school.smap.laser.ui.configuration.LaserDiodeConfigurationPanel;
import cz.jbradle.school.smap.laser.ui.configuration.LaserDiodeSensorConfigurationPanel;
import cz.jbradle.school.smap.laser.ui.configuration.PhotoDiodeConfigurationPanel;
import cz.jbradle.school.smap.laser.ui.controls.LaserPinStatus;
import cz.jbradle.school.smap.laser.ui.controls.LaserSwitchController;
import org.apache.log4j.Logger;
import org.zu.ardulink.Link;
import org.zu.ardulink.event.ConnectionEvent;
import org.zu.ardulink.event.ConnectionListener;
import org.zu.ardulink.event.DisconnectionEvent;
import org.zu.ardulink.gui.ConnectionStatus;
import org.zu.ardulink.gui.DigitalPinStatus;
import org.zu.ardulink.gui.Linkable;
import org.zu.ardulink.gui.SerialConnectionPanel;
import org.zu.ardulink.protocol.ReplyMessageCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;


/**
 * Laser control center application
 * <p>
 * Created by George on 22.11.2015.
 */
public class LaserControlCenter extends JFrame implements ConnectionListener, Linkable {

    private JButton btnConnect;
    private JButton btnDisconnect;

    private SerialConnectionPanel serialConnectionPanel;

    private Link link;

    private final List<Linkable> linkables = new LinkedList<Linkable>();


    private static final Logger logger = Logger.getLogger(LaserControlCenter.class);


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo laf : UIManager
                        .getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(laf.getName())) {
                        UIManager.setLookAndFeel(laf.getClassName());
                    }
                }
                LaserControlCenter frame = new LaserControlCenter();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public LaserControlCenter() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                LaserControlCenter.class.getResource("laser_icon.png")));
        setTitle("Laser Control Center");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 620);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel connectionPanel = new JPanel();
        tabbedPane.addTab("Connection", null, connectionPanel, null);
        connectionPanel.setLayout(new BorderLayout(0, 0));

        JPanel connectPanel = new JPanel();
        connectionPanel.add(connectPanel, BorderLayout.SOUTH);

        btnConnect = new JButton("Connect");
        btnConnect.addActionListener(actionEvent -> {
            String comPort = serialConnectionPanel.getConnectionPort();
            String baudRateS = serialConnectionPanel.getBaudRate();

            if (comPort == null || comPort.isEmpty()) {
                JOptionPane.showMessageDialog(btnConnect, "Invalid COM PORT setted.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (baudRateS == null || baudRateS.isEmpty()) {
                JOptionPane.showMessageDialog(btnConnect, "Invalid baud rate setted. Advice: set "
                        + Link.DEFAULT_BAUDRATE, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int baudRate = Integer.parseInt(baudRateS);
                    boolean connected = link.connect(comPort, baudRate);
                    logConnectState(connected);
                    tabbedPane.setEnabled(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    String message = ex.getMessage();
                    if (message == null || message.isEmpty()) {
                        message = "Generic Error on connection";
                    }
                    JOptionPane.showMessageDialog(btnConnect, message, "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        connectPanel.add(btnConnect);

        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.addActionListener(actionEvent -> {
            if (link != null) {
                boolean connected = !link.disconnect();
                logConnectState(connected);
            }
            tabbedPane.setEnabled(false);
        });
        connectPanel.add(btnDisconnect);

        JPanel allConnectionsPanel = new JPanel();
        connectionPanel.add(allConnectionsPanel, BorderLayout.CENTER);
        GridBagLayout gbl_allConnectionsPanel = new GridBagLayout();
        gbl_allConnectionsPanel.columnWeights = new double[]{0.0, 0.0};
        gbl_allConnectionsPanel.rowWeights = new double[]{0.0, 0.0};
        allConnectionsPanel.setLayout(gbl_allConnectionsPanel);


        GridBagConstraints gbc_serialConnectionRadioButton = new GridBagConstraints();
        gbc_serialConnectionRadioButton.insets = new Insets(0, 0, 0, 10);
        gbc_serialConnectionRadioButton.anchor = GridBagConstraints.NORTH;
        gbc_serialConnectionRadioButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_serialConnectionRadioButton.gridx = 0;
        gbc_serialConnectionRadioButton.gridy = 0;


        serialConnectionPanel = new SerialConnectionPanel();
        serialConnectionPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        GridBagConstraints gbc_serialConnectionPanel = new GridBagConstraints();
        gbc_serialConnectionPanel.insets = new Insets(0, 0, 0, 10);
        gbc_serialConnectionPanel.anchor = GridBagConstraints.NORTH;
        gbc_serialConnectionPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_serialConnectionPanel.gridx = 0;
        gbc_serialConnectionPanel.gridy = 1;
        allConnectionsPanel.add(serialConnectionPanel, gbc_serialConnectionPanel);


        setLink(serialConnectionPanel.getLink());

        JPanel stateBar = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) stateBar.getLayout();
        flowLayout_1.setVgap(0);
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        contentPane.add(stateBar, BorderLayout.SOUTH);

        ConnectionStatus connectionStatus = new ConnectionStatus();
        linkables.add(connectionStatus);
        FlowLayout flowLayout = (FlowLayout) connectionStatus.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setAlignment(FlowLayout.LEFT);
        stateBar.add(connectionStatus, BorderLayout.SOUTH);

        JPanel configurationPanel = new JPanel();
        tabbedPane.addTab("Configuration", null, configurationPanel, null);
        configurationPanel.setLayout(new GridLayout(10, 1, 0, 0));

        configurationPanel.add(new LaserDiodeConfigurationPanel());
        configurationPanel.add(new PhotoDiodeConfigurationPanel());
        configurationPanel.add(new LaserDiodeSensorConfigurationPanel());

        JPanel communicationPanel = new JPanel();
        tabbedPane.addTab("Communication", null, communicationPanel, null);
        communicationPanel.setLayout(new BorderLayout(5, 5));

        JPanel westOfCommunicationPanel = new JPanel();
        westOfCommunicationPanel.setLayout(new GridLayout(5, 1));

        westOfCommunicationPanel.add(BorderLayout.WEST, new LaserSwitchController());
        westOfCommunicationPanel.add(BorderLayout.WEST,new LaserPinStatus());

        communicationPanel.add(BorderLayout.WEST, westOfCommunicationPanel);


        tabbedPane.setEnabled(false);
    }


    public void setLink(Link link) {
        if (this.link != null) {
            this.link.removeConnectionListener(this);
        }
        this.link = link;
        if (link == null) {
            disconnected(new DisconnectionEvent());
        } else {
            link.addConnectionListener(this);
        }
        for (Linkable linkable : linkables) {
            linkable.setLink(link);
        }
    }

    public ReplyMessageCallback getReplyMessageCallback() {
        throw new RuntimeException("Not developed yet");
    }

    public void setReplyMessageCallback(ReplyMessageCallback replyMessageCallback) {
        throw new RuntimeException("Not developed yet");
    }

    public void connected(ConnectionEvent e) {
        btnConnect.setEnabled(false);
        btnDisconnect.setEnabled(true);
    }


    public void disconnected(DisconnectionEvent e) {
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
    }

    private void logConnectState(boolean connected) {
        logger.info("Connection status: " + connected);
    }

}
