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
import java.security.Signature;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.net.ServerSocketFactory;

import sqlite.Connector;
import sqlite.Logs;
import sqlite.Peticion;

public class Server {

	private static final String RUTA_FICHEROS_LOG = "../src/servidor/";

	private ServerSocket serverSocket;

	// Constructor del Servidor
	public Server() throws Exception {

		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory
				.getDefault();
		serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
	}

	// Ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		while (true) {
			generarLogs();
			// Espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.println("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				// Abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				// Abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				// ////////////////////////////////
				// LECTURA DEL MENSAJE DEL CLIENTE
				// ////////////////////////////////

				String mensaje = input.readLine();
				Date fecha = new Date(new Long(input.readLine()));
				// TODO clavePublica
				String clavePublica = input.readLine();
				String idCliente = input.readLine();
				// TODO firma
				String firma = input.readLine();

				// Se genera la petición que hemos recibido
				Peticion peticion = new Peticion(mensaje, String.valueOf(fecha
						.getTime()), idCliente, firma);

				// //////////////////////////////////
				// COMPROBACION DEL MENSAJE/PETICION
				// //////////////////////////////////
				Boolean comprobacionMensaje = true;
				/*
				 * Comprobar si clave publica concuerda con clave publica del
				 * idCliente, devolver true o false
				 */
				if (!comprobarClavePublica("1", "PUBLIC-KEY-1")) {
					comprobacionMensaje = false;
				} else 
//					
				if (!(fecha.before(new Date())))
					comprobacionMensaje = false;

//				// TODO Verificar firma, devolver true o false
				else if (!(comprobacionFirma(firma)))
					comprobacionMensaje = false;

				// Si true mensaje OK sino mensaje INCORRECTO
				System.err.println("Peticion: \n" + peticion);
				if (comprobacionMensaje) {
					output.println("Peticion OK");
					System.out.println("Peticion OK");
					// Guardamos el pedido
					peticion.save(true);

				} else {
					output.println("Peticion INCORRECTA");
					System.out.println("Peticion INCORRECTA");
					peticion.save(false);

				}

				// //////////////////////////////
				// GENERAR LOGS
				// /////////////////////////////
				// Comprueba si ha pasado un mes para generar el log
				generarLogs();

				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private boolean comprobacionFirma(String firma) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		boolean result = false;
		

		 Signature sg = Signature.getInstance("SHA256withRSA","SC");
		 result = sg.verify(firma.getBytes());

		return result;
	}

	private boolean comprobarClavePublica(String idCliente,
			String clavePublicaRecibida) {
		boolean result = false;
		Connector con = new Connector();
		con.connect();
		result = con.comprobarClavePublica(idCliente, clavePublicaRecibida);
		con.close();

		return result;
	}

	private void generarLogs() {
		Connector con = new Connector();
		con.connect();
		List<Logs> l = null;
		try {
			//Devuelve lista de log guardados en la BD
			l = con.generarLog(); 
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (l.size() > 0) {
			// guardarFichero(String)
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

	// Programa principal
	public static void main(String args[]) throws Exception {

		Server server = new Server();
		server.runServer();
	}

}
