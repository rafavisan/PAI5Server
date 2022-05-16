package sqlite;

public class Logs {
	private Integer logID;
	private Integer mes;
	private Integer year;
	private Integer porcentaje;
	private String tendencia;

	public Logs(int id,int mes, int year, int porcentaje, String tendencia) {
		super();
		this.logID = id;
		this.mes = mes;
		this.year = year;
		this.porcentaje = porcentaje;
		this.tendencia = tendencia;
	}

	public Integer getLogID() {
		return logID;
	}

	public void setLogID(Integer logID) {
		this.logID = logID;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Integer porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getTendencia() {
		return tendencia;
	}

	public void setTendencia(String tendencia) {
		this.tendencia = tendencia;
	}

	
	
	@Override
	public String toString() {
		return  logID + " - " + mes + " / " + year
				+ " - " + porcentaje + " - " + tendencia;
	}

	
	
	
	
	
}
