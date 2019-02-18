package frc.robot.util.network;

import static java.lang.System.out;

import java.net.InetAddress;
import java.net.Socket;

import java.io.*;




import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.lang.Float;

public class Network
{

	public static int decode_int(byte[] buffer, int start)
	{
		int res = 0x0;
		char length = 4;
		for (int i = 0; i < length; i++)
		{
			int this_one = 0xff&(buffer[i+start]);

			res |= this_one <<  (8*((length-i)-1)); //TODO
		}
		return res;
	}
	public static float decode_float(byte[] buffer, int start)
	{
		int res = 0x0;
		char length = 4;
		for (int i = 0; i < length; i++)
		{
			int this_one = 0xff&(buffer[i+start]);

			res |= this_one <<  (8*((length-i)-1)); //TODO
		}
		return Float.intBitsToFloat(res);
	}
	public static long decode_long(byte[] buffer, int start)
	{
		long res = 0x0;
		char length = 8;
		for (int i = 0; i < length; i++)
		{
			int this_one = 0xff&(buffer[i+start]);
			//System.out.println("this byte "+(int) buffer[i+start]);
			res |= this_one << (8*((length-i)-1)); //TODO
		}
		return res;
	}
	public static int get_next_size(DataInputStream in) throws Exception
	{
		byte[] buffer = new byte[12];

		in.readFully(buffer,0,12);

		long size = decode_long(buffer,4);
		return (int) size;
	}

	Socket mysocket;
	PrintWriter                                                                            out;
	DataInputStream in;
	

	public Network(String addr, short port) throws Exception
	{	
		InetAddress inet_addr = InetAddress.getByName(addr);
		mysocket = new Socket(inet_addr,port);
		out = new PrintWriter(mysocket.getOutputStream(),true);
		in = new DataInputStream(mysocket.getInputStream());
	}
	public void close() throws Exception
	{
		mysocket.close();
	}
	public void write(String response) throws Exception
	{
		out.println(response);
		out.flush();
	}
	public byte[] read_variable_size_message() throws Exception
	{
		int size = get_next_size(in);
		byte[] buffer = new byte[size];
		in.readFully(buffer,0,(int)size);
		return buffer;
	}

	/*public static void main(String[] args) throws Exception
	{
		
		Socket mysocket = new Socket("127.0.0.1",5667);
		PrintWriter out = new PrintWriter(mysocket.getOutputStream(),true);
		DataInputStream in = new DataInputStream(mysocket.getInputStream());
		while(true)
		{
			int size = getNextSize(in);
			byte[] buffer = new byte[(int) size];
			in.readFully(buffer,0,(int)size);
			//System.out.println(count);

			int sentval = decode_int(buffer,4);
			float other = decode_float(buffer,8);
			System.out.println(sentval);
			System.out.println("Float val " +other);

		}
		//mysocket.close();

	}*/

}