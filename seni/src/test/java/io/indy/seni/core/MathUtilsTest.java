/*
 * Copyright 2013 Inderjit Gill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.indy.seni.core;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class MathUtilsTest {

    @Test
    public void interpolationTest() {
        assertThat(MathUtils.interpolate(0.5f, 1.0f, 0.5f)).isEqualTo(0.75f);
        assertThat(MathUtils.interpolate(0.5f, 1.0f, 0.0f)).isEqualTo(0.5f);
        assertThat(MathUtils.interpolate(0.5f, 1.0f, 1.0f)).isEqualTo(1.0f);
    }
}
