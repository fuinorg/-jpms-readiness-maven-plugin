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
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

/**
 * Test for {@link ListDependenciesMojo}.
 */
public class ListDependenciesMojoTest {

    private static final File DIR = new File("src/test/resources");

    @Test
    public void testSetGet() throws MojoExecutionException {

        // PREPARE
        final ListDependenciesMojo testee = new ListDependenciesMojo();

        // TEST
        testee.setScope("runtime");

        // VERIFY
        assertThat(testee.getScope()).isEqualTo("runtime");

    }

    @Test
    public void testDependencyMap() {

        // PREPARE
        final File file = new File(DIR, "dependencies.txt");
        final ListDependenciesMojo testee = new ListDependenciesMojo();

        // TEST
        final Map<String, File> map = testee.dependencyMap(file);
        
        // VERIFY
        assertThat(map.get("org.jboss:jandex:jar:2.1.3.Final:compile"))
                .isEqualTo(new File("/home/developer/.m2/repository/org/jboss/jandex/2.1.3.Final/jandex-2.1.3.Final.jar"));
        assertThat(map.get("jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.3:compile")).isEqualTo(
                new File("/home/developer/.m2/repository/jakarta/xml/bind/jakarta.xml.bind-api/2.3.3/jakarta.xml.bind-api-2.3.3.jar"));
        assertThat(map.get("jakarta.activation:jakarta.activation-api:jar:1.2.2:compile")).isEqualTo(new File(
                "/home/developer/.m2/repository/jakarta/activation/jakarta.activation-api/1.2.2/jakarta.activation-api-1.2.2.jar"));
    }

    @Test
    public void testDependenciesNone() {

        // PREPARE
        final File file = new File(DIR, "dependencies-none.txt");
        final ListDependenciesMojo testee = new ListDependenciesMojo();

        // TEST
        final Map<String, File> map = testee.dependencyMap(file);
        
        // VERIFY
        assertThat(map).isEmpty();
        
    }
    
    @Test
    public void testDependencies3() {

        // PREPARE
        final File file = new File(DIR, "dependencies-3.txt");
        final ListDependenciesMojo testee = new ListDependenciesMojo();

        // TEST
        final Map<String, File> map = testee.dependencyMap(file);
        
        // VERIFY
        assertThat(map).isEmpty();
        
    }

}
