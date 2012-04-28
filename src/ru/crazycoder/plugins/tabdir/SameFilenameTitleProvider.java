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

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.ProjectScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.apache.commons.lang.StringUtils;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;
import ru.crazycoder.plugins.tabdir.configuration.GlobalConfig;
import ru.crazycoder.plugins.tabdir.configuration.ProjectConfig;

import java.io.File;
import java.util.*;

/**
 * User: crazycoder
 * Date: Aug 14, 2010
 * Time: 7:06:45 PM
 */
public class SameFilenameTitleProvider
        implements EditorTabTitleProvider {

    private Logger log = Logger.getInstance(this.getClass().getCanonicalName());

    private GlobalConfig configuration = ServiceManager.getService(GlobalConfig.class);
    private final Comparator<VirtualFile> comparator = new Comparator<VirtualFile>() {
        @Override
        public int compare(VirtualFile file1, VirtualFile file2) {
            return file1.getPath().length() - file2.getPath().length();
        }
    };

    @Override
    public String getEditorTabTitle(Project project, VirtualFile file) {
        try {
            return getEditorTabTitleInternal(project, file);
        } catch (Exception e) {
            return null;
        }
    }

    public String getEditorTabTitleInternal(final Project project, final VirtualFile file) {
        try {
            FolderConfiguration matchedConfiguration = findConfiguration(project, file);
            if (!needProcessFile(file, matchedConfiguration)) {
                return null;
            }
            if (StringUtils.isNotEmpty(matchedConfiguration.getRelativeTo())) {
                return titleRelativeTo(file, matchedConfiguration);
            } else {
                return titleWithDiffs(project, file, matchedConfiguration);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    private FolderConfiguration findConfiguration(final Project project, final VirtualFile file) {
        if (!configuration.isProjectConfigEnabled()) {
            return configuration;
        }
        ProjectConfig projectConfig = ServiceManager.getService(project, ProjectConfig.class);
        Map<String, FolderConfiguration> folderConfigs = projectConfig.getFolderConfigurations();
        FolderConfiguration matchedConfiguration = null;
        int biggestKeyLength = 0;
        // search configuration where path(key in map) is biggest prefix for file
        for (Map.Entry<String, FolderConfiguration> entry : folderConfigs.entrySet()) {
            String key = entry.getKey();
            if (file.getPath().startsWith(key) && biggestKeyLength < key.length()) {
                biggestKeyLength = key.length();
                matchedConfiguration = entry.getValue();
            }
        }
        if (matchedConfiguration == null) {
            // no project config for current file - use global config
            matchedConfiguration = configuration;
        }
        return matchedConfiguration;
    }

    private String titleRelativeTo(final VirtualFile file, final FolderConfiguration configuration) {
        String relativePath = FileUtil.getRelativePath(configuration.getRelativeTo(), file.getPath(), File.separatorChar);
        String[] parts = StringUtils.split(relativePath, File.separatorChar);
        List<String> prefixes = Arrays.asList(parts);
        return TitleFormatter.format(prefixes, file.getPresentableName(), configuration);
    }

    private String titleWithDiffs(final Project project, final VirtualFile file, final FolderConfiguration configuration) {
        Collection<VirtualFile> similarFiles = FileBasedIndex.getInstance().getContainingFiles(FilenameIndex.NAME, file.getName(), ProjectScope.getProjectScope(project));
        if (similarFiles.size() < 2) {
            return file.getPresentableName();
        }
        List<String> prefixes = calculatePrefixes(file, similarFiles);

        if (prefixes.size() > 0) {
            return TitleFormatter.format(prefixes, file.getPresentableName(), configuration);
        }
        return null;
    }

    private List<String> calculatePrefixes(final VirtualFile file, final Collection<VirtualFile> similarFiles) {
        List<String> prefixes = new ArrayList<String>();
        SortedSet<VirtualFile> ancestors = new TreeSet<VirtualFile>(comparator);
        for (VirtualFile similarFile : similarFiles) {
            if (file.equals(similarFile)) {
                continue;
            }
            if (file.getPath() != null) {
                VirtualFile ancestor = VfsUtil.getCommonAncestor(similarFile, file);
                if (ancestor != null && ancestor.getPath() != null) {
                    ancestors.add(ancestor);
                }
            }
        }

        for (VirtualFile ancestor : ancestors) {
            String relativePath = VfsUtil.getRelativePath(file, ancestor, File.separatorChar);
            if (relativePath != null && relativePath.indexOf(File.separatorChar) != -1) {
                List<String> pathElements = StringUtil.split(relativePath, File.separator);
                prefixes.add(pathElements.get(0));
            }
        }
        return prefixes;
    }

    private boolean needProcessFile(VirtualFile file, FolderConfiguration configuration) {
        if (file.getExtension() != null) {
            String[] extensions = StringUtil.splitByLines(configuration.getFilesExtensions());
            boolean isInExtensionsConfig = Arrays.asList(extensions).contains(file.getExtension());
            return isInExtensionsConfig == configuration.getUseExtensions().getValue();
        }
        return true;
    }
}
