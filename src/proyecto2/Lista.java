package proyecto2;

<<<<<<< HEAD
/**
* It represents a simple linked list of Nodes of type T
*/
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
class Lista<T> {
    private Nodo<T> pFirst;
    private int size = 0;
    
<<<<<<< HEAD
    // Adds a new node to the list
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public void Insertar(T data){
        Nodo<T> newNode = new Nodo<>(data);
        
        if(getpFirst() == null){
            setpFirst(newNode);
        } else {
            Nodo<T> aux = getpFirst();
            while(aux.getpNext() != null)
                aux = aux.getpNext();
            aux.setpNext(newNode);
        }
        
        setSize(getSize() + 1);
    }
    
<<<<<<< HEAD
    // It gets the node at the position given by index
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public T get(int index){
        if(getpFirst() == null)
            return null;
        if(index == 0)
            return getpFirst().getData();
        int i = 1;
        Nodo<T> current = getpFirst();
        while(current.getpNext() != null){
            if(i == index)
                return current.getpNext().getData();
            current = current.getpNext();
            i++;
        }
        return null;
    }
    
<<<<<<< HEAD
    // Deletes the node given by key
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public void delete(T key){
        if(getpFirst() == null)
            return;
        if(getpFirst().getData() == key){
            setpFirst(getpFirst().getpNext());
            setSize(getSize() - 1);
            return;
         }
        Nodo current = getpFirst();
        while(current.getpNext() != null){
            if(current.getpNext().getData() == key){
                current.setpNext(current.getpNext().getpNext());
                setSize(getSize() - 1);
                return;
            }
            current = current.getpNext();
        }
    }


    public Lista() {
        this.pFirst = null;
    }

<<<<<<< HEAD

    
    /**
     * @return the pFirst
     */
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public Nodo<T> getpFirst() {
        return pFirst;
    }

<<<<<<< HEAD
    /**
     * @param pFirst the pFirst to set
     */
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public void setpFirst(Nodo<T> pFirst) {
        this.pFirst = pFirst;
    }

<<<<<<< HEAD
    /**
     * @return the size
     */
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public int getSize() {
        return size;
    }

<<<<<<< HEAD
    /**
     * @param size the size to set
     */
=======
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
    public void setSize(int size) {
        this.size = size;
    }
    
<<<<<<< HEAD
    
    
}
=======
       
}
>>>>>>> 362e614c194f68c10a21081b4032c96d9365e1d4
