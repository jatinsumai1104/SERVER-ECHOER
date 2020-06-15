

import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class EchoReadServer extends Thread
{
    Logger logger;
    EchoReadServer(Logger logger)
    {
        this.logger = logger;
    }
    PrintWriter output;
    Process proc;
    public void run()
    {
        try
        {
            while(true)
            {
                // System.out.println("hii");
                Message m = ServerCode.pq.take();
                Socket s = ServerCode.hm.get(m.uniqueId);
                output = new PrintWriter(s.getOutputStream(), true);
                if(m.command)
                {
                    try
                    {
                        proc = Runtime.getRuntime().exec(m.message);
                        StreamGobbler errorGobbler = new
                        StreamGobbler(proc.getErrorStream(), "ERROR");

                        // any output?
                        StreamGobbler outputGobbler = new
                        StreamGobbler(proc.getInputStream(), "OUTPUT");

                        // kick them off
                        errorGobbler.start();
                        outputGobbler.start();

                        // any error???
                        int exitVal = proc.waitFor();
                        System.out.println("ExitValue: " + exitVal);
                        if(exitVal == 0)
                        {
                            logger.info("RESPONSE FOR CLIENT ID " + m.uniqueId + ": ACK" + " ON REQUEST : " + m.message);
                            output.println("ACK");
                        }
                        else
                        {
                            logger.info("RESPONSE FOR CLIENT ID " + m.uniqueId + ": NO ACK" + " ON REQUEST : " + m.message);
                            output.println("NO ACK");
                        }
                    }
                    catch (Throwable t)
                    {   
                        logger.info("INCORRECT COMMAND REQUESTED BY CLIENT ID " + m.uniqueId + ": NO ACK" + " ON REQUEST MESSAGE : " + m.message);
                        output.println("NO ACK");
                    }
                }
                else
                {   
                    logger.info("RESPONSE FOR CLIENT ID " + m.uniqueId + ": ACK" + " ON REQUEST : " + m.message);
                    output.println("ACK");
                }

            }
        }
        catch(Exception e)
        {
            //proc.destroy();
            //output.println("NO ACK");
            e.printStackTrace();
        }
    }
}