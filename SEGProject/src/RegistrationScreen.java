import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class RegistrationScreen extends JFrame {

	private JLabel userLabel, passwordLabel, fNameLabel, lNameLabel, gmcLabel;
	private JTextField userText, passText, fNameText, lNameText, gmcText;
	private JButton submit, cancel;

	public RegistrationScreen() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		initialiseGUI();
		pack();
		setResizable(false);
		setTitle("Registration");
		setVisible(true);

	}

	private void initialiseGUI() {
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {
				new LoginExtended();			
				dispose();
			}

		});
		
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource("res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setIconImage(ic);

		userLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		fNameLabel = new JLabel("First name: ");
		lNameLabel = new JLabel("Last name: ");
		gmcLabel = new JLabel("GMC Number: ");

		userText = new JTextField(10);
		passText = new JTextField(10);
		fNameText = new JTextField(10);
		lNameText = new JTextField(10);
		gmcText = new JTextField(7);

		JPanel back = new JPanel();
		back.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		GroupLayout grplayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(grplayout);

		grplayout.setAutoCreateGaps(true);
		grplayout.setAutoCreateContainerGaps(true);

		grplayout.setHorizontalGroup(grplayout.createSequentialGroup()
				.addGroup(
						grplayout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								userLabel).addComponent(passwordLabel)
								.addComponent(fNameLabel).addComponent(
										lNameLabel).addComponent(gmcLabel))
				.addGroup(
						grplayout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								userText).addComponent(passText).addComponent(
								fNameText).addComponent(lNameText)
								.addComponent(gmcText))

		);

		grplayout.setVerticalGroup(grplayout.createSequentialGroup().addGroup(
				grplayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(userLabel).addComponent(userText))
				.addGroup(
						grplayout.createParallelGroup(
								GroupLayout.Alignment.CENTER).addComponent(
								passwordLabel).addComponent(passText))
				.addGroup(
						grplayout.createParallelGroup(
								GroupLayout.Alignment.CENTER).addComponent(
								fNameLabel).addComponent(fNameText)).addGroup(
						grplayout.createParallelGroup(
								GroupLayout.Alignment.CENTER).addComponent(
								lNameLabel).addComponent(lNameText)).addGroup(
						grplayout.createParallelGroup(
								GroupLayout.Alignment.CENTER).addComponent(
								gmcLabel).addComponent(gmcText))

		);

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());

		submit = new JButton("Submit");
		cancel = new JButton("Cancel");

		south.add(submit);
		south.add(cancel);

		back.add(mainPanel, BorderLayout.CENTER);
		back.add(south, BorderLayout.SOUTH);
		add(back);
		
		gmcText.addKeyListener(new java.awt.event.KeyAdapter() {

	        @SuppressWarnings("static-access")
			public void keyReleased(java.awt.event.KeyEvent evt) {
	        	if(evt.getKeyCode() ==evt.VK_ENTER){}
	        	else{
	            try {
	                @SuppressWarnings("unused")
					int x = Integer.parseInt(gmcText.getText());
	            } catch (Exception e) {
	                JOptionPane.showMessageDialog(null, "Please Ensure only digits are entered for GMC number");
	            	gmcText.setText("");
	            }
	        	}
	        }
	    });
		submit.addActionListener(new ActionListener() {

			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (userText.getText().equals("") || passText.getText().equals("")
						|| fNameText.getText().equals("") || lNameText.getText().equals("")
						|| gmcText.getText().equals("")
						) {
					JOptionPane.showMessageDialog(null, "Please Enter a value for each field", "Error",
							JOptionPane.ERROR_MESSAGE);
					

				}
				else if (Integer.parseInt(gmcText.getText()) < 999999 || Integer.parseInt(gmcText.getText()) > 9999999 ){
					JOptionPane.showMessageDialog(null, "Please ensure GMC number is a 7 digit number", "Error",
							JOptionPane.ERROR_MESSAGE);
					
				}

				else {
					
					
					
					

				LoginFunctionality addUser = new LoginFunctionality(userText
						.getText().toLowerCase(), passText.getText(), fNameText.getText(),
						lNameText.getText(), Integer.parseInt(gmcText.getText()));

				try{
					
				ArrayList<String> existingGMC  = addUser.checkIfUserAlreadyExistByGMC();
				if(existingGMC.isEmpty()){
					
					ArrayList<String> existingUsername = addUser.checkIfUserAlreadyExistByUsername();
					
					if(existingUsername.isEmpty()){
					
					addUser.addUserInfo();
					JOptionPane.showMessageDialog(null, "Successful!!");
					
					LoginExtended loginPage = new LoginExtended();
					dispose();
					}
					else{
						JOptionPane.showMessageDialog(null, "An account with this username already exist");	
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "An account with this GMC number already exist");				
				}
				}
				catch (SQLException e1) {
				
					e1.printStackTrace();
				}
				


				}

			}

		});
		
		cancel.addActionListener( new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new LoginExtended();
				
				dispose();
		
			}
		
		});
		
	}
}
