import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginExtended extends JFrame {

	private JLabel theUserText, thePassText;
	private JTextField userField;
	private JPasswordField passField;
	private JButton login, registerNewAccount;
	private static ArrayList<String> loginInfo;

	public LoginExtended() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		initialiseGUI();
		pack();
		setResizable(false);
		setTitle("Log in");
		setVisible(true);
	}

	public void initialiseGUI() {

		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource(
					"res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		setIconImage(ic);
		setLayout(new BorderLayout());
		// JPanel north = new JPanel();
		JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));

		add(center, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);

		theUserText = new JLabel("UserName:");
		thePassText = new JLabel("Password:");
		userField = new JTextField(15);
		passField = new JPasswordField(15);
		login = new JButton("Login");
		registerNewAccount = new JButton("Register");

		theUserText.setPreferredSize(new Dimension(75, 100));
		thePassText.setPreferredSize(new Dimension(75, 100));

		south.add(login);
		south.add(registerNewAccount);

		center.add(theUserText);
		center.add(userField);
		center.add(thePassText);
		center.add(passField);

		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				loginInfo = getLoginInfo();

				if (loginInfo.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Incorrect username or password");
				} else {
					new welcomeScreen(Integer.parseInt(loginInfo.get(0)),
							loginInfo.get(1), loginInfo.get(2));
					dispose();
				}
			}
		});

		getRootPane().setDefaultButton(login);
		registerNewAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<String> loginInfo = getLoginInfo();

				if (loginInfo.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Only the administrator can add new users");
				} else if (loginInfo.get(0).equals("0")) {
					new RegistrationScreen();
					dispose();
				}
			}
		});
	}

	public ArrayList<String> getLoginInfo() {

		LoginFunctionality connecting = new LoginFunctionality(
				userField.getText(), new String(passField.getPassword()));
		ArrayList<String> loginInfo = null;
		try {

			loginInfo = connecting.getUserInfo();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return loginInfo;

	}

	public static ArrayList<String> doctorInfo() {
		return loginInfo;
	}

	public static void clearDoctorInfo() {
		loginInfo.clear();

	}

}
