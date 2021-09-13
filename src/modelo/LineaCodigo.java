package modelo;

import java.util.Scanner;

public class LineaCodigo {

	private String codigo;
	private int lineasComentadas;
	private int lineasCodigo;
	private int lineasBlanco;
	private int lineas;

	public LineaCodigo(String codigo) {
		this.codigo = codigo;
		lineasComentadas = 0;
		lineasBlanco = 0;
		lineasCodigo = 0;
		lineas = 0;
		obtenerDetalleLineas();
	}

	@SuppressWarnings("resource")
	private void obtenerDetalleLineas() {

		Scanner sc = new Scanner(codigo);
		String linea;
		while (sc.hasNextLine()) {
			lineas++;
			linea = sc.nextLine();
			if (linea.contains("//")) {
				lineasComentadas++;
			} else if (linea.replaceAll("\\s", "").length() == 0) {
				lineasBlanco++;
			} else if (linea.contains("/*")) {
				if (linea.contains("*/")) {
					lineasComentadas++;
				} else {
					lineasComentadas++;
					while (sc.hasNextLine()) {//lineasComentadas++;
						lineas++;
						lineasComentadas++;
						linea = sc.nextLine();
						if(linea.contains("*/")) {
							break;
						}
					}
				}
			} else {
				lineasCodigo++;
			}
		}
	}

	public int getLineasComentadas() {
		return lineasComentadas;
	}

	public int getLineasCodigo() {
		return lineasCodigo;
	}

	public int getLineasBlanco() {
		return lineasBlanco;
	}
	
	public int getLineas() {
		return lineas;
	}
}
