

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
    


public class ClientCode{
	public static void main(String args[]) throws Exception {
		try(Socket socket = new Socket("localhost",8765)){
            int c;
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int uniqueId = Integer.valueOf(input.readLine());
            InputStream clientIn = socket.getInputStream();
			PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
			Scanner scanner = new Scanner(System.in);
			String str;
			String response;
            BufferedReader br = new BufferedReader(new FileReader("messages.txt")); 
            String st; 
            ArrayList<String> strings = new ArrayList<>();

            Logger logger = LoggerUtil.createLogger();

            while ((st = br.readLine()) != null) {
                strings.add(st);
            }
            Random rand = new Random();
            int rand_int;
            int rand_int2;
			for(int i=0;i<1;i++){
                rand_int = rand.nextInt(strings.size()-1);
                String[] text = strings.get(rand_int).split("==>");
                String textMessage = text[0];
                int textPriority = Integer.parseInt(text[1].trim());
                //rand_int2 = rand.nextInt(2);
                if(rand_int%2==0){
                    str = "<command>" + textMessage + "</command>";
                    System.out.println("The client has selected a message which is being sent to the server from messages.txt and is being send as command as : " + str + " and the priority as: " + textPriority);
                    logger.info("INSTRUCTION "+ textMessage + " PRIORITY "+ textPriority +" REQUESTED BY CLIENT ID : " + uniqueId);                        
                }else{
                    str = textMessage;
                    System.out.println("The client has selected a message which is being sent to the server from messages.txt and is being send as normal text as : " + str + " and the priority as: " + textPriority);
                    logger.info("MESSAGE "+ textMessage + " PRIORITY "+ textPriority +" REQUESTED BY CLIENT ID : " + uniqueId);                        
                }

                byte[] encryptedMessage = RSA.rsaEncrypt(str.getBytes());
                OutputStream os = socket.getOutputStream();                
                os.write(encryptedMessage);
                os.flush();
				output.println(textPriority);
                output.println(uniqueId);
			}
            while((c = clientIn.read())!=-1)
							System.out.println((char)c);
		}catch(SocketTimeoutException e){
			System.out.println("issue: "+ e);
		}catch(IOException e){
			System.out.println("issue:"+ e);
		}catch(Exception e){
			System.out.println("issue: " +e);
		}
	}
}

