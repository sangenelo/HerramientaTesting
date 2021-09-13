package interfaz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import modelo.Reporte;
import modelo.Parser;

import static interfaz.Constants.BTN_BUSCAR;
import static interfaz.Constants.TITLE;

public class HerramientaTestingInterfaz extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField txfPath;
	private JButton btnBuscar;
	private JTextArea textAreaMetodo; 
	private JList<String> listaClases;
	private JList<String> listaMetodos;
	private Map<String,String> clasesMap;
	private Parser parser;
	private JLabel lbLineasTotales;
	private JLabel lbLineasBlanco;
	private JLabel lbLineasCodigo;
	private JLabel lbComentarios;
	private JLabel lbPorcentaje;
	private JLabel lbComplejidad;
	private JLabel lbFanIn;
	private JLabel lbFanOut;
	private JLabel lbLongitud;
	private JLabel lbVolumen;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HerramientaTestingInterfaz frame = new HerramientaTestingInterfaz();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Se crea el frame.
	 */
	public HerramientaTestingInterfaz() {
		setTitle(TITLE);
		setResizable(Boolean.FALSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		getContentPane().setLayout(null);

		JLabel lblExaminar = new JLabel("1. Elija un archivo o directorio:");
		lblExaminar.setBounds(12, 15, 300, 12);
		getContentPane().add(lblExaminar);

		txfPath = new JTextField();
		txfPath.setBounds(12, 32, 700, 20);
		getContentPane().add(txfPath);
		txfPath.setColumns(10);
		
		btnBuscar = new JButton(BTN_BUSCAR);
		btnBuscar.setBackground(new Color(1, 253, 169));
		btnBuscar.setBounds(730, 32, 150, 20);
		getContentPane().add(btnBuscar);

		// classes
		JLabel lblClases = new JLabel("2. Elija la clase");
		lblClases.setBounds(12, 67, 200, 12);
		getContentPane().add(lblClases);

		JScrollPane scrollPaneClases = new JScrollPane();
		scrollPaneClases.setBounds(12, 94, 420, 100);

		listaClases = new JList<>();
		listaClases.setBounds(0, 0, 478, 100);
		listaClases.addListSelectionListener(e->cargarMetodos());
		listaClases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneClases.setViewportView(listaClases);
		getContentPane().add(scrollPaneClases);

		// methods
		JLabel lblMtodos = new JLabel("3. Elija el método");
		lblMtodos.setBounds(466, 67, 200, 12);
		getContentPane().add(lblMtodos);

		JScrollPane scrollPaneMetodos = new JScrollPane();
		scrollPaneMetodos.setBounds(466, 94, 420, 100);

		listaMetodos = new JList<>();
		listaMetodos.setBounds(0, 0, 478, 100);
		listaMetodos.addListSelectionListener(e->cargarCodigoMetodo());
		listaMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneMetodos.setViewportView(listaMetodos);
		getContentPane().add(scrollPaneMetodos);

		// code
		JLabel lblCodigo = new JLabel("Código fuente del método");
		lblCodigo.setBounds(12, 380, 250, 15);
		getContentPane().add(lblCodigo);

		JScrollPane scrollCodigoMetodo = new JScrollPane();
		scrollCodigoMetodo.setBounds(12, 400, 874, 260);
		textAreaMetodo = new JTextArea();
		textAreaMetodo.setBounds(12, 0, 485, 280);
		scrollCodigoMetodo.setViewportView(textAreaMetodo);

		getContentPane().add(scrollCodigoMetodo);

		// Analysis
		int MARGIN_LEFT = 10;
		int LBL_WIDTH = 277;
		int LBL_HEIGHT = 14;
		int VAL_WIDTH = 150;
		int VAL_HEIGHT = 14;
		int COL_1_LBL_X = MARGIN_LEFT;
		int COL_1_VAL_X = COL_1_LBL_X + LBL_WIDTH;
		int COL_2_LBL_X = COL_1_VAL_X + VAL_WIDTH;
		int COL_2_VAL_X = COL_2_LBL_X + LBL_WIDTH;
		int MARGIN_TOP = 30;
		int PADDING = 10;

		int FONT_SIZE = 12;
		Font metricFont = new Font("Tahoma", Font.BOLD, FONT_SIZE);
		Color METRIC_COLOR = new Color(41, 116, 5);

		JPanel panelAnalisis = new JPanel();
		panelAnalisis.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192)),
				"Análisis del método", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		((TitledBorder) panelAnalisis.getBorder())
				.setTitleFont(((TitledBorder) panelAnalisis.getBorder()).getTitleFont().deriveFont(Font.BOLD));
		panelAnalisis.setBounds(
				12,
				200,
				2 * (MARGIN_LEFT + LBL_WIDTH + VAL_WIDTH),
				3 * MARGIN_TOP + 5 * LBL_HEIGHT);
		getContentPane().add(panelAnalisis);
		panelAnalisis.setLayout(null);

		// Lineas Totales
		JLabel lblLneasTotales = new JLabel("Lineas Totales:");
		lblLneasTotales.setBounds(COL_1_LBL_X, MARGIN_TOP, LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblLneasTotales);

		lbLineasTotales = new JLabel("-");
		lbLineasTotales.setForeground(METRIC_COLOR);
		lbLineasTotales.setFont(metricFont);
		lbLineasTotales.setBounds(COL_1_VAL_X, MARGIN_TOP, VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbLineasTotales);

		// CC
		JLabel lblComplejidadCiclomatica = new JLabel("Complejidad Ciclomática:");
		lblComplejidadCiclomatica.setBounds(COL_2_LBL_X, MARGIN_TOP, LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblComplejidadCiclomatica);

		lbComplejidad = new JLabel("-");
		lbComplejidad.setForeground(METRIC_COLOR);
		lbComplejidad.setFont(metricFont);
		lbComplejidad.setBounds(COL_2_VAL_X, MARGIN_TOP, VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbComplejidad);

		// CLOC P
		JLabel lblPorcentajeDeLineas = new JLabel("CLOC (%):");
		lblPorcentajeDeLineas.setBounds(COL_2_LBL_X, MARGIN_TOP + LBL_HEIGHT + PADDING, LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblPorcentajeDeLineas);

		lbPorcentaje = new JLabel("-");
		lbPorcentaje.setForeground(METRIC_COLOR);
		lbPorcentaje.setFont(metricFont);
		lbPorcentaje.setBounds(COL_2_VAL_X, MARGIN_TOP + LBL_HEIGHT + PADDING, VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbPorcentaje);

		// CLOC
		JLabel lblLneasDeCodigo_1 = new JLabel("CLOC:");
		lblLneasDeCodigo_1.setBounds(COL_1_LBL_X, MARGIN_TOP + LBL_HEIGHT + PADDING, LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblLneasDeCodigo_1);

		lbComentarios = new JLabel("-");
		lbComentarios.setForeground(METRIC_COLOR);
		lbComentarios.setFont(metricFont);
		lbComentarios.setBounds(COL_1_VAL_X, MARGIN_TOP + LBL_HEIGHT + PADDING, VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbComentarios);

		// LOC
		JLabel lblLneasDeCodigo = new JLabel("LOC:");
		lblLneasDeCodigo.setBounds(COL_1_LBL_X, MARGIN_TOP + 2 * (LBL_HEIGHT + PADDING), LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblLneasDeCodigo);

		lbLineasCodigo = new JLabel("-");
		lbLineasCodigo.setForeground(METRIC_COLOR);
		lbLineasCodigo.setFont(metricFont);
		lbLineasCodigo.setBounds(COL_1_VAL_X, MARGIN_TOP + 2 * (LBL_HEIGHT + PADDING), VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbLineasCodigo);

		// Blank lines
		JLabel lblLineasBlanco = new JLabel("Lineas en Blanco:");
		lblLineasBlanco.setBounds(COL_2_LBL_X, MARGIN_TOP + 2 * (LBL_HEIGHT + PADDING), LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblLineasBlanco);

		lbLineasBlanco = new JLabel("-");
		lbLineasBlanco.setForeground(METRIC_COLOR);
		lbLineasBlanco.setFont(metricFont);
		lbLineasBlanco.setBounds(COL_2_VAL_X, MARGIN_TOP + 2 * (LBL_HEIGHT + PADDING), VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbLineasBlanco);


		// Halstead Volumen
		JLabel lblHalsteadVolumen = new JLabel("Volumen de Halstead:");
		lblHalsteadVolumen.setBounds(COL_1_LBL_X, MARGIN_TOP + 3 * (LBL_HEIGHT + PADDING), LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblHalsteadVolumen);

		lbVolumen = new JLabel("-");
		lbVolumen.setForeground(METRIC_COLOR);
		lbVolumen.setFont(metricFont);
		lbVolumen.setBounds(COL_1_VAL_X, MARGIN_TOP + 3 * (LBL_HEIGHT + PADDING), VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbVolumen);

		// Halstead Longitud
		JLabel lblHalsteadLongitud = new JLabel("Longitud de Halstead:");
		lblHalsteadLongitud.setBounds(COL_2_LBL_X, MARGIN_TOP + 3 * (LBL_HEIGHT + PADDING), LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblHalsteadLongitud);

		lbLongitud = new JLabel("-");
		lbLongitud.setForeground(METRIC_COLOR);
		lbLongitud.setFont(metricFont);
		lbLongitud.setBounds(COL_2_VAL_X, MARGIN_TOP + 3 * (LBL_HEIGHT + PADDING), VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbLongitud);

		// Fan In
		JLabel lblFanIn = new JLabel("Fan In:");
		lblFanIn.setBounds(COL_1_LBL_X, MARGIN_TOP + 4 * (LBL_HEIGHT + PADDING), LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblFanIn);

		lbFanIn = new JLabel("-");
		lbFanIn.setForeground(METRIC_COLOR);
		lbFanIn.setFont(metricFont);
		lbFanIn.setBounds(COL_1_VAL_X, MARGIN_TOP + 4 * (LBL_HEIGHT + PADDING), VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbFanIn);

		// Fan out
		JLabel lblFanOut = new JLabel("Fan Out:");
		lblFanOut.setBounds(COL_2_LBL_X, MARGIN_TOP + 4 * (LBL_HEIGHT + PADDING), LBL_WIDTH, LBL_HEIGHT);
		panelAnalisis.add(lblFanOut);

		lbFanOut = new JLabel("-");
		lbFanOut.setForeground(METRIC_COLOR);
		lbFanOut.setFont(metricFont);
		lbFanOut.setBounds(COL_2_VAL_X, MARGIN_TOP + 4 * (LBL_HEIGHT + PADDING), VAL_WIDTH, VAL_HEIGHT);
		panelAnalisis.add(lbFanOut);

		btnBuscar.addActionListener(e->openChooserFile());
	}

	private void cargarReporte(String codigo) {
		Reporte reporte = new Reporte(codigo, clasesMap.get(listaClases.getSelectedValue()), clasesMap.values());
		lbLineasTotales.setText(String.valueOf(reporte.getLineasTotales()));
		lbLineasCodigo.setText(String.valueOf(reporte.getLineasCodigo()));
		lbLineasBlanco.setText(String.valueOf(reporte.getLineasBlanco()));
		lbComentarios.setText(String.valueOf(reporte.getLineasComentadas()));
		lbPorcentaje.setText(reporte.getPorcentajeLineasComentadas());
		lbComplejidad.setText(String.valueOf(reporte.getComplejidadCiclomatica()));
		lbFanIn.setText(String.valueOf(reporte.getFanIn()));
		lbFanOut.setText(String.valueOf(reporte.getFanOut()));
		lbLongitud.setText(String.valueOf(String.format("%.1f", reporte.getLongHalstead())));
		lbVolumen.setText(String.valueOf(String.format("%.1f", reporte.getVolHalstead())));
	}

	private void cargarCodigoMetodo() {
		String codigo = parser.codigoMetodo(listaMetodos.getSelectedValue());
		textAreaMetodo.setText(codigo);
		cargarReporte(codigo);
	}

	private void cargarMetodos() {
		parser = new Parser(clasesMap.get(listaClases.getSelectedValue()));
		actualizarLista(listaMetodos,listToArray(parser.getMetodos()));
	}

	private void openChooserFile() {

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			txfPath.setText(chooser.getSelectedFile().getPath());
			listaMetodos.removeAll();
			// Buscamos todos los archivos .java en la carpeta
			// seleccionada
			File[] archivos = chooser.getSelectedFile().listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".java");
				}
			});

			// Obtenemos la ruta de cada archivo y actualizamos la lista
			// Si la carpeta no contiene ningun archivo .java, mostramos un mensaje de error.
			clasesMap = new HashMap<>();
			if (archivos.length > 0) {
				String path;
				for (int i = 0; i < archivos.length; i++) {
					path = archivos[i].getPath();
					clasesMap.put(path.substring(path.lastIndexOf("/")+1,path.lastIndexOf(".java")), path);
				}
				actualizarLista(listaClases,mapToArray(clasesMap));
			} else {
				JOptionPane.showMessageDialog(this,
						"La carpeta seleccionada no contiene archivos .java", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private static void actualizarLista(JList<String> lista, String[] elementos) {
		lista.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] values = elementos;
				
			public int getSize() {
				return values.length;
			}

			public String getElementAt(int index) {
				return values[index];
			}
		});
	}
	
	private String[] mapToArray(Map<String, String> map) {
		String[] array =  new String[map.size()];
		int i=0;
		for (String key:map.keySet()) {
			array[i++] = key;
			
		}
		return array;
	}
	
	private String[] listToArray(List<String> list) {
		String[] array =  new String[list.size()];
		int i=0;
		for (String key:list) {
			array[i++] = key;
			
		}
		return array;
	}
}
