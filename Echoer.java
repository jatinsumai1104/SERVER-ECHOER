import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.io.PrintWriter;

public class Echoer extends Thread {
	private Socket socket;
	Logger logger;
	int uniqueId;
	Scanner sc = new Scanner(System.in);

	Echoer(Socket socket, Logger logger, int uniqueId) {
		this.socket = socket;
		this.logger = logger;
		this.uniqueId = uniqueId;
	}

	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			// int uniqueId = 0;
			while (true) {

				boolean command = false;

				String inputText = input.readLine();
				/** Decrypting Here */
				String echoString = new String(RSA.rsaDecrypt(Base64.getDecoder().decode(inputText)));
				/** Decrypted Message */
				System.out.println("Server got this: " + echoString);
				/***********/
				String textPriorityString = input.readLine();
				if (textPriorityString != null) {
					int textPriority = Integer.parseInt(textPriorityString);

					String h2TextGroupRegex = "(<command>)(.*?)(</command>)";
					Pattern h2TextGroupPattern = Pattern.compile(h2TextGroupRegex);
					Matcher h2TextGroupMatcher = h2TextGroupPattern.matcher(echoString);
					if (h2TextGroupMatcher.matches()) {
						command = true;
						echoString = echoString.replaceAll("<command>", "");
						echoString = echoString.replaceAll("</command>", "");
					}
					if ("exit".equals(echoString.trim())) {
						break;
					}
					System.out.println("Server interpreted input as: " + echoString);

					Thread.sleep(1000);

					// ADDING DATA INTO PRIORITY QUEUE
					logger.info("REQUEST  " + echoString + " ADDED TO PRIORITY QUEUE BY CLIENT ID : " + this.uniqueId);
					ServerCode.pq.add(new Message(uniqueId, echoString, textPriority, command));
				}
			}
		} catch (IOException e) {
			System.out.println("issue: " + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// THESE 3 LINES BELOW SHOW THAT THE PRIORITY QUEUE SET IS WORKING PROPERLY.

			while (!ServerCode.pq.isEmpty()) {
				System.out.println(ServerCode.pq.poll().priority);
			}
			System.out.println("FOR FURTHER LOGS PLEASE CHECK LOG FILES");
		}
	}
}
