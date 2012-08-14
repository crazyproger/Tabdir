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
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.testFramework.IdeaTestCase;
import com.intellij.testFramework.PsiTestUtil;
import org.apache.commons.lang.StringUtils;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;
import ru.crazycoder.plugins.tabdir.configuration.GlobalConfig;

import java.io.IOException;
import java.util.*;

/**
 * User: crazyproger
 * Date: 28.04.12
 */
public class SameFilenameTitleProviderTest extends IdeaTestCase {
    public static final String FILE_NAME = "simpleTest.java";
    private VirtualFile root;
    protected PsiManagerImpl myPsiManager;

    public void setUp() throws Exception {
        super.setUp();
        myPsiManager = (PsiManagerImpl) PsiManager.getInstance(myProject);
        root = myProject.getBaseDir();
    }

    public void testTruncating() throws Exception {
        Map<String, VirtualFile> myFileSystem = createTree(Arrays.asList(
                "aaaaFirstFolderbbbbb/",
                "  `" + FILE_NAME,
                "aaaaSecondFolderbbbbb/",
                "  `" + FILE_NAME));

        assertTitleEquals(myFileSystem, "[aaaaS] " + FILE_NAME, "aaaaSecondFolderbbbbb/" + FILE_NAME);
    }

    public void testRemoveDuplicates() throws Exception {
        Map<String, VirtualFile> myFileSystem = createTree(Arrays.asList(
                "aaaaFirstFolderbbbbb/",
                "  |-bbbbbbccFirst1/",
                "  |   |-bcFirst1/",
                "  |   |    `" + FILE_NAME,
                "  |   |-bcSecond1/",
                "  |   |    `" + FILE_NAME,
                "  |    `" + FILE_NAME,
                "  |-bbbbbbccThird1/",
                "  |    `" + FILE_NAME,
                "   `" + FILE_NAME,
                "                     ",
                "aaaaSecondFolderbbbbb/",
                "  |-bbbbbbccSecond1/",
                "  |    `" + FILE_NAME,
                "  |-bbbbbbccFirst1Fouth1/",
                "  |    `" + FILE_NAME,
                "   `" + FILE_NAME,
                FILE_NAME,
                ""
        ));
        GlobalConfig configuration = ServiceManager.getService(GlobalConfig.class);
        configuration.setRemoveDuplicates(true);
        final String D = FolderConfiguration.DUPLICATES_DELIMITER;

        assertTitleEquals(myFileSystem, "[a" + D + "Sec] " + FILE_NAME, "aaaaSecondFolderbbbbb/" + FILE_NAME);
        assertTitleEquals(myFileSystem, "[a" + D + "Sec|b" + D + "Sec] " + FILE_NAME, "aaaaSecondFolderbbbbb/bbbbbbccSecond1/" + FILE_NAME);
        assertTitleEquals(myFileSystem, "[a" + D + "Sec|b" + D + "Sec|bcSec] " + FILE_NAME, "aaaaSecondFolderbbbbb/bbbbbbccSecond1/bcSecond1/" + FILE_NAME);

        // must remove prefix with max length
        assertTitleEquals(myFileSystem, "[a" + D + "Sec|b" + D + "Fou] " + FILE_NAME, "aaaaSecondFolderbbbbb/bbbbbbccSecond1/bbbbbbccFirst1Fouth1/" + FILE_NAME);
        assertTitleEquals(myFileSystem, "[a" + D + "Sec|b" + D + "rst1] " + FILE_NAME, "aaaaFirstFolderbbbbb/bbbbbbccFirst1/" + FILE_NAME);
    }

    // utility methods

    private void assertTitleEquals(Map<String, VirtualFile> myFileSystem, final String expectedTitle, final String filePath) {
        VirtualFile target = myFileSystem.get(filePath);

        SameFilenameTitleProvider provider = new SameFilenameTitleProvider();
        String title = provider.getEditorTabTitle(myProject, target);
        assertEquals(expectedTitle, title);
    }

    private Map<String, VirtualFile> createTree(List<String> tree) throws IOException {
        List<VirtualFile> levels = new ArrayList<VirtualFile>();
        Map<String, VirtualFile> fileSystem = new HashMap<String, VirtualFile>();
        for (String row : tree) {
            processRow(row, levels, fileSystem);
        }
        return fileSystem;
    }

    private void processRow(String row, List<VirtualFile> levels, Map<String, VirtualFile> fileSystem) throws IOException {
        int levelPointer = 0;
        if (StringUtils.isBlank(row)) {
            return;
        }
        boolean entryCreated = false;
        while (!entryCreated) {
            row = row.replaceAll("\\s", "");
            if (row.startsWith("|-")) {
                createEntry(row.substring(2), levels, levelPointer, fileSystem);
                entryCreated = true;
            } else if (row.startsWith("|")) {
                levelPointer += 1;
                row = row.substring(1);
            } else if (row.startsWith("`")) {
                CreatedType type = createEntry(row.substring(1), levels, levelPointer, fileSystem);
                if (type == CreatedType.FILE) {
                    levels.remove(levels.size() - 1);
                } else {
                    levels.remove(levels.size() - 2);
                }
                levelPointer -= 1;
                entryCreated = true;
            } else {
                createEntry(row, levels, levelPointer - 1, fileSystem);
                entryCreated = true;
            }
        }
    }

    private CreatedType createEntry(String name, List<VirtualFile> levels, int level, Map<String, VirtualFile> fileSystem) throws IOException {
        VirtualFile parent = getParent(levels, level);
        CreatedType type = CreatedType.DIRECTORY;
        VirtualFile created;
        if (name.endsWith("/")) {
            created = parent.createChildDirectory(this, StringUtils.chomp(name, "/"));
            levels.add(created);
        } else {
            created = createFile(parent, name);
            type = CreatedType.FILE;
        }
        String absolutePath = created.getPath();
        fileSystem.put(StringUtils.removeStart(absolutePath, myProject.getBasePath() + "/"), created);
        return type;
    }

    private VirtualFile getParent(List<VirtualFile> levels, int i) {
        if (i == -1) {
            return root;
        }
        return levels.get(i);
    }

    protected VirtualFile createFile(final VirtualFile vDir, final String fileName) throws IOException {
        if (!ModuleRootManager.getInstance(myModule).getFileIndex().isInSourceContent(vDir)) {
            PsiTestUtil.addSourceContentToRoots(myModule, vDir);
        }

        final VirtualFile vFile = vDir.createChildData(vDir, fileName);
        VfsUtil.saveText(vFile, "");
        assertNotNull(vFile);
        final PsiFile file = myPsiManager.findFile(vFile);
        assertNotNull(file);
        return file.getVirtualFile();
    }

    private enum CreatedType {
        FILE,
        DIRECTORY
    }
}
