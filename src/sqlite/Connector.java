package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {

	String url = "../src/sqlite/BD-MOBIFIRMA";
	Connection connect;

	public void connect() {
		try {
			connect = DriverManager.getConnection("jdbc:sqlite:" + url);
			} catch (SQLException ex) {
			System.err.println("No se ha conectado a la base de datos\n");
		}
	}

	public void close() {
		try {

			connect.close();
		} catch (SQLException ex) {
			Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public void guardarPeticion(Peticion peticion, boolean tipo) {
		try {
			String tabla = "";

			if (tipo)
				tabla = "pedidos";
			else
				tabla = "pedidosincorrecta";

			PreparedStatement st = connect.prepareStatement("insert into "
					+ tabla
					+ " (mensaje, firma, ClienteId , fecha) values (?,?,?,?)");
			st.setString(1, peticion.getMensaje());
			st.setString(2, peticion.getFirma());
			st.setString(3, peticion.getClienteID());
			st.setString(4, peticion.getFecha());
			st.execute();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

	}

	public void guardarLog(Logs log) {
		try {
			PreparedStatement st = connect
					.prepareStatement("insert into logs (mes, year, porcentaje, tendencia) values (?,?,?,?)");
			st.setString(1, String.valueOf(log.getMes()));
			st.setString(2, String.valueOf(log.getYear()));
			st.setString(3, String.valueOf(log.getPorcentaje()));
			st.setString(4, log.getTendencia());
			st.execute();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

	}

	public boolean comprobarFechaLogs() throws SQLException {
		boolean result = false;
		Date fecha = new Date();
		PreparedStatement st = connect
				.prepareStatement("select max(LogID) , mes , anio  from registros;");

		ResultSet r = st.executeQuery();
		int mes = r.getInt(2);
		int anio = r.getInt(3);

		@SuppressWarnings("deprecation")
		int mesActual = fecha.getMonth() + 1;
		@SuppressWarnings("deprecation")
		int anioActual = fecha.getYear() + 1900;

		if (((mesActual > mes) && (anioActual == anio)) || (anioActual > anio))
			result = true;

		return result;
	}

	public List<Logs> generarLog() throws SQLException {
		List<Logs> listaLogs = new ArrayList<>();
		if (comprobarFechaLogs()) {
			Date fecha = new Date();
			PreparedStatement st = connect
					.prepareStatement("select * from logs");
			ResultSet r = st.executeQuery();

			listaLogs = new ArrayList<>();

			Logs l;
			int cont = 0;
			while (r.next()) {
				cont++;
				l = new Logs(cont, r.getInt(2), r.getInt(3), r.getInt(4),
						r.getString(5));
				listaLogs.add(l);

			}

			Logs ultimoLog = listaLogs.get(listaLogs.size() - 1);

			Logs antePenultimoLog = listaLogs.get(listaLogs.size() - 2);

			PreparedStatement st1 = connect
					.prepareStatement("select count(*) from pedidos");
			ResultSet r1 = st1.executeQuery();

			double numPeticionesVerificadas = r1.getInt(1);

			PreparedStatement st2 = connect
					.prepareStatement("select count(*) from pedidosincorrecta");
			ResultSet r2 = st2.executeQuery();

			double numPeticionesIncorrectas = r2.getInt(1);

			double total = numPeticionesIncorrectas + numPeticionesVerificadas;

			Integer porcentajeActual = (int) ((numPeticionesVerificadas / total) * 100);

			String tendenciaActual = "0";

			if (ultimoLog.getPorcentaje() == porcentajeActual
					&& antePenultimoLog.getPorcentaje() == porcentajeActual) {
				tendenciaActual = "0";
			} else if (ultimoLog.getPorcentaje() <= porcentajeActual
					&& antePenultimoLog.getPorcentaje() <= porcentajeActual) {
				tendenciaActual = "+";
			} else if (ultimoLog.getPorcentaje() > porcentajeActual
					|| antePenultimoLog.getPorcentaje() > porcentajeActual) {
				tendenciaActual = "-";

			}

			
			int mesActual = fecha.getMonth() + 1;
			
			int anioActual = fecha.getYear() + 1900;

			Logs registro = new Logs(cont + 1, mesActual, anioActual,
					porcentajeActual, tendenciaActual);
			this.guardarLog(registro);

			listaLogs.add(registro);
		}

		return listaLogs;

	}

	public static void main(String args[]) throws Exception {
		Connector con = new Connector();
		con.connect();

		System.out.println(con.comprobarFechaLogs());
		con.generarLog();

	}
}
