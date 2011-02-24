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
import com.intellij.util.xmlb.XmlSerializerUtil;
import ru.crazycoder.plugins.tabdir.ProjectConfigRegistrator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:42:34 PM
 * todo add projectConfigEnabled saving
 */
@State(
        name = "TabdirConfiguration",
        storages = {@Storage(id = "other", file = "$APP_CONFIG$/other.xml", scheme = StorageScheme.DIRECTORY_BASED)})
public class GlobalConfig
        extends FolderConfiguration
        implements PersistentStateComponent<FolderConfiguration> {

    private static final String DEFAULT_TITLE_FORMAT = "[{0}] {1}";
    private static final String DEFAULT_DIR_SEPARATOR = "|";
    private final List<ProjectConfigRegistrator> projectConfgiListeners = Collections
            .synchronizedList(new LinkedList<ProjectConfigRegistrator>());

    private boolean projectConfigEnabled = false;

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
    public FolderConfiguration getState() {
        // clone need to return FolderConfiguration object
        // because if return GlobalConfig object configuration will not be saved
        return this.cloneMe();
    }

    @Override
    public void loadState(final FolderConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isProjectConfigEnabled() {
        return projectConfigEnabled;
    }

    public void setProjectConfigEnabled(final boolean projectConfigEnabled) {
        this.projectConfigEnabled = projectConfigEnabled;
        synchronized (projectConfgiListeners) {
            for (ProjectConfigRegistrator projectConfgiListener : projectConfgiListeners) {
                projectConfgiListener.checkAndRegister(projectConfigEnabled);
            }
        }
    }

    public void addProjectConfigListener(ProjectConfigRegistrator listener) {
        projectConfgiListeners.add(listener);
    }

    public void removeProjectConfigListener(ProjectConfigRegistrator listener) {
        projectConfgiListeners.remove(listener);
    }
}
