package proyecto2;

public class TablaDispersion<K, V> {
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

    public TablaDispersion(int capacity) {
        this.bucket = new Lista[capacity];
        this.capacity = capacity;
        size = 0;
    }
    
    /**
     * Obtiene mediante su hash el índice perteneciente a la key. 
     */
     private int getIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
     /**
      * Agrega el elemento en la lista apropiada según su valor de hash. Si ya se encunetra insertada entonces
      * se actualiza el valor del nodo.
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
     * Mediante el valor de hash de la llave accedemos al nodo deseado.
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
