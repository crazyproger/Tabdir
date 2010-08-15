package ru.crazycoder.plugins.tabdir;

import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;

import java.io.File;
import java.util.*;

/**
 * User: crazycoder
 * Date: Aug 14, 2010
 * Time: 7:06:45 PM
 */
public class SameFilenameTitleProvider implements EditorTabTitleProvider {

    @Override
    public String getEditorTabTitle(Project project, VirtualFile file) {
        PsiShortNamesCache namesCache = JavaPsiFacade.getInstance(project).getShortNamesCache();
        PsiFile[] similarPsiFiles = namesCache.getFilesByName(file.getName());
        if (similarPsiFiles.length < 2) {
            return file.getPresentableName();
        }
        List<String> prefixes = new ArrayList<String>();
        VirtualFile[] similarFiles = toVirtualFiles(similarPsiFiles);
        SortedSet<VirtualFile> ancestors = new TreeSet<VirtualFile>(new Comparator<VirtualFile>() {
            @Override
            public int compare(VirtualFile file1, VirtualFile file2) {
                return file1.getPath().length() - file2.getPath().length();
            }
        });
        for (VirtualFile similarFile : similarFiles) {
            if (file.equals(similarFile)) {
                continue;
            }
            ancestors.add(VfsUtil.getCommonAncestor(similarFile, file));
        }

        for (VirtualFile ancestor : ancestors) {
            String relativePath = VfsUtil.getRelativePath(file, ancestor, File.separatorChar);
            if (relativePath.indexOf(File.separatorChar) != -1) {
                List<String> pathElements = StringUtil.split(relativePath, File.separator);
                prefixes.add(pathElements.get(0));
            }
        }

        String prefix = "";
        if (prefixes.size() > 0) {
            prefix = "[" + StringUtil.join(prefixes, "|") + "]";
        }
        return prefix + file.getPresentableName();
    }

    private VirtualFile[] toVirtualFiles(PsiFile[] array) {
        List<VirtualFile> virtualFileList = new ArrayList<VirtualFile>(array.length);
        for (PsiFile file : array) {
            virtualFileList.add(file.getVirtualFile());
        }
        return VfsUtil.toVirtualFileArray(virtualFileList);
    }
}
