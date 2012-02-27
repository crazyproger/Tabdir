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

import ru.crazycoder.plugins.tabdir.configuration.GlobalConfig;

import javax.swing.*;

/**
 * User: crazycoder
 * Date: 25.02.11
 */
public class GlobalSettingsPanel {

    private JPanel sharedSettingsPanel;
    private JCheckBox projectConfigEnabledCB;
    private JPanel rootPanel;

    private SharedSettingsPanel sharedSettings;

    public boolean isModified(GlobalConfig config) {
        if (sharedSettings.isModified(config)) {
            return true;
        } else {
            return !(config.isProjectConfigEnabled() == projectConfigEnabledCB.isSelected());
        }
    }

    public void getData(GlobalConfig config) {
        sharedSettings.getData(config);
        config.setProjectConfigEnabled(projectConfigEnabledCB.isSelected());
    }

    public void setData(GlobalConfig config) {
        sharedSettings.setData(config);
        projectConfigEnabledCB.setSelected(config.isProjectConfigEnabled());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        sharedSettings = new SharedSettingsPanel();
        sharedSettingsPanel = sharedSettings.getRootPanel();
    }
}
