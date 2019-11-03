/*
    Autor: João Victor Nascimento
    RA: 1817442
    Descrição: Sequencial One-Dimensional Stencil
 */
package SequencialOneDimensionalStencil;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        float[] array = new float[] { 1, 3, 3, 3, 1};
        float[] ClonedArray = array.clone();

        int iterations = 0;
        boolean valueHasChanged;

        do {
            valueHasChanged = false;
            for (int i = 1; i < array.length - 1; i++) {
                ClonedArray[i] = (array[i-1] + array[i+1])/2;
                valueHasChanged = valueHasChanged || (ClonedArray[i] != array[i]);
            }
            iterations++;
            array = ClonedArray.clone();
        } while (valueHasChanged);


        System.out.println(Arrays.toString(array));
        System.out.println("Iterations: " + iterations);
    }
}
