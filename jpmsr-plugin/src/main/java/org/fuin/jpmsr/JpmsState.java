package org.fuin.jpmsr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Captures the availability of the JPMS information.
 */
public final class JpmsState {

    private final boolean moduleInfo;

    private final String automaticModuleName;

    /**
     * Constructor with JAR to analyze.
     * 
     * @param jarFile
     *            Jar file.
     */
    public JpmsState(final File jarFile) {
        super();
        try (final JarFile jar = new JarFile(jarFile)) {
            moduleInfo = hasModuleInfo(jar);
            automaticModuleName = automaticModuleName(jar);
        } catch (final IOException ex) {
            throw new RuntimeException("Error reading JAR: " + jarFile, ex);
        }
    }

    /**
     * Determines if the file has a module information.
     * 
     * @return If the file has a module-info class {@literal true}, else {@literal false}.
     */
    public final boolean isModuleInfo() {
        return moduleInfo;
    }

    /**
     * Determines if the manifest file has an automatic module name.
     * 
     * @return If the file has a "META-INF/MANIFEST.MF" with a "Automatic-Module-Name" entry {@literal true}, else {@literal false}.
     */
    public final boolean isAutomaticModuleName() {
        return automaticModuleName != null;
    }

    /**
     * Returns the automatic module name if available.
     * 
     * @return Name or {@literal null}.
     */
    public final String getAutomaticModuleName() {
        return automaticModuleName;
    }

    /**
     * Determines if either an automatic module name is provided or a module info.
     * 
     * @return If the JAR is (almost) modularized {@literal true}, else {@literal false}.
     */
    public boolean isReady() {
        return isAutomaticModuleName() || isModuleInfo();
    }

    @Override
    public final String toString() {
        if (isModuleInfo()) {
            return "module-info";
        }
        if (isAutomaticModuleName()) {
            return "Automatic-Module-Name";
        }
        return "No module-info and no Automatic-Module-Name";
    }

    private static boolean hasModuleInfo(final JarFile jarFile) {
        final String name = "module-info.class";
        return jarFile.getJarEntry(name) != null;
    }

    private static String automaticModuleName(final JarFile jarFile) {
        final String name = "META-INF/MANIFEST.MF";
        final JarEntry entry = jarFile.getJarEntry(name);
        if (entry == null) {
            return null;
        }
        try (final InputStream in = jarFile.getInputStream(entry)) {
            final Manifest manifest = new Manifest(in);
            return manifest.getMainAttributes().getValue("Automatic-Module-Name");

        } catch (final IOException ex) {
            throw new RuntimeException("Failed to read " + name, ex);
        }
    }

}
