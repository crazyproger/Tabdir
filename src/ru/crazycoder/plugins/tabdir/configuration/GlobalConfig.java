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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazycoder.plugins.tabdir.configuration.ui.SettingsPanel;

import javax.swing.*;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:42:34 PM
 */
@State(
        name = "TabdirConfiguration",
        storages = {@Storage(id = "dir", file = "$APP_CONFIG$/other.xml", scheme = StorageScheme.DIRECTORY_BASED)})
public class GlobalConfig
        extends FolderConfiguration
        implements SearchableConfigurable, PersistentStateComponent<FolderConfiguration> {

    private static final String DEFAULT_TITLE_FORMAT = "[{0}] {1}";
    private static final String DEFAULT_DIR_SEPARATOR = "|";

    private SettingsPanel settingsPanel;

    public GlobalConfig() {
        this.setCharsInName(5);
        this.setDirSeparator(DEFAULT_DIR_SEPARATOR);
        this.setMaxDirsToShow(3);
        this.setUseExtensions(FolderConfiguration.UseExtensionsEnum.DO_NOT_USE);
        this.setFilesExtensions("java\ngroovy");
        this.setTitleFormat(DEFAULT_TITLE_FORMAT);
        this.setReduceDirNames(true);
    }

    @Override
    public JComponent createComponent() {
        settingsPanel = new SettingsPanel();
        settingsPanel.setData(this);
        return settingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified(this);
    }

    @Override
    public void apply() throws ConfigurationException {
        settingsPanel.getData(this);
    }

    @Override
    public void reset() {
        settingsPanel.setData(this);
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

    @Override
    public FolderConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(final FolderConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
