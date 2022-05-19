package servidor;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.List;

import javax.net.ServerSocketFactory;

import sqlite.Connector;
import sqlite.Logs;
import sqlite.Peticion;

public class Server {

	private static final String RUTA_FICHEROS_LOG = "../src/servidor/";

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
				System.out.println(fecha);
				String idCliente = input.readLine();

				System.out.println(idCliente);
				
				String firma = input.readLine();

				
				Peticion peticion = new Peticion(mensaje, fecha, idCliente, firma);
				
				Boolean comprobacionMensaje = true;
				
				System.err.println("Peticion: \n" + peticion);
				if (comprobacionMensaje) {
					output.println("Peticion OK");
					System.out.println("Peticion OK");
					peticion.save(true);

				} else {
					output.println("Peticion INCORRECTA");
					System.out.println("Peticion INCORRECTA");
					peticion.save(false);

				}

				generarLogs();

				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private void generarLogs() {
		Connector con = new Connector();
		con.connect();
		List<Logs> l = null;
		try {
			l = con.generarLog(); 
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (l.size() > 0) {
			FileWriter fichero = null;
			PrintWriter pw = null;
			try {
				fichero = new FileWriter(RUTA_FICHEROS_LOG + "prueba.txt");
				pw = new PrintWriter(fichero);
				for (Logs logs : l) {
					pw.println(logs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fichero)
						fichero.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		con.close();
	}

	public static void main(String args[]) throws Exception {

		Server server = new Server();
		server.runServer();
	}

}
