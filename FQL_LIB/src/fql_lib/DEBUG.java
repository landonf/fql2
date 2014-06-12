package fql_lib;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import fql_lib.examples.Example;
import fql_lib.examples.Examples;
import fql_lib.gui.GUI;

/**
 * 
 * @author ryan
 * 
 *         Contains global constants for debugging.
 */
@SuppressWarnings("serial")
public class DEBUG implements Serializable {

	//TODO category of elements for intances
	
	public static enum SQLKIND {
		H2, NATIVE, JDBC
	};

//	static String[] kan_options = new String[] {
//		"Hybrid (dangerous, fast)", "Parallel (very dangerous, very fast)", "Standard (probably correct, slow)"
//	};
	
//	public String kan_option = kan_options[0];
	
	public String instFlow_graph = "ISOMLayout";
	public String schFlow_graph = "ISOMLayout";
	public String schema_graph = "ISOMLayout";
	public String mapping_graph = "FRLayout";
	public String inst_graph = "ISOMLayout";
	//public String trans_graph = "ISOMLayout";
	public String query_graph = "ISOMLayout";
	public String look_and_feel = UIManager.getSystemLookAndFeelClassName();


	public SQLKIND sqlKind = SQLKIND.NATIVE;

	public String FILE_PATH = "";

	public boolean ALLOW_NULLS = true;

	public static DEBUG debug = new DEBUG();
	
	public int MAX_NODES = 32;

	public void clear() {
		debug = new DEBUG();
	}

	public void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream("fqlpp_options.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (Exception i) {
			i.printStackTrace();
			JOptionPane.showMessageDialog(null, i.getLocalizedMessage());
		}
	}

	public static DEBUG load(boolean silent) {
		try {
			FileInputStream fileIn = new FileInputStream("fqlpp_options.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			DEBUG e = (DEBUG) in.readObject();
			if (e != null) {
				if (silent) {
					debug = e;
				} else {
					e.showOptions();
				}
			}
			in.close();
			fileIn.close();
		} catch (Exception i) {
			if (!silent) {
				i.printStackTrace();
				JOptionPane.showMessageDialog(null, i.getLocalizedMessage());
			}
		}
		return null;
	}

	public String prelude = "CREATE DATABASE FQL; USE FQL; SET @guid := 0;";

	public String afterlude = "DROP DATABASE FQL; ";

	public String jdbcUrl = "jdbc:mysql://localhost:3306/?user=root";
	// public boolean useJDBC = false;
	public String jdbcClass = "com.mysql.jdbc.Driver";

	public boolean MultiView = true;

	public int varlen = 128;

	public boolean VALIDATE = true;

	public int MAX_PATH_LENGTH = 8;

	public int MAX_DENOTE_ITERATIONS = 65536;

	public boolean ALL_GR_PATHS = false;

	public boolean VALIDATE_WITH_EDS = false;

	public boolean continue_on_error = false;

	// public static int MAX_JOIN_SIZE = 1024;

	public boolean ALLOW_INFINITES = false;

	// public static boolean DO_NOT_GUIDIFY = false;

	public boolean set_textual = true;
	public boolean fn_textual = true;
	public boolean set_tabular = true;
	public boolean fn_tabular = true;
	public boolean cat_tabular = true;
	public boolean cat_textual = true;
	public boolean ftr_tabular = true;
	public boolean ftr_textual = true;
	public boolean cat_graph = true;
	public boolean ftr_graph = true;
	public boolean set_graph = true;
	public boolean fn_graph = true;
	public boolean trans_textual = true;
	public boolean trans_tabular = true;
	public boolean trans_graph = true;
	
	public boolean schema_rdf = true;
	public boolean schema_graphical = true;
	public boolean schema_tabular = true;
	public boolean schema_textual = true;
	public boolean schema_denotation = true;
	public boolean schema_ed = true;

	public boolean mapping_graphical = true;
	public boolean mapping_tabular = true;
	public boolean mapping_textual = true;
	public boolean mapping_ed = true;

	public boolean query_graphical = true;
	public boolean query_textual = true;

	public boolean inst_rdf = true;
	public boolean inst_graphical = true;
	public boolean inst_tabular = true;
	public boolean inst_textual = true;
	public boolean inst_joined = true;
	public boolean inst_gr = true;
	public boolean inst_obs = true;
	public boolean limit_examples = true;
	
	public boolean cat_schema = true;
	public boolean ftr_elements = true;
	public boolean ftr_instance = true;
	public boolean ftr_joined = true;
	public boolean ftr_mapping = true;
	public boolean trans_elements =  true;
	
	public boolean allow_surjective = true;

	public boolean transform_graphical = true;
	public boolean transform_tabular = true;
	public boolean transform_textual = true;

	static String label1text = "If un-checked, the schemas in the viewer for queries will not contain any arrows.";
	static String label2text = "<html>The none and some options only shows declarations from the input program.<br>The all option shows all declarations including those generated by query composition.<br>The some option suppresses identity mappings.</html>";
	static String label3text = "<html>Instances in FQL must have globally unique keys.<br>To ensure this, FQL (and the generated SQL) will often compute new isomorphic instances with freshly chosen keys.<br>For debugging purposes it is sometimes useful to suppress this behavior.";
	static String label4text = "<html>By not computing source and target categories, and by not performing the check that a mapping takes path equivalences to path equivalences,<br>it is possible to compute Delta and SIGMA for infinite schemas.  See the employees example.</html>";
	static String label5text = "This is an internal consistency check that checks if intermediate categories generated by FQL do in fact obey the category axioms.";
	static String label6text = "Bounds the maximum length that the paths in a schema can be.";
	static String label7text = "Bounds the maximum number of iterations to compute the category denoted by a schema.";
	static String label8text = "Sets the size of Strings in the SQL output (used for ID columns and string columns).";
	static String labelMtext = "Allows multiple viewers for the same editor.";

	static int selected_tab = 0;

	public void showOptions() {

		JTabbedPane jtb = new JTabbedPane();

		JPanel general1 = new JPanel(new GridLayout(7, 1));
		JPanel general2 = new JPanel(new GridLayout(7, 1));

		JPanel sql1 = new JPanel(new GridLayout(10, 1));
		JPanel sql2 = new JPanel(new GridLayout(10, 1));

		JPanel viewer1 = new JPanel(new GridLayout(7, 1));
		JPanel viewer2 = new JPanel(new GridLayout(7, 1));

		JSplitPane generalsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane sqlsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane viewersplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		generalsplit.add(general1);
		generalsplit.add(general2);
		sqlsplit.add(sql1);
		sqlsplit.add(sql2);
		viewersplit.add(viewer1);
		viewersplit.add(viewer2);

		jtb.add("General", generalsplit);
		jtb.add("Viewer", viewersplit);
	//	jtb.add("SQL", sqlsplit);

		jtb.setSelectedIndex(selected_tab);

		/*
		JCheckBox surjB = new JCheckBox("", allow_surjective);
		JLabel surjL = new JLabel("Allow surjective pi (dangerous):");
		general1.add(surjL);
		general2.add(surjB);
		surjL.setToolTipText("Allows the attribute mapping of pi migrations to be surjections.");
*/
		
		JCheckBox limex = new JCheckBox("", limit_examples);
		JLabel limexL = new JLabel("Show only some examples:");
		general1.add(limexL);
		general2.add(limex);
		limexL.setToolTipText("The examples combo box will only display some examples.");
/*
		JCheckBox coeB = new JCheckBox("", continue_on_error);
		JLabel coeL = new JLabel("Continue on errors (dangerous):");
		general1.add(coeL);
		general2.add(coeB);
		coeL.setToolTipText("FQL will attempt to continue on errors, and report these after opening the viewer.");
*/
		/*
		JCheckBox ed = new JCheckBox("", VALIDATE_WITH_EDS);
		ed.setToolTipText("Validates Sigma/Delta migrations (on objects) using embedded dependencies");
		JLabel edL = new JLabel("Validate sigmas and deltas using EDs:");
		general1.add(edL);
		general2.add(ed);
		*/
/*
		JCheckBox nullbox = new JCheckBox("", ALLOW_NULLS);
		nullbox.setToolTipText("Allow full sigma to create null attribute values");
		JLabel nullL = new JLabel("Allow SIGMA to create nulls (dangerous):");
		general1.add(nullL);
		general2.add(nullbox);
		*/

		JCheckBox gr = new JCheckBox("", ALL_GR_PATHS);
		gr.setToolTipText("Show all paths in category of elements");
		//JLabel grL = new JLabel("Show all paths in elements view:");
		//viewer1.add(grL);
		//viewer2.add(gr);
		
		JTextField node_limit_field = new JTextField(Integer.toString(MAX_NODES));
		viewer1.add(new JLabel("Graph node size limit"));
		viewer2.add(node_limit_field);

		JCheckBox jcbM = new JCheckBox("", MultiView);
		jcbM.setToolTipText(labelMtext);
		JLabel labelM = new JLabel("Allow multiple viewers per editor:");
		viewer1.add(labelM);
		viewer2.add(jcbM);

		/*
		 * JCheckBox jcbX = new JCheckBox("", DO_NOT_GUIDIFY); JPanel p0 = new
		 * JPanel(); JLabel label3 = new JLabel("Do not GUIDify (dangerous):");
		 * label3.setToolTipText(label3text); p.add(label3); p.add(jcbX);
		 */
/*
		JCheckBox jcb0 = new JCheckBox("", ALLOW_INFINITES);
		JLabel label4 = new JLabel(
				"Allow some infinite schemas and migrations (dangerous):");
		label4.setToolTipText(label4text);
		general1.add(label4);
		general2.add(jcb0); */

		JCheckBox jcb = new JCheckBox("", VALIDATE);
		JLabel label5 = new JLabel("Validate categories/functors/etc:");
		label5.setToolTipText(label5text);
		general1.add(label5);
		general2.add(jcb); 

		ButtonGroup group = new ButtonGroup();
		JRadioButton nativeButton = new JRadioButton("Naive");
		JRadioButton h2Button = new JRadioButton("H2");
		JRadioButton jdbcButton = new JRadioButton("JDBC");
		group.add(nativeButton);
		group.add(h2Button);
		group.add(jdbcButton);
		JPanel jdbcBox = new JPanel(); // new GridLayout(1,3));
		jdbcBox.add(nativeButton);
		jdbcBox.add(h2Button);
		jdbcBox.add(jdbcButton);
		switch (sqlKind) {
		case H2:
			group.setSelected(h2Button.getModel(), true);
			break;
		case JDBC:
			group.setSelected(jdbcButton.getModel(), true);
			break;
		case NATIVE:
			group.setSelected(nativeButton.getModel(), true);
			break;
		default:
			throw new RuntimeException();
		}

		// JCheckBox jdbcBox = new JCheckBox("", useJDBC);
		JLabel jdbcLabel = new JLabel("SQL Engine:");
		jdbcLabel
				.setToolTipText("Choose between a naive SQL engine, the H2 engine, or an external JDBC engine.");
		sql1.add(jdbcLabel);
		sql2.add(jdbcBox);

		JTextField jdbcField = new JTextField(jdbcUrl);
		JLabel jdbcLabel2 = new JLabel("JDBC URL:");
		jdbcLabel2.setToolTipText("The JDBC connection to use.");
		sql1.add(jdbcLabel2);
		sql2.add(jdbcField);

		JTextField jdbcField2 = new JTextField(jdbcClass);
		JLabel jdbcLabel22 = new JLabel("JDBC Driver Class:");
		jdbcLabel22.setToolTipText("The JDBC class to use.");
		sql1.add(jdbcLabel22);
		sql2.add(jdbcField2);

		JTextField plen = new JTextField(Integer.toString(MAX_PATH_LENGTH));
		JLabel label6 = new JLabel("Maximum path length:");
		label6.setToolTipText(label6text);
		general1.add(label6);
		general2.add(plen);

		JTextField iter = new JTextField(
				Integer.toString(MAX_DENOTE_ITERATIONS));
		JLabel label7 = new JLabel(
				"Maximum iterations for left-kan computation:");
		label7.setToolTipText(label7text);
		general1.add(label7);
		general2.add(iter);
		
		JTextField font_field = new JTextField(
				Integer.toString(FONT_SIZE));
		JLabel font_label = new JLabel(
				"Editor font size:");
		font_label.setToolTipText("Sets the size of the font used in all editors.");
		general1.add(font_label);
		general2.add(font_field);

		JTextField vlen = new JTextField(Integer.toString(varlen));
		JLabel label8 = new JLabel("VARCHAR size:");
		label8.setToolTipText(label8text);
		sql1.add(label8);
		sql2.add(vlen);

		JTextField area = new JTextField(12);
		area.setText(prelude);
		JLabel areaLabel = new JLabel("Generated SQL prelude:");
		areaLabel.setToolTipText("Set the prelude for the generated SQL.");
		sql1.add(areaLabel);
		sql2.add(area);
		// area.setMaximumSize(new Dimension(200, 300));

		JTextField area2 = new JTextField(12);
		area2.setText(afterlude);
		JLabel areaLabel2 = new JLabel("Generated SQL postlude:");
		areaLabel2.setToolTipText("Set the postlude for the generated SQL.");
		sql1.add(areaLabel2);
		sql2.add(area2);
		// area2.setMaximumSize(new Dimension(200, 300));

		JTextField fileArea = new JTextField(12);
		fileArea.setText(FILE_PATH);
		JLabel fileLabel = new JLabel("Default File Chooser Path:");
		areaLabel2
				.setToolTipText("Sets the directory for the file chooser to open on.");
		general1.add(fileLabel);
		general2.add(fileArea);
		// fileArea.setMaximumSize(new Dimension(200, 300));

		JPanel schemaArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox schema_graphical_box = new JCheckBox("Graph",
				schema_graphical);
		JCheckBox schema_textual_box = new JCheckBox("Text", schema_textual);
		JCheckBox schema_tabular_box = new JCheckBox("Table", schema_tabular);
		JCheckBox schema_ed_box = new JCheckBox("ED", schema_ed);
		JCheckBox schema_denotation_box = new JCheckBox("Denotation",
				schema_denotation);
		JCheckBox schema_rdf_box = new JCheckBox("OWL", schema_rdf);

		// JPanel schemaTemp = new JPanel();
		// schemaTemp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JComboBox<String> schemaBox = new JComboBox<>(layouts);
		schemaBox.setSelectedItem(schema_graph);
		schemaBox.setToolTipText(layout_string);
		schemaArea.add(schemaBox);
		schemaArea.add(schema_graphical_box);

		// schemaArea.add(schemaTemp);

		schemaArea.add(schema_textual_box);
		schemaArea.add(schema_tabular_box);
		schemaArea.add(schema_ed_box);
		schemaArea.add(schema_denotation_box);
		schemaArea.add(schema_rdf_box);
		// schemaArea.add(schemaBox);
		JLabel schema_label = new JLabel("Schema viewer panels:");
		schema_label.setToolTipText("Sets which viewers to use for schemas.");
		//viewer1.add(schema_label);
		//viewer2.add(schemaArea);

		

		// JComboBox schemaLayout = new JComboBox(layouts);
		// viewer.add(schemaLayoutLabel);
		// viewer.add(schemaGraphPanel);

		JPanel setArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox set_textual_box = new JCheckBox("Text", set_textual);
		setArea.add(set_textual_box);
		JCheckBox set_tabular_box = new JCheckBox("Table", set_tabular);
		setArea.add(set_tabular_box);
		JCheckBox set_graph_box = new JCheckBox("Graph", set_graph);
		setArea.add(set_graph_box);

		JLabel set_label = new JLabel("Set viewer panels:");
		set_label.setToolTipText("Sets which viewers to use for sets.");
		viewer1.add(set_label);
		viewer2.add(setArea);

		JPanel fnArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox fn_textual_box = new JCheckBox("Text", fn_textual);
		fnArea.add(fn_textual_box);
		JCheckBox fn_tabular_box = new JCheckBox("Table", fn_tabular);
		fnArea.add(fn_tabular_box);
		JCheckBox fn_graph_box = new JCheckBox("Graph", fn_graph);
		fnArea.add(fn_graph_box);
		
		JLabel fn_label = new JLabel("Function viewer panels:");
		fn_label.setToolTipText("Sets which viewers to use for functions.");
		viewer1.add(fn_label);
		viewer2.add(fnArea);
		
		

		JPanel catArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox cat_textual_box = new JCheckBox("Text", cat_textual);
		catArea.add(cat_textual_box);
		JCheckBox cat_tabular_box = new JCheckBox("Table", cat_tabular);
		catArea.add(cat_tabular_box);
		JCheckBox cat_graph_box = new JCheckBox("Graph", cat_graph);
		catArea.add(cat_graph_box);
		JCheckBox cat_schema_box = new JCheckBox("Schema", cat_schema);
		catArea.add(cat_schema_box);

		JLabel cat_label = new JLabel("Category viewer panels:");
		cat_label.setToolTipText("Sets which viewers to use for categories.");
		viewer1.add(cat_label);
		viewer2.add(catArea);

		JPanel ftrArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox ftr_textual_box = new JCheckBox("Text", ftr_textual);
		ftrArea.add(ftr_textual_box);
		JCheckBox ftr_tabular_box = new JCheckBox("Table", ftr_tabular);
		ftrArea.add(ftr_tabular_box);
		JCheckBox ftr_graph_box = new JCheckBox("Graph", ftr_graph);
		ftrArea.add(ftr_graph_box);
		JCheckBox ftr_elements_box = new JCheckBox("Elements", ftr_elements);
		ftrArea.add(ftr_elements_box);
		JCheckBox ftr_instance_box = new JCheckBox("Instance", ftr_instance);
		ftrArea.add(ftr_instance_box);
		JCheckBox ftr_joined_box = new JCheckBox("Joined", ftr_joined);
		ftrArea.add(ftr_joined_box);
		JCheckBox ftr_mapping_box = new JCheckBox("Mapping", ftr_mapping);
		ftrArea.add(ftr_mapping_box);

		JLabel ftr_label = new JLabel("Functor viewer panels:");
		ftr_label.setToolTipText("Sets which viewers to use for functors.");
		viewer1.add(ftr_label);
		viewer2.add(ftrArea);
		
		
		JPanel tArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox t_textual_box = new JCheckBox("Text", trans_textual);
		tArea.add(t_textual_box);
		JCheckBox t_tabular_box = new JCheckBox("Table", trans_tabular);
		tArea.add(t_tabular_box);
		JCheckBox t_graph_box = new JCheckBox("Graph", trans_graph);
		tArea.add(t_graph_box);
		JCheckBox t_elements_box = new JCheckBox("Elements", trans_elements);
		tArea.add(t_elements_box);
		
		JLabel t_label = new JLabel("Function viewer panels:");
		t_label.setToolTipText("Sets which viewers to use for transforms.");
		viewer1.add(t_label);
		viewer2.add(tArea);
		
		
		JPanel mappingArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox mapping_graphical_box = new JCheckBox("Graph",
				mapping_graphical);
		JCheckBox mapping_textual_box = new JCheckBox("Text", mapping_textual);
		JCheckBox mapping_tabular_box = new JCheckBox("Table", mapping_tabular);
		JCheckBox mapping_ed_box = new JCheckBox("ED", mapping_ed);
		// JPanel mappingTemp = new JPanel();
		// schemaTemp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JComboBox<String> mappingBox = new JComboBox<>(layouts);
		mappingBox.setToolTipText(layout_string);
		mappingBox.setSelectedItem(mapping_graph);

		mappingArea.add(mappingBox);
		mappingArea.add(mapping_graphical_box);

		// mappingArea.add(mappingTemp);
		mappingArea.add(mapping_textual_box);
		mappingArea.add(mapping_tabular_box);
		mappingArea.add(mapping_ed_box);
		JLabel mapping_label = new JLabel("Mapping viewer panels:");
		mapping_label.setToolTipText("Sets which viewers to use for mappings.");
	//	viewer1.add(mapping_label);
	//	viewer2.add(mappingArea);


		// JLabel mappingLayoutLabel = new
		// JLabel("Layout engine to use for mapping graph (requires re-compile)");
		// JComboBox mappingLayout = new JComboBox(layouts);
		// viewer.add(mappingLayoutLabel);
		// viewer.add(mappingLayout);

		JPanel instArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox inst_graphical_box = new JCheckBox("Graph", inst_graphical);
		JCheckBox inst_textual_box = new JCheckBox("Text", inst_textual);
		JCheckBox inst_tabular_box = new JCheckBox("Table", inst_tabular);
		JCheckBox inst_joined_box = new JCheckBox("Joined", inst_joined);
		JCheckBox inst_gr_box = new JCheckBox("Elements", inst_gr);
		JCheckBox inst_obs_box = new JCheckBox("Observables", inst_obs);
		JCheckBox inst_rdf_box = new JCheckBox("RDF", inst_rdf);
		JComboBox<String> instBox = new JComboBox<>(layouts);
		instBox.setToolTipText(layout_string);
		instBox.setSelectedItem(inst_graph);
		instArea.add(instBox);
		instArea.add(inst_graphical_box);

		instArea.add(inst_textual_box);
		instArea.add(inst_tabular_box);
		instArea.add(inst_joined_box);
		instArea.add(inst_gr_box);
		instArea.add(inst_obs_box);
		instArea.add(inst_rdf_box);
		JLabel inst_label = new JLabel("Instance viewer panels:");
		inst_label.setToolTipText("Sets which viewers to use for instances.");
	//	viewer1.add(inst_label);
	//	viewer2.add(instArea);

	//	JLabel instLayoutLabel = new JLabel(
		//		"Layout engine to use for instance graph (requires re-compile)");
		//JComboBox instLayout = new JComboBox(layouts);
		instBox.setToolTipText(layout_string);
		// viewer.add(instLayoutLabel);
		// viewer.add(instLayout);

		JPanel transformArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox transform_graphical_box = new JCheckBox("Graph",
				transform_graphical);
		JCheckBox transform_textual_box = new JCheckBox("Text",
				transform_textual);
		JCheckBox transform_tabular_box = new JCheckBox("Table",
				transform_tabular);
		JComboBox<String> transBox = new JComboBox<>(layouts);
		transBox.setToolTipText(layout_string);
		transBox.setSelectedItem(trans_graph);

		transformArea.add(transBox);
		transformArea.add(transform_graphical_box);

		transformArea.add(transform_textual_box);
		transformArea.add(transform_tabular_box);
		//JLabel transform_label = new JLabel("Transform viewer panels:");
		mapping_label
				.setToolTipText("Sets which viewers to use for transforms.");
	//	viewer1.add(transform_label);
	//	viewer2.add(transformArea);

//		JLabel transLayoutLabel = new JLabel(
	//			"Layout engine to use for transform graph (requires re-compile)");
		//JComboBox transLayout = new JComboBox(layouts);
		// viewer.add(transLayoutLabel);
		// viewer.add(transLayout);

		JPanel fullQueryArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox query_graphical_box = new JCheckBox("Graph", query_graphical);
		JCheckBox query_textual_box = new JCheckBox("Text", query_textual);
		JComboBox<String> queryBox = new JComboBox<>(layouts);
		queryBox.setToolTipText(layout_string);
		queryBox.setSelectedItem(query_graph);

		fullQueryArea.add(queryBox);
		fullQueryArea.add(query_graphical_box);

		fullQueryArea.add(query_textual_box);
		JLabel query_label = new JLabel("Full Query viewer panels:");
		query_label
				.setToolTipText("Sets which viewers to use for full queries.");
	//	viewer1.add(query_label);
	//	viewer2.add(fullQueryArea);
		
		JComboBox<String> instFlowBox = new JComboBox<>(layouts);
		instFlowBox.setToolTipText(layout_string);
		instFlowBox.setSelectedItem(instFlow_graph);

		JPanel instFlowArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		instFlowArea.add(instFlowBox);
		JLabel instFlow_label = new JLabel("Instance Flow graph layouts:");
		instFlow_label.setToolTipText("Sets which graph layout to use for instance flow.");
	//	viewer1.add(instFlow_label);
	//	viewer2.add(instFlowArea);
		
		JComboBox<String> schFlowBox = new JComboBox<>(layouts);
		schFlowBox.setToolTipText(layout_string);
		schFlowBox.setSelectedItem(schFlow_graph);

		JPanel schFlowArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		schFlowArea.add(schFlowBox);
		JLabel schFlow_label = new JLabel("Schema Flow graph layouts:");
		schFlow_label.setToolTipText("Sets which graph layout to use for schema flow.");
	//	viewer1.add(schFlow_label);
	//	viewer2.add(schFlowArea);


		// JLabel queryLayoutLabel = new
		// JLabel("Layout engine to use for full query graph (requires re-compile)");
		// JComboBox queryLayout = new JComboBox(layouts);
		// viewer.add(queryLayoutLabel);
		// viewer.add(queryLayout);

	//	viewer1.add(new JLabel());
	//	viewer2.add(new JLabel());
	//	viewer1.add(new JLabel());
	//	viewer2.add(new JLabel());
		//viewer1.add(new JLabel());
		//viewer2.add(new JLabel());

		sql1.add(new JLabel());
		sql2.add(new JLabel());
		sql1.add(new JLabel());
		sql2.add(new JLabel());
		sql1.add(new JLabel());
		sql2.add(new JLabel());
		sql1.add(new JLabel());
		sql2.add(new JLabel()); 

		general1.add(new JLabel("Look and feel:"));
		String[] items = new String[UIManager.getInstalledLookAndFeels().length];
		int i = 0;
		for (LookAndFeelInfo k : UIManager.getInstalledLookAndFeels()) {
			items[i++] = k.getClassName();
		}
		JComboBox<String> lfb = new JComboBox<>(items);
		lfb.setSelectedItem(look_and_feel);
		general2.add(lfb);
		
	//	general1.add(new JLabel("Left-Kan Algorithm:"));
	//	JComboBox<String> lkb = new JComboBox<>(kan_options);
	//	lkb.setSelectedItem(kan_option);
	//	general2.add(lkb);
		
		//general1.add(new JLabel());
		//general2.add(new JLabel());
		
		int ret = JOptionPane.showOptionDialog(null, jtb, "Options",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new String[] { "OK", "Cancel", "Reset", "Save", "Load" }, "OK");
		// showConfirmDialog(null, p, "Options",
		// JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

		selected_tab = jtb.getSelectedIndex();

		if (ret == 0 || ret == 3) {
			int a = MAX_PATH_LENGTH;
			int b = MAX_DENOTE_ITERATIONS;
			int d = varlen;
			int f = FONT_SIZE;
			int n = MAX_NODES;
			try {
				a = Integer.parseInt(plen.getText());
				b = Integer.parseInt(iter.getText());
				d = Integer.parseInt(vlen.getText());
				f = Integer.parseInt(font_field.getText());
				n = Integer.parseInt(node_limit_field.getText());
				if (f < 1) {
					f = FONT_SIZE;
				}
			} catch (NumberFormatException nfe) {
				return;
			}
			MAX_NODES = n;
//			continue_on_error = coeB.isSelected();
//			VALIDATE_WITH_EDS = ed.isSelected();
//			ALLOW_NULLS = nullbox.isSelected();
			ALL_GR_PATHS = gr.isSelected();
//			ALLOW_INFINITES = jcb0.isSelected();
			VALIDATE = jcb.isSelected();
			// DO_NOT_GUIDIFY = jcbX.isSelected();
			// SHOW_QUERY_PATHS = jcb1.isSelected();
			MultiView = jcbM.isSelected();
			limit_examples = limex.isSelected();

			// CHECK_MAPPINGS = jcb1.isSelected();
			MAX_PATH_LENGTH = a;
			MAX_DENOTE_ITERATIONS = b;
			varlen = d;
			prelude = area.getText();
			afterlude = area2.getText();
			
			GUI.setFontSize(f);
			FONT_SIZE = f;

			schema_denotation = schema_denotation_box.isSelected();
			schema_ed = schema_ed_box.isSelected();
			schema_graphical = schema_graphical_box.isSelected();
			schema_tabular = schema_tabular_box.isSelected();
			schema_textual = schema_textual_box.isSelected();
			schema_rdf = schema_rdf_box.isSelected();
			
			mapping_ed = mapping_ed_box.isSelected();
			mapping_graphical = mapping_graphical_box.isSelected();
			mapping_tabular = mapping_tabular_box.isSelected();
			mapping_textual = mapping_textual_box.isSelected();

			query_graphical = query_graphical_box.isSelected();
			query_textual = query_textual_box.isSelected();

			inst_graphical = inst_graphical_box.isSelected();
			inst_tabular = inst_tabular_box.isSelected();
			inst_textual = inst_textual_box.isSelected();
			inst_joined = inst_joined_box.isSelected();
			inst_gr = inst_gr_box.isSelected();
			inst_obs = inst_obs_box.isSelected();
			inst_rdf = inst_rdf_box.isSelected();

			schema_graph = (String) schemaBox.getSelectedItem();
			mapping_graph = (String) mappingBox.getSelectedItem();
			inst_graph = (String) instBox.getSelectedItem();
			//trans_graph = (String) transBox.getSelectedItem();
			query_graph = (String) queryBox.getSelectedItem();
			instFlow_graph = (String) instFlowBox.getSelectedItem();
			schFlow_graph = (String) schFlowBox.getSelectedItem();
			
			set_textual = set_textual_box.isSelected();
			set_tabular = set_tabular_box.isSelected();
			set_graph = set_graph_box.isSelected();
			fn_tabular = fn_tabular_box.isSelected();
			fn_textual = fn_textual_box.isSelected();
			fn_graph = fn_graph_box.isSelected();
			ftr_textual = ftr_textual_box.isSelected();
			ftr_tabular = ftr_tabular_box.isSelected();
			ftr_graph = ftr_graph_box.isSelected();
			trans_textual = t_textual_box.isSelected();
			trans_tabular = t_tabular_box.isSelected();
			trans_graph = t_graph_box.isSelected();
			
			cat_schema = cat_schema_box.isSelected();
			ftr_elements = ftr_elements_box.isSelected();
			ftr_instance = ftr_instance_box.isSelected();
			ftr_joined = ftr_joined_box.isSelected();
			ftr_mapping = ftr_mapping_box.isSelected();
			trans_elements = t_elements_box.isSelected();

			
		//	allow_surjective = surjB.isSelected();
			
			transform_graphical = transform_graphical_box.isSelected();
			transform_tabular = transform_tabular_box.isSelected();
			transform_textual = transform_textual_box.isSelected();

			if (h2Button.isSelected()) {
				sqlKind = SQLKIND.H2;
			} else if (jdbcButton.isSelected()) {
				sqlKind = SQLKIND.JDBC;
			} else if (nativeButton.isSelected()) {
				sqlKind = SQLKIND.NATIVE;
			}

			jdbcUrl = jdbcField.getText();
			jdbcClass = jdbcField2.getText();
			FILE_PATH = fileArea.getText();
			
		//	kan_option =  lkb.getSelectedItem().toString();
			
			if (!lfb.getSelectedItem().equals(look_and_feel)) {
				try {
					UIManager.setLookAndFeel(lfb.getSelectedItem().toString());
					SwingUtilities.updateComponentTreeUI(GUI.topFrame);
					look_and_feel = lfb.getSelectedItem().toString();
					//FQL.f.pack();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
			
		} else if (ret == 2) {
			new DEBUG().showOptions();
		}
		if (ret == 3) { // save
			save();
			showOptions();
		}

		if (ret == 4) { // load
			load(false);
			// debug.showOptions();
		}

		int nbox = GUI.box.getModel().getSize();
		if (limit_examples) {
			if (nbox != Examples.key_examples.length) {
				DefaultComboBoxModel<Example> m = new DefaultComboBoxModel<>(
						Examples.key_examples);
				GUI.box.setModel(m);
				GUI.box.setSelectedIndex(-1);
			}
		} else {
			if (nbox != Examples.examples.length) {
				DefaultComboBoxModel<Example> m = new DefaultComboBoxModel<>(
						Examples.examples);
				GUI.box.setModel(m);
				GUI.box.setSelectedIndex(-1);
			}
		}
	}

	public static void showAbout() {
		JOptionPane.showMessageDialog(null, about, "About",
				JOptionPane.PLAIN_MESSAGE, null);
	}

	static String about = "FQL++ IDE Copyright (C) 2012-2014 David Spivak and Ryan Wisnesky"
			+ "\n\nLicense: Creative-Commons Attribution-NonCommercial-NoDerivs 3.0 Unported"
			+ "\n\nLibraries used:\n\nJParsec (parsing)\nJUNG (graph visualization)\nRSyntaxTextArea (code editor)"; //\nH2 (SQL)";

	public static int chase_limit = 64;

	public static final String layout_prefix = "edu.uci.ics.jung.algorithms.layout.";

	static String[] layouts = { "CircleLayout", "FRLayout", "ISOMLayout",
			"KKLayout", "SpringLayout" };

	String layout_string = "<html>" +
			"" +
			"<li>CircleLayout: places vertices on a circle</li>" +
			"<li>FRLayout: Fruchterman-Reingold algorithm (force-directed)</li>" +
			"<li>SOMLayout: self-organizing map layout</li>" +
			"<li>KKLayout: Kamada-Kawai algorithm (tries to maintain distances)</li>" +
			"<li>SpringLayout: simple force-directed layout</li>" +
			"" +
			"</html>";
	
	public int FONT_SIZE = 12;
			
	
	/*
	 * CircleLayout: places vertices on a circle FRLayout: Fruchterman-Reingold
	 * algorithm (force-directed) ISOMLayout: self-organizing map layout
	 * KKLayout: Kamada-Kawai algorithm (tries to maintain specified distances)
	 * SpringLayout: simple force-directed layout
	 */

}