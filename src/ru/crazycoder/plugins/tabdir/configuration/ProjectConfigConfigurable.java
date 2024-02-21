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

package ru.crazycoder.plugins.tabdir.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazycoder.plugins.tabdir.configuration.ui.MappingPanel;

import javax.swing.*;
import java.util.Map;

/**
 * User: crazycoder
 * Date: 10.01.11
 */
public class ProjectConfigConfigurable
        implements SearchableConfigurable {

    private MappingPanel mappingPanel;
    private final ProjectConfig projectConfig;
    private final Project project;

    public ProjectConfigConfigurable(final Project project) {
        this.project = project;
        this.projectConfig = project.getService(ProjectConfig.class);
    }

    @NotNull
    @Override
    public String getId() {
        return "Tabdir.ProjectConfig";
    }

    @Override
    public Runnable enableSearch(final String option) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Tabdir";
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        if (mappingPanel == null) {
            mappingPanel = new MappingPanel(project);
        }
        mappingPanel.initializeModel(projectConfig.getFolderConfigurations());
        return mappingPanel;
    }

    @Override
    public boolean isModified() {
        Map<String, FolderConfiguration> currentMap = projectConfig.getFolderConfigurations();
        Map<String, FolderConfiguration> newMap = mappingPanel.getConfigurationsMap();
        if (currentMap.size() != newMap.size()) {
            return true;
        }
        for (Map.Entry<String, FolderConfiguration> entry : newMap.entrySet()) {
            FolderConfiguration configuration = currentMap.get(entry.getKey());
            if (configuration == null || !configuration.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        projectConfig.setFolderConfigurations(mappingPanel.getConfigurationsMap());
    }

    @Override
    public void reset() {
        mappingPanel.initializeModel(projectConfig.getFolderConfigurations());
    }

    @Override
    public void disposeUIResources() {
        mappingPanel = null;
    }
}
