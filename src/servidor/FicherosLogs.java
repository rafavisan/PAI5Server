package servidor;

import java.io.*;

public class FicherosLogs {
	public static void main(String[] args) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("c:/prueba.txt");
			pw = new PrintWriter(fichero);

			for (int i = 0; i < 10; i++)
				pw.println("Linea " + i);

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
}
