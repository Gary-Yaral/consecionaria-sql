
package systemaconcesionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Vendedor extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String docSeleccionado;
    boolean clickeado = false;
    static String sql = "SELECT vendedor.`documento`, vendedor.`nombre`, vendedor.`telefono`, vendedor.`domicilio`, sucursal.`nro`"
            +"FROM vendedor INNER JOIN sucursal ON vendedor.`nro_sucursal` = sucursal.`id`";
    public Vendedor() {
        initComponents();
        rs = con.getData(sql);
        if(modelo.getColumnCount() < 1) {
            agregarCabeceras();
        }
        llenarTabla(rs);
        
        String query = "select nro from sucursal";    
        rs = con.getData(query);
        numero.removeAllItems();
        numero.addItem("Seleccionar");
        try {
            while(rs.next()) {
                numero.addItem(rs.getString("nro"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Vendedor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("Documento");
        modelo.addColumn("Nombre");
        modelo.addColumn("Domicilio");
        modelo.addColumn("Teléfono");
        modelo.addColumn("N° Sucursal");
    } 
       
    public void agregarRegistro() {
        
        String doc = documento.getText();
        String nom = nombre.getText();
        String dom = domicilio.getText();
        String tel = telefono.getText();
        String num = numero.getSelectedItem().toString();
        
        if(estanLlenos()){
            if(exists(doc)) {
                JOptionPane.showMessageDialog(null,
                "Ya existe un vendedor registrado con ese documento",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
                return;
            }
            
            String sql1 = "INSERT INTO vendedor(documento, nombre, domicilio, telefono, nro_sucursal) VALUES('"+doc+"','"+nom+"','"+dom+"','"+tel+"','"+num+"')";
            boolean seGuardo = con.setData(sql1);
            
            if(seGuardo) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                    "Sucursal agregada correctamente",
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
        String query = "select * from vendedor where documento='"+doc+"'";
        rs = con.getData(query);
        boolean existe = false;
        try {
            while(rs.next()) {
                existe = true;
            }
            
            return existe;
        } catch (SQLException ex) {
            Logger.getLogger(TipoPolizas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return existe;
    }
    
    public void eliminarRegistro() {
       if(filaSeleccionada >= 0 && clickeado == true) {
           String sql1 = "delete from vendedor where documento='"+docSeleccionado+"'";
           boolean fueEliminado = con.setData(sql1);
           
           if(fueEliminado) {
               JOptionPane.showMessageDialog(null,
                    "Registro eliminado correctamente",
                    "Atención",
                    JOptionPane.INFORMATION_MESSAGE
                );
                modelo.removeRow(filaSeleccionada);          
           }
       }
       
       clickeado = false;

    }
    
    public void buscarRegistro() {
        String vendor = textoBuscar.getText();
        if(!vendor.isEmpty()) {
            
            String sql1 = sql+ " WHERE documento='"+vendor+"' OR vendedor.nombre='"+vendor+"' OR vendedor.domicilio='"+vendor+"' OR vendedor.telefono='"+vendor+"' OR sucursal.nro='"+vendor+"'";
            rs = con.getData(sql1);           
            llenarTabla(rs);

        } else {
             JOptionPane.showMessageDialog(null,
                "Debe ingresar el parametro a buscar",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
        }
    }
    
    public boolean estanLlenos () {
        String nom = nombre.getText();
        String dom = domicilio.getText();
        String tel = telefono.getText();
        String doc = documento.getText();
        int index = numero.getSelectedIndex();
        
        if(index == 0) {
            return false;
        } 
        
        if( nom.isEmpty() &&  doc.isEmpty() && dom.isEmpty() && tel.isEmpty()){
            return false;
        }
        
        return true;
                
    }
    
    public static void limpiarTabla() {
        int numDatos = modelo.getRowCount();
        for (int i = 0; i < numDatos; i++) {
         modelo.removeRow(0);
        }
    }

    public void modificarRegistro() {
        String nom = nombre.getText();
        String doc = documento.getText();
        String dom = domicilio.getText();
        String tel = telefono.getText();
        String suc = numero.getSelectedItem().toString();
        System.out.println(suc);
        
        if(estanLlenos()) {     
            try {
                if(docSeleccionado.equals(doc) == false) {
                    if(exists(doc)) {
                        JOptionPane.showMessageDialog(null,
                                "Ya existe un vendedor registrado con ese numero de documento",
                                "Atención",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
                
                String sql1 = "Select id from sucursal where nro='"+suc+"'";
                String sucId = "";
                rs = con.getData(sql1);
                
                while(rs.next()) {
                    sucId = rs.getString("id");
                }
                
                sql1 = "update vendedor set documento='"+doc+"', nombre='"+nom+"', domicilio='"+dom+"',telefono='"+tel+"',nro_sucursal='"+sucId+"' where documento = '"+docSeleccionado+"'";
                boolean seGuardo = con.setData(sql1);
                
                if(seGuardo) {
                    rs = con.getData(sql);
                    llenarTabla(rs);
                    JOptionPane.showMessageDialog(null,
                            "Vendedor modificado correctamente",
                            "Atención",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    limpiarCampos(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Vendedor.class.getName()).log(Level.SEVERE, null, ex);
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
        domicilio.setText("");
        telefono.setText("");
        numero.setSelectedIndex(0);
        documento.setText("");     
        tabla.clearSelection();
        clickeado = false;
        if (esBuscar) {        
            textoBuscar.setText("");
        }
    }

    
    public void llenarTabla(ResultSet rs) {
        try {
         
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("documento");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("domicilio");
                fila[3] = rs.getString("telefono");
                fila[4] = rs.getString("nro");
                modelo.addRow(fila);
            }
            tabla.setModel(modelo);
         
        } catch (SQLException ex) {
            Logger.getLogger(Vendedor.class.getName()).log(Level.SEVERE, null, ex);
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
        numero = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        refresh = new javax.swing.JButton();

        setClosable(true);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Vendedor");

        documento.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel3.setText("N° Sucursal");

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
        guardarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnActionPerformed(evt);
            }
        });

        modificarBtn.setBackground(new java.awt.Color(255, 255, 0));
        modificarBtn.setText("Modificar");
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
            }
        });

        eliminarBtn.setBackground(new java.awt.Color(255, 51, 51));
        eliminarBtn.setForeground(new java.awt.Color(255, 204, 204));
        eliminarBtn.setText("Delete");
        eliminarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBtnActionPerformed(evt);
            }
        });

        textoBuscar.setMinimumSize(new java.awt.Dimension(6, 23));

        buscarBtn.setBackground(new java.awt.Color(204, 204, 204));
        buscarBtn.setText("Buscar");
        buscarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarBtnActionPerformed(evt);
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

        numero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Documento");

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(numero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(domicilio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(telefono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(documento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(guardarBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(modificarBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(eliminarBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(limpiarBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(refresh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buscarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarBtn)
                    .addComponent(numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(refresh))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
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

    private void guardarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnActionPerformed
        agregarRegistro();
    }//GEN-LAST:event_guardarBtnActionPerformed

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if(clickeado = false) return;
        modificarRegistro();
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void eliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnActionPerformed
        eliminarRegistro();
        limpiarCampos(true);
    }//GEN-LAST:event_eliminarBtnActionPerformed

    private void limpiarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarBtnActionPerformed
        limpiarCampos(true);
    }//GEN-LAST:event_limpiarBtnActionPerformed

    private void buscarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarBtnActionPerformed
        buscarRegistro();
        limpiarCampos(false);
    }//GEN-LAST:event_buscarBtnActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            filaSeleccionada = indice;
            docSeleccionado = modelo.getValueAt(indice, 0).toString();
            String nom = modelo.getValueAt(indice, 1).toString();
            String dom = modelo.getValueAt(indice, 2).toString();
            String tel= modelo.getValueAt(indice, 3).toString();
            String num= modelo.getValueAt(indice, 4).toString();

            int items = numero.getItemCount();
            
            for (int i = 0; i < items; i++) {
                String n = numero.getItemAt(i).toString();
                if(n.equals(num)) {
                    numero.setSelectedIndex(i);
                }
            }
            nombre.setText(nom);
            domicilio.setText(dom);
            telefono.setText(tel);
            documento.setText(docSeleccionado);

            clickeado = true;    
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        rs = con.getData(sql);
        llenarTabla(rs);
        clickeado = false;
        limpiarCampos(true);
    }//GEN-LAST:event_refreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscarBtn;
    private javax.swing.JTextField documento;
    private javax.swing.JTextField domicilio;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField nombre;
    private javax.swing.JComboBox numero;
    private javax.swing.JButton refresh;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField telefono;
    private javax.swing.JTextField textoBuscar;
    // End of variables declaration//GEN-END:variables
}
