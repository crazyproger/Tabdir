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
    private JLabel charsLabel;
    private SpinnerNumberModel dirsToShowModel = new SpinnerNumberModel(3, 1, 10, 1);
    private SpinnerNumberModel charsInNameModel = new SpinnerNumberModel(3, 1, 20, 1);

    public SettingsPanel() {
        reduceDirNamesCB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                charsLabel.setEnabled(reduceDirNamesCB.isSelected());
                charsInNameSpinner.setEnabled(reduceDirNamesCB.isSelected());
//                charsInDirPanel.setEnabled(reduceDirNamesCB.isSelected());
            }
        });
        dirsToShowSpinner.setModel(dirsToShowModel);
        charsInNameSpinner.setModel(charsInNameModel);
    }

    public void setData(Configuration data) {
        reduceDirNamesCB.setSelected(data.isReduceDirNames());
        extensionsTA.setText(data.getFilesExtensions());
        dirsToShowModel.setValue(data.getMaxDirsToShow());
        charsInNameModel.setValue(data.getCharsInName());
        //todo notExtension
    }

    public void getData(Configuration data) {
        data.setReduceDirNames(reduceDirNamesCB.isSelected());
        data.setFilesExtensions(extensionsTA.getText());
        data.setMaxDirsToShow((Integer) dirsToShowModel.getValue());
        data.setCharsInName((Integer) charsInNameModel.getValue());
        //todo notExtension
    }

    public boolean isModified(Configuration data) {
        if (reduceDirNamesCB.isSelected() != data.isReduceDirNames()) return true;
        if (extensionsTA.getText() != null ? !extensionsTA.getText().equals(data.getFilesExtensions()) : data.getFilesExtensions() != null)
            return true;
        return false;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
