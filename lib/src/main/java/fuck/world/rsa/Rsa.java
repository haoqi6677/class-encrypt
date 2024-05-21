package fuck.world.rsa;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Rsa {
    private Rsa() {
    }

    private PrivateKey privateKey = null;
    private PublicKey publicKey = null;

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

    public void saveFile() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        saveKeyToFile(publicKey.getEncoded(), "./pub");
        saveKeyToFile(privateKey.getEncoded(), "./pri");
    }

    public static void main(String[] args) {
        // 获取本地 RSA 密钥对
        try {
            Rsa rsaExample = new Rsa("./pub", "./pri");
            PrivateKey privateKey = rsaExample.privateKey;
            PublicKey publicKey = rsaExample.publicKey;


            // 假设我们要加密的数据是
            String originalData = "Fuck World!";

            // 加密数据
            byte[] encryptedData = rsaExample.encryptData(originalData.getBytes(), publicKey);
            String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
            System.out.println("Encrypted data (Base64): " + encryptedDataBase64);

            // 解密数据
            byte[] decode = Base64.getDecoder().decode(encryptedDataBase64);
            byte[] decryptedData = rsaExample.decryptData(decode, privateKey);
            String decryptedDataString = new String(decryptedData);
            System.out.println("Decrypted data: " + decryptedDataString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] encryptData(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decryptData(byte[] data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decryptData(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveKeyToFile(byte[] key, String filePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            String privateKeyBase64 = Base64.getEncoder().encodeToString(key);
            outputStream.write(privateKeyBase64.getBytes());
            outputStream.flush();
        }
    }

    public PrivateKey readPrivateKeyFromFile(String filePath) throws IOException, InvalidKeySpecException {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            byte[] privateKeyBytes = readAllBytes(inputStream);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey readPublicKeyFromFile(String filePath) throws IOException, InvalidKeySpecException {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            byte[] publicKeyBytes = readAllBytes(inputStream);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
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
