package ru.crazycoder.plugins.tabdir.configuration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 8:09:17 PM
 */
public class SettingsPanel {
    private JPanel rootPanel;
    private JComboBox useSwitchCB;
    private JTextArea extensionsTA;
    private JSpinner dirsToShowSpinner;
    private JCheckBox reduceDirNamesCB;
    private JSpinner charsInNameSpinner;
    private JPanel charsInDirPanel;
    private SpinnerNumberModel dirsToShowModel = new SpinnerNumberModel(3, 0, 10, 1);
    private SpinnerNumberModel charsInNameModel = new SpinnerNumberModel(3, 0, 20, 1);

    public SettingsPanel() {
        reduceDirNamesCB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                charsInDirPanel.setEnabled(reduceDirNamesCB.isSelected());
            }
        });
        dirsToShowSpinner.setModel(dirsToShowModel);
        charsInNameSpinner.setModel(charsInNameModel);
    }
}
