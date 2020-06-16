
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Logger;

public class ServerCode {
	static PriorityBlockingQueue<Message> pq = new PriorityBlockingQueue<>(100, new MessageComparator());
	static Map<Integer, Socket> hm = new HashMap<>();

	public static void main(String args[]) throws Exception {
		Logger logger = LoggerUtil.createLogger();

		/** Generating Public and Private Keys by Server **/
		RSA.generateKeys();

		new EchoReadServer(logger).start();
		int uniqueId;
		try (ServerSocket serverSocket = new ServerSocket(8765)) {

			for (int i = 0; i < 2; i++) {
				Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"javac ClientCode.java && java ClientCode\" ");
			}

			while (true) {
				Socket s = serverSocket.accept();
				Random rand = new Random();
				uniqueId = rand.nextInt(1000);
				hm.put(uniqueId, s);
				PrintWriter output = new PrintWriter(s.getOutputStream(), true);
				output.println(uniqueId);
				new Echoer(s, logger, uniqueId).start();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}