import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.lang.*;

public class ChatServer
{
	public static void main(String args[])
	{
		LoginFrame frm=new LoginFrame("Chat Server");
		Toolkit theKit=frm.getToolkit();
		Dimension wndSize=theKit.getScreenSize();
		
		frm.setBounds(wndSize.width/3,wndSize.height/4,wndSize.width/3,wndSize.height/4);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
}

class LoginFrame extends JFrame
{
	public String tStr;
	public JPanel pan;
	public JTextField tLogin;
	public JLabel lLogin;
	public JButton bLogin;
	public LoginFrame(String str)
	{
		super(str);
		pan=new JPanel(new FlowLayout(FlowLayout.CENTER,30,20));
		lLogin=new JLabel("Enter the User Name");
		tLogin=new JTextField(20);
		bLogin=new JButton("LOGIN");
		pan.add(bLogin);
		Box top=Box.createHorizontalBox();
		
		top.add(Box.createHorizontalStrut(20));
		top.add(lLogin);
		top.add(Box.createHorizontalStrut(20));
		top.add(tLogin);
		top.add(Box.createHorizontalStrut(20));
		
		Box ver=Box.createVerticalBox();
		ver.add(Box.createVerticalStrut(40));
		ver.add(top);
		
		Container content=getContentPane();
		content.setLayout(new BorderLayout(30,10));
		content.add(ver,BorderLayout.CENTER);
		content.add(pan,BorderLayout.SOUTH);
		
		MyBtnListener listen=new MyBtnListener();
		bLogin.addActionListener(listen);
	}
	private class MyBtnListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evn) 
		{
			tStr=tLogin.getText();
			MyFrame frm1=new MyFrame("Chat Server");
			Toolkit theKit=frm1.getToolkit();
			Dimension wndSize=theKit.getScreenSize();
		
			frm1.setBounds(wndSize.width/4,wndSize.height/4,wndSize.width/2,wndSize.height/2);
			frm1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm1.setVisible(true);
			try
			{
				frm1.startService(tStr);
			}
			catch(Exception e)
			{}
			dispose();
				
		}
	}
}

class MyFrame extends JFrame
{
	public JPanel pan;
	public JLabel lSend;
	public JLabel lWanna;
	public JTextArea tSend;
	public JTextArea tLog;
	public JTextArea tWanna;
	public JScrollPane sSend;
	public JScrollPane sLog;
	public JScrollPane sWanna;
	public JButton bSend;
	public JButton bLogout;
	public Box top;
	public Box down;
	public Box mn;
	public Container content;
	public String name;
	public MultiThreadClient mult;
	
	public MyFrame(String str)
	{
		super(str);
		pan=new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,100,10));
		lSend=new JLabel("Send Items",JLabel.CENTER);
		lWanna=new JLabel("Wanna Send",JLabel.CENTER);
		
		tSend=new JTextArea(5,10);
		tLog=new JTextArea(5,5);
		tWanna=new JTextArea(5,15);
		
		sSend=new JScrollPane(tSend);
		sLog=new JScrollPane(tLog);
		sWanna=new JScrollPane(tWanna);
		
		JButton bSend=new JButton("SEND");
		JButton bLogout=new JButton("LOGOUT");
		pan.add(bSend);
		pan.add(bLogout);
		
		top=Box.createHorizontalBox();
		top.add(Box.createHorizontalStrut(20));
		top.add(lSend);
		top.add(Box.createHorizontalStrut(10));
		top.add(sSend);
		top.add(Box.createHorizontalStrut(70));
		top.add(sLog);
		top.add(Box.createHorizontalStrut(100));
		
		down=Box.createHorizontalBox();
		down.add(Box.createHorizontalStrut(20));
		down.add(lWanna);
		down.add(Box.createHorizontalStrut(10));
		down.add(sWanna);
		down.add(Box.createHorizontalStrut(10));
		
		mn=Box.createVerticalBox();
		mn.add(Box.createVerticalStrut(30));
		mn.add(top);
		mn.add(Box.createVerticalStrut(20));
		mn.add(down);
		mn.add(Box.createVerticalStrut(30));
		content=getContentPane();
		content.setLayout(new BorderLayout());
		content.add(mn,BorderLayout.CENTER);
		content.add(pan,BorderLayout.SOUTH);
		MyBtnSendListener listenSend=new MyBtnSendListener();
		MyBtnLogoutListener listenLogout=new MyBtnLogoutListener();
		bSend.addActionListener(listenSend);
		bLogout.addActionListener(listenLogout);
		
	}
	class MyBtnSendListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evn) 
		{
			try
			{
				mult.clientChat();
			}
			catch(Exception e)
			{}
		}
		
	}
	class MyBtnLogoutListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evn) 
		{
			try
			{
				mult.logOut();
			}
			catch(Exception e)
			{}
		
		}
	}
	
		
	public void startService(String str4) throws Exception
	{
		name=str4;
		mult=new MultiThreadClient();
		
	}
	class MultiThreadClient
	{
		Socket s;
		DataInputStream din;
		DataOutputStream dout;
		public MultiThreadClient()
		{
			try
			{
				s=new Socket("localhost",10);
				
				//dout.writeUTF(name);
				
				dout=new DataOutputStream(s.getOutputStream());
				
				din=new DataInputStream(s.getInputStream());
				dout.writeUTF(name);
				dout.flush();
				My m=new My(din);
				Thread t1=new Thread(m);
				t1.start();
			}
			catch(Exception e)
			{}
		}

		public void clientChat() throws Exception
		{
			String s1="";
			s1=tWanna.getText();
			s1=name+" : "+s1;
		
			dout.writeUTF(s1);
			dout.flush();
			tWanna.setText("");
		}
		
		public void logOut() throws Exception
		{
			dout.writeUTF("^");
			dout.flush();
			dispose();
			return;
		}
	}

	class My implements Runnable
	{	
		
		DataInputStream din;
		public My(DataInputStream din)
		{
			this.din=din;
		}
	
		public void run()
		{
			String s2="";
			do
			{
				try
				{
					
					s2=din.readUTF();
					FMatch mat=new FMatch();
					if(mat.findMatch(s2))
					{	
						tLog.setText("");
						s2=din.readUTF();
						while(!mat.findMatch(s2))
						{
							s2=tLog.getText()+"\n"+s2+" is logged in";
							tLog.setText(s2);
							s2=din.readUTF();
						}
					}
					else if(s2.equals("%"))
					{
						s2=din.readUTF();
						s2=tLog.getText()+"\n"+s2;
						tLog.setText(s2);
					}
					else
					{
						s2=tSend.getText()+"\n"+s2;
						tSend.setText(s2);
					}
				}
				catch(Exception e)
				{}
			}while(!s2.equals("stop"));
		}
	}
	
}

class FMatch
{
	public boolean findMatch(String str)
	{
		
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='^')
			return true;
		}
		return false;
	}
}
