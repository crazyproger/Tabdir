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

    @NotNull
    private String folderPath;
    private String relativeTo;
    @NotNull
    private String dirsSeparator;
    @NotNull
    private String titleFormat;
    private boolean hideVowels;
    private int nCharsInDirName;
    private int nDirsToShow;
    private String filesExtensions;

    public FolderConfiguration() {
    }

    public FolderConfiguration(@NotNull final String folderPath, final String relativeTo, @NotNull final String dirsSeparator,
                               @NotNull final String titleFormat, final boolean hideVowels, final int nCharsInDirName,
                               final int nDirsToShow, final String filesExtensions) {
        this.folderPath = folderPath;
        this.relativeTo = relativeTo;
        this.dirsSeparator = dirsSeparator;
        this.titleFormat = titleFormat;
        this.hideVowels = hideVowels;
        this.nCharsInDirName = nCharsInDirName;
        this.nDirsToShow = nDirsToShow;
        this.filesExtensions = filesExtensions;
    }

    @NotNull
    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(@NotNull final String folderPath) {
        this.folderPath = folderPath;
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

    public boolean isHideVowels() {
        return hideVowels;
    }

    public void setHideVowels(final boolean hideVowels) {
        this.hideVowels = hideVowels;
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

    public FolderConfiguration cloneMe() {
        return new FolderConfiguration(folderPath, relativeTo, dirsSeparator, titleFormat, hideVowels, nCharsInDirName, nDirsToShow,
                filesExtensions);
    }
}
