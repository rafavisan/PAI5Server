package cliente;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;

public class GeneradorRSA {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();

		System.out.println("Clave pública: " + kp.getPublic());
		System.out.println("Clave privada: " + kp.getPrivate().toString());

		
		
		Signature sg = Signature.getInstance("SHA256withRSA", "SC");
		//sg.initVerify(publicKey)
		
		
		
		
		// byte[] publicKeyBase64 = kp.getPublic().getEncoded();
		//
		//
		// BigInteger modulus = kp.
		//
		//
		//
		// KeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent)
		//
		// KeyFactory.getInstance("RSA").generatePublic(keySpec);
		//
		//
		//
		// System.out.println("Clave pública codificada: "+ publicKeyBase64);

	}

}
