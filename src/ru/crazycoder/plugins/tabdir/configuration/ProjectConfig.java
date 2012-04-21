/*
 * Copyright 2012 Vladimir Rudev
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

import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: crazycoder
 * Date: Aug 15, 2010
 * Time: 6:48:00 PM
 */
@State(
        name = "TabdirProjectConfiguration",
        storages = {@Storage(id = "default", file = "$PROJECT_FILE$"), @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/other.xml",
                scheme = StorageScheme.DIRECTORY_BASED)})
public class ProjectConfig
        implements PersistentStateComponent<Element> {

    private final Logger log = Logger.getInstance(this.getClass().getCanonicalName());

    private static final String FOLDER_CONFIGURATIONS_NAME = "folderConfigurations";

    private Map<String, FolderConfiguration> folderConfigurations;

    private final PathMacroManager macroManager;

    public ProjectConfig(Project project) {

        folderConfigurations = new HashMap<String, FolderConfiguration>();
        macroManager = PathMacroManager.getInstance(project);
    }

    public Map<String, FolderConfiguration> getFolderConfigurations() {
        return folderConfigurations;
    }

    public void setFolderConfigurations(final Map<String, FolderConfiguration> folderConfigurations) {
        this.folderConfigurations = folderConfigurations;
    }

    @Override
    public Element getState() {
        Element configurationsElement = new Element(FOLDER_CONFIGURATIONS_NAME);
        for (Map.Entry<String, FolderConfiguration> entry : folderConfigurations.entrySet()) {
            Element element = new Element("Entry");
            element.setAttribute("folder", macroManager.collapsePath(entry.getKey()));
            Element folderConfig = XmlSerializer.serialize(entry.getValue());
            element.addContent(folderConfig);
            configurationsElement.addContent(element);
        }
        Element element = new Element("TabdirConfiguration");
        element.addContent(configurationsElement);
        return element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadState(Element state) {
        Element configurationsElement = state.getChild(FOLDER_CONFIGURATIONS_NAME);
        if (configurationsElement == null) {
            log.debug("no config element");
            return;
        }
        Map<String, FolderConfiguration> folderConfigurations = new HashMap<String, FolderConfiguration>();
        List<Element> entries = configurationsElement.getChildren("Entry");
        for (Element entry : entries) {
            String key = macroManager.expandPath(entry.getAttributeValue("folder"));
            FolderConfiguration value = XmlSerializer.deserialize(entry.getChild("FolderConfiguration"), FolderConfiguration.class);
            folderConfigurations.put(key, value);
        }
        this.folderConfigurations = folderConfigurations;
    }
}
