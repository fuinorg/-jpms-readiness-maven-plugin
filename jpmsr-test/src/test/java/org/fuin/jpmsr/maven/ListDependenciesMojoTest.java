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
package org.fuin.jpmsr.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link SystemMojo}.
 */
public class ListDependenciesMojoTest {

    // CHECKSTYLE:OFF Test

    private static final File TEST_DIR = new File("target/test-classes/test-project");

    private Verifier verifier;

    @Before
    public void setup() throws Exception {
        verifier = new Verifier(TEST_DIR.getAbsolutePath(), true);
        verifier.deleteArtifacts("org.fuin.jpmsr", "jpmsr-test-project", "0.0.1");
    }

    @After
    public void teardown() throws VerificationException {
        verifier.displayStreamBuffers();
    }

    @Test
    public void testMojo() throws VerificationException {

        // PREPARE
        final List<String> goals = new ArrayList<String>();
        // goals.add("clean");        
        // goals.add("verify");
        goals.add("jpms-readiness:verify");

        // TEST
        verifier.executeGoals(goals);

        // VERIFY
        System.out.println(
                "=================================== PLUGIN OUTPUT BEGIN ===================================");
        final List<String> lines = verifier.loadFile(verifier.getBasedir(), verifier.getLogFileName(), false);
        for (final String line : lines) {
            System.out.println(line);
        }
        System.out.println(
                "=================================== PLUGIN OUTPUT END =====================================");
        verifier.verifyErrorFreeLog();

        // Check for resolved dependencies
        verifier.verifyTextInLog("Java Platform Module System (JPMS) Readiness:");
        verifier.verifyTextInLog("org.jboss:jandex:jar:2.1.3.Final:compile");
        verifier.verifyTextInLog("jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.3:compile");
        verifier.verifyTextInLog("jakarta.activation:jakarta.activation-api:jar:1.2.2:compile");

    }

    // CHECKSTYLE:OFF Test

}
