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
    @Override
    public Element getState() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void loadState(Element state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
