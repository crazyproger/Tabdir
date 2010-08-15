package ru.crazycoder.plugins.tabdir;

import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.ArrayList;
import java.util.List;

/**
 * User: crazycoder
 * Date: Aug 14, 2010
 * Time: 7:06:45 PM
 */
public class SameFilenameTitleProvider implements EditorTabTitleProvider {

    @Override
    public String getEditorTabTitle(Project project, VirtualFile file) {
        PsiShortNamesCache namesCache = JavaPsiFacade.getInstance(project).getShortNamesCache();
        PsiFile[] similarFiles = namesCache.getFilesByName(file.getName());
        if (similarFiles.length < 2) {
            return file.getPresentableName();
        }
        List<String> prefixes = new ArrayList<String>();
        VirtualFile[] similarVirtualFiles = toVirtualFiles(similarFiles);
        if (similarVirtualFiles.length == 2) {
            // when 2 - the simplest case
            VirtualFile ancestor = VfsUtil.getCommonAncestor(similarVirtualFiles[0], similarVirtualFiles[1]);
            String relativePath = VfsUtil.getRelativePath(file, ancestor, '/');
            if (relativePath.indexOf('/') != -1) {
                List<String> pathElements = StringUtil.split(relativePath, "/");
                prefixes.add(pathElements.get(0));
            }
        }
//        VirtualFile[] ancestors = VfsUtil.getCommonAncestors(toVirtualFiles(similarFiles));
//        for (VirtualFile ancestor : ancestors) {
//            Messages.showInfoMessage(ancestor.getPath(),"OOPS");
//        }

//        String ancestor = null;
//        for (PsiFile similarFile : similarFiles) {
//            ancestor = VfsUtil.getCommonAncestor(file, similarFile.getVirtualFile()).getName();
//        }
//        if (ancestor != null) {
//            return "["+ancestor+"]"+file.getPresentableName();
//        }
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
