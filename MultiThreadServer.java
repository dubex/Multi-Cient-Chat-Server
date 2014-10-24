import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class MultiThreadServer
{
	ArrayList al=new ArrayList();
	ArrayList alName=new ArrayList();
	ServerSocket ss;
	Socket s;
	public MultiThreadServer()
	{
		try
		{
			ss=new ServerSocket(10);
			while(true)
			{
				s=ss.accept();
				al.add(s);
				DataInputStream din=new DataInputStream(s.getInputStream());
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				String name=din.readUTF();
				alName.add(name);
				Runnable r1=new AnThread(al,alName);
				Thread t1=new Thread(r1);
			    t1.start();
				Runnable r=new MyThread(s,al,alName,name);
				Thread t=new Thread(r);
				t.start();
				
			}
		}
		catch(Exception e)
		{}
	}
	public static void main(String arg[])
	{
		new MultiThreadServer();
	}
}

class MyThread implements Runnable
{
	Socket s;
	ArrayList al;
	ArrayList alName;
	String name;
	MyThread(Socket s,ArrayList al,ArrayList alName,String name)
	{
		this.s=s;
		this.al=al;
		this.alName=alName;
		this.name=name;
	}
	
	public void run()
	{
		String s1="";
		try
		{
			DataInputStream din=new DataInputStream(s.getInputStream());
			do
			{
				s1=din.readUTF();
				System.out.println(s1);
				if(!s1.equals("^"))
					tellEveryOne(s1);
				else
				{
					Runnable l=new MyLogout(s,al,alName,name);
					Thread lt=new Thread(l);
					lt.start();
					
				}
			}while(!s1.equals("^"));		
		}
		catch(Exception e)
		{}
	}
	
	public void tellEveryOne(String s1)
	{
		Iterator i=al.iterator();
		while(i.hasNext())
		{
			try
			{
				Socket sc=(Socket)i.next();
				DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
				
				dout.writeUTF(s1);
				dout.flush();
				System.out.println("Client");
			}
			catch(Exception e)
			{}
		}
	}
}

class AnThread implements Runnable 
{
	ArrayList al;
	ArrayList alName;
	AnThread(ArrayList al,ArrayList alName)
	{
		this.al=al;
		this.alName=alName;
	}
	public void run()
	{
		Iterator ial=al.iterator();
		while(ial.hasNext())
		{
			try
			{
				Socket sc=(Socket)ial.next();
				DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
				dout.writeUTF("&^%");
				dout.flush();
				Iterator ialn=alName.iterator();
				while(ialn.hasNext())
				{
					try
					{
						String str=(String)ialn.next();
					//	DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
					//	dout.flush();
						dout.writeUTF(str);
						dout.flush();
						
					}
					catch(Exception e)
					{}
				}
				//DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
				dout.writeUTF("&^%");
				dout.flush();
			}
			catch(Exception e)
			{}
		}
	}
}

class MyLogout implements Runnable
{
	Socket s;
	ArrayList al;
	ArrayList alName;
	String name;
	MyLogout(Socket s,ArrayList al,ArrayList alName,String name)
	{
		this.s=s;
		this.al=al;
		this.alName=alName;
		this.name=name;
	}
	
	public void run()
	{
		String s1="";
		try
		{
			alName.remove(name);
			al.remove(s);
			tellEveryOne(name+" is logged out");
		}
		catch(Exception e)
		{}
	}
	
	public void tellEveryOne(String s1)
	{
		Iterator i=al.iterator();
		while(i.hasNext())
		{
			try
			{
				Socket sc=(Socket)i.next();
				DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
				dout.writeUTF("%");
				dout.flush();
				dout.writeUTF(s1);
				dout.flush();
				
				System.out.println("Client");
			}
			catch(Exception e)
			{}
		}
	}
}
