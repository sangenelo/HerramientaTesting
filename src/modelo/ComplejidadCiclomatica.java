package modelo;

public class ComplejidadCiclomatica {

	public static int obtener(String codigo) {
		int complejidad = 1, andUOr;
		String[] lineas = codigo.split("\n");
		for (int i = 0; i < lineas.length; i++) {
			andUOr = (Parser.ocurrenciasPorLinea(lineas[i], "&&") + Parser.ocurrenciasPorLinea(lineas[i], "||"))+1;
			if(andUOr==1) {
				andUOr--;
			}
			if (andUOr==0 && (lineas[i].contains("if") || lineas[i].contains("while") || lineas[i].contains("for(") || 
					lineas[i].contains("for (")
					|| lineas[i].contains("case") || lineas[i].contains("catch"))) {
				andUOr=1;
			}
			complejidad+=andUOr;
		}

		return complejidad;
	}
}