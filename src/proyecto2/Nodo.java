package proyecto2;

public class Nodo<T> {
    private T data;
    private Nodo<T> pNext;

    public Nodo(T data){
        this.data = data;
        this.pNext = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Nodo<T> getpNext() {
        return pNext;
    }

    public void setpNext(Nodo<T> pNext) {
        this.pNext = pNext;
    }
      
}