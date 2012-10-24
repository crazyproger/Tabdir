/*
 * Copyright 2012 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.crazycoder.plugins.tabdir.configuration.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.StringUtils;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;
import ru.crazycoder.plugins.tabdir.configuration.GlobalConfig;

import javax.swing.*;
import java.util.Map;

/**
 * User: crazycoder
 * Date: 02.03.11
 */
public class MappingEditor
        extends DialogWrapper {

    private JPanel mainPanel;
    private TextFieldWithBrowseButton configurationDirectoryTF;
    private TextFieldWithBrowseButton relativeToTF;
    private SharedSettingsPanel sharedSettingsComp;

    private FolderConfiguration folderConfiguration = null;
    private final Map<String, FolderConfiguration> configurationMap;

    public MappingEditor(final Project project, final String directory, final Map<String, FolderConfiguration> configurationMap) {
        super(project, false);
        this.configurationMap = configurationMap;
        if (directory != null) {
            configurationDirectoryTF.getChildComponent().setText(directory);
            folderConfiguration = configurationMap.get(directory).cloneMe();
        }
        if (folderConfiguration == null) {
            folderConfiguration = new GlobalConfig().cloneMe();
        }
        relativeToTF.getChildComponent().setText(folderConfiguration.getRelativeTo());
        sharedSettingsComp.setData(folderConfiguration);
        configurationDirectoryTF.addActionListener(new BrowseFolderListener(project));
        relativeToTF.addActionListener(new ComponentWithBrowseButton.BrowseFolderActionListener<JTextField>("Select Directory",
                "Select directory relative to which you want see path in tab", relativeToTF, project,
                new FileChooserDescriptor(false, true, false, false, false, false), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT));
        setTitle("Folder Tabdir Configuration");
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected ValidationInfo doValidate() {
        boolean isBlank = StringUtils.isBlank(configurationDirectoryTF.getChildComponent().getText());
        if (isBlank) {
            return new ValidationInfo("Target folder must be specified", configurationDirectoryTF);
        }
        if (!sharedSettingsComp.isValid()) {
            return new ValidationInfo("Invalid format options", sharedSettingsComp.getRootPanel());
        }
        return null;
    }

    private class BrowseFolderListener
            extends ComponentWithBrowseButton.BrowseFolderActionListener<JTextField> {

        private final Project project;

        public BrowseFolderListener(Project project) {
            super("Select Directory", "Select directory to which you want set config", configurationDirectoryTF, project,
                    new FileChooserDescriptor(false, true, false, false, false, false), TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
            this.project = project;
        }

        @Override
        protected VirtualFile getInitialFile() {
            // suggest project base dir only if nothing is typed in the component.
            String text = getComponentText();
            if (text.length() == 0) {
                VirtualFile file = project.getBaseDir();
                if (file != null) {
                    return file;
                }
            }
            return super.getInitialFile();
        }

        @Override
        protected void onFileChoosen(final VirtualFile chosenFile) {
            FolderConfiguration configuration = configurationMap.get(chosenFile.getPath());
            if (configuration != null) {
                sharedSettingsComp.setData(configuration);
                configurationDirectoryTF.getChildComponent().setText(configuration.getRelativeTo());
            }
            super.onFileChoosen(chosenFile);
        }
    }

    @Override
    protected void doOKAction() {
        sharedSettingsComp.getData(folderConfiguration);
        folderConfiguration.setRelativeTo(relativeToTF.getChildComponent().getText());
        super.doOKAction();
    }

    public String getKey() {
        return configurationDirectoryTF.getChildComponent().getText();
    }

    public FolderConfiguration getFolderConfiguration() {
        return folderConfiguration;
    }
}
