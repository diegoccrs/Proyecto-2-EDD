package proyecto2;

public class Usuario {
    private String nombre, prioridad;
    private int ahorroTiempo;
    private Lista<Documento> documentosPorImprimir;
    
    public Usuario(String nombre, String prioridad){
        this.nombre = nombre;
        this.prioridad = prioridad;
        ahorroTiempo = switch (prioridad) {
            case "prioridad_baja" -> 2;
            case "prioridad_media" -> 4;
            case "prioridad_alta" -> 8;
            default -> 1;
        };
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
    
    public int getAhorroTiempo(){
        return ahorroTiempo;
    }
}
