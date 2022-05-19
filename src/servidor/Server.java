package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import javax.net.ServerSocketFactory;
public class Server {
	
	private ServerSocket serverSocket;


	public Server() throws Exception {

		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory
				.getDefault();
		serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
	}

	
	private void runServer() throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		while (true) {
			
			try {
				System.err.println("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				
				PrintWriter output = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				
				String mensaje = input.readLine();
				
				String fecha =input.readLine();
				String idCliente = input.readLine();
				String firma = input.readLine();
				System.out.println(mensaje+" "+ idCliente+" "+ fecha+ " "+ firma);
				Boolean comprobacionMensaje = true;
				
				System.err.println("Peticion: \n" + mensaje);
				if (comprobacionMensaje) {
					output.println("Peticion OK");
					System.out.println("Peticion OK");
				

				} else {
					output.println("Peticion INCORRECTA");
					System.out.println("Peticion INCORRECTA");
					

				}

				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws Exception {

		Server server = new Server();
		server.runServer();
	}

}
