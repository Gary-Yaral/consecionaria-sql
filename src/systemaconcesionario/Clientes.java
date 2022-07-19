
package systemaconcesionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Clientes extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String docSeleccionado;
    boolean clickeado = false;
    static String sql = "select * from clientes";
    public Clientes() {
        initComponents();  
        rs = con.getData(sql);
        if(modelo.getColumnCount() < 1) {
            agregarCabeceras();
        }
       
        llenarTabla(rs);
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("Documento");
        modelo.addColumn("Nombre");
        modelo.addColumn("Domicilio");
        modelo.addColumn("Telefono");
    }  
      
    public void llenarTabla(ResultSet rs) {
        try {
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("documento");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("domicilio");
                fila[3] = rs.getString("telefono");
                modelo.addRow(fila);
            }  
            tabla.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarCliente() {
        String doc = documento.getText();
        String nom = nombre.getText();
        String dom= domicilio.getText();
        String tel = telefono.getText();
        
        if( !doc.isEmpty() 
            && !nom.isEmpty()
            && !dom.isEmpty() 
            && !tel.isEmpty()
      
        ){
            if(exists(doc)) {
                JOptionPane.showMessageDialog(null,
                "Ya existe un usuario con ese documento",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
                return;
            }
            
            String sql1 = "INSERT INTO clientes(documento, nombre, domicilio, telefono) VALUES('"+doc+"','"+nom+"','"+dom+"','"+tel+"')";
            boolean wasInserted = con.setData(sql1);
            
            if(wasInserted) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                    "Cliente guardado correctamente",
                    "Atención",
                    JOptionPane.INFORMATION_MESSAGE
                );
                limpiarCampos(true);
            }
        } else {
             JOptionPane.showMessageDialog(null,
                "Debe rellenar todos los campos",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
             );
        }
    }
    
    public boolean exists(String doc) {
        String query = "select * from clientes where documento='"+doc+"'";
        rs = con.getData(query);
        boolean existe = false;
        try {
            while(rs.next()) {
                existe = true;
            }
            
            return existe;
        } catch (SQLException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return existe;
    }
    
    public void eliminarRegistro() {
       if(filaSeleccionada >= 0 && clickeado == true) {
           try {
               String query = "SELECT*FROM clientes INNER JOIN vehiculos ON clientes.`documento` = vehiculos.`documento_dueno`WHERE clientes.`documento` = '"+docSeleccionado+"'";
               rs = con.getData(query);
               boolean tieneDependencia = false;
               while(rs.next()){
                   tieneDependencia = true;
               }
               
               if(tieneDependencia) {
                   JOptionPane.showMessageDialog(null,
                           "No se puede eliminar, tienes vehículos registrados con este cliente",
                           "Atención",
                           JOptionPane.WARNING_MESSAGE
                   );
                   tabla.clearSelection();
                   return;
               }
               
               
               String sql1 = "delete from clientes where documento='"+docSeleccionado+"'";
               boolean fueEliminado = con.setData(sql1);
               
               if(fueEliminado) {
                   JOptionPane.showMessageDialog(null,
                           "Registro eliminado correctamente",
                           "Atención",
                           JOptionPane.INFORMATION_MESSAGE
                   );
                   modelo.removeRow(filaSeleccionada);
               }
           } catch (SQLException ex) {
               Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       
       clickeado = false;

    }
    
    public void buscarRegistro() {
        String doc = textoBuscar.getText();
        if(!doc.isEmpty()) {
            String sql1 = "SELECT * FROM clientes WHERE documento='"+doc+"' OR nombre='"+doc+"' OR domicilio ='"+doc+"' OR telefono= '"+doc+"'";
            rs = con.getData(sql1);           
            llenarTabla(rs);

        } else {
             JOptionPane.showMessageDialog(null,
                "Debe ingresar un parámetro a buscar",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
        }
    }
    
    public static void limpiarTabla() {
        int numDatos = modelo.getRowCount();
        for (int i = 0; i < numDatos; i++) {
         modelo.removeRow(0);
        }
    }

    public void modificarRegistro() {
        String doc = documento.getText();
        String nom = nombre.getText();
        String dom= domicilio.getText();
        String tel = telefono.getText();
        
        if( !doc.isEmpty() 
            && !nom.isEmpty()
            && !dom.isEmpty() 
            && !tel.isEmpty()
      
        ){
      
            if(docSeleccionado.equals(doc) == false) {
                if(exists(doc)) {
                    JOptionPane.showMessageDialog(null,
                    "Ya existe un usuario con ese documento",
                    "Atención",
                    JOptionPane.ERROR_MESSAGE
                 );
                    return;
                }
            }
 
            String sql1 = "update clientes set documento='"+doc+"', nombre='"+nom+"',domicilio='"+dom+"',telefono='"+tel+"' where documento ='"+docSeleccionado+"'";
            boolean wasInserted = con.setData(sql1);
            
            if(wasInserted) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                    "Cliente modificado correctamente",
                    "Atención",
                    JOptionPane.INFORMATION_MESSAGE
                );
                limpiarCampos(true);
            }
        } else {
             JOptionPane.showMessageDialog(null,
                "Debe rellenar todos los campos",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
             );
        }
    }
    
    public void limpiarCampos(boolean esBuscar) {
        nombre.setText("");
        documento.setText("");
        domicilio.setText("");
        telefono.setText("");
        tabla.clearSelection();
        clickeado = false;
        if (esBuscar) {        
            textoBuscar.setText("");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        documento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        domicilio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        guardarBtn = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();
        textoBuscar = new javax.swing.JTextField();
        buscarBtn = new javax.swing.JButton();
        limpiarBtn = new javax.swing.JButton();
        refrescarBtn = new javax.swing.JButton();
        docSel = new javax.swing.JLabel();

        setClosable(true);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Clientes");

        documento.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel3.setText("Documento");

        jLabel4.setText("Nombre");

        nombre.setMinimumSize(new java.awt.Dimension(6, 23));

        domicilio.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel5.setText("Domicilio");

        jLabel6.setText("Teléfono");

        telefono.setMinimumSize(new java.awt.Dimension(6, 23));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabla);

        guardarBtn.setBackground(new java.awt.Color(51, 51, 255));
        guardarBtn.setForeground(new java.awt.Color(255, 255, 255));
        guardarBtn.setText("Guardar");
        guardarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guardarBtnMouseClicked(evt);
            }
        });

        modificarBtn.setBackground(new java.awt.Color(255, 255, 0));
        modificarBtn.setText("Modificar");
        modificarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modificarBtnMouseClicked(evt);
            }
        });

        eliminarBtn.setBackground(new java.awt.Color(255, 51, 51));
        eliminarBtn.setForeground(new java.awt.Color(255, 204, 204));
        eliminarBtn.setText("Delete");
        eliminarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminarBtnMouseClicked(evt);
            }
        });

        textoBuscar.setMinimumSize(new java.awt.Dimension(6, 23));

        buscarBtn.setBackground(new java.awt.Color(204, 204, 204));
        buscarBtn.setText("Buscar");
        buscarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buscarBtnMouseClicked(evt);
            }
        });

        limpiarBtn.setBackground(new java.awt.Color(255, 255, 255));
        limpiarBtn.setForeground(new java.awt.Color(102, 102, 255));
        limpiarBtn.setText("Limpiar");
        limpiarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarBtnActionPerformed(evt);
            }
        });

        refrescarBtn.setText("Refresh");
        refrescarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refrescarBtnMouseClicked(evt);
            }
        });

        docSel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(domicilio, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(telefono, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(jLabel4)
                                    .addComponent(nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(jLabel3)
                                    .addComponent(documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(guardarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(modificarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(eliminarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(limpiarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(docSel))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refrescarBtn)
                        .addGap(18, 18, 18)
                        .addComponent(buscarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(docSel))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarBtn)
                    .addComponent(refrescarBtn))
                .addGap(11, 11, 11)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(1, 1, 1)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(1, 1, 1)
                        .addComponent(domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(1, 1, 1)
                        .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(guardarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(limpiarBtn))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarBtnMouseClicked
       agregarCliente();
    }//GEN-LAST:event_guardarBtnMouseClicked

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            filaSeleccionada = indice;
            docSeleccionado = modelo.getValueAt(indice, 0).toString();
            String nom = modelo.getValueAt(indice, 1).toString();
            String dom = modelo.getValueAt(indice, 2).toString();
            String tel= modelo.getValueAt(indice, 3).toString();

            documento.setText(docSeleccionado);
            nombre.setText(nom);
            domicilio.setText(dom);
            telefono.setText(tel);

            clickeado = true;    
        }
        
    }//GEN-LAST:event_tablaMouseClicked

    
    private void eliminarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBtnMouseClicked
        eliminarRegistro();
        limpiarCampos(true);
    }//GEN-LAST:event_eliminarBtnMouseClicked

    private void buscarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarBtnMouseClicked
        buscarRegistro();
        limpiarCampos(false);
    }//GEN-LAST:event_buscarBtnMouseClicked

    private void refrescarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refrescarBtnMouseClicked
        rs = con.getData(sql);
        llenarTabla(rs);
        clickeado = false;
        limpiarCampos(true);
    }//GEN-LAST:event_refrescarBtnMouseClicked

    private void modificarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificarBtnMouseClicked
        if(clickeado = false) return;
        modificarRegistro();
    }//GEN-LAST:event_modificarBtnMouseClicked

    private void limpiarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarBtnActionPerformed
       limpiarCampos(true);
    }//GEN-LAST:event_limpiarBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscarBtn;
    private javax.swing.JLabel docSel;
    private javax.swing.JTextField documento;
    private javax.swing.JTextField domicilio;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton refrescarBtn;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField telefono;
    private javax.swing.JTextField textoBuscar;
    // End of variables declaration//GEN-END:variables
}
