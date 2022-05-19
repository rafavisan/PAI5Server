package sqlite;

public class Peticion {
	private Integer peticionID;
	private String mensaje;
	private String fecha;
	private String clienteID;
	private String firma;

	public Peticion(String mensaje, String fecha, String clienteID, String firma) {
		super();
		this.peticionID=0;
		this.mensaje = mensaje;
		this.fecha = fecha;
		this.clienteID = clienteID;
		this.firma = firma;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getClienteID() {
		return clienteID;
	}

	public void setClienteID(String clienteID) {
		this.clienteID = clienteID;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public void save(boolean tipo) {
		Connector con = new Connector();
		con.connect();
		con.guardarPeticion(this, tipo);
		con.close();

	}

}
