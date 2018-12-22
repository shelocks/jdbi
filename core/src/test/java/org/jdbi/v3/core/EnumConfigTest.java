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

import org.jdbi.v3.core.result.UnableToProduceResultException;
import org.jdbi.v3.core.rule.H2DatabaseRule;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EnumConfigTest {
    @Rule
    public H2DatabaseRule dbRule = new H2DatabaseRule();

    @Test
    public void byNameIsDefault() {
        assertThat(dbRule.getJdbi().getConfig(EnumConfig.class).isEnumHandledByName()).isTrue();
    }

    @Test
    public void namesAreBoundCorrectly() {
        Handle h = dbRule.openHandle();

        h.createUpdate("insert into something (id, name) values (1, :name)")
            .bind("name", Foobar.FOO)
            .execute();

        String ordinal = h.createQuery("select name from something")
            .mapTo(String.class)
            .findOnly();

        assertThat(ordinal)
            .isEqualTo(Foobar.FOO.name());
    }

    @Test
    public void namesAreMappedCorrectly() {
        Handle h = dbRule.openHandle();

        Foobar name = h.createQuery("select :name")
            .bind("name", Foobar.FOO.name())
            .mapTo(Foobar.class)
            .findOnly();

        assertThat(name)
            .isEqualTo(Foobar.FOO);
    }

    @Test
    public void ordinalsAreBoundCorrectly() {
        Handle h = dbRule.openHandle();
        h.getConfig(EnumConfig.class).setEnumHandledByName(false);

        h.createUpdate("insert into something (id, intValue) values (1, :ordinal)")
            .bind("ordinal", Foobar.FOO)
            .execute();

        Integer ordinal = h.createQuery("select intValue from something")
            .mapTo(Integer.class)
            .findOnly();

        assertThat(ordinal)
            .isEqualTo(Foobar.FOO.ordinal());
    }

    @Test
    public void ordinalsAreMappedCorrectly() {
        Handle h = dbRule.openHandle();
        h.getConfig(EnumConfig.class).setEnumHandledByName(false);

        Foobar name = h.createQuery("select :ordinal")
            .bind("ordinal", Foobar.FOO.ordinal())
            .mapTo(Foobar.class)
            .findOnly();

        assertThat(name)
            .isEqualTo(Foobar.FOO);
    }

    @Test
    public void badNameThrows() {
        dbRule.getJdbi().useHandle(h -> {
            assertThatThrownBy(h.createQuery("select 'xxx'").mapTo(Foobar.class)::findOnly)
                .isInstanceOf(UnableToProduceResultException.class)
                .hasMessageContaining("no Foobar value could be matched to the name xxx");
        });
    }

    @Test
    public void badOrdinalThrows() {
        dbRule.getJdbi().useHandle(h -> {
            h.getConfig(EnumConfig.class).setEnumHandledByName(false);

            assertThatThrownBy(h.createQuery("select 2").mapTo(Foobar.class)::findOnly)
                .isInstanceOf(UnableToProduceResultException.class)
                .hasMessageContaining("no Foobar value could be matched to the ordinal 2");
        });
    }

    @Test
    public void testNull() {
        dbRule.getJdbi().useHandle(h -> {
            h.createUpdate("create table enums(value varchar)").execute();

            h.createUpdate("insert into enums(value) values(:enum)")
                .bindByType("enum", null, Foobar.class)
                .execute();

            String inserted = h.createQuery("select value from enums")
                .mapTo(String.class)
                .findOnly();
            assertThat(inserted).isNull();

            Foobar mapped = h.createQuery("select value from enums")
                .mapTo(Foobar.class)
                .findOnly();
            assertThat(mapped).isNull();
        });
    }

    // bar is unused to make sure we don't have any coincidental correctness from only having a single value otherwise
    private enum Foobar {
        FOO, BAR
    }
}
