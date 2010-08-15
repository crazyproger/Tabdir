package ru.crazycoder.plugins.tabdir.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StorageScheme;
import org.jdom.Element;

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
public class Configuration implements PersistentStateComponent<Element> {
    private boolean reduceDirNames;
    private int charsInName;
    private int maxDirsToShow;
    private UseExtensionsEnum useExtensions;
    private String filesExtensions;

    @Override
    public Element getState() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void loadState(Element state) {
        //To change body of implemented methods use File | Settings | File Templates.
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
