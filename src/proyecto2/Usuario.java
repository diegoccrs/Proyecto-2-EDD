package proyecto2;

public class Usuario {
    private String nombre, prioridad;
    private Lista<Documento> documentosPorImprimir;
    
    public Usuario(String nombre, String prioridad){
        this.nombre = nombre;
        this.prioridad = prioridad;
        documentosPorImprimir = new Lista<>();
    }
    
    public void agregarDoc(Documento newDoc){
        documentosPorImprimir.Insertar(newDoc);
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public Lista<Documento> getDocumentos(){
        return documentosPorImprimir;
    }
}
