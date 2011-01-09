/*
 * Copyright 2010 Vladimir Rudev
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

import org.jetbrains.annotations.NotNull;

/**
 * User: crazycoder
 * Date: 19.12.10
 */
public class FolderConfiguration {

    private String relativeTo;
    @NotNull
    private String dirsSeparator;
    @NotNull
    private String titleFormat;
    private int nCharsInDirName;
    private int nDirsToShow;
    private String filesExtensions;
    private UseExtensionsEnum useEnum;

    public FolderConfiguration() {
    }

    public FolderConfiguration(final String relativeTo, @NotNull final String dirsSeparator, @NotNull final String titleFormat,
                               final int nCharsInDirName, final int nDirsToShow, final String filesExtensions,
                               final UseExtensionsEnum useEnum) {
        this.relativeTo = relativeTo;
        this.dirsSeparator = dirsSeparator;
        this.titleFormat = titleFormat;
        this.nCharsInDirName = nCharsInDirName;
        this.nDirsToShow = nDirsToShow;
        this.filesExtensions = filesExtensions;
        this.useEnum = useEnum;
    }

    public String getRelativeTo() {
        return relativeTo;
    }

    public void setRelativeTo(final String relativeTo) {
        this.relativeTo = relativeTo;
    }

    @NotNull
    public String getDirsSeparator() {
        return dirsSeparator;
    }

    public void setDirsSeparator(@NotNull final String dirsSeparator) {
        this.dirsSeparator = dirsSeparator;
    }

    @NotNull
    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(@NotNull final String titleFormat) {
        this.titleFormat = titleFormat;
    }

    public int getnCharsInDirName() {
        return nCharsInDirName;
    }

    public void setnCharsInDirName(final int nCharsInDirName) {
        this.nCharsInDirName = nCharsInDirName;
    }

    public int getnDirsToShow() {
        return nDirsToShow;
    }

    public void setnDirsToShow(final int nDirsToShow) {
        this.nDirsToShow = nDirsToShow;
    }

    public String getFilesExtensions() {
        return filesExtensions;
    }

    public void setFilesExtensions(final String filesExtensions) {
        this.filesExtensions = filesExtensions;
    }

    public UseExtensionsEnum getUseEnum() {
        return useEnum;
    }

    public void setUseEnum(final UseExtensionsEnum useEnum) {
        this.useEnum = useEnum;
    }

    public FolderConfiguration cloneMe() {
        return new FolderConfiguration(relativeTo, dirsSeparator, titleFormat, nCharsInDirName, nDirsToShow, filesExtensions, useEnum);
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
