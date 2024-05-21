package fuck.world.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public abstract class Rsa {

    private PrivateKey privateKey = null;
    private PublicKey publicKey = null;


    private Rsa() {
    }

    public Rsa(String pubKey, String priKey) throws Exception {
        if (pubKey != null) {
            this.publicKey = readPublicKeyFromFile(pubKey);
        }
        if (priKey != null) {
            this.privateKey = readPrivateKeyFromFile(priKey);
        }
    }

    public Rsa(String priKey) throws Exception {
        this(null, priKey);
    }

    public int getPreEncryptLength() {
        return 128;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234 || c == 0 || c == 12644 || c == 10240 || c == 6158;
    }

    private boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!isBlankChar(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public void saveFile() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        saveKeyToFile(publicKey.getEncoded(), "./pub");
        saveKeyToFile(privateKey.getEncoded(), "./pri");
    }

    public byte[] encryptData(byte[] data) throws Exception {
        MethodCodeStruct methodCodeStruct = new MethodCodeStruct();

        int n = data.length;
        //如果长度未超过数量
        if (n < getPreEncryptLength()) {
            byte[] subCode = encryptData(data, publicKey);
            int subCodeLength = subCode.length;
            byte[] code = new byte[2 + subCodeLength];
            code[0] = (byte) ((subCodeLength >> 8) & 0xff);
            code[1] = (byte) (subCodeLength & 0xff);
            System.arraycopy(subCode, 0, code, 2, subCode.length);


            methodCodeStruct.setCount(1);
            methodCodeStruct.setCode(code);
            return methodCodeStruct.toByte();
        }

        //如果长度超过数量
        int count = n / getPreEncryptLength();
        count = n % getPreEncryptLength() != 0 ? count + 1 : count;

        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            byte[] subCode = null;
            if (i < count - 1) {
                subCode = new byte[getPreEncryptLength()];
            } else {
                subCode = new byte[n % getPreEncryptLength()];
            }
            System.arraycopy(data, i * getPreEncryptLength(), subCode, 0, subCode.length);
            byte[] encryptCode = encryptData(subCode, publicKey);

            int encryptCodeLength = encryptCode.length;
            list.add((byte) (encryptCodeLength >> 8));
            list.add((byte) (encryptCodeLength & 0xff));
            for (byte b : encryptCode) {
                list.add(b);
            }
        }
        byte[] code = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            code[i] = list.get(i);
        }
        methodCodeStruct.setCount(count & 0xffff);
        methodCodeStruct.setCode(code);
        return methodCodeStruct.toByte();
    }

    protected abstract byte[] encryptData(byte[] data, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    protected abstract byte[] decryptData(byte[] data, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

    public byte[] decryptData(byte[] data) throws Exception {
        if (data == null || (data[0] & 0xff) != MethodCodeStruct.ENCRYPT_FLAG) {
            return data;
        }
        int count = ((data[1] & 0xff) << 8) | (data[2] & 0xff);
        int i = 3;
        List<Byte> list = new ArrayList<>();
        for (int i1 = 0; i1 < count; i1++) {
            int length = (data[i++] << 8) | data[i++];
            byte[] subCode = new byte[length];
            System.arraycopy(data, i, subCode, 0, length);
            i += length;

            subCode = decryptData(subCode, getPrivateKey());
            for (byte b : subCode) {
                list.add(b);
            }
        }
        byte[] res = new byte[list.size()];
        for (int i1 = 0; i1 < list.size(); i1++) {
            res[i1] = list.get(i1);
        }
        return res;
    }

    protected abstract void saveKeyToFile(byte[] key, String filePath) throws IOException;

    protected abstract PrivateKey readPrivateKeyFromFile(String filePath) throws IOException, InvalidKeySpecException;

    protected abstract PublicKey readPublicKeyFromFile(String filePath) throws IOException, InvalidKeySpecException;

    protected byte[] readAllBytes(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream();) {
            byte[] data = new byte[1024]; // 每次读取的缓冲区大小
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }
}
