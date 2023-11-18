package proyecto2;

public class HashTable<K, V> {
    private Lista<HashNode<K, V>>[] bucket;
    private int capacity;
    private int size;

    public class HashNode<K, V> {
        K key;
        V value;

        HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashTable(int capacity) {
        this.bucket = new Lista[capacity];
        this.capacity = capacity;
        size = 0;
    }
    
    /**
     * Obtiene el índice perteneciente a la key, mediante su hash. 
     */
     private int getIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
     /**
      * Inserta el nodo en la lista apropiada según su valor de hash. Si se intenta insertar
      * un nodo con una llave que que ya se encunetra insertada, se actualiza el valor del nodo.
      */
    public void put(K key, V value) {
        int index = getIndex(key);
        Lista<HashNode<K, V>> list = bucket[index];

        if (list == null) {
            list = new Lista<>();
            bucket[index] = list;
        }
        
        int n = list.getSize();
        for (int i=0; i < n; i++) {
            HashNode<K, V> entry = list.get(i);
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        list.Insertar(new HashNode<>(key, value));
        size++;
    }
    
    /**
     * Mediante el valor de hash de la llave accedemos a la posición de la lista deseada y 
     * en ella buscamos el nodo con el valor de la llave deseada. El orden de esta operación
     * en el mejor de los casos es O(1) y en el peor O(n).
     */
    public V get(K key) {
        int index = getIndex(key);
        Lista<HashNode<K, V>> list = bucket[index];

        if (list != null) {
            int n = list.getSize();
            for (int i=0; i < n; i++) {
                HashNode<K, V> entry = list.get(i);
                if (entry.key.equals(key))
                    return entry.value;
            }  
        }

        return null;
    }
    
    /**
     * Mediante el valor de hash de la llave accedemos a la posición de la lista deseada y 
     * la recorremos para eliminar el nodo deseado de esta lista.
     */
    public void delete(K key) {
        int index = getIndex(key);
        Lista<HashNode<K, V>> list = bucket[index];

        if (list != null) {
            int n = list.getSize();
            for (int i=0; i < n; i++) {
                HashNode<K, V> entry = list.get(i);
                if (entry.key.equals(key)){
                    list.delete(entry);
                    size--;
                    return;
                }
            }  
        }
    }
}
