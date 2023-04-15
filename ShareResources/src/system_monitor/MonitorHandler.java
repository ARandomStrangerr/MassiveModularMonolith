package system_monitor;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;

public class MonitorHandler implements Runnable {
    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    public static void addQueue(String data){
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private final String logFileName;

    public MonitorHandler(String logFileName) {
        this.logFileName = logFileName;
    }

    @Override
    public void run() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            FileWriter writeToFile = new FileWriter(logFileName, true);
            while (true) {
                String data = queue.take();
                System.out.printf("%s %s\n", LocalDateTime.now().format(dateTimeFormatter),data);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
