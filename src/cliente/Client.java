package cliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class Client {
	public Client() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("127.0.0.1", 7070);
			
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.generateKeyPair();

			System.out.println("Clave pública: " + kp.getPublic());
			System.out.println("Clave privada: " + kp.getPrivate().toString());
			
			
			
			String fecha = "20/05/2022";
			
			Signature dsa = Signature.getInstance("RSA");
			
			PrivateKey clavepriv= kp.getPrivate();
 			dsa.initSign(clavepriv);
			String mensaje = "123 Camas, 4 Sillas, 20 Sillones, 10 Mesas";
 			dsa.update(mensaje.getBytes());
 			byte[] firma = dsa.sign(); // MENSAJE FIRMADO
 			
			String idCliente = "53";
			

			
			
			output.println(mensaje);
			output.println(fecha);
			output.println(idCliente);
			output.println(firma);
			
			output.flush();
			
			
			
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String respuesta = input.readLine();
			
			JOptionPane.showMessageDialog(null, respuesta);
			
			output.close();
			input.close();
			socket.close();
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

		
		finally {
			System.exit(0);
		}

	}

	public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		new Client();
	}
}
