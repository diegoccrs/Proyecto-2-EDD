package proyecto2;

/**
 * Clase utilizada para almacenar la cola de impresion, extiende la clase Comparable para poder
 * comparar dos elementos T y respetar las condiciones del minHeap
 */
public class MonticuloBinario<T extends Comparable<T>> {
    private T[] monticulo;
    private int n, max;

    public MonticuloBinario(int n) {
        this.monticulo = (T[]) new Comparable[n + 1];
        this.max = n;
        this.n = 0;      
    }
    
    public void insertar(T value) {
        if (n == max) {
            return;
        }

        n++;
        monticulo[n] = value;
        int currentIndex = n;

        // Restaurar la propiedad del min-heap después de la inserción
        int half = currentIndex / 2;
        while (currentIndex > 1 && monticulo[currentIndex].compareTo(monticulo[half]) < 0) {
            T temp = monticulo[currentIndex];
            monticulo[currentIndex] = monticulo[half];
            monticulo[half] = temp;
            currentIndex = half;
        }
    }

    public T eliminar_min() {
        if (n == 0) {
            return null;
        }

        T min = monticulo[1];
        monticulo[1] = monticulo[n];
        n--;

        restaurar(1);

        return min;
    }
    
    public void restaurarTodo() {
        for (int i = n / 2; i >= 1; i--) {
            restaurar(i);
        }
    }
    
    void restaurar(int index) {
        int smallest = index;
        int leftChild = 2 * index;
        int rightChild = 2 * index + 1;

        if (leftChild <= n && monticulo[leftChild].compareTo(monticulo[smallest]) < 0) {
            smallest = leftChild;
        }

        if (rightChild <= n && monticulo[rightChild].compareTo(monticulo[smallest]) < 0) {
            smallest = rightChild;
        }

        if (smallest != index) {
            T temp = monticulo[index];
            monticulo[index] = monticulo[smallest];
            monticulo[smallest] = temp;
            restaurar(smallest);
        }
    }
    
    public int getSize(){
        return n;
    }
    
    public T getElement(int index){
        return monticulo[index];
    }
    
}
