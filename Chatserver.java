import java.io.*;
import java.net.*;
import java.util.*;

public class Chatserver {
	boolean started = false;
	ServerSocket ss = null;
	List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		new Chatserver().start();
	}
	public void start() {
		try {
			ss = new ServerSocket(8888);
			started = true;
		} catch (BindException e) {
			System.out.println("Port is processing!");
			System.out.println("Please close the program and restart server!");
			System.exit(0);
		}catch (IOException e) {
			System.out.println("Server fails to start!");
		}
		try {

			while (started) {
				// boolean bConnected = false;
				Socket s = ss.accept();
				Client c = new Client(s);

				// s = ss.accept();
				System.out.println("A CLIENT CONNECTED!");
				new Thread(c).start();
				clients.add(c);
				}
		}catch (IOException e) {

			e.printStackTrace();
			 //System.out.print("Client closed");
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		
		public Client(Socket s) {
			this.s = s;
			try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			bConnected = true;
		}catch (IOException e) {
			e.printStackTrace();
			}
		}
		
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				//e.printStackTrace();
				System.out.println("your friend close the window!");
			}
			
		}
		
		public void run() {
			Client c = null;
			try {
			while(bConnected) 
			{
				String str = dis.readUTF();
System.out.println(str);
				for(int i=0;i<clients.size();i++)
					{	 c = clients.get(i);
						c.send(str);
					}
				//for(Iterator<Client> it=clients.iterator();it.hasNext();){
				//Client c= it.next();
				//c.send(str);
				// 效率低}
			}
			//}catch (SocketException e) {
			//	if (c!=null) clients.remove(c);
			//	System.out.println("a client quit!");
				
			
			}catch (EOFException e) {
				//e.printStackTrace();
				System.out.println("Client closed!");
			} 
			catch (IOException e) {
				e.printStackTrace();
				//System.out.print("Client closed");
			}finally{
				try {
					if(dis !=null)
						dis.close();
					if(dos!=null)
						dos.close();
					if(s!=null) {
						s.close();
						//s = null;
					}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				//if (c!=null) clients.remove(c);
				//System.out.println("a client quit!");
			}
			
			
		}
}

}
