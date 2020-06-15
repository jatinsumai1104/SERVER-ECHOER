import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.lang.ClassNotFoundException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;

public class RSA{
    public static void saveToFile(String fileName,BigInteger mod, BigInteger exp) throws IOException{
        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        try{
            oout.writeObject(mod);
            oout.writeObject(exp);
        }
        catch (Exception e){
            throw new IOException("Unexpected error", e);
        }
        finally{
            oout.close();
        }
    }

    public static void generateKeys() throws NoSuchAlgorithmException,InvalidKeySpecException,IOException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,ClassNotFoundException,BadPaddingException {
        /**Generating Keys**/
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();

        /**Generating Algorithm Specific details**/
        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

        /**Saving Public and Private Keys Specs in public and private key files**/
        saveToFile("keyFiles\\public.key", pub.getModulus(), pub.getPublicExponent());
        saveToFile("keyFiles\\private.key", priv.getModulus(), priv.getPrivateExponent());        

        // String str = new String("Hello");
        // byte[] arr = rsaEncrypt(str.getBytes());
        // System.out.println(new String(arr));
        // System.out.println(new String(rsaDecrypt(arr)));
    }

   	static PublicKey readKeyFromFile(String keyFileName) throws IOException,ClassNotFoundException{
        Class RSA = Class.forName("RSA");
        InputStream in = RSA.class.getResourceAsStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try{
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        }
        catch (Exception e){
            throw new RuntimeException("Spurious serialisation error", e);
        }
        finally
        {
            oin.close();
        }
	}

	static PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException,ClassNotFoundException{
        Class RSA = Class.forName("RSA");
        InputStream in = RSA.class.getResourceAsStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try{
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey priKey = fact.generatePrivate(keySpec);
            return priKey;
        }
        catch (Exception e){
            throw new RuntimeException("Spurious serialisation error", e);
        }
        finally
        {
            oin.close();
        }
	}

	public static byte[] fromHexString(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public static byte[] rsaEncrypt(byte[] data) throws NoSuchAlgorithmException,InvalidKeySpecException,IOException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,ClassNotFoundException,BadPaddingException{
		PublicKey pubKey = readKeyFromFile("keyFiles\\public.key");
	    Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
	    byte[] cipherData = cipher.doFinal(data);
	    return cipherData;
	}

	public static byte[] rsaDecrypt(byte[] data) throws NoSuchAlgorithmException,InvalidKeySpecException,IOException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,ClassNotFoundException,BadPaddingException{
		PrivateKey privateKey = readPrivateKeyFromFile("keyFiles\\private.key");
	    Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(Cipher.DECRYPT_MODE,privateKey);
	    byte[] cipherData = cipher.doFinal(data);
	    return cipherData;
	}
	public static void main(String[] args) throws Exception{
		String str = "Hello";
		byte[] arr = rsaEncrypt(str.getBytes());	
		String encrypted = new String(arr,"ISO-8859-1");
		byte[] decrypt = encrypted.getBytes("ISO-8859-1");
		System.out.println(new String(rsaDecrypt(decrypt)));
	}
}
