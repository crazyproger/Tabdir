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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:42:34 PM
 */
@Service
@State(
        name = "TabdirConfiguration",
        storages = {@Storage("$APP_CONFIG$/other.xml")})
final public class GlobalConfig
        extends FolderConfiguration
        implements PersistentStateComponent<Element> {

    private static final String DEFAULT_TITLE_FORMAT = "[{0}] {1}";
    private static final String DEFAULT_DIR_SEPARATOR = "|";

    private boolean projectConfigEnabled;

    public GlobalConfig() {
        this.setCharsInName(5);
        this.setDirSeparator(DEFAULT_DIR_SEPARATOR);
        this.setMaxDirsToShow(3);
        this.setUseExtensions(FolderConfiguration.UseExtensionsEnum.DO_NOT_USE);
        this.setFilesExtensions("");
        this.setTitleFormat(DEFAULT_TITLE_FORMAT);
        this.setReduceDirNames(true);
        this.setCountMaxDirsFromStart(true);
        this.setEmptyPathReplacement("");
        projectConfigEnabled = false;
    }

    @Override
    public Element getState() {
        return XmlSerializer.serialize(this);
    }

    @Override
    public void loadState(@NotNull final Element state) {
        XmlSerializer.deserializeInto(this, state);
    }

    public boolean isProjectConfigEnabled() {
        return projectConfigEnabled;
    }

    public void setProjectConfigEnabled(final boolean projectConfigEnabled) {
        this.projectConfigEnabled = projectConfigEnabled;
    }
}
