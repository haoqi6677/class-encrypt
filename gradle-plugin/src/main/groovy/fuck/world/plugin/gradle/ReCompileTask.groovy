package fuck.world.plugin.gradle

import fuck.world.custom.ReCompileFile
import fuck.world.rsa.Rsa
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class ReCompileTask extends DefaultTask {
    //env
    private boolean enable = true
    private String pubKey = ""
    private String priKey = ""
    private boolean encrypt = true

    @Input
    boolean getEnable() {
        return enable
    }

    void setEnable(boolean enable) {
        this.enable = enable
    }

    @Input
    String getPubKey() {
        return pubKey
    }

    void setPubKey(String pubKey) {
        this.pubKey = pubKey
    }

    @Input
    String getPriKey() {
        return priKey
    }

    void setPriKey(String priKey) {
        this.priKey = priKey
    }

    @Input
    boolean getEncrypt() {
        return encrypt
    }

    void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt
    }

    @TaskAction
    void recompile() {
        if (!enable) {
            return
        }
        if (encrypt && (pubKey == null || pubKey.isEmpty() || pubKey.isBlank())) {
            println("pubKey empty")
            return
        }
        if (!encrypt && (priKey == null || priKey.isEmpty() || priKey.isBlank())) {
            println("priKey empty")
            return
        }

        def reCompileFile = new ReCompileFile(new ArrayList<>(), new Rsa(pubKey, priKey))

        FileCollection localFiles = project.tasks.compileJava.outputs.files
        if (localFiles == null) {
            println("class file empty")
            return
        }
        for (File file : localFiles) {
            reCompileFile.listFiles(file)
        }

        if (reCompileFile.originClassFiles.isEmpty()) {
            return
        }

        if (encrypt) {
            reCompileFile.encrypt()
            return
        }
        reCompileFile.decrypt()
    }
}