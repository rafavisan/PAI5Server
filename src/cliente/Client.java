package cliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class Client {
	// Constructor que abre una conexión Socket para enviar mensaje/MAC al
	// servidor
	public Client() throws NoSuchAlgorithmException {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);
			// Crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			//String userName = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
			// Envío del mensaje al servidor
			
			
			
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			KeyPair kp = kpg.generateKeyPair();
			//RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),RSAPublicKeySpec.class);

			System.out.println("Clave pública: " + kp.getPublic());
			System.out.println("Clave privada: " + kp.getPrivate().toString());
			
			
			String peticion = "123 Camas";
			String fecha = String.valueOf((new Date()).getTime());
			
			String clavePublica = "clave publica";
			String idCliente = "1";
			String firma = "firma del mensaje"; // TODO FIRMAR MENSAJE
			
//			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//
//			kpg.initialize(1024);
//			KeyPair kp = kpg.generateKeyPair();
			

			
			
			
			output.println(peticion);
			output.println(fecha);
			output.println(clavePublica);
			output.println(idCliente);
			output.println(firma);
			
			
			output.println(kp.getPublic());
			

			
			
			// Importante para que el mensaje se envíe
			output.flush();
			
			
			
			// Crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Lee la respuesta del servidor
			String respuesta = input.readLine();
			// Muestra la respuesta al cliente
			JOptionPane.showMessageDialog(null, respuesta);
			// Se cierra la conexion
			output.close();
			input.close();
			socket.close();
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

		// Salida de la aplicacion
		finally {
			System.exit(0);
		}

	}

	public static void main(String args[]) throws NoSuchAlgorithmException {
		new Client();
	}
}
