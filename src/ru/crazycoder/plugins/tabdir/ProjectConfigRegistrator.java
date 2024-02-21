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

package ru.crazycoder.plugins.tabdir;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.*;
import com.intellij.openapi.options.ConfigurableEP;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.crazycoder.plugins.tabdir.configuration.GlobalConfig;
import ru.crazycoder.plugins.tabdir.configuration.ProjectConfigConfigurable;
import java.util.List;

/**
 * User: crazycoder
 * Date: 24.02.11
 * Register per project configuration extension.
 * Method {@link #checkAndRegister(boolean)} register or unregister
 * {@link ru.crazycoder.plugins.tabdir.configuration.ProjectConfigConfigurable}
 */
public class ProjectConfigRegistrator
        implements ProjectComponent {

    private final Project project;
    private ConfigurableEP<UnnamedConfigurable> configurableEP;

    public ProjectConfigRegistrator(Project project) {
        this.project = project;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ProjectConfigRegistrator";
    }

    public void projectOpened() {
        GlobalConfig globalConfig = ApplicationManager.getApplication().getService(GlobalConfig.class);
        globalConfig.addProjectConfigListener(this);
        checkAndRegister(globalConfig.isProjectConfigEnabled());
    }

    public void projectClosed() {
        ApplicationManager.getApplication().getService(GlobalConfig.class).removeProjectConfigListener(this);
    }

    public void checkAndRegister(boolean isNeedRegister) {
        try {
            List<Object> extensions = new ProjectExtensionPointName<>("com.intellij.projectConfigurable").getExtensionList(project);
            ConfigurableEP<UnnamedConfigurable> ourExtension = null;
            for (Object extension : extensions) {
                if (extension instanceof ConfigurableEP) {
                    String implementationClass = ((ConfigurableEP) extension).instanceClass;
                    if (implementationClass != null && implementationClass.equals(ProjectConfigConfigurable.class.getName())) {
                        ourExtension = (ConfigurableEP) extension;
                        break;
                    }
                }
            }
            ExtensionPoint<Object> projectConfigurableEP = Extensions.getArea(project).getExtensionPoint("com.intellij.projectConfigurable");
            if (ourExtension != null && !isNeedRegister) {
                // already registered
                projectConfigurableEP.unregisterExtension(ourExtension);
            } else if (ourExtension == null && isNeedRegister) {
                if (configurableEP == null) {
                    configurableEP = new ConfigurableEP(project);
                    configurableEP.setPluginDescriptor(PluginManager.getPlugin(PluginId.getId("ru.crazycoder.plugins.tabdir")));
                    configurableEP.instanceClass = ProjectConfigConfigurable.class.getName();
                }
                projectConfigurableEP.registerExtension(configurableEP);
            }
        } catch (Exception e) {
            Logger.getInstance(this.getClass().getName()).error("", e);
        }
    }
}
