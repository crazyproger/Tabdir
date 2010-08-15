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
    private boolean reduceDirNames;
    private int charsInName;
    private int maxDirsToShow;
    private UseExtensionsEnum useExtensions;
    private String filesExtensions;

    public Configuration() {
        // set default values to configuration
        reduceDirNames = true;
        charsInName = 5;
        maxDirsToShow = 3;
        useExtensions = UseExtensionsEnum.DO_NOT_USE;
        filesExtensions = "java\ngroovy";
    }

    @Override
    public Configuration getState() {
        return this;
    }

    @Override
    public void loadState(Configuration state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    enum UseExtensionsEnum {
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
}
