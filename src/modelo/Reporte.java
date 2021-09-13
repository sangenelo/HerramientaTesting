package modelo;

import java.text.DecimalFormat;
import java.util.Collection;

public class Reporte {
	private String codigo;
	private int lineasTotales; // Líneas totales de código
	private int lineasCodigo; // Líneas con sólo código
	private int lineasBlanco; // Líneas en blanco
	private int lineasComentadas; //Líneas comentadas
	private int complejidadCiclomatica;
	private int fanIn;
	private int fanOut;
	private float longitudHalstead;
	private float volumenHalstead;
	
	public Reporte(String codigo,String archivoSeleccionado,Collection<String> listaArchivos) {
		this.codigo = codigo;
		actualizarDatosReporte(listaArchivos,archivoSeleccionado);
	}

	private void actualizarDatosReporte( Collection<String> listaArchivos, String archivoSeleccionado){
		LineaCodigo lineaCodigo = new LineaCodigo(codigo);
		lineasTotales = lineaCodigo.getLineas();
		lineasCodigo = lineaCodigo.getLineasCodigo();
		lineasBlanco = lineaCodigo.getLineasBlanco();
		lineasComentadas = lineaCodigo.getLineasComentadas();
		complejidadCiclomatica = ComplejidadCiclomatica.obtener(codigo);
		fanIn = FanInOut.getFanIn(codigo, listaArchivos); 
		fanOut = FanInOut.getFanOut(codigo,archivoSeleccionado, listaArchivos);
		
		Halstead halstead = new Halstead(codigo);
		longitudHalstead = halstead.getLongitud();
		volumenHalstead = halstead.getVolumen();
	}
	
	public int getLineasTotales() {
		return lineasTotales;
	}

	public int getLineasCodigo() {
		return lineasCodigo;
	}

	public int getLineasBlanco() {
		return lineasBlanco;
	}
	public int getLineasComentadas() {
		return lineasComentadas;
	}

	public String getPorcentajeLineasComentadas() {
		DecimalFormat df = new DecimalFormat("0.00");
		return String.valueOf(df.format(((float) lineasComentadas / lineasCodigo) * 100) + "%");
	}

	public int getComplejidadCiclomatica() {
		return complejidadCiclomatica;
	}

	public int getFanIn() {
		return fanIn;
	}
	public int getFanOut() {
		return fanOut;
	}
	public float getLongHalstead() {
		return longitudHalstead;
	}
	public float getVolHalstead() {
		return volumenHalstead;
	}


}
