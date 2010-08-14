package ru.crazycoder.plugins.tabdir;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

/**
 * User: crazycoder
 * Date: Aug 14, 2010
 * Time: 1:44:53 PM
 */
public class ListenerRegistratorComponent implements ProjectComponent {

    private Project project;
    private MessageBusConnection messageBusConnection;

    public ListenerRegistratorComponent(Project project) {
        this.project = project;
    }

    public void initComponent() {
        messageBusConnection = project.getMessageBus().connect();
        messageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorListener());
    }

    public void disposeComponent() {
        messageBusConnection.disconnect();
    }

    @NotNull
    public String getComponentName() {
        return "ListenerRegistratorComponent";
    }

    public void projectOpened() {
        // called when project is opened
    }

    public void projectClosed() {
        // called when project is being closed
    }
}
