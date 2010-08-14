package ru.crazycoder.plugins.tabdir;

import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * User: crazycoder
 * Date: Aug 14, 2010
 * Time: 7:06:45 PM
 */
public class SameFilenameTitleProvider implements EditorTabTitleProvider {
    @Override
    public String getEditorTabTitle(Project project, VirtualFile file) {
        return "";
    }
}
