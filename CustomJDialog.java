package loomisWood;

import java.awt.Color;
import java.awt.Image;

import javax.swing.JDialog;

public abstract class CustomJDialog extends JDialog
{
	private static final long serialVersionUID = 4059260892029783756L;
	protected static final Color BACKGROUND_COLOR_1 = new Color(95, 144, 168), BACKGROUND_COLOR_2 = new Color(95, 144, 168);

	public CustomJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		setTitle(titleIn);
		setSize(widthIn, heightIn);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle(titleIn);
		setIconImage(iconIn);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
	}
}
