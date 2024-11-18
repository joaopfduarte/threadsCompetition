import java.util.Date;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

public class Consumer implements Runnable {
    private BlockingQueue<Date> queue;
    private Semaphore semaphore;
    private Random rand = new Random();

    public Consumer(BlockingQueue<Date> queue, Semaphore semaphore) {
        this.queue = queue;
        this.semaphore = semaphore;
    }

    public void run() {
        Date message;
        while (true) {
            SleepUtilities.nap();

            try {
                message = queue.take();

                semaphore.acquire(); // Adquire o semáforo antes de acessar o arquivo
                try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
                    String st = br.readLine(); // Leitura única da linha de saldo
                    if (st != null) {
                        int currentBalance = Integer.parseInt(st);
                        int value = (-1) * (rand.nextInt(100));
                        int updatedBalance = currentBalance + value;

                        // Gravação no arquivo do saldo atualizado
                        try (FileWriter myWriter = new FileWriter("file.txt")) {
                            myWriter.write(String.valueOf(updatedBalance));
                            System.out.println("Consumidor | Consumido: " + value);
                            System.out.println("Saldo Atualizado: " + updatedBalance);
                            System.out.println("-----------------------------------");
                        }
                    }
                } finally {
                    semaphore.release(); // Libera o semáforo
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
