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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazycoder.plugins.tabdir.configuration.ui.SettingsPanel;

import javax.swing.*;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:42:34 PM
 */
public class Settings
        implements SearchableConfigurable {

    private final Configuration configuration;

    private SettingsPanel settingsPanel;

    public Settings(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JComponent createComponent() {
        settingsPanel = new SettingsPanel();
        settingsPanel.setData(configuration);
        return settingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified(configuration);
    }

    @Override
    public void apply() throws ConfigurationException {
        settingsPanel.getData(configuration);
    }

    @Override
    public void reset() {
        settingsPanel.setData(configuration);
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Tabdir";
    }

    @Override
    public Icon getIcon() {
        return null;
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
