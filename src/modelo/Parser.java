package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

	private String path;
	private List<String> estructuraControl;
	private List<String> operadores;
	private List<String> comentarios;

	public Parser(String path) {
		this.path = path;
		setLists();
	}

	private void setLists() {
		
		estructuraControl = new ArrayList<>();
		estructuraControl.add("if");
		estructuraControl.add("else");
		estructuraControl.add("for");
		estructuraControl.add("while");
		estructuraControl.add("switch");
		estructuraControl.add("catch");
		
		comentarios= new ArrayList<>();
		comentarios.add("//");
		comentarios.add("/*");
		comentarios.add("*/");
		
		operadores = new ArrayList<>();
		operadores.add("+");
		operadores.add("-");
		operadores.add("&&");
		operadores.add("||");
		operadores.add("==");
		operadores.add("!=");
		operadores.add("=");
		operadores.add("*");
		operadores.add("/");
		operadores.add("++");
		operadores.add("--");
		operadores.add("+=");
		operadores.add("-=");
//		operadores.add("<");
//		operadores.add(">");
		operadores.add("<=");
		operadores.add(">=");
		operadores.add("!");
		operadores.add("^");
		operadores.add("?");
		operadores.add(">=");
		operadores.add(">=");
		operadores.add("@");

	}

	public List<String> getMetodos() {
		try {
			Scanner sc = new Scanner(new File(path));
			return getCabeceraMetodo(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getCabeceraMetodo(Scanner sc) {
		List<String> metodos = new ArrayList<>();
		String linea;
		while (sc.hasNextLine()) {
			linea = sc.nextLine();
			if (!linea.contains(";") && !encuentraCoincidencia(comentarios, linea)
					&& !encuentraCoincidencia(operadores, linea)
					&& !encuentraCoincidencia(metodos, linea)
					&& !encuentraCoincidencia(estructuraControl, linea)
					&& linea.contains("(") && linea.contains(")")
					&& linea.contains("{")) {
				metodos.add(linea.replace("{", "").trim());
			}
		}
		return metodos;
	}

	private boolean encuentraCoincidencia(List<String> lista, String linea) {
		for (String palabra : lista) {
			if (linea.contains(palabra)) {
				return true;
			}
		}
		return false;
	}

	public String codigoMetodo(String cabeceraMetodo) {
		String codigoMetodo = "";
		try {
			Scanner sc = new Scanner(new File(path));
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				if (linea.contains(cabeceraMetodo)) {
					codigoMetodo += linea + "\n";
					if(ocurrenciasPorLinea(linea,"{") == ocurrenciasPorLinea(linea,"}")) {
						return codigoMetodo;
					} else {
						codigoMetodo += getCodigoMetodoDesdeLinea(sc, ocurrenciasPorLinea(linea,"{"),ocurrenciasPorLinea(linea,"}"));
					}
				}
			}
			sc.close();
		} catch (Exception e) {
			return null;
		}
		return codigoMetodo;
	}

	private String getCodigoMetodoDesdeLinea(Scanner sc, int llaveApertura, int llaveCierre) {
		String codigoMetodo = "";
		while (sc.hasNextLine() && llaveApertura != llaveCierre) {
			String linea = sc.nextLine();
			llaveApertura += ocurrenciasPorLinea(linea,"{");
			llaveCierre += ocurrenciasPorLinea(linea,"}");
			codigoMetodo += linea + "\n";
		}
		return codigoMetodo;
	}
	
	public static int ocurrenciasPorLinea(String cadena, String palabra) {
		int contador=0,posicion=cadena.indexOf(palabra),posicionFinal = cadena.length();
		while(posicion!=-1 ) {
			contador++;
			if(posicion+palabra.length()>=posicionFinal)
			{
				return contador;
			}
			cadena=cadena.substring(posicion+palabra.length(), posicionFinal);
			posicion=cadena.indexOf(palabra);
			posicionFinal = cadena.length();
		}
		return contador;
	}
	
	public String getCodigo() {
		String cadena ="";
		try {
			Scanner sc = new Scanner(new File(path));
			while(sc.hasNextLine()) {
				cadena += sc.nextLine() + "\n";
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return cadena;
	}
}
