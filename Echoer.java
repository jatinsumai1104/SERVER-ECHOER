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
import java.io.PrintWriter;

public class Echoer extends Thread{
	private Socket socket;
    Logger logger;
	Scanner sc = new Scanner(System.in);
	Echoer(Socket socket,Logger logger){
		this.socket = socket;
        this.logger = logger;	
	}
	public void run(){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
			int uniqueId=0;
			while(true){
				
                boolean command = false;
                
				byte[] inputArray = new byte[256];				
				InputStream in = socket.getInputStream();
				in.read(inputArray);
				/**Encrypted Message Printed*/
				System.out.println(new String(inputArray));
				/** Decrypting Here */
				String echoString = new String(RSA.rsaDecrypt(inputArray));
				/**Decrypted Message*/
				System.out.println(echoString);
				/***********/
				String textPriorityString = input.readLine();
				String uniqueString = input.readLine();
				if(uniqueString!=null && textPriorityString!=null){
					int textPriority = Integer.parseInt(textPriorityString);
					uniqueId = Integer.parseInt(uniqueString);
				
				
                // System.out.println("Server got this: " + echoString + " with priority as " + textPriority);
                String h2TextGroupRegex = "(<command>)(.*?)(</command>)";
                Pattern h2TextGroupPattern = Pattern.compile(h2TextGroupRegex);
                Matcher h2TextGroupMatcher = h2TextGroupPattern.matcher(echoString);
                if(h2TextGroupMatcher.matches()){
                    command = true;
                    echoString = echoString.replaceAll("<command>","");
                    echoString = echoString.replaceAll("</command>","");
                }
				if(echoString.equals("exit")){
					break;
				}
				System.out.println("Server got this: " + echoString);
				try{
					Thread.sleep(1000);
				}catch(Exception e){}
				// output.println(sc.nextLine());

                // ADDING DATA INTO PRIORITY QUEUE
                logger.info("REQUEST  "+ echoString + " ADDED TO PRIORITYQUEUE " + " BY CLIENT ID : " + uniqueId);                        
				ServerCode.pq.add(new Message(uniqueId,echoString,textPriority,command));
				//System.out.println(ServerCode.pq.toString());
				}else{
					break;
				}
                

			}
            
		}catch(IOException e){
			System.out.println("issue: " + e);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// try{
			// 	socket.close();
			// }catch(IOException e){
			// }
            //THESE 3 LINES BELOW SHOW THAT THE PRIORITY QUEUE SET IS WORKING PROPERLY.

            while (!ServerCode.pq.isEmpty()) { 
                System.out.println(ServerCode.pq.poll().priority);
                }
			System.out.println("FOR FURTHER LOGS PLEASE CHECK LOGFILES");
			//System.out.println(ServerCode.finalCount);
			//ServerCode.finalCount--;
		}
	}
}

