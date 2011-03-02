/*
 * Copyright 2010 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.crazycoder.plugins.tabdir;

import org.apache.commons.lang.StringUtils;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * User: crazycoder
 * Date: Aug 21, 2010
 * Time: 9:28:09 AM
 */
public class TitleFormatter {

    public static String format(List<String> prefixes, String tabName, FolderConfiguration configuration) {
        String joinedPrefixes = joinPrefixes(prefixes, configuration);
        return MessageFormat.format(configuration.getTitleFormat(), joinedPrefixes, tabName);
    }

    public static String example(FolderConfiguration configuration) {
        List<String> examplePrefixes = Arrays.asList("first", "second", "third", "fourth", "fifth", "sixs");
        String exampleFileName = "FileName";
        return format(examplePrefixes, exampleFileName, configuration);
    }

    private static String joinPrefixes(List<String> prefixes, FolderConfiguration configuration) {
        StringBuilder buffer = new StringBuilder();
        int i = 0;
        for (String prefix : prefixes) {
            if(configuration.isReduceDirNames()) {
                String reducedDir = StringUtils.substring(prefix, 0, configuration.getCharsInName());
                buffer.append(reducedDir);
            } else {
                buffer.append(prefix);
            }
            buffer.append(configuration.getDirSeparator());
            i++;
            if(i == configuration.getMaxDirsToShow()) {
                break;
            }
        }
        return StringUtils.removeEnd(buffer.toString(), configuration.getDirSeparator());
    }
}

