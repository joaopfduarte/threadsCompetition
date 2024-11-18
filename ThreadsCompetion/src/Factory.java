import java.util.Date;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

public class Factory {
   public static void main(String[] args) {
    BlockingQueue<Date> queue = new LinkedBlockingQueue<>();
    Semaphore semaphore = new Semaphore(1);

    Thread producer = new Thread(new Producer(queue, semaphore));
    Thread consumer = new Thread(new Consumer(queue, semaphore));

    producer.start();
    consumer.start();
   } 
}
