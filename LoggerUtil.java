

import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.IOException;

public class LoggerUtil
{
    public static Logger createLogger() throws IOException
    {
        LogManager lgmngr = LogManager.getLogManager();
        // lgmngr now contains a reference to the log manager.
        Logger logger = lgmngr.getLogger(Logger.GLOBAL_LOGGER_NAME);
        FileHandler logFileHandler;
        logFileHandler = new FileHandler("logFiles\\logs.log");
        logger.addHandler(logFileHandler);
        return logger;
    }
}
