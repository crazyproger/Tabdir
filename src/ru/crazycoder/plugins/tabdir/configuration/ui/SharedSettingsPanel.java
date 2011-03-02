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

package ru.crazycoder.plugins.tabdir.configuration.ui;

import ru.crazycoder.plugins.tabdir.TitleFormatter;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 8:09:17 PM
 */
public class SharedSettingsPanel {

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
    private JLabel formatInfo;
    private JLabel dirSeparatorL;
    private JLabel dirsToShowL;
    private SpinnerNumberModel dirsToShowModel = new SpinnerNumberModel(3, 1, 10, 1);
    private SpinnerNumberModel charsInNameModel = new SpinnerNumberModel(3, 1, 20, 1);

    private boolean isValid = false;

    public SharedSettingsPanel() {
        reduceDirNamesCB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                charsLabel.setEnabled(reduceDirNamesCB.isSelected());
                charsInNameSpinner.setEnabled(reduceDirNamesCB.isSelected());
            }
        });
        dirsToShowSpinner.setModel(dirsToShowModel);
        charsInNameSpinner.setModel(charsInNameModel);
        useSwitchCB.setModel(new DefaultComboBoxModel(FolderConfiguration.UseExtensionsEnum.values()));
        dirSeparatorTF.getDocument().addDocumentListener(new ExampleUpdaterDocumentListener());
        titleFormatTF.getDocument().addDocumentListener(new ExampleUpdaterDocumentListener());
        dirsToShowSpinner.addChangeListener(new ExampleUpdaterChangeListener());
        charsInNameSpinner.addChangeListener(new ExampleUpdaterChangeListener());
    }

    private void updateExample() {
        FolderConfiguration configuration = new FolderConfiguration();
        getData(configuration);
        updateExample(configuration);
    }

    public void updateExample(final FolderConfiguration configuration) {
        String formattedExample;
        try {
            formattedExample = TitleFormatter.example(configuration);
            formatInfo.setText("<html><font color=green> Ok</font></html>");
            isValid = true;
        } catch (Exception e) {
            isValid = false;
            formattedExample = "FileName";
            formatInfo.setText("<html><font color=red> Bad</font></html>");
        }
        this.formattedExample.setText(formattedExample);
    }

    public void setData(FolderConfiguration data) {
        reduceDirNamesCB.setSelected(data.isReduceDirNames());
        extensionsTA.setText(data.getFilesExtensions());
        dirsToShowModel.setValue(data.getMaxDirsToShow());
        charsInNameModel.setValue(data.getCharsInName());
        useSwitchCB.getModel().setSelectedItem(data.getUseExtensions());
        dirSeparatorTF.setText(data.getDirSeparator());
        titleFormatTF.setText(data.getTitleFormat());
        updateExample();
    }

    public void getData(FolderConfiguration data) {
        data.setReduceDirNames(reduceDirNamesCB.isSelected());
        data.setFilesExtensions(extensionsTA.getText().trim());
        data.setMaxDirsToShow((Integer)dirsToShowModel.getValue());
        data.setCharsInName((Integer)charsInNameModel.getValue());
        data.setUseExtensions((FolderConfiguration.UseExtensionsEnum)useSwitchCB.getModel().getSelectedItem());
        data.setTitleFormat(titleFormatTF.getText().trim());
        data.setDirSeparator(dirSeparatorTF.getText().trim());
    }

    public boolean isModified(FolderConfiguration data) {
        if(reduceDirNamesCB.isSelected() != data.isReduceDirNames()) return true;
        if(extensionsTA.getText() != null ? !extensionsTA.getText().equals(data.getFilesExtensions()) : data.getFilesExtensions() != null)
            return true;
        if((Integer)dirsToShowModel.getValue() != data.getMaxDirsToShow()) return true;
        if((Integer)charsInNameModel.getValue() != data.getCharsInName()) return true;
        if(!titleFormatTF.getText().equals(data.getTitleFormat())) return true;
        if(!dirSeparatorTF.getText().trim().equals(data.getDirSeparator())) return true;
        if(useSwitchCB.getModel().getSelectedItem() != data.getUseExtensions()) return true;
        return false;
    }

    public void setEnabledDirSeparatorField(boolean enabled) {
        dirSeparatorL.setEnabled(enabled);
        dirSeparatorTF.setEnabled(enabled);
    }

    public void setEnabledDirsToShow(boolean enabled) {
        dirsToShowSpinner.setEnabled(enabled);
        dirsToShowL.setEnabled(enabled);
    }

    class ExampleUpdaterDocumentListener
            implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateExample();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateExample();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateExample();
        }
    }

    class ExampleUpdaterChangeListener
            implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            updateExample();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public boolean isValid() {
        return isValid;
    }
}
