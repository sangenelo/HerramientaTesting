package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class FanInOut {
	/**
	 * Cuenta cuantos métodos llama el método seleccionado
	 * */
	public static int getFanIn(String codigo,  Collection<String> archivos) {
		int fanIn = 0;	
		List<String> metodos = getMetodosDeArchivos(archivos);
		
		Scanner sc = new Scanner(codigo);
		/**
		 * la primer linea solo contendra el nombre mismo del metodo
		 * */
		sc.nextLine();
		while(sc.hasNextLine()) {
			String linea = sc.nextLine();
			for(String metodo: metodos) {
				if(linea.contains(metodo)) {
					fanIn++;
				}
			}
		}
		sc.close();
		return fanIn;
	}

	private static List<String> getMetodosDeArchivos(Collection<String> archivos) {
		List<String> metodos = new ArrayList<>();
		for(String archivo: archivos) {
			Parser parser = new Parser(archivo);
			for(String metodo: parser.getMetodos()) {
				if(!metodos.contains(getNombreMetodo(metodo))) {
					metodos.add(getNombreMetodo(metodo));
				}
			}
		}
		return metodos;
	}

	private static String getNombreMetodo(String lineaMetodo) {
		String lineaMetodoSinParentesis = lineaMetodo.substring(0, lineaMetodo.indexOf("(")).trim();
		return lineaMetodoSinParentesis.substring(lineaMetodoSinParentesis.lastIndexOf(" ")+1, lineaMetodoSinParentesis.length());
	}
	
	public static int getFanOut(String metodoSeleccionado, String archivoSeleccionado, Collection<String> archivos) {
		int contadorFanOut = 0;
		metodoSeleccionado = getNombreMetodo(metodoSeleccionado);
		List<String> lineasTotales = getLineaDeArchivosSinMetodoSeleccionado(metodoSeleccionado,archivoSeleccionado,archivos);
		for(String linea: lineasTotales) {
			if(linea.contains(metodoSeleccionado)) {
				contadorFanOut++;
			}
		}
		return contadorFanOut;
	}

	private static List<String> getLineaDeArchivosSinMetodoSeleccionado(String metodoSeleccionado, String archivoSeleccionado,Collection<String> archivos) {
		List<String> lineasTotales = new ArrayList<>();
		/**
		 * Primero obtenemos las lineas de las clases que no se seleccionaron
		 */
		for(String archivo: archivos) {
			if(!archivo.equalsIgnoreCase(archivoSeleccionado)) {
				Scanner sc;
				try {
					sc = new Scanner(new File(archivo));
					while(sc.hasNextLine()) {
						lineasTotales.add(sc.nextLine());
					}
					sc.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}
		
		/**
		 * Luego obtenemos las lineas de codigo de la clases seleccionada sin tener en cuenta el codigo del metodo seleccionado
		 */
		
		Parser parser = new Parser(archivoSeleccionado);
		for(String metodo: parser.getMetodos()) {
			if(!metodo.equals(metodoSeleccionado)){
				String[] cod = parser.codigoMetodo(metodo).split("\n");
				for(int i=0;i<cod.length;i++) {
					lineasTotales.add(cod[i]);
				}
			}
		}	
		
		return lineasTotales;
	}
}
