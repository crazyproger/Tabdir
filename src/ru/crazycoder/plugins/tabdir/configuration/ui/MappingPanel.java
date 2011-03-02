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
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import ru.crazycoder.plugins.tabdir.TitleFormatter;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private TableView<FolderMapping> folderMappingTable;

    private LabelWithBrowseButton labelWithButton;

    // this map need just for fast finding duplicates when
    // creating new mappings.
    private Map<String, FolderConfiguration> configurationsMap;

    private ColumnInfo<FolderMapping, String> DIRECTORY = new ColumnInfo<FolderMapping, String>("Directory") {

        @Override
        public String valueOf(final FolderMapping folderMapping) {
            return folderMapping.folder;
        }
    };
    private ColumnInfo<FolderMapping, String> PREVIEW = new ColumnInfo<FolderMapping, String>("Tab preview") {

        @Override
        public String valueOf(final FolderMapping folderMapping) {
            String example;
            try {
                example = TitleFormatter.example(folderMapping.myConfig);
            } catch (Exception e) {
                example = "FileName";
            }
            return example;
        }

        @Override
        public boolean isCellEditable(final FolderMapping folderMapping) {
            return true;
        }

        @Override
        public TableCellEditor getEditor(final FolderMapping o) {
            return new AbstractTableCellEditor() {
                @Override
                public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
                                                             final int row, final int column) {
                    labelWithButton.getLabel().setText((String)value);
                    return labelWithButton;
                }

                @Override
                public Object getCellEditorValue() {
                    return labelWithButton.getLabel().getText();
                }
            };
        }

        @Override
        public TableCellRenderer getRenderer(final FolderMapping folderMapping) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                               final boolean hasFocus, final int row, final int column) {
                    if(hasFocus || isSelected) {
                        labelWithButton.getLabel().setText((String)value);
                        return labelWithButton;
                    } else {
                        return new JLabel((String)value);
                    }
                }
            };
        }

        @Override
        public void setValue(final FolderMapping folderMapping, final String value) {
        }
    };
    private ColumnInfo[] COLUMNS = new ColumnInfo[]{DIRECTORY,PREVIEW};
    private boolean isDisabled;

    public MappingPanel(boolean disabled) {
        JLabel label = new JLabel();
        Dimension preferredSize = new JComboBox().getPreferredSize();
        label.setPreferredSize(preferredSize);
        labelWithButton = new LabelWithBrowseButton(label, null);
        labelWithButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                editMapping();
            }
        });

        folderMappingTable = new TableView<FolderMapping>();
        folderMappingTable.setRowHeight(preferredSize.height);
        folderMappingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        folderMappingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                updateButtons();
            }
        });

        initPanel();
        updateButtons();
        isDisabled = disabled;
        if(isDisabled) {
            folderMappingTable.setEnabled(false);
            addButton.setEnabled(false);
        }
    }

    public void initializeModel(Map<String, FolderConfiguration> configurations) {
        List<FolderMapping> modelList = new ArrayList<FolderMapping>(configurations.size());
        configurationsMap = new HashMap<String, FolderConfiguration>(configurations.size());
        for (Map.Entry<String, FolderConfiguration> entry : configurations.entrySet()) {
            FolderConfiguration configuration = entry.getValue().cloneMe();
            configurationsMap.put(entry.getKey(), configuration);
            modelList.add(new FolderMapping(entry.getKey(), configuration));
        }
        ListTableModel<FolderMapping> model = new ListTableModel<FolderMapping>(COLUMNS, modelList, 0, SortOrder.DESCENDING);
        folderMappingTable.setModel(model);
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

    private void updateButtons() {
        final boolean hasSelection = folderMappingTable.getSelectedObject() != null;
        editButton.setEnabled((!isDisabled) && hasSelection);
        deleteButton.setEnabled((!isDisabled) && hasSelection);
    }

    private class FolderMapping {

        String folder;
        FolderConfiguration myConfig;

        public FolderMapping(final String folder, final FolderConfiguration configuration) {
            this.folder = folder;
            myConfig = configuration;
        }
    }

    public Map<String, FolderConfiguration> getConfigurationsMap() {
        return configurationsMap;
    }
}
