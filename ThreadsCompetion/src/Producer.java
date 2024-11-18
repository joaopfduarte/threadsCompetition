import java.util.Date;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

public class Producer implements Runnable {

    private BlockingQueue<Date> queue;
    private Semaphore semaphore;

    public Producer(BlockingQueue<Date> queue, Semaphore semaphore) {
        this.queue = queue;
        this.semaphore = semaphore;
    }

    public void run() {
        Date message;
        while (true) {
            SleepUtilities.nap();

            message = new Date();
            Random rand = new Random();
            int value = rand.nextInt(100);

            try {
                queue.put(message);
                semaphore.acquire();

                try (FileWriter myWriter = new FileWriter("file.txt")) {
                    myWriter.write(String.valueOf(value));
                    System.out.println("Produtor | Produzido: " + value);
                } finally {
                    semaphore.release(); // Corrigido
                }
            } catch (InterruptedException e) { // Corrigido
                e.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex); // Corrigido
            }
        }
    }
}
