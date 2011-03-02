/*
 * Copyright 2011 Vladimir Rudev
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

/**
 * User: crazycoder
 * Date: 19.12.10
 */
public class FolderConfiguration {

    private String relativeTo;
    private boolean reduceDirNames;
    private String dirSeparator;
    private String titleFormat;
    private int charsInName;
    private int maxDirsToShow;
    private String filesExtensions;
    private UseExtensionsEnum useExtensions;

    public FolderConfiguration() {
    }

    public FolderConfiguration(final String relativeTo, boolean reduceDirNames, final String dirSeparator, final String titleFormat,
                               final int charsInName, final int maxDirsToShow, final String filesExtensions,
                               final UseExtensionsEnum useExtensions) {
        this.relativeTo = relativeTo;
        this.reduceDirNames = reduceDirNames;
        this.dirSeparator = dirSeparator;
        this.titleFormat = titleFormat;
        this.charsInName = charsInName;
        this.maxDirsToShow = maxDirsToShow;
        this.filesExtensions = filesExtensions;
        this.useExtensions = useExtensions;
    }

    public String getRelativeTo() {
        return relativeTo;
    }

    public void setRelativeTo(final String relativeTo) {
        this.relativeTo = relativeTo;
    }

    public boolean isReduceDirNames() {
        return reduceDirNames;
    }

    public void setReduceDirNames(final boolean reduceDirNames) {
        this.reduceDirNames = reduceDirNames;
    }

    public String getDirSeparator() {
        return dirSeparator;
    }

    public void setDirSeparator(final String dirSeparator) {
        this.dirSeparator = dirSeparator;
    }

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(final String titleFormat) {
        this.titleFormat = titleFormat;
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

    public String getFilesExtensions() {
        return filesExtensions;
    }

    public void setFilesExtensions(final String filesExtensions) {
        this.filesExtensions = filesExtensions;
    }

    public UseExtensionsEnum getUseExtensions() {
        return useExtensions;
    }

    public void setUseExtensions(final UseExtensionsEnum useExtensions) {
        this.useExtensions = useExtensions;
    }

    @Override
    public boolean equals(final Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        final FolderConfiguration that = (FolderConfiguration)o;

        if(charsInName != that.charsInName) return false;
        if(maxDirsToShow != that.maxDirsToShow) return false;
        if(reduceDirNames != that.reduceDirNames) return false;
        if(dirSeparator != null ? !dirSeparator.equals(that.dirSeparator) : that.dirSeparator != null) return false;
        if(filesExtensions != null ? !filesExtensions.equals(that.filesExtensions) : that.filesExtensions != null) return false;
        if(relativeTo != null ? !relativeTo.equals(that.relativeTo) : that.relativeTo != null) return false;
        if(titleFormat != null ? !titleFormat.equals(that.titleFormat) : that.titleFormat != null) return false;
        if(useExtensions != that.useExtensions) return false;

        return true;
    }

    public FolderConfiguration cloneMe() {
        return new FolderConfiguration(relativeTo, reduceDirNames, dirSeparator, titleFormat, charsInName, maxDirsToShow, filesExtensions,
                useExtensions);
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
}
