package proyecto2;

/**
 * Clase para almacenar documentos ingresados por los usuarios
 */
public class Documento {
    private String nombre, size, tipo;
    
    public Documento(String nombre, String size, String tipo){
        this.nombre = nombre;
        this.size = size;
        this.tipo = tipo;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getSize(){
        return size;
    }
    
    public String getTipo(){
        return tipo;
    }
}
