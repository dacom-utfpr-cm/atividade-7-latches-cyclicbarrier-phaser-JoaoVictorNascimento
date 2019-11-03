package CycliclicBarrier;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class CycliclicBarrier implements Runnable {

    static boolean valueHasChanged;
    static boolean loop;
    static float[] array = new float[] { 1, 4, 5, 4, 1 };
    static float[] newArray = array.clone();

    static CyclicBarrier cyclicBarrier;
    static AtomicInteger iterations = new AtomicInteger(0);
    int pos;

    CycliclicBarrier(int pos) {
        this.pos = pos;
    }

    @Override
    public void run() {
        do {
            newArray[pos] = (array[pos - 1] + array[pos + 1]) / 2;
            valueHasChanged = valueHasChanged || (newArray[pos] != array[pos]);
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (loop);
    }

    public static void main(String[] args) {
        cyclicBarrier = new CyclicBarrier(array.length - 2, new Runnable(){
            @Override
            public void run() {
                iterations.incrementAndGet();
                array = newArray.clone();
                loop = valueHasChanged;
                valueHasChanged = false;
            }
        });

        Thread[] t = new Thread[array.length - 2];
        for (int i = 1; i < array.length - 1; i++) {
            t[i-1] = new Thread(new CycliclicBarrier(i));
            t[i-1].start();
        }

        for (int i = 1; i < array.length - 1; i++) {
            try {
                t[i-1].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(Arrays.toString(array));
        System.out.println("Interations: " + iterations);
    }
}
