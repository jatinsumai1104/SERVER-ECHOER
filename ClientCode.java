
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Logger;

public class ClientCode {
    public static void main(String args[]) throws Exception {
        try (Socket socket = new Socket("localhost", 8765)) {
            int c;
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int uniqueId = Integer.valueOf(input.readLine());
            InputStream clientIn = socket.getInputStream();
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            String str;
            BufferedReader br = new BufferedReader(new FileReader("messages.txt"));
            String st = "";
            ArrayList<String> msg_strings = new ArrayList<>();

            Logger logger = LoggerUtil.createLogger();

            while ((st = br.readLine()) != null) {
                msg_strings.add(st);
            }
            Random rand = new Random();
            int rand_int;
            for (int i = 0; i < 1; i++) {
                rand_int = rand.nextInt(msg_strings.size() - 1);
                String[] text = msg_strings.get(rand_int).split("==>");
                String textMessage = text[0].trim();
                int textPriority = Integer.parseInt(text[1].trim());
                switch (rand_int % 2) {
                    case 0:
                        str = "<command>" + textMessage + "</command>";
                        System.out.println(
                                "The client has selected a message which is being sent to the server from messages.txt and is being send as command : "
                                        + str + " and the priority as: " + textPriority);
                        logger.info("COMMAND " + textMessage + " PRIORITY " + textPriority
                                + " REQUESTED BY CLIENT ID : " + uniqueId);
                        break;
                    default:
                        str = textMessage;
                        System.out.println(
                                "The client has selected a message which is being sent to the server from messages.txt and is being send as normal text as : "
                                        + str + " and the priority as: " + textPriority);
                        logger.info("MESSAGE " + textMessage + " PRIORITY " + textPriority
                                + " REQUESTED BY CLIENT ID : " + uniqueId);
                        break;
                }

                byte[] encryptedMessageBytes = RSA.rsaEncrypt(str.getBytes());

                String encryptedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

                output.println(encryptedMessage);
                output.println(textPriority);
            }
            while ((c = clientIn.read()) != -1)
                System.out.print((char) c);
        } catch (SocketTimeoutException e) {
            System.out.println("issue: " + e);
        } catch (IOException e) {
            System.out.println("issue:" + e);
        } catch (Exception e) {
            System.out.println("issue: " + e);
        }
    }
}
