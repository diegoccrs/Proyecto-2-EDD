package proyecto2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.time.ZonedDateTime;  
import java.time.temporal.ChronoUnit;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.*;

public class Interfaz extends javax.swing.JFrame {
    
    Lista<Usuario> usuarios;
    MonticuloBinario<DocumentoEncolado> colaPrioridad;
    ZonedDateTime inicio;
    TablaDispersion<Usuario, Lista<DocumentoEncolado>> tablaDispersion;
    
    public Interfaz() {
        initComponents();
        inicio = ZonedDateTime.now();
        usuarios = new Lista<>();
        colaPrioridad = new MonticuloBinario<>(99);
        tablaDispersion = new TablaDispersion<>(99);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Csv Files","csv");
        fileChooser.setFileFilter(filter);
        error1.setVisible(false);
        error2.setVisible(false);
        
        readFile(".\\src\\data\\data.csv");
        updateUsers();
    }
    
    private void readFile(String path){
        try {
            File archivo = new File(path);
            FileReader lector = new FileReader(archivo);
            try (java.io.BufferedReader buffer = new BufferedReader(lector)) {
                String linea;                  
                buffer.readLine();
                while ((linea = buffer.readLine()) != null ) {
                    String[] datosUsuario = linea.split(",");
                    if(datosUsuario.length == 2){
                        String nombre = datosUsuario[0];
                        String prioridad = datosUsuario[1];
                        if(!nombre.equals(" ") && !prioridad.equals(" ")){
                            Usuario nuevoUsuario = new Usuario(nombre, prioridad);
                            usuarios.Insertar(nuevoUsuario);
                        }
                    }                     
                }
            }

        }
        catch(IOException ioe) {
          ioe.printStackTrace();
        }
    }
    // Actualizar los dropdowns de los usuarios
    private void updateUsers(){
        userList.removeAllItems();
        userSelectionList.removeAllItems();
        usersDeleteDoc.removeAllItems();
        Nodo<Usuario> current = usuarios.getpFirst();
        while(current != null){
            String name = current.getData().getNombre();
            userList.addItem(name);
            userSelectionList.addItem(name);
            usersDeleteDoc.addItem(name);
            current = current.getpNext();
        }
    }
    
    /**
     * Actualizar contenidos de la ventana donde se eliminan los documentos en la cola
     */
    private void updateDeleteWindow(){
        if(usersDeleteDoc.getItemCount() > 0){
            String nombre = usersDeleteDoc.getSelectedItem().toString();
            Usuario u = getUser(nombre);
            Lista<DocumentoEncolado> l = tablaDispersion.get(u);
            deleteDocQueue.removeAllItems();
            if(l != null){
                updateDeleteDocList(l);
            }
        }
    }
    
    /**
     * Recorrer monticulo y mostrar contenidos en la interfaz
     */
    private void showHeapText(){
        String cola = "Documentos a imprimir: \n\n";
        int n = colaPrioridad.getSize();
        
        for (int i = 1; i <= n; i++) {
            DocumentoEncolado d = colaPrioridad.getElement(i);
            cola += d.getDocumento().getNombre() + d.getDocumento().getTipo() +" (Tiempo: " + d.getEtiqueta() + ") \n";
        }
        colaImpresion.setText(cola);
    }
    
    /**
     * Crear grafo que representa al monticulo binario en una nueva ventana
     */
    private void initGraph(){
        System.setProperty("org.graphstream.ui", "swing");
        
        Graph grafoDibujo = new SingleGraph("Cola de Impresion");
        grafoDibujo.setAttribute("ui.stylesheet", "node{ fill-color: black; size:30; text-background-mode: plain;}");
        grafoDibujo.setAttribute("ui.antialias");

        // Add nodes and edges to represent the MinHeap
        for (int i = 1; i <= colaPrioridad.getSize(); i++) {
            String nodeName = String.valueOf(i);
            grafoDibujo.addNode(nodeName).setAttribute("label", colaPrioridad.getElement(i).getDocumento().getNombre() + " (" + colaPrioridad.getElement(i).getEtiqueta() + ")");

            if (i > 1) {
                String parentName = String.valueOf(i / 2);
                grafoDibujo.addEdge(nodeName + "-" + parentName, nodeName, parentName);
            }
        }
        
        Viewer viewer = grafoDibujo.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }
    
    private void updateDocList(Usuario user){
        docsList.removeAllItems();
        Nodo<Documento> current = user.getDocumentos().getpFirst();
        while(current != null){
            Documento doc = current.getData();
            String name = doc.getNombre();
            docsList.addItem(name);
            current = current.getpNext();
        }
    }
    
    private void updateDeleteDocList(Lista<DocumentoEncolado> l){
        Nodo<DocumentoEncolado> current = l.getpFirst();
        while(current != null){
            String name = current.getData().getDocumento().getNombre();
            deleteDocQueue.addItem(name);
            current = current.getpNext();
        }
    }
    
    private Usuario getUser(String name){
        Nodo<Usuario> current = usuarios.getpFirst();
        
        if(current == null)
            return null;
        
        if(current.getData().getNombre().equals(name)){
            return current.getData();
        }
        else{
            while(current!= null){
                if(current.getData().getNombre().equals(name)){
                    return current.getData();
                }
                current = current.getpNext();
            }
        }
        return null;
    }
    
    private Documento getDocument(String username, String docName){
        Usuario user = getUser(username);
        Nodo<Documento> current = user.getDocumentos().getpFirst();
        
        if(current == null)
            return null;
        
        if(current.getData().getNombre().equals(docName)){
            return current.getData();
        }
        else{
            while(current!= null){
                if(current.getData().getNombre().equals(docName)){
                    return current.getData();
                }
                current = current.getpNext();
            }
        }
        return null;
    }
    
    private DocumentoEncolado getDocumentoEncolado(Lista<DocumentoEncolado> l, String docName){
        Nodo<DocumentoEncolado> current = l.getpFirst();
        
        if(current == null)
            return null;
        
        if(current.getData().getDocumento().getNombre().equals(docName)){
            return current.getData();
        }
        else{
            while(current!= null){
                if(current.getData().getDocumento().getNombre().equals(docName)){
                    return current.getData();
                }
                current = current.getpNext();
            }
        }
        return null;
    }
    
    private void deleteUser(String user){
        Nodo<Usuario> current = usuarios.getpFirst();
        
        if(current == null)
            return;
        
        if(current.getData().getNombre().equals(user)){
            usuarios.setpFirst(current.getpNext());
            usuarios.setSize(usuarios.getSize() - 1);
        }
        else{
            while(current.getpNext() != null){
                String name = current.getpNext().getData().getNombre();

                if(name.equals(user)){
                    current.setpNext(current.getpNext().getpNext());
                    usuarios.setSize(usuarios.getSize() - 1);
                    break;
                }
                current = current.getpNext();
            }
        }
       
        updateUsers();
    } 
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        dialogSave = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        priorityButton = new javax.swing.JRadioButton();
        deleteWindow = new javax.swing.JDialog();
        usersDeleteDoc = new javax.swing.JComboBox<>();
        deleteDocQueue = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        load = new javax.swing.JButton();
        userList = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nameNewUser = new javax.swing.JTextField();
        agregar = new javax.swing.JToggleButton();
        eliminar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        prioridadBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        userSelectionList = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        nombreDocumento = new javax.swing.JTextField();
        tamanho = new javax.swing.JTextField();
        tipoList = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        docsList = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        colaImpresion = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        error1 = new javax.swing.JLabel();
        error2 = new javax.swing.JLabel();

        jLabel6.setText("El documento a imprimir es de prioridad ?");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("IMPORTANTE");

        jButton2.setText("Continuar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        priorityButton.setText("Si, si es de prioridad");
        priorityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priorityButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogSaveLayout = new javax.swing.GroupLayout(dialogSave.getContentPane());
        dialogSave.getContentPane().setLayout(dialogSaveLayout);
        dialogSaveLayout.setHorizontalGroup(
            dialogSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogSaveLayout.createSequentialGroup()
                .addGroup(dialogSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogSaveLayout.createSequentialGroup()
                        .addContainerGap(31, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addGroup(dialogSaveLayout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
            .addGroup(dialogSaveLayout.createSequentialGroup()
                .addGroup(dialogSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogSaveLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(priorityButton))
                    .addGroup(dialogSaveLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dialogSaveLayout.setVerticalGroup(
            dialogSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogSaveLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(priorityButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        usersDeleteDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersDeleteDocActionPerformed(evt);
            }
        });

        jLabel12.setText("Seleccione el documento a eliminar:");

        jLabel13.setText("Seleccione un usuario:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Eliminar Documento en Cola");

        jButton8.setText("Eliminar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout deleteWindowLayout = new javax.swing.GroupLayout(deleteWindow.getContentPane());
        deleteWindow.getContentPane().setLayout(deleteWindowLayout);
        deleteWindowLayout.setHorizontalGroup(
            deleteWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteWindowLayout.createSequentialGroup()
                .addGroup(deleteWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deleteWindowLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel14))
                    .addGroup(deleteWindowLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(deleteWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(usersDeleteDoc, 0, 223, Short.MAX_VALUE)
                            .addComponent(deleteDocQueue, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(deleteWindowLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jButton8)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        deleteWindowLayout.setVerticalGroup(
            deleteWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteWindowLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usersDeleteDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteDocQueue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        load.setText("Cargar Usuarios");
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });

        userList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userListActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Eliminar Usuarios");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Crear Usuarios");

        nameNewUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameNewUserActionPerformed(evt);
            }
        });

        agregar.setText("Crear");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Archivos");

        prioridadBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "prioridad_baja", "prioridad_media", "prioridad_alta" }));
        prioridadBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prioridadBoxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Gestionar Documentos");

        jLabel5.setText("Seleccione su usuario:");

        userSelectionList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSelectionListActionPerformed(evt);
            }
        });

        jLabel8.setText("Indique los datos de su documento (nombre, tamaño y tipo):");

        nombreDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreDocumentoActionPerformed(evt);
            }
        });

        tamanho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tamanhoActionPerformed(evt);
            }
        });

        tipoList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ".pdf", ".docx", ".pptx", ".jpg" }));

        jLabel9.setText("Seleccione un documento:");

        jButton3.setText("Crear Documento");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        docsList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docsListActionPerformed(evt);
            }
        });

        jButton4.setText("Imprimir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Cola de Impresión");

        jButton5.setText("Eliminar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        colaImpresion.setColumns(20);
        colaImpresion.setRows(5);
        jScrollPane1.setViewportView(colaImpresion);

        jButton1.setText("Ver Monticulo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setText("Liberar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Eliminar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        error1.setForeground(new java.awt.Color(255, 0, 0));
        error1.setText("El nombre no puede estar vacio");

        error2.setForeground(new java.awt.Color(255, 0, 0));
        error2.setText("Debe especificar un nombre y un tamaño");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(load)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(userList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(18, 18, 18)
                                    .addComponent(eliminar))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(nameNewUser, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(prioridadBox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(agregar))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(error2)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(nombreDocumento)
                                                .addGap(18, 18, 18)
                                                .addComponent(tamanho, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(tipoList, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(30, 30, 30)
                                                .addComponent(userSelectionList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(18, 18, 18)
                                                .addComponent(docsList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(79, 79, 79)
                                                        .addComponent(jLabel10)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))))
                        .addContainerGap(16, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(error1)
                            .addComponent(jLabel11)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addComponent(load)
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameNewUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agregar)
                    .addComponent(prioridadBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(error1)
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(userSelectionList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tamanho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipoList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(error2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(docsList, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            readFile(fileChooser.getSelectedFile().getAbsolutePath());
            updateUsers();
        }    
    }//GEN-LAST:event_loadActionPerformed

    private void nameNewUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameNewUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameNewUserActionPerformed
    
    /**
     * Crear nuevo usuario con su nombre y disponibilidad
     */
    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        String name = nameNewUser.getText();
        if(!name.isBlank()){
            String prioridad = String.valueOf(prioridadBox.getSelectedItem());
            Usuario usuario = new Usuario(name, prioridad);
            usuarios.Insertar(usuario);
            nameNewUser.setText("");
            updateUsers();
            error1.setVisible(false);
        } else{
            error1.setVisible(true);
        }
        
    }//GEN-LAST:event_agregarActionPerformed
    
    /**
     * Eliminar usuario seleccionado
     */
    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        String user = String.valueOf(userList.getSelectedItem());
        deleteUser(user);
    }//GEN-LAST:event_eliminarActionPerformed
    
    /**
     * Generar etiqueta de tiempo que le da la prioidad del documento encolado
     * se inserta este documento en la estructura DocumentoEncolado y se añade al monticulo
     * y el usuario con su cola de documetnosEncolados, se añade en la tabla de dispersion
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dialogSave.setVisible(false);
        long etiqueta = inicio.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
        
        String usuarioSeleccionado = userSelectionList.getSelectedItem().toString();
        Usuario u = getUser(usuarioSeleccionado);
        String docNameSeleccionado = docsList.getSelectedItem().toString();
        
        if(priorityButton.isSelected()){
            etiqueta /= u.getAhorroTiempo();
            if(etiqueta < 0){
                etiqueta = 0;
            }
        }
        
        Documento doc = getDocument(usuarioSeleccionado, docNameSeleccionado);
        DocumentoEncolado docEncolado = new DocumentoEncolado(etiqueta, doc);
        colaPrioridad.insertar(docEncolado); 
        showHeapText();
        docsList.removeItem(doc.getNombre());
        
        Lista<DocumentoEncolado> l = tablaDispersion.get(u);
        if(l == null){
            l = new Lista<>();
        }
        l.Insertar(docEncolado);
        tablaDispersion.put(u, l);
        updateDeleteWindow();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void prioridadBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prioridadBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prioridadBoxActionPerformed

    private void nombreDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreDocumentoActionPerformed
    
    /**
     * Crear documento y agregarlo a la lista de los documentos del usuario que lo creo 
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(usersDeleteDoc.getItemCount() > 0){
            String usuarioSeleccionado = userSelectionList.getSelectedItem().toString();
            Usuario usuario = getUser(usuarioSeleccionado);
            if(usuario != null){
                String nombreDoc = nombreDocumento.getText();
                String tamanhoDoc = tamanho.getText();
                String tipoDoc = tipoList.getSelectedItem().toString();
                if(!nombreDoc.isBlank() && !tamanhoDoc.isBlank()){
                    usuario.agregarDoc(new Documento(nombreDoc, tamanhoDoc, tipoDoc));
                    updateDocList(usuario);

                    nombreDocumento.setText("");
                    tamanho.setText("");
                    error2.setVisible(false);
                }else {
                    error2.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tamanhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamanhoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tamanhoActionPerformed

    private void userSelectionListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSelectionListActionPerformed
        if(userSelectionList.getItemCount() > 0){
            String username = userSelectionList.getSelectedItem().toString();
            Usuario user = getUser(username);
            updateDocList(user);
        }
    }//GEN-LAST:event_userSelectionListActionPerformed
    
    /**
     * Eliminar el documento seleccionado de la lista de documentos del usuario
     */
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if(userSelectionList.getItemCount() > 0 && docsList.getItemCount() > 0){
            String nombreUsuario = userSelectionList.getSelectedItem().toString();
            Usuario u = getUser(nombreUsuario);
            Documento d = getDocument(nombreUsuario, docsList.getSelectedItem().toString());
            u.getDocumentos().delete(d);
            docsList.removeItem(d.getNombre());
        }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void priorityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorityButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priorityButtonActionPerformed
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(docsList.getItemCount() > 0){
            dialogSave.pack();
            dialogSave.setLocationRelativeTo(null);
            dialogSave.setResizable(false);
            dialogSave.setVisible(true); 
        }      
    }//GEN-LAST:event_jButton4ActionPerformed

    private void docsListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docsListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_docsListActionPerformed

    private void userListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userListActionPerformed
    
    /**
     * Sacar un elemento del monticulo binario
     */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        colaPrioridad.eliminar_min();
        showHeapText();
        updateDeleteWindow();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        initGraph();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        deleteWindow.pack();
        deleteWindow.setLocationRelativeTo(null);
        deleteWindow.setResizable(false);
        deleteWindow.setVisible(true);     
    }//GEN-LAST:event_jButton7ActionPerformed

    private void usersDeleteDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersDeleteDocActionPerformed
        updateDeleteWindow();    
    }//GEN-LAST:event_usersDeleteDocActionPerformed
    
    /**
     * Eliminar documento encolado de la tabla de dispersion y del monticulo binario,
     * para esto ultimo se pone la etiqueta de dicho documento en -1 y luego se extrae el elemento min del monticulo
     */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if(usersDeleteDoc.getItemCount() > 0){
            String nombre = usersDeleteDoc.getSelectedItem().toString();
            Usuario u = getUser(nombre);
            Lista<DocumentoEncolado> l = tablaDispersion.get(u);
            String nombreDoc = deleteDocQueue.getSelectedItem().toString();
            DocumentoEncolado d = getDocumentoEncolado(l, nombreDoc);
            d.setEtiqueta(-1);
            colaPrioridad.restaurarTodo();
            colaPrioridad.eliminar_min();
            l.delete(d);
            tablaDispersion.put(u, l);
            deleteDocQueue.removeAllItems();
            updateDeleteDocList(l);
            showHeapText();   
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton agregar;
    private javax.swing.JTextArea colaImpresion;
    private javax.swing.JComboBox<String> deleteDocQueue;
    private javax.swing.JDialog deleteWindow;
    private javax.swing.JDialog dialogSave;
    private javax.swing.JComboBox<String> docsList;
    private javax.swing.JButton eliminar;
    private javax.swing.JLabel error1;
    private javax.swing.JLabel error2;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton load;
    private javax.swing.JTextField nameNewUser;
    private javax.swing.JTextField nombreDocumento;
    private javax.swing.JComboBox<String> prioridadBox;
    private javax.swing.JRadioButton priorityButton;
    private javax.swing.JTextField tamanho;
    private javax.swing.JComboBox<String> tipoList;
    private javax.swing.JComboBox<String> userList;
    private javax.swing.JComboBox<String> userSelectionList;
    private javax.swing.JComboBox<String> usersDeleteDoc;
    // End of variables declaration//GEN-END:variables
}
