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

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazycoder.plugins.tabdir.configuration.ui.GlobalSettingsPanel;

import javax.swing.*;

/**
 * SearchableConfigurable for GlobalConfig service
 */
public class GlobalConfigConfigurable
        implements SearchableConfigurable {

    private GlobalSettingsPanel globalSettingsPanel;

    private final GlobalConfig configuration = ServiceManager.getService(GlobalConfig.class);

    @Override
    public JComponent createComponent() {
        globalSettingsPanel = new GlobalSettingsPanel();
        globalSettingsPanel.setData(configuration);
        return globalSettingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return globalSettingsPanel != null && globalSettingsPanel.isModified(configuration);
    }

    @Override
    public void apply() throws ConfigurationException {
        globalSettingsPanel.getData(configuration);
    }

    @Override
    public void reset() {
        globalSettingsPanel.setData(configuration);
    }

    @Override
    public void disposeUIResources() {
        globalSettingsPanel = null;
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

    @NotNull
    @Override
    public String getId() {
        return "Tabdir.Configuration";
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;
    }
}
