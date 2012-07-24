package lab08;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Checkpoint1 {

	public static void main(String[] args) throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream("src/lab08/data.bin"));
		out.writeDouble(3.14159);
		out.writeInt(500);
		out.writeBoolean(false);
		out.close();

		
		DataInputStream in = new DataInputStream(new FileInputStream("src/lab08/data.bin"));
		System.out.println(in.readDouble());
		System.out.println(in.readInt());
		System.out.println(in.readBoolean());
		in.close();
		
		System.out.println();
		System.out.println();
		
		byte[] title = new byte[30];
		byte[] artist = new byte[30];
		byte[] album = new byte[30];
		byte[] year = new byte[4];
		byte[] comment = new byte[30];
		byte[] genre = new byte[1];
		
		RandomAccessFile file = new RandomAccessFile(new File("src/lab08/song.mp3"), "r");
		
		file.seek(file.length() - 125);
		
		file.read(title);
		file.read(artist);
		file.read(album);
		file.read(year);
		file.read(comment);
		file.read(genre);

		file.close();

		System.out.println(new String(title));
		System.out.println(new String(artist));
		System.out.println(new String(album));
		System.out.println(new String(year));
		System.out.println(new String(comment));
		System.out.println(genre[0]);

		
	}
	
}
