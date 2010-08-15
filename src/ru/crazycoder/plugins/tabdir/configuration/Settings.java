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

    private final Configuration configuration;

    private SettingsPanel settingsPanel;

    public Settings(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JComponent createComponent() {
        settingsPanel = new SettingsPanel();
        settingsPanel.setData(configuration);
        return settingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified(configuration);
    }

    @Override
    public void apply() throws ConfigurationException {
        settingsPanel.getData(configuration);
    }

    @Override
    public void reset() {
        settingsPanel.setData(configuration);
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Tabdir";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public String getId() {
        return "Tabdir.Configuration";
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;
    }
}
