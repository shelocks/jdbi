/*
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
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
package org.jdbi.v3.core.internal.exceptions;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jdbi.v3.core.internal.UtilityClassException;

public class Unchecked {
    private Unchecked() {
        throw new UtilityClassException();
    }

    public static <T> Callable<T> callable(CheckedCallable<T> checkedCallable) {
        return () -> {
            try {
                return checkedCallable.call();
            } catch (Throwable t) {
                throw Sneaky.throwAnyway(t);
            }
        };
    }

    public static <T> Supplier<T> supplier(CheckedSupplier<T> checkedSupplier) {
        return () -> {
            try {
                return checkedSupplier.get();
            } catch (Throwable t) {
                throw Sneaky.throwAnyway(t);
            }
        };
    }

    public static <X, T> Function<X, T> function(CheckedFunction<X, T> checkedFunction) {
        return x -> {
            try {
                return checkedFunction.apply(x);
            } catch (Throwable t) {
                throw Sneaky.throwAnyway(t);
            }
        };
    }

    public static <X, Y, T> BiFunction<X, Y, T> biFunction(CheckedBiFunction<X, Y, T> checkedBiFunction) {
        return (x, y) -> {
            try {
                return checkedBiFunction.apply(x, y);
            } catch (Throwable t) {
                throw Sneaky.throwAnyway(t);
            }
        };
    }
}
