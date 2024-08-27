import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public static void main(String[] args)
	{
		try (ServerSocket serverSocket
			= new ServerSocket(9999)) {
			System.out.println(
				"Server is Starting in Port 9999");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Connected");
			dataInputStream = new DataInputStream(
				clientSocket.getInputStream());
			dataOutputStream = new DataOutputStream(
				clientSocket.getOutputStream());
			receiveFile("MyDetails_Output.txt");

			dataInputStream.close();
			dataOutputStream.close();
			clientSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void receiveFile(String fileName)
		throws Exception
	{
		int bytes = 0;
		FileOutputStream fileOutputStream
			= new FileOutputStream(fileName);

		long size = dataInputStream.readLong();
		byte[] buffer = new byte[4 * 1024];
		while (size > 0
			&& (bytes = dataInputStream.read(
					buffer, 0,
					(int)Math.min(buffer.length, size)))
					!= -1) {
			fileOutputStream.write(buffer, 0, bytes);
			size -= bytes;
		}
		System.out.println("File is Received");
		fileOutputStream.close();
	}
}

