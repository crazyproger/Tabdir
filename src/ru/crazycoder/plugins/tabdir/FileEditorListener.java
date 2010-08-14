package ru.crazycoder.plugins.tabdir;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * User: crazycoder
 * Date: Aug 14, 2010
 * Time: 1:56:20 PM
 */
public class FileEditorListener implements FileEditorManagerListener{

    @Override
    public void fileOpened(FileEditorManager source, VirtualFile file) {
        
    }

    @Override
    public void fileClosed(FileEditorManager source, VirtualFile file) {
        // nothing here
    }

    @Override
    public void selectionChanged(FileEditorManagerEvent event) {
        // nothing here
    }
}
