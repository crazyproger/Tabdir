package ru.crazycoder.plugins.tabdir.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:42:34 PM
 */
public class Settings implements SearchableConfigurable {
    @Override
    public JComponent createComponent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isModified() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void apply() throws ConfigurationException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void disposeUIResources() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nls
    @Override
    public String getDisplayName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Icon getIcon() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHelpTopic() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
