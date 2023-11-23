package proyecto2;

public class DocumentoEncolado implements Comparable<DocumentoEncolado> {
    long etiqueta;
    Documento documento;
    
    public DocumentoEncolado(long etiqueta, Documento documento){
        this.etiqueta = etiqueta;
        this.documento = documento;
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
