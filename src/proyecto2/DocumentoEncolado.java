package proyecto2;

/**
 * Clase utilizada para almacenar documentos que fueron enviados a la cola de impresion
 * la variable etiqueta representa el tiempo pasado desde el inicio de la simulacion
 */
public class DocumentoEncolado implements Comparable<DocumentoEncolado> {
    private long etiqueta;
    private Documento documento;
    
    public DocumentoEncolado(long etiqueta, Documento documento){
        this.etiqueta = etiqueta;
        this.documento = documento;
    }
    
    public void setEtiqueta(long etiqueta){
        this.etiqueta = etiqueta;
    }
    
    public long getEtiqueta(){
        return etiqueta;
    }
    
    public Documento getDocumento(){
        return documento;
    }
    
    @Override public int compareTo(DocumentoEncolado doc){
        if(this.etiqueta==doc.etiqueta)  
            return 0;  
         else if(this.etiqueta > doc.etiqueta)  
            return 1;  
         else  
            return -1; 
    }
}
