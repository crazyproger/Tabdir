/*
 * Copyright 2010 Vladimir Rudev
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

package ru.crazycoder.plugins.tabdir.configuration.ui;

import com.intellij.ui.PanelWithButtons;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModel;
import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.util.ui.ColumnInfo;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

/**
 * User: crazycoder
 * Date: 20.12.10
 */
public class MappingPanel
        extends PanelWithButtons {

    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private TreeTable folderMappingTable;
    private ColumnInfo<MyTreeNode, String> DIRECTORY = new ColumnInfo<MyTreeNode, String>("Directory") {

        @Override
        public String valueOf(final MyTreeNode myTreeNode) {
            return myTreeNode.folderText;
        }

        @Override
        public Icon getIcon() {
            //todo разобраться
            return super.getIcon();    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public Class getColumnClass() {
            return TreeTableModel.class;
        }

        @Override
        public TableCellRenderer getRenderer(final MyTreeNode myTreeNode) {
            return super.getRenderer(myTreeNode);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public TableCellEditor getEditor(final MyTreeNode o) {
            return super.getEditor(o);    //To change body of overridden methods use File | Settings | File Templates.
        }
    };
    private ColumnInfo<MyTreeNode, String> PREVIEW = new ColumnInfo<MyTreeNode, String>("Tab preview") {
        @Override
        public String valueOf(final MyTreeNode myTreeNode) {
            return myTreeNode.tabPreview;
        }
    };
    private ColumnInfo[] COLUMNS = new ColumnInfo[]{DIRECTORY,PREVIEW};

    public MappingPanel() {
        initPanel();
    }

    @Override
    protected String getLabelText() {
        return "'Folder' -> 'show rule' mapping";
    }

    @Override
    protected JButton[] createButtons() {
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addMapping();
            }
        });
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                deleteMapping();
            }
        });
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                editMapping();
            }
        });
        return new JButton[]{addButton,deleteButton,editButton};
    }

    @Override
    protected JComponent createMainComponent() {
        TreeTableModel model = new ListTreeTableModel(new MyTreeNode("."),COLUMNS);
        folderMappingTable = new TreeTable(model);
        return ScrollPaneFactory.createScrollPane(folderMappingTable);
    }

    private void editMapping() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void deleteMapping() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void addMapping() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private class MyTreeNode
            extends DefaultMutableTreeNode {

        private String folderText;
        private String tabPreview;
        private File folder;
        private FolderConfiguration myConfig;

        private MyTreeNode(final String folderText) {
            this.folderText = folderText;
        }

        public boolean isModified(Map<String, FolderConfiguration> configurations) {
            return !(configurations != null && myConfig.equals(configurations.get(myConfig.getFolderPath())));
        }

        public void reset(Map<String, FolderConfiguration> configurations) {
            myConfig = configurations.get(myConfig.getFolderPath()).cloneMe();
            // todo reset preview
        }

        public void apply(Map<String, FolderConfiguration> configurations) {
            configurations.put(myConfig.getFolderPath(), myConfig.cloneMe());
        }

    }
}
