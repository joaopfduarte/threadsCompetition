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

                // Escrever o valor no arquivo de saldo
                try (FileWriter myWriter = new FileWriter("C:\\Users\\joaop\\OneDrive\\Projetos Dev\\PROJ-Java\\file.txt")) {
                    myWriter.write(String.valueOf(value));
                }

                // Registrar a mensagem no arquivo de log
                try (FileWriter logWriter = new FileWriter("C:\\Users\\joaop\\OneDrive\\Projetos Dev\\PROJ-Java\\escrita.txt", true)) {
                    logWriter.write("Produtor | Produzido: " + value + "\n");
                }

                // Ainda exibe no terminal, se desejar
                System.out.println("Produtor | Produzido: " + value);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                semaphore.release();
            }
        }
    }
}
