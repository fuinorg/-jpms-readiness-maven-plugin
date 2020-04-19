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

import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;

/**
 * Lists the dependencies.
 */
@Mojo(name = "verify", requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true, threadSafe = true)
@Execute(phase = LifecyclePhase.TEST_COMPILE)
public final class ListDependenciesMojo extends AbstractMojo {

    private static final String DIVIDER = "=============================================";

    private static final Logger LOG = LoggerFactory.getLogger(ListDependenciesMojo.class);

    @Component
    private MavenProject mavenProject;

    @Component
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    /**
     * Defines the scope for which to list dependencies.
     */
    @Parameter(name = "scope", defaultValue = "compile")
    private String scope = null;

    /**
     * Returns the scope.
     * 
     * @return Scope.
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the scope.
     * 
     * @param scope
     *            Scope to set.
     */
    public void setScope(final String scope) {
        this.scope = scope;
    }

    private String extractName(final String trimmed) {
        final StringTokenizer tok = new StringTokenizer(trimmed, ":");
        final String groupId = tok.nextToken();
        final String artifactId = tok.nextToken();
        final String type = tok.nextToken();
        final String version = tok.nextToken();
        final String scop = tok.nextToken();
        return groupId + ":" + artifactId + ":" + type + ":" + version + ":" + scop;
    }

    Map<String, File> dependencyMap(final File txtFile) {
        final Map<String, File> map = new HashMap<>();
        try (final LineNumberReader lnr = new LineNumberReader(new FileReader(txtFile))) {
            String line = null;
            while ((line = lnr.readLine()) != null) {
                final String trimmed = line.trim();
                if ((trimmed.length() > 0) 
                        && (!trimmed.equalsIgnoreCase("The following files have been resolved:"))
                        && (!trimmed.contains("none"))) {
                    final String name = extractName(trimmed);
                    final String rest = trimmed.substring(name.length() + 1);
                    final int p = rest.indexOf(".jar");
                    final String filename = rest.substring(0, p + 4);
                    map.put(name, new File(filename));
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException("Failed to read output of Maven Dependency Plugin: " + txtFile, ex);
        }
        return map;
    }

    @Override
    public final void execute() throws MojoExecutionException {
        StaticLoggerBinder.getSingleton().setMavenLog(getLog());

        LOG.info(DIVIDER);
        LOG.info("Java Platform Module System (JPMS) Readiness:");
        LOG.info(DIVIDER);

        final String filename = mavenProject.getBuild().getDirectory() + File.separator + "dependencies.txt";

        final Plugin plugin = plugin("org.apache.maven.plugins", "maven-dependency-plugin", "3.1.2");
        final Xpp3Dom configuration = configuration(element(name("includeScope"), scope), element(name("outputFile"), filename),
                element(name("outputAbsoluteArtifactFilename"), "true"));
        final ExecutionEnvironment executionEnvironment = executionEnvironment(mavenProject, mavenSession, pluginManager);
        executeMojo(plugin, goal("list"), configuration, executionEnvironment);

        final Map<String, File> map = dependencyMap(new File(filename));
        final Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            final String name = it.next();
            final File jarFile = map.get(name);
            final JpmsState state = new JpmsState(jarFile);
            if (state.isReady()) {
                LOG.info("{} => {}", name, state);
            } else {
                LOG.warn("{} => {}", name, state);
            }
        }
        
        LOG.info(DIVIDER);

    }

}
