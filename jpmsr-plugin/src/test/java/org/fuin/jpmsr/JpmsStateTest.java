/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.jpmsr;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

/**
 * Test for {@link JpmsState} class.
 */
public class JpmsStateTest {

    private static final File DIR = new File("src/test/resources");

    @Test
    public void testNoInfo() {

        // PREPARE
        final File file = new File(DIR, "gson-2.8.5.jar");

        // TEST
        final JpmsState testee = new JpmsState(file);

        // VERIFY
        assertThat(testee.isModuleInfo()).isFalse();
        assertThat(testee.isAutomaticModuleName()).isFalse();

    }

    @Test
    public void testModuleInfo() {

        // PREPARE
        final File file = new File(DIR, "slf4j-api-1.8.0-beta2.jar");

        // TEST
        final JpmsState testee = new JpmsState(file);

        // VERIFY
        assertThat(testee.isModuleInfo()).isTrue();
        assertThat(testee.isAutomaticModuleName()).isFalse();

    }

    @Test
    public void testAutomaticModuleName() {

        // PREPARE
        final File file = new File(DIR, "utils4j-0.11.0-SNAPSHOT.jar");

        // TEST
        final JpmsState testee = new JpmsState(file);

        // VERIFY
        assertThat(testee.isModuleInfo()).isFalse();
        assertThat(testee.isAutomaticModuleName()).isTrue();
        assertThat(testee.getAutomaticModuleName()).isEqualTo("org.fuin.utils4j");

    }

}
