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
package org.jdbi.v3.sqlobject.customizer.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import org.jdbi.v3.core.Time;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizer;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;
import org.jdbi.v3.sqlobject.customizer.Timestamped;

public class TimestampedFactory implements SqlStatementCustomizerFactory {
    private static final Map<Class<?>, Function<Clock, Temporal>> TIMERS;

    static {
        Map<Class<?>, Function<Clock, Temporal>> timers = new IdentityHashMap<>();
        timers.put(LocalDate.class, LocalDate::now);
        timers.put(LocalTime.class, LocalTime::now);
        timers.put(LocalDateTime.class, LocalDateTime::now);
        timers.put(ZonedDateTime.class, ZonedDateTime::now);
        timers.put(OffsetDateTime.class, OffsetDateTime::now);
        timers.put(Instant.class, Instant::now);
        timers.put(OffsetTime.class, OffsetTime::now);
        timers.put(Year.class, Year::now);
        timers.put(YearMonth.class, YearMonth::now);

        TIMERS = Collections.unmodifiableMap(timers);
    }

    @Override
    public SqlStatementCustomizer createForMethod(Annotation annotation, Class<?> sqlObjectType, Method method) {
        Timestamped cast = (Timestamped) annotation;

        String parameterName = cast.value();
        Class<?> type = cast.type();

        return stmt -> {
            Clock clock = stmt.getConfig(Time.class).getClock();
            Temporal timestamp = TIMERS.get(type).apply(clock);

            stmt.bind(parameterName, timestamp);
        };
    }
}
