package fuck.world.plugin.maven;

import fuck.world.custom.ReCompileFile;
import fuck.world.rsa.RsaDefaultImpl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.ArrayList;

public class ReCompilePlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
    private File outputDirectory;
    @Parameter(property = "pubKey", required = true)
    private String pubKey;
    @Parameter(property = "priKey", required = true)
    private String priKey;
    @Parameter(property = "encrypt", defaultValue = "true")
    private Boolean encrypt;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Recompiling classes in: " + outputDirectory);
        try {
            ReCompileFile reCompileFile = new ReCompileFile(new ArrayList<>(), new RsaDefaultImpl(pubKey, priKey));

            if (outputDirectory == null) {
                getLog().info("class file empty");
                return;
            }
            reCompileFile.listFiles(outputDirectory);

            if (reCompileFile.getOriginClassFiles().isEmpty()) {
                return;
            }

            if (encrypt) {
                reCompileFile.encrypt();
                return;
            }
            reCompileFile.decrypt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
