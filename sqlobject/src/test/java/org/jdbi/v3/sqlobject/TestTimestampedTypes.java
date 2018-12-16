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
package org.jdbi.v3.sqlobject;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.jdbi.v3.core.Time;
import org.jdbi.v3.sqlobject.customizer.Timestamped;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.testing.JdbiRule;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTimestampedTypes {
    @Rule
    public JdbiRule dbRule = JdbiRule.h2().withPlugin(new SqlObjectPlugin());

    @Test
    public void typeShouldBeConfigurable() {
        dbRule.getJdbi().useHandle(h -> {
            ZonedDateTime now = ZonedDateTime.now();
            Clock fixedOnNow = Clock.fixed(now.toInstant(), now.getZone());

            h.getConfig(Time.class).setClock(fixedOnNow);

            Dao dao = h.attach(Dao.class);

            assertThat(dao.offsetDateTimeDefault().toString())
                .isEqualTo(OffsetDateTime.now(fixedOnNow).toString());

            assertThat(dao.localDate().toString())
                .isEqualTo(LocalDate.now(fixedOnNow).toString());

            // TODO why the truncation?
            assertThat(dao.localTime().toString())
                .isEqualTo(LocalTime.now(fixedOnNow).truncatedTo(ChronoUnit.SECONDS).toString());

            assertThat(dao.localDateTime().toString())
                .isEqualTo(LocalDateTime.now(fixedOnNow).toString());

            assertThat(dao.zonedDateTime().toString())
                .isEqualTo(ZonedDateTime.now(fixedOnNow).toString());

            assertThat(dao.instant().toString())
                .isEqualTo(Instant.now(fixedOnNow).toString());

            // TODO OffsetTime, Year, and YearMonth support
//            assertThat(dao.offsetTime().toString())
//                .isEqualTo(OffsetTime.now(fixedOnNow).toString());

//            assertThat(dao.year().toString())
//                .isEqualTo(Year.now(fixedOnNow).toString());

//            assertThat(dao.yearMonth().toString())
//                .isEqualTo(YearMonth.now(fixedOnNow).toString());
        });
    }

    public interface Dao {
        String SELECT = "select :time";

        @SqlQuery(SELECT)
        @Timestamped("time")
        OffsetDateTime offsetDateTimeDefault();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = LocalDate.class)
        LocalDate localDate();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = LocalTime.class)
        LocalTime localTime();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = LocalDateTime.class)
        LocalDateTime localDateTime();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = ZonedDateTime.class)
        ZonedDateTime zonedDateTime();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = Instant.class)
        Instant instant();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = OffsetTime.class)
        OffsetTime offsetTime();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = Year.class)
        Year year();

        @SqlQuery(SELECT)
        @Timestamped(value = "time", type = YearMonth.class)
        YearMonth yearMonth();
    }
}
