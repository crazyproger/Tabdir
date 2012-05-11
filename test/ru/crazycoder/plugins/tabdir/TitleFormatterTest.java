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

import org.junit.Assert;
import org.junit.Test;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import java.util.Arrays;

/**
 * User: crazyproger
 * Date: 02.05.12
 */
public class TitleFormatterTest {

    @Test
    public void testFormat() throws Exception {
        FolderConfiguration configuration = getDefaultConfiguration();
        String formatted = TitleFormatter.format(Arrays.asList("admin", "front"), "tab", configuration);
        Assert.assertEquals("[admin|front] tab", formatted);
    }

    @Test
    public void testFromStart() throws Exception {
        FolderConfiguration configuration = getDefaultConfiguration();
        configuration.setMaxDirsToShow(3);
        configuration.setCountMaxDirsFromStart(true);
        String formatted = TitleFormatter.format(Arrays.asList("admin", "front", "test", "another", "duplicate"), "tab", configuration);
        Assert.assertEquals("[admin|front|test] tab", formatted);
    }

    @Test
    public void testFromEnd() throws Exception {
        FolderConfiguration configuration = getDefaultConfiguration();
        configuration.setMaxDirsToShow(3);
        configuration.setCountMaxDirsFromStart(false);
        String formatted = TitleFormatter.format(Arrays.asList("admin", "front", "test", "another", "duplicate"), "tab", configuration);
        Assert.assertEquals("[test|another|duplicate] tab", formatted);
    }

    private FolderConfiguration getDefaultConfiguration() {
        FolderConfiguration configuration = new FolderConfiguration();
        configuration.setDirSeparator("|");
        configuration.setTitleFormat("[{0}] {1}");
        return configuration;
    }
}
