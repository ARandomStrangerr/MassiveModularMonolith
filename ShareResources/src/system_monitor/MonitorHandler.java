package system_monitor;

import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;

public class MonitorHandler implements Runnable {
    private static final LinkedBlockingQueue<JsonObject> queue = new LinkedBlockingQueue<>();

    public static void addQueue(JsonObject data) {
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
        try (FileWriter writeToFile = new FileWriter(logFileName, true)) {
            while (true) {
                JsonObject data = queue.take();
                String recordData = String.format("%s | %s | %s", LocalDateTime.now().format(dateTimeFormatter), data.get("status").getAsBoolean()? "Success":"Failure", data.remove("notification").getAsString());
                if(data.has("request")) recordData += " | " + data.remove("request").toString();
                recordData += "\n";
                writeToFile.write(recordData);
                writeToFile.flush();
                if (data.remove("status").getAsBoolean()) System.out.print(recordData);
                else System.err.print(recordData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
