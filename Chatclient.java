import java.awt.*;
import java.net.*;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.awt.event.*;
import java.io.*;

public class Chatclient extends Frame {
	DataOutputStream dos =null;
	DataInputStream dis = null;
	Socket s = null;
	private boolean bConneted = false;
	
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();

	public static void main(String[] args) {
		new Chatclient().launchFrame();
		
	}

	public void launchFrame() {
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				disconnect();
				System.exit(0);
			}
			
		});
		tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		new Thread(new RecvThread()).start();
	}
	
	public void connect() {
		try {
			 //s = new Socket("192.168.1.6",8888);
			 s = new Socket("localhost",8888);
			 dos = new DataOutputStream(s.getOutputStream());
			 dis = new DataInputStream(s.getInputStream());
			System.out.println("connected!");
			bConneted = true;
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private class TFListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
			//taContent.setText(str);
			tfTxt.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			
			
		}
		
	}
	private class RecvThread implements Runnable{
		
		public void run() {
			
				try {
					while(bConneted) {
					String str;
					str = dis.readUTF();
					//System.out.println(str);
					taContent.setText(taContent.getText()+str+'\n');
				}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
				
			}
		}
	}
 


