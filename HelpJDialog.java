package loomisWood;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpJDialog extends CustomJDialog
{
	private static final long serialVersionUID = -6148647579581955449L;
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private GradientPanel panel;
	
	public HelpJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		initComponents();
		setComponentSettings();
		addComponents();
		
		setVisible(true);
	}

	private void initComponents()
	{
		textArea = new JTextArea();
		scrollPane = new JScrollPane();
		panel = new GradientPanel(BACKGROUND_COLOR_1, BACKGROUND_COLOR_2);
	}

	private void setComponentSettings()
	{
		String textAreaContent = "This is a tool to automatically generate Loomis-Wood plots and track spectral \r\n" + 
				"assignments. These assignments can be overlayed onto the display.\r\n" + 
				"\r\n" + 
				"The first thing to do is:\r\n" + 
				"1) Open OPUS\r\n" + 
				"2) Load in the desired spectral file\r\n" + 
				"3) Go to File -> Save File As -> Mode\r\n" + 
				"4) Select \"Pirouette .dat\"\r\n" + 
				"5) Save\r\n" + 
				"\r\n" + 
				"Now you can exit OPUS.\r\n" + 
				"\r\n" + 
				"In this program go to the File tab and select Load Spectra File and select\r\n" + 
				"the file just created from OPUS. This will load in the data. (This may take a few seconds!)\r\n" + 
				"\r\n" + 
				"Now all the features of this program are ready to be used.\r\n" + 
				"\r\n" + 
				"Features:\r\n" + 
				"1) View the spectra in a Loomis-Wood format by going to Display -> Create Loomis-Wood Diagram and \r\n" + 
				"    entering in the desired parameters.\r\n" + 
				"2) Navigate through the spectra by using the arrow buttons in the top right of the display\r\n" + 
				"3) Set the intensity scale (y-axis) of the display by going to Display -> Set intensity Range\r\n" + 
				"4) Right click on the diagrams to change a variety of properties of the chart. Can also left click \r\n" + 
				"    and drag on the chart to zoom in on a part of the spectra. \r\n" + 
				"5) Under Display -> \"Split the diagram into parts\" you can split the diagrams into multiple pieces"
				+ " \r\n    if the spectra is too dense in a region. Saving images will react to this.\r\n" +
				"6) Add and edit assignments by going to the Assignments tab. When editing assignments or branches,\r\n" + 
				"    right clicking on the cell will bring up additional options. Note: You can copy and paste\r\n" + 
				"    data straight from the tables that come up when adding or editing a branch. (Including the ability\r\n" + 
				"    to paste in Excel data!)\r\n" + 
				"7) Can display assignments straight onto the Loomis-Wood plots by going to Display\r\n" + 
				"   -> Toggle Assignment Overlay. Pressing this again will remove the assignments from the display.\r\n" + 
				"8) Hovering the mouse over a part of the spectra will show the points x,y values. If the assignment\r\n" + 
				"    overlay is toggled on, it will show assignment information on assignmented points (eg. points\r\n" + 
				"    with an upside down colored triangle)\r\n" + 
				"9) Take an image of the current Loomis-Wood display by going to Display -> Create a single\r\n" + 
				"    Loomis-Wood image\r\n" + 
				"10) Take images over a given range of the spectra in Loomis-Wood format by going to Display ->\r\n" + 
				"    Create many Loomis-Wood images. The first field is the wavenumber value of where the images\r\n" + 
				"    whill start from, the second is the wavenumber value of where the images will go to, and the\r\n" + 
				"    final is the row width of the Loomis-Wood diagrams.\r\n" + 
				"11) Save and Load the assignments currently in memory. This is done by going to File -> Save Assignments...\r\n" + 
				"      or File -> Load Assignments...\r\n" + 
				"12) In the File tab, reading and writing to Excel Peak Lists is a planned feature.\r\n" + 
				"\r\n";
		
		textArea.setFont(new Font("Times new Roman", Font.PLAIN, 22));
		textArea.setEditable(false);
		textArea.setText(textAreaContent);
		textArea.setLineWrap(true);
		textArea.setCaretPosition(0);

		scrollPane.setPreferredSize(new Dimension(400,400));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.setLayout(new GridBagLayout());
	}

	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		scrollPane.getViewport().add(textArea);
		panel.add(scrollPane, gbc);
		
		add(panel);
	}

}
