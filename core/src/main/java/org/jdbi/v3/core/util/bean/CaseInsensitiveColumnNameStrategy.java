/*
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
package org.jdbi.v3.core.util.bean;

import java.util.Locale;

public class CaseInsensitiveColumnNameStrategy implements ColumnNameMappingStrategy {

    public static final CaseInsensitiveColumnNameStrategy INSTANCE = new CaseInsensitiveColumnNameStrategy();

    private final Locale locale;

    public CaseInsensitiveColumnNameStrategy() {
        this(Locale.ROOT);
    }

    public CaseInsensitiveColumnNameStrategy(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean nameMatches(String propertyName, String columnName) {
        return propertyName.toLowerCase(locale).equals(columnName.toLowerCase(locale));
    }

    @Override
    public String toString() {
        return "LowercaseColumnNamingStrategy" + (locale != Locale.ROOT ? " (" + locale + ")" : "");
    }
}