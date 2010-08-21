/*
 * Copyright 2010 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private JLabel charsLabel;
    private JTextField titleFormatTF;
    private JTextField dirSeparatorTF;
    private JLabel formattedExample;
    private SpinnerNumberModel dirsToShowModel = new SpinnerNumberModel(3, 1, 10, 1);
    private SpinnerNumberModel charsInNameModel = new SpinnerNumberModel(3, 1, 20, 1);

    public SettingsPanel() {
        reduceDirNamesCB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                charsLabel.setEnabled(reduceDirNamesCB.isSelected());
                charsInNameSpinner.setEnabled(reduceDirNamesCB.isSelected());
            }
        });
        dirsToShowSpinner.setModel(dirsToShowModel);
        charsInNameSpinner.setModel(charsInNameModel);
        useSwitchCB.setModel(new DefaultComboBoxModel(Configuration.UseExtensionsEnum.values()));
    }

    public void setData(Configuration data) {
        reduceDirNamesCB.setSelected(data.isReduceDirNames());
        extensionsTA.setText(data.getFilesExtensions());
        dirsToShowModel.setValue(data.getMaxDirsToShow());
        charsInNameModel.setValue(data.getCharsInName());
        useSwitchCB.getModel().setSelectedItem(data.getUseExtensions());
        dirSeparatorTF.setText(data.getDirSeparator());
        titleFormatTF.setText(data.getTitleFormat());
    }

    public void getData(Configuration data) {
        data.setReduceDirNames(reduceDirNamesCB.isSelected());
        data.setFilesExtensions(extensionsTA.getText().trim());
        data.setMaxDirsToShow((Integer) dirsToShowModel.getValue());
        data.setCharsInName((Integer) charsInNameModel.getValue());
        data.setUseExtensions((Configuration.UseExtensionsEnum) useSwitchCB.getModel().getSelectedItem());
        data.setTitleFormat(titleFormatTF.getText());
        data.setDirSeparator(dirSeparatorTF.getText());
    }

    public boolean isModified(Configuration data) {
        if (reduceDirNamesCB.isSelected() != data.isReduceDirNames()) return true;
        if (extensionsTA.getText() != null ? !extensionsTA.getText().equals(data.getFilesExtensions()) : data.getFilesExtensions() != null)
            return true;
        if ((Integer) dirsToShowModel.getValue() != data.getMaxDirsToShow()) return true;
        if ((Integer) charsInNameModel.getValue() != data.getCharsInName()) return true;
        if (!titleFormatTF.getText().equals(data.getTitleFormat())) return true;
        if (!dirSeparatorTF.getText().equals(data.getDirSeparator())) return true;
        if (useSwitchCB.getModel().getSelectedItem() != data.getUseExtensions()) return true;
        return false;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
