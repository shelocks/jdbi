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
package org.jdbi.v3.core;

import org.jdbi.v3.core.config.JdbiConfig;

/**
 * Configuration for behavior related to {@link Enum}s.
 */
public class EnumConfig implements JdbiConfig<EnumConfig> {
    private boolean handleEnumsByName = true;

    public EnumConfig() {}

    private EnumConfig(EnumConfig other) {
        this.handleEnumsByName = other.handleEnumsByName;
    }

    /**
     * Applies to both binding and mapping.
     *
     * @return true if enums are handled by name, false if enums are handled by ordinal
     */
    public boolean isEnumHandledByName() {
        return handleEnumsByName;
    }

    /**
     * Applies to both binding and mapping.
     *
     * @param byName true if enums should be handled by name, false if enums should be handled by ordinal
     */
    public void setEnumHandledByName(boolean byName) {
        this.handleEnumsByName = byName;
    }

    @Override
    public EnumConfig createCopy() {
        return new EnumConfig(this);
    }
}
