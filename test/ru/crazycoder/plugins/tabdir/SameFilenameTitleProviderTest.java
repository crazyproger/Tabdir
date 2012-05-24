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
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;
import ru.crazycoder.plugins.tabdir.configuration.GlobalConfig;

import java.io.IOException;

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
        VirtualFile firstFolder = root.createChildDirectory(this, "aaaaFirstFolderbbbbb");
        VirtualFile secondFolder = root.createChildDirectory(this, "aaaaSecondFolderbbbbb");

        createFile(firstFolder, FILE_NAME, "");
        VirtualFile second = createFile(secondFolder, FILE_NAME, "");
        SameFilenameTitleProvider provider = new SameFilenameTitleProvider();
        String title = provider.getEditorTabTitle(myProject, second);
        assertEquals("[aaaaS] " + FILE_NAME, title);
    }

    public void testRemoveDuplicates() throws Exception {
        VirtualFile firstFolder = root.createChildDirectory(this, "aaaaFirstFolderbbbbb");
        VirtualFile secondFolder = root.createChildDirectory(this, "aaaaSecondFolderbbbbb");

        VirtualFile rootFile = createFile(root, FILE_NAME, "");
        createFile(firstFolder, FILE_NAME, "");
        VirtualFile second = createFile(secondFolder, FILE_NAME, "");

        VirtualFile first1Folder = firstFolder.createChildDirectory(this, "bbbbbbccFirst1");
        VirtualFile second1Folder = secondFolder.createChildDirectory(this, "bbbbbbccSecond1");
        VirtualFile first1 = createFile(first1Folder, FILE_NAME, "");
        VirtualFile second1 = createFile(second1Folder, FILE_NAME, "");
        VirtualFile third1Folder = firstFolder.createChildDirectory(this, "bbbbbbccThird1");
        VirtualFile fouth1Folder = secondFolder.createChildDirectory(this, "bbbbbbccFirst1Fouth1");
        createFile(third1Folder, FILE_NAME, "");
        VirtualFile fouth1 = createFile(fouth1Folder, FILE_NAME, "");

        VirtualFile first2Folder = first1Folder.createChildDirectory(this, "bcFirst1");
        VirtualFile second2Folder = first1Folder.createChildDirectory(this, "bcSecond1");
        createFile(first2Folder, FILE_NAME, "");
        VirtualFile second2 = createFile(second2Folder, FILE_NAME, "");

        SameFilenameTitleProvider provider = new SameFilenameTitleProvider();
        GlobalConfig configuration = ServiceManager.getService(GlobalConfig.class);
        configuration.setRemoveDuplicates(true);

        String rootTitle = provider.getEditorTabTitle(myProject, rootFile);
        assertNull(rootTitle);
        String title = provider.getEditorTabTitle(myProject, second);
        final String D = FolderConfiguration.DUPLICATES_DELIMITER;
        assertEquals("[a" + D + "Sec] " + FILE_NAME, title);
        String title1 = provider.getEditorTabTitle(myProject, second1);
        assertEquals("[a" + D + "Sec|b" + D + "Sec] " + FILE_NAME, title1);
        String title2 = provider.getEditorTabTitle(myProject, second2);
        assertEquals("[a" + D + "Sec|b" + D + "Sec|bcSec] " + FILE_NAME, title2);
        String title3 = provider.getEditorTabTitle(myProject, fouth1);
        // must remove prefix with max length
        assertEquals("[a" + D + "Sec|b" + D + "Fou] " + FILE_NAME, title3);
        String title4 = provider.getEditorTabTitle(myProject, first1);
        assertEquals("[a" + D + "Sec|b" + D + "rst1] " + FILE_NAME, title4);
    }

    protected VirtualFile createFile(final VirtualFile vDir, final String fileName, final String text) throws IOException {
        if (!ModuleRootManager.getInstance(myModule).getFileIndex().isInSourceContent(vDir)) {
            PsiTestUtil.addSourceContentToRoots(myModule, vDir);
        }

        final VirtualFile vFile = vDir.createChildData(vDir, fileName);
        VfsUtil.saveText(vFile, text);
        assertNotNull(vFile);
        final PsiFile file = myPsiManager.findFile(vFile);
        assertNotNull(file);
        return file.getVirtualFile();
    }
}
