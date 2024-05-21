package fuck.world.agent;

import fuck.world.classfile.ClassFile;
import fuck.world.classfile.ClassWriter;
import fuck.world.rsa.Rsa;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JpClassFileTransformer implements ClassFileTransformer {
    Rsa rsa;
    AgentArg agentArg;

    public JpClassFileTransformer(Rsa rsa, AgentArg agentArg) {
        this.rsa = rsa;
        this.agentArg = agentArg;
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        String[] pkgs = agentArg.getPkgs();
        if (!containsAll(classNamePathSeparator(className), pkgs)) {
            return null;
        }

        try {
            ClassFile classFile = ClassFile.read(classfileBuffer);
            classFile.setRsa(rsa);
            classFile.methodDecrypt();

            try (ClassWriter classWriter = new ClassWriter()) {
                return classWriter.writeToByte(classFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 无论win还是linux，都替换classname路径为 . 链接
     *
     * @param className com/example/dog
     * @return com.example
     */
    private String classNamePathSeparator(String className) {
        return className.replace("/", ".");
    }

    private boolean containsAll(String className, String[] pkgs) {
        for (String classPath : pkgs) {
            if (className.startsWith(classPath)) {
                return true;
            }
        }
        return false;
    }
}