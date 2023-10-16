/*
 *    Copyright 2017 Luke Sosnicki, Arild G. Gjerd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Modified from version 3.0 to version 3.6 - Arild G. Gjerd
 */

package ljpf.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.dependency.fromConfiguration.ArtifactItem;
import org.apache.maven.plugins.dependency.fromConfiguration.UnpackMojo;
import org.apache.maven.plugins.dependency.utils.markers.MarkerHandler;
import org.apache.maven.plugins.dependency.utils.markers.UnpackFileMarkerHandler;

import java.io.File;
import java.util.List;

/**
 * Created by souzen on 03.04.2017.
 */
@Mojo(name = "make-plugin-repository", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class MakePluginRepositoryMojo extends UnpackMojo {

    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/plugins")
    private File pluginsDir;

    @Parameter(defaultValue = "plugin")
    private String defaultClassifier;


    //@Parameter(defaultValue = "${plexus.container}")
    //private PlexusContainer container;

    //@Component
    //private PlexusContainer container;

    //@Component
    //private ArchiverManager archiverManager;


    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) {
            return;
        }

        verifyRequirements();

        for (ArtifactItem artifactItem : getArtifactItems()) {
            if (artifactItem.getClassifier() == null) {
                artifactItem.setClassifier(defaultClassifier);
            }
        }

        List<ArtifactItem> processedItems = getProcessedArtifactItems(false);
        for (ArtifactItem artifactItem : processedItems) {
            if (artifactItem.isNeedsProcessing()) {
                unpackArtifact(artifactItem);
            } else {
                this.getLog().info(artifactItem.getArtifact().getFile().getName() + " already unpacked.");
            }
        }
    }

    private void unpackArtifact(ArtifactItem artifactItem)
            throws MojoExecutionException {
        MarkerHandler handler = new UnpackFileMarkerHandler(artifactItem, getMarkersDirectory());

        unpack(artifactItem.getArtifact(), artifactItem.getType(), artifactItem.getOutputDirectory(),
                artifactItem.getIncludes(), artifactItem.getExcludes(), artifactItem.getEncoding());
        handler.setMarker();
    }


        /*
    // ORIGINAL
    private void unpackArtifact(ArtifactItem artifactItem)
            throws MojoExecutionException {
        MarkerHandler handler = new UnpackFileMarkerHandler(artifactItem, getMarkersDirectory());

        unpack(artifactItem.getArtifact(), artifactItem.getType(), artifactItem.getOutputDirectory(),
                artifactItem.getIncludes(), artifactItem.getExcludes(), artifactItem.getEncoding());
        handler.setMarker();
    }
     */


    /*
    private void unpackArtifact(ArtifactItem artifactItem) throws MojoExecutionException {
        MarkerHandler handler = new UnpackFileMarkerHandler(artifactItem, getMarkersDirectory());

        File artifactFile = artifactItem.getArtifact().getFile();
        File outputDirectory = artifactItem.getOutputDirectory();

        // Check if artifact file is present
        if (artifactFile == null || !artifactFile.isFile()) {
            throw new MojoExecutionException("Artifact file is missing for: " + artifactItem.getArtifact());
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(artifactFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[2048];  // Adjust buffer size as needed
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File outputFile = new File(outputDirectory, entry.getName());

                if (entry.isDirectory()) {
                    if (!outputFile.exists()) {
                        if (!outputFile.mkdirs()) {
                            throw new MojoExecutionException("Could not create directory: " + outputFile.getAbsolutePath());
                        }
                    }
                } else {
                    // Make sure all directories are created
                    File parent = outputFile.getParentFile();
                    if (!parent.exists()) {
                        if (!parent.mkdirs()) {
                            throw new MojoExecutionException("Could not create directory: " + parent.getAbsolutePath());
                        }
                    }

                    // Write files to disk
                    try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error unpacking artifact: " + artifactItem.getArtifact(), e);
        }

        handler.setMarker();  // Indicate this artifact is processed
    }

     */

    /*
    private void unpackArtifact(ArtifactItem artifactItem) throws MojoExecutionException {
        MarkerHandler handler = new UnpackFileMarkerHandler(artifactItem, getMarkersDirectory());
        File artifactFile = artifactItem.getArtifact().getFile();

        if (artifactFile == null || !artifactFile.exists()) {
            throw new MojoExecutionException("Artifact file does not exist: " + artifactItem.getArtifact());
        }

        // The UnArchiver will be dynamically looked up, based on the type of file (zip, tar, etc.)
        UnArchiver unArchiver;
        try {
            // Adjust the logic here to properly handle .jar files as .zip archives.
            String archiveType = (artifactFile.getName().endsWith(".zip") || artifactFile.getName().endsWith(".jar")) ? "zip" : "tar";
            unArchiver = container.lookup(UnArchiver.class, archiveType);
        } catch (ComponentLookupException e) {
            throw new MojoExecutionException("Unable to lookup UnArchiver component: ", e);
        }

        unArchiver.setSourceFile(artifactFile);
        unArchiver.setDestDirectory(artifactItem.getOutputDirectory());

        try {
            unArchiver.extract(); // This performs the actual unpack operation
        } catch (Exception e) {
            throw new MojoExecutionException("Error unpacking file: " + artifactFile + " to: " + artifactItem.getOutputDirectory(), e);
        }

        handler.setMarker();
    }

     */

    /*
    private void unpackArtifact(ArtifactItem artifactItem) throws MojoExecutionException {
        MarkerHandler handler = new UnpackFileMarkerHandler(artifactItem, getMarkersDirectory());
        File artifactFile = artifactItem.getArtifact().getFile();

        if (artifactFile == null || !artifactFile.exists()) {
            throw new MojoExecutionException("Artifact file does not exist: " + artifactItem.getArtifact());
        }

        // Log for debugging: to verify if this method processes all necessary artifacts
        getLog().info("Unpacking: " + artifactFile.getName());

        try {
            // Obtain the appropriate unarchiver. JARs are essentially ZIP files.
            UnArchiver unArchiver;
            if (artifactFile.getName().endsWith(".jar")) {
                unArchiver = (ZipUnArchiver) container.lookup(UnArchiver.class, "zip");
            } else {
                // For other types, you could extend the logic to handle different file types.
                unArchiver = container.lookup(UnArchiver.class, "tar");
            }

            unArchiver.setSourceFile(artifactFile);
            unArchiver.setDestDirectory(artifactItem.getOutputDirectory());

            // Set additional options on the unArchiver here, if needed.

            // Perform the extraction
            unArchiver.extract();

        } catch (ArchiverException | ComponentLookupException e) {
            throw new MojoExecutionException("Error unpacking file: " + artifactFile + " to: " + artifactItem.getOutputDirectory(), e);
        }

        handler.setMarker();  // Mark this artifact as processed
    }

     */

}
