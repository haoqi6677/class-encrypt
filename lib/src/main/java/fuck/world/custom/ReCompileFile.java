package fuck.world.custom;

import fuck.world.classfile.ClassFile;
import fuck.world.classfile.ClassWriter;
import fuck.world.classfile.ConstantPoolException;
import fuck.world.rsa.Rsa;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ReCompileFile {
    private List<File> originClassFiles;
    Rsa rsa;

    public List<File> getOriginClassFiles() {
        return originClassFiles;
    }

    public void setOriginClassFiles(List<File> originClassFiles) {
        this.originClassFiles = originClassFiles;
    }

    public Rsa getRsa() {
        return rsa;
    }

    public void setRsa(Rsa rsa) {
        this.rsa = rsa;
    }

    private ReCompileFile() {
    }

    public ReCompileFile(List<File> originClassFiles, Rsa rsa) {
        this.originClassFiles = originClassFiles;
        this.rsa = rsa;
    }

    public void listFiles(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (final File subFile : Objects.requireNonNull(file.listFiles())) {
                listFiles(subFile);
            }
        }
        if (file.getName().endsWith(".class")) {
            originClassFiles.add(file);
        }
    }

    public void encrypt() throws ConstantPoolException, IOException {
        for (File it : originClassFiles) {
            ClassFile classFile = ClassFile.read(it);
            classFile.setRsa(rsa);
            classFile.methodEncrypt();

            ClassWriter classWriter = new ClassWriter();
            classWriter.write(classFile, it);
            classWriter.close();
        }
    }

    public void decrypt() throws ConstantPoolException, IOException {
        for (File it : originClassFiles) {
            ClassFile classFile = ClassFile.read(it);
            classFile.setRsa(rsa);
            classFile.methodDecrypt();

            ClassWriter classWriter = new ClassWriter();
            classWriter.write(classFile, it);
            classWriter.close();
        }
    }
}
