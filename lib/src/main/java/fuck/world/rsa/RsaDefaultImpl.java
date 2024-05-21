package fuck.world.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaDefaultImpl extends Rsa {

    public static void main(String[] args) {
        // 获取本地 RSA 密钥对
        try {
            Rsa rsaExample = new RsaDefaultImpl("./pub", "./pri");
            PrivateKey privateKey = rsaExample.getPrivateKey();
            PublicKey publicKey = rsaExample.getPublicKey();

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

    public RsaDefaultImpl(String priKey) throws Exception {
        super(priKey);
    }

    public RsaDefaultImpl(String pubKey, String priKey) throws Exception {
        super(pubKey, priKey);
    }

    protected byte[] encryptData(byte[] data, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    protected byte[] decryptData(byte[] data, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);

    }

    protected void saveKeyToFile(byte[] key, String filePath) throws IOException {
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
}
