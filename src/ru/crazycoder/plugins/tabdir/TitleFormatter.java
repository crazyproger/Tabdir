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

import org.apache.commons.lang.StringUtils;
import ru.crazycoder.plugins.tabdir.configuration.FolderConfiguration;

import java.text.MessageFormat;
import java.util.*;

/**
 * User: crazycoder
 * Date: Aug 21, 2010
 * Time: 9:28:09 AM
 */
public class TitleFormatter {

    public static String format(LinkedHashMap<String, Set<String>> prefixes, String tabName, FolderConfiguration configuration) {
        String joinedPrefixes = joinPrefixesWithRemoveDuplication(prefixes, configuration);
        return MessageFormat.format(configuration.getTitleFormat(), joinedPrefixes, tabName);
    }

    private static String joinPrefixesWithRemoveDuplication(LinkedHashMap<String, Set<String>> prefixes, FolderConfiguration configuration) {
        List<String> keys = new LinkedList<String>(prefixes.keySet());
        keys = getPrefixesSublist(keys, configuration);
        List<String> resultPrefixes = new ArrayList<String>(keys.size());
        for (String key : keys) {
            resultPrefixes.add(removeDuplicates(key, prefixes.get(key)));
        }
        StringBuilder buffer = join(resultPrefixes, configuration);
        return StringUtils.removeEnd(buffer.toString(), configuration.getDirSeparator());
    }

    private static String removeDuplicates(String key, Set<String> strings) {
        String result = "";
        strings.remove(null);
        List<String> list = new ArrayList<String>(strings);
        list.add(key);
        String commonPrefix = StringUtils.getCommonPrefix(list.toArray(new String[list.size()]));
        String suffix = key;
        while (!StringUtils.isBlank(commonPrefix)) {
            result += commonPrefix.substring(0, 1) + FolderConfiguration.DUPLICATES_DELIMITER;
            int prefixLength = commonPrefix.length();

            if (prefixLength == suffix.length()) {
                return suffix;
            }

            suffix = suffix.substring(prefixLength);
            List<String> newList = new ArrayList<String>();
            for (String s : list) {
                String substring = s.substring(prefixLength);
                if (StringUtils.isNotBlank(substring)) {
                    newList.add(substring);
                }
            }
            list = newList;
            commonPrefix = StringUtils.getCommonPrefix(list.toArray(new String[list.size()]));
        }
        return result + suffix;
    }

    //-----simple format

    public static String format(List<String> prefixes, String tabName, FolderConfiguration configuration) {
        String joinedPrefixes = joinPrefixes(prefixes, configuration);
        return MessageFormat.format(configuration.getTitleFormat(), joinedPrefixes, tabName);
    }

    private static String joinPrefixes(List<String> prefixes, FolderConfiguration configuration) {
        prefixes = getPrefixesSublist(prefixes, configuration);
        StringBuilder buffer = join(prefixes, configuration);
        return StringUtils.removeEnd(buffer.toString(), configuration.getDirSeparator());
    }

    private static List<String> getPrefixesSublist(List<String> prefixes, FolderConfiguration configuration) {
        int maxDirsToShow = configuration.getMaxDirsToShow();
        if (maxDirsToShow > 0 && maxDirsToShow < prefixes.size()) {
            int beginIndex = prefixes.size() - maxDirsToShow;
            int endIndex = prefixes.size();
            if (configuration.isCountMaxDirsFromStart()) {
                beginIndex = 0;
                endIndex = maxDirsToShow;
            }
            return prefixes.subList(beginIndex, endIndex);
        }
        return prefixes;
    }

    private static StringBuilder join(List<String> prefixes, FolderConfiguration configuration) {
        StringBuilder buffer = new StringBuilder();
        for (String prefix : prefixes) {
            if (configuration.isReduceDirNames()) {
                String reducedDir;
                if (configuration.isReduceDirNames() && prefix.contains(FolderConfiguration.DUPLICATES_DELIMITER)) {
                    int start = prefix.lastIndexOf(FolderConfiguration.DUPLICATES_DELIMITER);
                    reducedDir = StringUtils.substring(prefix, 0, start + configuration.getCharsInName() + 1);
                } else {
                    reducedDir = StringUtils.substring(prefix, 0, configuration.getCharsInName());
                }
                buffer.append(reducedDir);
            } else {
                buffer.append(prefix);
            }
            buffer.append(configuration.getDirSeparator());
        }
        return buffer;
    }

    public static String example(FolderConfiguration configuration) {
        List<String> examplePrefixes = Arrays.asList("first", "second", "third", "fourth", "fifth", "sixs");
        String exampleFileName = "FileName";
        return format(examplePrefixes, exampleFileName, configuration);
    }
}

