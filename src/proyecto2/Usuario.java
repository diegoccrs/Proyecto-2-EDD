package proyecto2;

/**
 * Clase utilizada para almacenar los usuarios generados, el ahorroTiempo decide cuanto 
 * se disminuira la etiqueta de tiempo de los documentos encolados dependiendo de la prioridad asignada a estos usuarios
 */
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

