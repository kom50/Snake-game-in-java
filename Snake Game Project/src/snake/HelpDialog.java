package snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

public class HelpDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	public HelpDialog() {
		setBounds(100, 100, 551, 392);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblInstructions = new JLabel("Instructions");
		lblInstructions.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 0, 0)));
		lblInstructions.setFont(new Font("Consolas", Font.BOLD, 18));
		lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstructions.setBounds(31, 11, 481, 43);
		contentPanel.add(lblInstructions);
		
		JLabel label = new JLabel("");
		label.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		label.setBounds(31, 89, 481, 194);
		contentPanel.add(label);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setBorderPainted(false);
				okButton.setAlignmentY(Component.TOP_ALIGNMENT);
				okButton.setBorder(new LineBorder(new Color(0, 0, 0)));
				okButton.setFocusable(false);
				okButton.setHorizontalTextPosition(SwingConstants.CENTER);
				okButton.setMnemonic(KeyEvent.VK_TAB);
				okButton.setFont(new Font("Century", Font.BOLD, 14));
				okButton.setForeground(Color.WHITE);
				okButton.setBackground(Color.blue);
				okButton.setPreferredSize(new Dimension(100, 30));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						HelpDialog.this.dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
