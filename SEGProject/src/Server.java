import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//https://www.youtube.com/watch?v=qEcztLI84A4

@SuppressWarnings("serial")
public class Server extends JFrame {

	private Socket connection;
	private ServerSocket server;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private JSONGenerator generator;
	private JTextArea status;
	private JButton sendQuestionnaires;
	private JPanel  east, south, bottomFlow;
	private JComboBox<Object> questList;
	private BufferedImage red, green;
	private JLabel redLab;
	private boolean connected;
	private JScrollPane jsp;

	public Server() {
		generator = new JSONGenerator();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		connected = false;
		setResizable(false);
		setTitle("Server");
		initialiseGUI();
		
		pack();
		

		startServer();
		setVisible(true);
		
	}

	public void initialiseGUI() {
		
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource("res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setIconImage(ic);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {
				if (connected) {
					sendMessage("Connection Ended \n");
					closeConnection();
					dispose();
				} else {
					closeConnection2();
					dispose();
				}
			}

		});

		setLayout(new BorderLayout());

		red = null;
		try {
			red = ImageIO.read(this.getClass().getResource("res/red2.png"));
			green = ImageIO.read(this.getClass().getResource("res/green2.png"));
			redLab = new JLabel(new ImageIcon(red));
			redLab.setPreferredSize(new Dimension(100, 200));

		} catch (IOException e) {
			e.printStackTrace();
		}

		status = new JTextArea("Please open connection from android device \n");
		status.setEditable(false);
		jsp = new JScrollPane();
		jsp.setViewportView(status);
		jsp.setPreferredSize(new Dimension(400, 200));
		jsp.setBounds(10, 101, 10, 10);
		sendQuestionnaires = new JButton("Send Questionnaires");

		sendQuestionnaires.setEnabled(false);

		south = new JPanel();
		south.setLayout(new BoxLayout(south, BoxLayout.PAGE_AXIS));
		ArrayList<String> questionnaires = new ArrayList<String>();
		try {
			questionnaires = (new FeedbackFunctionality()).getQuestionnaires();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		questList = new JComboBox<Object>(questionnaires.toArray());
		bottomFlow = new JPanel(new FlowLayout());
		bottomFlow.add(new JLabel("Choose Questionnaire: "));
		bottomFlow.add(questList);
		south.add(bottomFlow);
		south.add(Box.createRigidArea(new Dimension(100, 10)));
		south.add(sendQuestionnaires);

		east = new JPanel();
		east.add(redLab);

		add(jsp, BorderLayout.CENTER);
		add(east, BorderLayout.EAST);
		add(south, BorderLayout.SOUTH);
		

		sendQuestionnaires.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				sendMessage(generator.generateQuestions(((String) questList
						.getSelectedItem().toString())));
				
			}

		});

	}

	public void startServer() {

		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					
					
					server = new ServerSocket();
					server.setReuseAddress(true);
					server.bind(new InetSocketAddress(4117));


					
					while (true) {
						try {
							String ip = "";
							int i = 0;
							for (final Enumeration<NetworkInterface> interfaces = NetworkInterface
									.getNetworkInterfaces(); interfaces
									.hasMoreElements();) {
								final NetworkInterface cur = interfaces
										.nextElement();
								
								for (final InterfaceAddress addr : cur
										.getInterfaceAddresses()) {
									
									final InetAddress inet_addr = addr
											.getAddress();

									if (!(inet_addr instanceof Inet4Address)) {
										continue;
									}
									if (i == 1) {
										ip = inet_addr.getHostAddress();
									}
									i++;

								}
							}
							
							showStatus("Please enter this code into the android tablet:  "
									+ ip + "\n");
							showStatus("waiting.... \n");

							connection = server.accept();
							showStatus("Now connected to : "
									+ connection.getInetAddress().getHostName()
									+ "\n");
							connected = true;
							sendQuestionnaires.setEnabled(true);
							output = new ObjectOutputStream(connection
									.getOutputStream());
							output.flush();
							input = new ObjectInputStream(connection
									.getInputStream());
							whileConnected();
						} catch (EOFException ended) {
							redLab.setIcon(new ImageIcon(red));
							connected = false;
							JOptionPane.showMessageDialog(null,
									"Connection Ended");

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					closeConnection();
				}

				return null;
			}

		}.execute();

	}

	public void whileConnected() throws IOException {

		redLab.setIcon(new ImageIcon(green));

		String message = "Connected to client \n";
		showStatus(message);

		do {
			try {

				message = (String) input.readObject();
				if (message.charAt(3) == 'A'||message.charAt(3) == 'Q' ||message.charAt(3) == 'P') {
					parseJSONFeedback(message);

				} else if (message.charAt(3) == 'I'|| message.charAt(3) == 'f'||message.charAt(3) == 'l'||message.charAt(3) == 'd') {
					parseJSONPatient(message);
					showStatus("Data Received");

				} else {
					showStatus(message + "\n");
				}
			} catch (ClassNotFoundException c) {
				showStatus("Could not send message");
			}

		} while (!message.equals("end connection"));
		connected = false;
		redLab.setIcon(new ImageIcon(red));
	}

	public void parseJSONFeedback(String JSON){
		String answer,patientID;
		int questionID, questionnaire;
		 JSONParser parser=new JSONParser();
	      try{
	         Object obj = parser.parse(JSON);
	         JSONArray array = (JSONArray)obj;
	         for(int i = 0 ; i < array.size();i++){
	        	JSONObject everyQuestion = (JSONObject)array.get(i);
	        	
	        	answer = everyQuestion.get("Answer").toString();
	        	questionID = Integer.parseInt(everyQuestion.get("QuestionID").toString());
	        	patientID = everyQuestion.get("PatientID").toString();
	        	questionnaire = Integer.parseInt(everyQuestion.get("QuestionnaireID").toString());
	        	FeedbackFunctionality addToTable = new FeedbackFunctionality(questionnaire, patientID,questionID,answer);
	        	try {
					addToTable.addfeedback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	 	        	 
	         }	       
	      }catch(ParseException pe){
	      }
	}
	
	/*
	 * [{"IDpatient": "12-09-1000.Bayoumy","firstname": "Mohamed","lastname": "Bayoumy","dateofbirth": "12-09-1000"}, 
	 * {"IDpatient": "12-02-1500.Omran","firstname": "Ahmed","lastname": "Omran","dateofbirth": "12-02-1500"}]
	 * 
	 */
	public void parseJSONPatient(String JSON){
		String IDpatient ,firstname, lastname, dateofbirth;
		 JSONParser parser=new JSONParser();
	      try{
	         Object obj = parser.parse(JSON);
	         JSONArray array = (JSONArray)obj;
	         for(int i = 0 ; i < array.size();i++){
	        	JSONObject everyQuestion = (JSONObject)array.get(i);
	        	
	        	IDpatient = everyQuestion.get("IDPatient").toString();
	        	firstname = everyQuestion.get("firstname").toString();
	        	lastname = everyQuestion.get("lastname").toString();
	        	dateofbirth = everyQuestion.get("dateofbirth").toString();

	        	
	        	FeedbackFunctionality addToTable = new FeedbackFunctionality(IDpatient,firstname,lastname, dateofbirth);
	        	try {
					addToTable.addpatients();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	 	        	 
	         }	       
	      }catch(ParseException pe){
	      }
	}

	public void closeConnection() {
		try {
				output.close();
				output.flush();
				input.close();
				server.close();
				connection.close();
				
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void closeConnection2(){
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
	}

	public void sendMessage(String message) {
		try {
			output.writeObject(message);
			output.flush();
			//showStatus(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showStatus(final String statusForTheField) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				status.append(statusForTheField);
			}
		});
	}

}
