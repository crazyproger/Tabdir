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

import com.intellij.openapi.ui.ComponentWithBrowseButton;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * User: crazycoder
 * Date: 01.03.11
 */
public class LabelWithBrowseButton
        extends ComponentWithBrowseButton<JLabel> {

    private FolderConfiguration configuration;

    public LabelWithBrowseButton(final JLabel component, final ActionListener browseActionListener) {
        super(component, browseActionListener);
    }

    public JLabel getLabel() {
        return getChildComponent();
    }

    public FolderConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final FolderConfiguration configuration) {
        this.configuration = configuration;
        getChildComponent().setText(configuration.getTitleFormat());
    }
}
