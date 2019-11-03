/*
    Autor: João Victor Nascimento
    RA: 1817442
    Descrição: - Criar um novo latch para cada iteração com um novo igual K
               - Criar K tarefas (1 thread por tarefas) para serem executadas em paralelo por threads. Uma thread principal
               - espera todas as tarefas. Cada tarefas corresponde a N/K elementos.
               - Uma tarefa termina e avisa a thread principal que finalizou.
               - Quando todas as tarefas finalizam, a thread principal retoma sua atividade e inicia uma nova iteração.
               - Uma vez que uma tarefa é finalizada, libera uma thread para executar outra tarefa. Sugestão de manter um thread pool pequeno.
               - O latch é uma barreira para a execução da thread principal.

 */
package Latch;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CountDownLatch;



public class LatchMain implements Runnable {

    static float[] array = new float[] { 1, 4, 5, 4, 1 };
    static float[] clonedArray = array.clone();
    static boolean valueHasChanged;
    CountDownLatch latch;
    int pos;


    LatchMain(int pos, CountDownLatch latch) {
        this.pos = pos;
        this.latch = latch;
    }

    @Override
    public void run() {
        clonedArray[pos] = (array[pos - 1] + array[pos + 1]) / 2;
        valueHasChanged = valueHasChanged || (clonedArray[pos] != array[pos]);
        latch.countDown();
    }

    public static void main(String[] args) {
        int iterations = 0;
        ExecutorService executor = Executors.newFixedThreadPool(array.length - 2);

        do {
            CountDownLatch latch = new CountDownLatch(array.length - 2);
            valueHasChanged = false;
            for (int i = 1; i < array.length - 1; i++) {
                executor.submit(new LatchMain(i, latch));
            }
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            iterations++;
            array = clonedArray.clone();
        } while (valueHasChanged);


        System.out.println(Arrays.toString(array));
        System.out.println("Interations: " + iterations);
        executor.shutdown();
    }
}
