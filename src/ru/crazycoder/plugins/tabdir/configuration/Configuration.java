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

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:48:00 PM
 */
@State(
        name = "TabdirConfiguration",
        storages = {
                @Storage(id = "dir", file = "$APP_CONFIG$/other.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class Configuration implements PersistentStateComponent<Configuration> {

    private static final String DEFAULT_TITLE_FORMAT = "[${1}] ${2}";
    private static final String DEFAULT_DIR_SEPARATOR = "|";

    private boolean reduceDirNames;
    private int charsInName;
    private int maxDirsToShow;
    private UseExtensionsEnum useExtensions;
    private String filesExtensions;
    private String dirSeparator;
    private String titleFormat;

    public Configuration() {
        // set default values to configuration
        reduceDirNames = true;
        charsInName = 5;
        maxDirsToShow = 3;
        useExtensions = UseExtensionsEnum.DO_NOT_USE;
        filesExtensions = "java\ngroovy";
        dirSeparator = DEFAULT_DIR_SEPARATOR;
        titleFormat = DEFAULT_TITLE_FORMAT;
    }

    @Override
    public Configuration getState() {
        return this;
    }

    @Override
    public void loadState(Configuration state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public enum UseExtensionsEnum {
        DO_NOT_USE("Do not use", false),
        USE("Use", true);

        private String shownText;
        private boolean value;

        UseExtensionsEnum(String text, boolean value) {
            this.shownText = text;
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        @Override
        public String toString() {
            return shownText;
        }
    }

    public boolean isReduceDirNames() {
        return reduceDirNames;
    }

    public void setReduceDirNames(final boolean reduceDirNames) {
        this.reduceDirNames = reduceDirNames;
    }

    public String getFilesExtensions() {
        return filesExtensions;
    }

    public void setFilesExtensions(final String filesExtensions) {
        this.filesExtensions = filesExtensions;
    }

    public int getCharsInName() {
        return charsInName;
    }

    public void setCharsInName(final int charsInName) {
        this.charsInName = charsInName;
    }

    public int getMaxDirsToShow() {
        return maxDirsToShow;
    }

    public void setMaxDirsToShow(final int maxDirsToShow) {
        this.maxDirsToShow = maxDirsToShow;
    }

    public UseExtensionsEnum getUseExtensions() {
        return useExtensions;
    }

    public void setUseExtensions(final UseExtensionsEnum useExtensions) {
        this.useExtensions = useExtensions;
    }

    public String getDirSeparator() {
        return dirSeparator;
    }

    public void setDirSeparator(String dirSeparator) {
        this.dirSeparator = dirSeparator;
    }

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
    }
}
