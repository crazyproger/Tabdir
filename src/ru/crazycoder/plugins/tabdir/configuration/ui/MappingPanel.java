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

package ru.crazycoder.plugins.tabdir.configuration.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.PanelWithButtons;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.TableView;
import com.intellij.openapi.ui.ComboBox;

import com.intellij.util.ui.AbstractTableCellEditor;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import ru.crazycoder.plugins.tabdir.TitleFormatter;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: crazycoder
 * Date: 20.12.10
 */
public class MappingPanel
        extends PanelWithButtons {

    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private final TableView<FolderMapping> folderMappingTable;

    private final LabelWithBrowseButton labelWithButton;

    // this map need just for fast finding duplicates when
    // creating new mappings.
    private Map<String, FolderConfiguration> configurationsMap;

    private final ColumnInfo<FolderMapping, String> DIRECTORY = new ColumnInfo<>("Directory") {

        @Override
        public String valueOf(final FolderMapping folderMapping) {
            return folderMapping.folder;
        }
    };
    private final ColumnInfo<FolderMapping, String> PREVIEW = new ColumnInfo<>("Tab Preview") {

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
                    labelWithButton.getLabel().setText((String) value);
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
                    if (hasFocus || isSelected) {
                        labelWithButton.getLabel().setText((String) value);
                        return labelWithButton;
                    } else {
                        return new JLabel((String) value);
                    }
                }
            };
        }

        @Override
        public void setValue(final FolderMapping folderMapping, final String value) {
        }
    };
    private final ColumnInfo[] COLUMNS = new ColumnInfo[]{DIRECTORY, PREVIEW};
    private final boolean isDisabled;
    private final Project project;
    private ListTableModel<FolderMapping> model;

    public MappingPanel(Project project) {
        this.project = project;
        JLabel label = new JLabel();
        Dimension preferredSize = new ComboBox<Dimension>().getPreferredSize();
        label.setPreferredSize(preferredSize);
        labelWithButton = new LabelWithBrowseButton(label, null);
        labelWithButton.addActionListener(e -> editMapping());

        folderMappingTable = new TableView<>();
        folderMappingTable.setRowHeight(preferredSize.height);
        folderMappingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        folderMappingTable.getSelectionModel().addListSelectionListener(e -> updateButtons());

        initPanel();
        updateButtons();
        isDisabled = project.isDefault();
        if (isDisabled) {
            folderMappingTable.setEnabled(false);
            addButton.setEnabled(false);
        }
    }

    public void initializeModel(Map<String, FolderConfiguration> configurations) {
        List<FolderMapping> modelList = new ArrayList<>(configurations.size());
        configurationsMap = new HashMap<>(configurations.size());
        for (Map.Entry<String, FolderConfiguration> entry : configurations.entrySet()) {
            FolderConfiguration configuration = entry.getValue().cloneMe();
            configurationsMap.put(entry.getKey(), configuration);
            modelList.add(new FolderMapping(entry.getKey(), configuration));
        }
        model = new ListTableModel<>(COLUMNS, modelList, 0, SortOrder.DESCENDING);
        folderMappingTable.setModelAndUpdateColumns(model);
    }

    @Override
    protected String getLabelText() {
        return "'Folder' -> 'show rule' mapping";
    }

    @Override
    protected JButton[] createButtons() {
        addButton = new JButton("Add");
        addButton.addActionListener(e -> addMapping());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteMapping());
        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editMapping());
        return new JButton[]{addButton, deleteButton, editButton};
    }

    @Override
    protected JComponent createMainComponent() {
        return ScrollPaneFactory.createScrollPane(folderMappingTable);
    }

    private void editMapping() {
        FolderMapping mapping = folderMappingTable.getSelectedObject();
        if (mapping == null) {
            return;
        }
        MappingEditor mappingEditor = new MappingEditor(project, mapping.folder, configurationsMap);
        mappingEditor.show();
        if (mappingEditor.isOK()) {
            mapping.myConfig = mappingEditor.getFolderConfiguration();
            if (!mapping.folder.equals(mappingEditor.getKey())) {
                configurationsMap.remove(mapping.folder);
                if (configurationsMap.containsKey(mappingEditor.getKey())) {
                    model.removeRow(folderMappingTable.getSelectedRow());
                    for (FolderMapping folderMapping : model.getItems()) {
                        if (folderMapping.folder.equals(mappingEditor.getKey())) {
                            folderMapping.myConfig = mappingEditor.getFolderConfiguration();
                            break;
                        }
                    }
                } else {
                    mapping.folder = mappingEditor.getKey();
                    mapping.myConfig = mappingEditor.getFolderConfiguration();
                }
            } else {
                mapping.myConfig = mappingEditor.getFolderConfiguration();
            }
            configurationsMap.put(mappingEditor.getKey(), mappingEditor.getFolderConfiguration());
            folderMappingTable.repaint();
        }
    }

    private void deleteMapping() {
        List<FolderMapping> mappings = new ArrayList<>(model.getItems());
        int index = folderMappingTable.getSelectionModel().getMinSelectionIndex();
        Collection<FolderMapping> selection = folderMappingTable.getSelection();
        mappings.removeAll(selection);
        for (FolderMapping mapping : selection) {
            configurationsMap.remove(mapping.folder);
        }
        model.setItems(mappings);
        if (!mappings.isEmpty()) {
            if (index >= mappings.size()) {
                index = mappings.size() - 1;
            }
            folderMappingTable.getSelectionModel().setSelectionInterval(index, index);
        }
    }

    private void addMapping() {
        MappingEditor mappingEditor = new MappingEditor(project, null, configurationsMap);
        mappingEditor.show();
        if (mappingEditor.isOK()) {
            if (configurationsMap.containsKey(mappingEditor.getKey())) {
                for (FolderMapping folderMapping : model.getItems()) {
                    if (folderMapping.folder.equals(mappingEditor.getKey())) {
                        folderMapping.myConfig = mappingEditor.getFolderConfiguration();
                        break;
                    }
                }
            } else {
                // new element
                model.addRow(new FolderMapping(mappingEditor.getKey(), mappingEditor.getFolderConfiguration()));
            }
            configurationsMap.put(mappingEditor.getKey(), mappingEditor.getFolderConfiguration());
            folderMappingTable.repaint();
        }
    }

    private void updateButtons() {
        final boolean hasSelection = folderMappingTable.getSelectedObject() != null;
        editButton.setEnabled((!isDisabled) && hasSelection);
        deleteButton.setEnabled((!isDisabled) && hasSelection);
    }

    private static class FolderMapping {

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
