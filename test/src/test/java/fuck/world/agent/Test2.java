package fuck.world.agent;

import fuck.world.classfile.*;
import fuck.world.rsa.Rsa;
import fuck.world.rsa.RsaDefaultImpl;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class Test2 {
    static String path;
    static Rsa rsaExample;

    @BeforeAll
    public static void before() {
        URL resource = Test2.class.getResource("/");
        path = Objects.requireNonNull(resource).getPath();

        try {
            rsaExample = new RsaDefaultImpl("d://pub", "d://pri");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取class
     */
 /*   @Test
    public void test1() throws IOException {
        File file = new File(path + "fuck/world/DumbDog.class");
        byte[] bytes = Files.readAllBytes(file.toPath());
//        for (byte aByte : bytes) {
//            //left add 0
//            System.out.print(String.format("%2s", Integer.toHexString(aByte & 0xff)) + " ");
//        }
//        System.out.println();
        ClassFile classFile = new ClassFile(bytes);
        ArrayList<ClassFile.CpEntry> constantPool = classFile.getConstant_pool();
        ArrayList<ClassFile.Method> methods = classFile.getMethods();
        for (ClassFile.Method method : methods) {
//            method.setAccess_flags((short) AccessFlag.PRIVATE.mask());
            short nameIndex = method.getName_index();
            ClassFile.CpEntry methodCpEntry = constantPool.get(nameIndex);

            byte[] info = ((ClassFile.CpUtf8) methodCpEntry).getBytes();
            for (byte b : info) {
                System.out.print(Character.toChars(b));
            }
            System.out.println();


            ArrayList<ClassFile.Attribute> attributes = method.getAttributes();
            for (ClassFile.Attribute attribute : attributes) {
                byte[] attrInfo = attribute.getInfo();
                for (byte b : attrInfo) {
                    System.out.print(Character.toChars(b));
                }
                System.out.println();
            }

        }

*//*        ArrayList<ClassFile.Attribute> attributes = classFile.getAttributes();
        for (ClassFile.Attribute attribute : attributes) {
            byte[] info = attribute.getInfo();
            for (byte b : info) {
                System.out.print(Character.toChars(b));
            }
            System.out.println();
        }*//*


//        for (int i = 0; i < constantPool.size(); i++) {
//            ClassFile.CpEntry cpEntry = constantPool.get(i);
//            if (cpEntry instanceof ClassFile.CpClass cpClass) {
//                ClassFile.CpEntry classEntity = constantPool.get(cpClass.getName_index());
//                byte[] info = ((ClassFile.CpUtf8) classEntity).getBytes();
//                for (byte b : info) {
//                    System.out.print(Character.toChars(b));
//                }
//                System.out.println();
//            }
//        }

        File newFile = new File(path + "fuck/world/DumbDog1.class");
        Files.write(newFile.toPath(), classFile.toByteArray());
    }*/
    @org.junit.jupiter.api.Test
    public void test2() throws Exception {
        File file = new File(path + "fuck/world/Dog.class");
        ClassFile classFile = ClassFile.read(file);
//        String name = classFile.getName();
//        System.out.println(name);

//        ConstantPool constantPool = classFile.constant_pool;
//        ConstantPool.CONSTANT_Class_info cpInfo = (ConstantPool.CONSTANT_Class_info) constantPool.get(classFile.this_class);
//        ConstantPool.CONSTANT_Utf8_info constantUtf8Info = (ConstantPool.CONSTANT_Utf8_info) constantPool.get(cpInfo.name_index);
//        constantUtf8Info.setValue("fuck/world/DumbDog1");


        Method[] methods = classFile.methods;
        for (Method method : methods) {
//            method.setAccess_flags(new AccessFlags((short) (AccessFlag.PRIVATE.mask())));
            Attributes attributes = method.getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute instanceof Code_attribute) {
                    Code_attribute codeAttribute = (Code_attribute) attribute;
                    byte[] code = codeAttribute.getCode();
                    byte[] encryptedData = rsaExample.encryptData(code);
                    codeAttribute.setCode(encryptedData);
                }
            }
        }

//        File newFile = new File(path + "fuck/world/DumbDog1.class");
        File newFile = new File(path + "fuck/world/redefine/Dog.class");
        ClassWriter classWriter = new ClassWriter();
        classWriter.write(classFile, newFile);
    }

    @org.junit.jupiter.api.Test
    public void test3() throws Exception {
        File file = new File(path + "fuck/world/redefine/Dog.class");
        ClassFile classFile = ClassFile.read(file);
//        String name = classFile.getName();
//        System.out.println(name);
        ConstantPool constantPool = classFile.constant_pool;
        ConstantPool.CONSTANT_Class_info cpInfo = (ConstantPool.CONSTANT_Class_info) constantPool.get(classFile.this_class);
        ConstantPool.CONSTANT_Utf8_info constantUtf8Info = (ConstantPool.CONSTANT_Utf8_info) constantPool.get(cpInfo.name_index);
        constantUtf8Info.setValue("fuck/world/redefine/Dog1");

        Method[] methods = classFile.methods;
        for (Method method : methods) {
//            method.setAccess_flags(new AccessFlags((short) (AccessFlag.PRIVATE.mask())));
            Attributes attributes = method.getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute instanceof Code_attribute) {
                    Code_attribute codeAttribute = (Code_attribute) attribute;
                    byte[] code = codeAttribute.getCode();
                    byte[] encryptedData = rsaExample.decryptData(code);
                    codeAttribute.setCode(encryptedData);
                }
            }
        }

        File newFile = new File(path + "fuck/world/redefine/Dog1.class");
        ClassWriter classWriter = new ClassWriter();
        classWriter.write(classFile, newFile);
    }
}
