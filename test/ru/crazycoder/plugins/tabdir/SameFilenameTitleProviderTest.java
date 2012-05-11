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

import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.testFramework.IdeaTestCase;
import com.intellij.testFramework.PsiTestUtil;

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
