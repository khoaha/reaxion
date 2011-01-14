package com.googlecode.reaxion.tools;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.googlecode.reaxion.tools.components.AttributePanel;
import com.googlecode.reaxion.tools.components.BurstGridPanel;
import com.googlecode.reaxion.tools.components.ToolButton;
import com.googlecode.reaxion.tools.components.ToolMenuItem;
import com.googlecode.reaxion.tools.events.NodeEvent;
import com.googlecode.reaxion.tools.filters.TextFilter;
import com.googlecode.reaxion.tools.listeners.NodeEventListener;
import com.googlecode.reaxion.tools.util.ToolUtils;
import com.googlecode.reaxion.tools.vo.EditorNode;

public class BurstGridEditor extends JFrame implements ActionListener, NodeEventListener {

	public static final String gridDir = "src/com/googlecode/reaxion/resources/burstgrid/";
	public static final String nodeIconDir = "src/com/googlecode/reaxion/tools/icons/";
	public static final String[] nodeTypes = {"ability", "attack", "gauge1", "gauge2", "hp", "rate", "strength"};
	public static final String[] attributes = {"Ability Name", "Attack Name", "Min Gauge Plus", "Max Gauge Plus", 
		"HP Plus", "Rate Plus", "Strength Plus"};
	
	private static int nodeID = 1;

	private JFileChooser fileChooser;

	private JComboBox character;
	private BurstGridPanel bgp;
	private JPanel nodeAttributes;
	private JComboBox types;
	private ToolButton createNode;
	
	public static void main(String[] args) {
		BurstGridEditor bge = new BurstGridEditor();
	}
	
	public BurstGridEditor() {
		super("Burst Grid Editor - Brian Clanton");
		
		init();
	}
	
	private void init() {
		ToolUtils.initialize();
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		fileChooser = new JFileChooser(new File(gridDir));
		fileChooser.setFileFilter(new TextFilter());
		
		initMenu();
		initToolbar();

		// Init Burst Grid
		Dimension temp = new Dimension(480, 480);
		bgp = new BurstGridPanel(temp);
		bgp.addNodeEventListener(this);
		add(bgp);		
		
		initFrame();
	}
	
	private void initMenu() {
		JMenu file = new JMenu("File");
		file.add(new ToolMenuItem("Save Grid", this));
		
		JMenuBar bar = new JMenuBar();
		bar.add(file);
		
		setJMenuBar(bar);
	}
	
	private void initToolbar() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panel.setPreferredSize(new Dimension(250, 480));
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
		
		character = new JComboBox(ToolUtils.getCharacterNames().toArray());
		
		String[] temp = new String[nodeTypes.length];
		
		for (int i = 0; i < temp.length; i++)
			temp[i] = Character.toUpperCase(nodeTypes[i].charAt(0)) + nodeTypes[i].substring(1);
		
		JLabel id = new JLabel("Node ID: " + nodeID);
		JLabel typeDesc = new JLabel("Node Type:");
		types = new JComboBox(temp);
		types.addActionListener(this);
		types.setActionCommand("node_types");
		
		initAttributesPanel();
		
		createNode = new ToolButton("Create Node", this);
		
		addComponentToToolbar(character, toolbar, true);
		addComponentToToolbar(new JSeparator(), toolbar, true);
		addComponentToToolbar(id, toolbar, true);
		addComponentToToolbar(typeDesc, toolbar, true);
		addComponentToToolbar(types, toolbar, true);
		addComponentToToolbar(nodeAttributes, toolbar, true);
		addComponentToToolbar(createNode, toolbar, false);
		
		panel.add(toolbar);
		add(panel);
	}
	
	private void initAttributesPanel() {
		nodeAttributes = new JPanel(new CardLayout());
		
		for (String s : attributes)
			nodeAttributes.add(new AttributePanel(s), s);
		
		CardLayout c = (CardLayout) nodeAttributes.getLayout();
		c.show(nodeAttributes, attributes[0]);
	}
	
	private void addComponentToToolbar(JComponent c, JPanel toolbar, boolean hasSpacing) {
		toolbar.add(c);
		c.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		if (hasSpacing)
			toolbar.add(Box.createRigidArea(new Dimension(230, 10)));
	}
	
	private ImageIcon getIcon(String filename) {
		try {
			BufferedImage original = ImageIO.read(new File(filename));
			int scaledWidth = (int) original.getWidth() / 2;
			int scaledHeight = (int) original.getHeight() / 2;
			BufferedImage scaled = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = scaled.createGraphics();
			AffineTransform scale = AffineTransform.getScaleInstance(.5, .5);
			g.drawRenderedImage(original, scale);
			return new ImageIcon(scaled);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,100);
		pack();
		setVisible(true);
		requestFocus();
	}

	private void saveGrid(File f) {
		try {
			if (!f.getName().contains(".txt"))
				f = new File(f.getPath() + ".txt");
			
			PrintWriter p = new PrintWriter(new FileWriter(f));
			Scanner reader = new Scanner(new File(gridDir + "info.txt"));
			
			while (reader.hasNextLine())
				p.println(reader.nextLine());
			
			reader.close();
			
			p.println((String) character.getSelectedItem() + "\n");
			
			for (EditorNode n : bgp.getNodes())
				p.println(n);
			
			p.close();
			
			JOptionPane.showMessageDialog(null, "File \"" + f.getName() + "\" saved.", "Saving Complete", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (e.getSource() instanceof JComboBox) {
			CardLayout c = (CardLayout) nodeAttributes.getLayout();
			c.show(nodeAttributes, attributes[((JComboBox) e.getSource()).getSelectedIndex()]);
		} else if (command.equals("Create Node")) {
			AttributePanel temp = (AttributePanel) nodeAttributes.getComponents()[types.getSelectedIndex()];
			EditorNode node = new EditorNode(nodeID, (String)types.getSelectedItem(), temp.getData());
			bgp.createSelectedNode(types.getSelectedIndex(), node);
			nodeID++;
		} else if (command.equals("Save Grid")) {
			int returnValue = fileChooser.showSaveDialog(this);
			
			if (returnValue == JFileChooser.APPROVE_OPTION)
				saveGrid(fileChooser.getSelectedFile());
				
		}
	}
	
	public void nodeCreated(NodeEvent e) {
		createNode.setEnabled(false);
	}

	public void nodeAdded(NodeEvent e) {
		createNode.setEnabled(true);
	}

	public void nodeRemoved(NodeEvent e) {
		// TODO Auto-generated method stub
		
	}

}
