/*
 * Copyright 2010 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import ru.crazycoder.plugins.tabdir.configuration.Configuration;

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

    private Configuration configuration;
    private TitleFormatter formatter;
    private final Comparator<VirtualFile> comparator = new Comparator<VirtualFile>() {
        @Override
        public int compare(VirtualFile file1, VirtualFile file2) {
            return file1.getPath().length() - file2.getPath().length();
        }
    };

    @Override
    public String getEditorTabTitle(final Project project, final VirtualFile file) {
        try {
            configuration = ServiceManager.getService(project, Configuration.class);
            formatter = ServiceManager.getService(project, TitleFormatter.class);

            if(!needProcessFile(file)) {
                return null;
            }
            return relativeToSourceTitle(project, file);
//        return relativeToProjectTitle(project, file);
//        return titleWithDiffs(project, file);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    private String relativeToSourceTitle(final Project project, final VirtualFile file) {
        Module module = ModuleUtil.findModuleForFile(file, project);
        if(module == null) {
            return null;
        }
        String filePath = file.getPath();
        VirtualFile[] sourceRoots = ModuleRootManager.getInstance(module).getSourceRoots();
        VirtualFile currentSourceRoot = null;
        for (VirtualFile sourceRoot : sourceRoots) {
            if(filePath.startsWith(sourceRoot.getPath())) {
                currentSourceRoot = sourceRoot;
                break;
            }
        }
        if(currentSourceRoot == null) {
            return null;
        }
        return FileUtil.getRelativePath(currentSourceRoot.getPath(), filePath, File.separatorChar);
    }

    private String relativeToProjectTitle(final Project project, final VirtualFile file) {
        VirtualFileSystem virtualFileSystem = file.getFileSystem();
        if(!(virtualFileSystem instanceof LocalFileSystem)) {
            // unknown filesystem
            return null;
        }
        String filePath = file.getPath();

        VirtualFile projectBaseDir = project.getBaseDir();
        if(projectBaseDir == null) {
            return null;
        }
        return FileUtil.getRelativePath(projectBaseDir.getPath(), filePath, File.separatorChar);
    }

    private String titleWithDiffs(final Project project, final VirtualFile file) {
        PsiShortNamesCache namesCache = JavaPsiFacade.getInstance(project).getShortNamesCache();
        PsiFile[] similarPsiFiles = namesCache.getFilesByName(file.getName());
        if(similarPsiFiles.length < 2) {
            return file.getPresentableName();
        }
        List<String> prefixes = calculatePrefixes(file, similarPsiFiles);

        if(prefixes.size() > 0) {
            try {
                return formatter.format(prefixes, file.getPresentableName());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private List<String> calculatePrefixes(final VirtualFile file, final PsiFile[] similarPsiFiles) {
        List<String> prefixes = new ArrayList<String>();
        VirtualFile[] similarFiles = toVirtualFiles(similarPsiFiles);
        SortedSet<VirtualFile> ancestors = new TreeSet<VirtualFile>(comparator);
        for (VirtualFile similarFile : similarFiles) {
            if(file.equals(similarFile)) {
                continue;
            }
            ancestors.add(VfsUtil.getCommonAncestor(similarFile, file));
        }

        for (VirtualFile ancestor : ancestors) {
            String relativePath = VfsUtil.getRelativePath(file, ancestor, File.separatorChar);
            if(relativePath.indexOf(File.separatorChar) != -1) {
                List<String> pathElements = StringUtil.split(relativePath, File.separator);
                prefixes.add(pathElements.get(0));
            }
        }
        return prefixes;
    }

    private boolean needProcessFile(VirtualFile file) {
        if(file.getExtension() != null) {
            String[] extensions = StringUtil.splitByLines(configuration.getFilesExtensions());
            boolean isInExtensionsConfig = Arrays.asList(extensions).contains(file.getExtension());
            return isInExtensionsConfig == configuration.getUseExtensions().getValue();
        }
        return true;
    }

    private VirtualFile[] toVirtualFiles(PsiFile[] array) {
        List<VirtualFile> virtualFileList = new ArrayList<VirtualFile>(array.length);
        for (PsiFile file : array) {
            virtualFileList.add(file.getVirtualFile());
        }
        return VfsUtil.toVirtualFileArray(virtualFileList);
    }
}
