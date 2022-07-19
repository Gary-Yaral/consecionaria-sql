
package systemaconcesionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Accidentes extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String patenteSeleccionada, fecSeleccionada;
    boolean clickeado = false;
    static String sql = "select * from accidentes";
    public Accidentes() {
        initComponents();
        
        rs = con.getData(sql);
        if(modelo.getColumnCount() < 1) {
            agregarCabeceras();
        }
        llenarTabla(rs);     
        String query = "select patente from vehiculos";  
        rs = con.getData(query);
        patente.removeAllItems();
        patente.addItem("Selecciona el dueño");
        try {
            while(rs.next()) {
                String pat = rs.getString("patente");            
                patente.addItem(pat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Vehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("Fecha");
        modelo.addColumn("Lugar");
        modelo.addColumn("Descripción");
        modelo.addColumn("Patente");
    } 
    
    public void llenarTabla(ResultSet rs) {
        try {
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("fecha");
                fila[1] = rs.getString("lugar");
                fila[2] = rs.getString("descripción");
                fila[3] = rs.getString("patente");
                modelo.addRow(fila);
            }  
            tabla.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void limpiarTabla() {
        int numDatos = modelo.getRowCount();
        for (int i = 0; i < numDatos; i++) {
         modelo.removeRow(0);
        }
    }
    
    public boolean estanLlenos () {
        int index = patente.getSelectedIndex();
        if(fecha.getDate() == null){
            return false;
        }

        if (index == 0) return false;
        
        if( descripcion.getText().isEmpty()&& lugar.getText().isEmpty()){
            return false;
        }
        
        return true;
                
    }
    
    public void buscarRegistro() {
        String buscar = textoBuscar.getText();
        if(buscar.isEmpty()) {
             JOptionPane.showMessageDialog(null,
                "Debes ingresar lo que vas a buscar",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
             );
             
             return;
        }
        
        String query = "SELECT * FROM accidentes WHERE fecha='"+buscar+"' OR lugar='"+buscar+"' OR descripción='"+buscar+"' OR patente='"+buscar+"'";
        rs = con.getData(query);
        llenarTabla(rs);
        limpiarCampos(false);
        
    }
    
    public boolean exists(String pat, String fec) {
        String query = "select * from accidentes where patente='"+pat+"' and fecha='"+fec+"'";
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
     
     public void limpiarCampos(boolean esTodo) {
        fecha.setCalendar(null);
        lugar.setText("");
        descripcion.setText("");
        patente.setSelectedIndex(0);
        tabla.clearSelection();
        if(esTodo) {      
            textoBuscar.setText("");
        }
        
        clickeado = false;
    }
    
    public void guardarRegistro() {
        
        String pat = patente.getSelectedItem().toString();
        Date date=fecha.getDate();
        long d = date.getTime(); 
        String fecAcc = new java.sql.Date(d).toString(); 
         
        String descAcc = descripcion.getText();
        String lugAcc = lugar.getText();
        
        if(exists(pat, fecAcc)) {
            JOptionPane.showMessageDialog(null,
                "Ya existe accidente registrado con esa patente en esa fecha ",
                "Atención",
                JOptionPane.ERROR_MESSAGE
            );      
            return;
        
        }
            
        String sql1 = "INSERT INTO accidentes(fecha, lugar, descripción, patente) VALUES('"+fecAcc+"','"+lugAcc+"','"+descAcc+"','"+pat+"')";
        boolean seGuardo = con.setData(sql1);

        if(seGuardo) {
            rs = con.getData(sql);
            llenarTabla(rs);
            JOptionPane.showMessageDialog(null,
                "Accidente registrado correctamente",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
            );
            limpiarCampos(true);
        } else {
            
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lugar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        guardarBtn = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();
        textoBuscar = new javax.swing.JTextField();
        buscarBtn = new javax.swing.JButton();
        limpiarBtn = new javax.swing.JButton();
        patente = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        fecha = new com.toedter.calendar.JDateChooser();
        refresh = new javax.swing.JButton();

        setClosable(true);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Accidentes");

        jLabel3.setText("Patente");

        jLabel4.setText("Fecha");

        lugar.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel5.setText("Lugar");

        jLabel6.setText("Descripción");

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
        modificarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarBtnActionPerformed(evt);
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
        limpiarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limpiarBtnMouseClicked(evt);
            }
        });

        patente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        patente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patenteActionPerformed(evt);
            }
        });

        descripcion.setColumns(20);
        descripcion.setRows(5);
        jScrollPane1.setViewportView(descripcion);

        refresh.setText("refresh");
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
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(guardarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lugar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(patente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(refresh)
                                    .addGap(18, 18, 18)
                                    .addComponent(buscarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(modificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(eliminarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(limpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane1)))
                    .addComponent(jScrollPane2))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarBtn)
                    .addComponent(refresh))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(patente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel5)
                        .addGap(1, 1, 1)
                        .addComponent(lugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardarBtn)
                    .addComponent(modificarBtn)
                    .addComponent(eliminarBtn)
                    .addComponent(limpiarBtn))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addGap(0, 34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void eliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnActionPerformed
        
    }//GEN-LAST:event_eliminarBtnActionPerformed

    private void patenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patenteActionPerformed
       
    }//GEN-LAST:event_patenteActionPerformed

    private void guardarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarBtnMouseClicked
        if(estanLlenos()){
            guardarRegistro();
        } else {
            JOptionPane.showMessageDialog(null,
               "Debe rellenar todos los campos o seleccionar dueño",
               "Atención",
               JOptionPane.INFORMATION_MESSAGE
            );
        }

    }//GEN-LAST:event_guardarBtnMouseClicked

    private void eliminarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBtnMouseClicked
        eliminarRegistro();
    }//GEN-LAST:event_eliminarBtnMouseClicked

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            filaSeleccionada = indice;
            fecSeleccionada = modelo.getValueAt(indice, 0).toString();
            String lugAcc = modelo.getValueAt(indice, 1).toString();
            String descAcc= modelo.getValueAt(indice, 2).toString();
            patenteSeleccionada = modelo.getValueAt(indice, 3).toString();
     
            java.util.Date fec;
            try {
                fec = new SimpleDateFormat("yyyy-MM-dd").parse(fecSeleccionada);
                fecha.setDate(fec);
            } catch (ParseException ex) {
                Logger.getLogger(Accidentes.class.getName()).log(Level.SEVERE, null, ex);
            }

            lugar.setText(lugAcc);
            descripcion.setText(descAcc);
           
            
            int items = patente.getItemCount();
            for (int i = 0; i < items; i++) {     
                String pat_data = patente.getItemAt(i).toString();
                if(pat_data.equals(patenteSeleccionada)) {
                    patente.setSelectedIndex(i);
                }
            }

            clickeado = true;    
        }
    }//GEN-LAST:event_tablaMouseClicked

    public void modificarRegistro() {
        String pat = patente.getSelectedItem().toString();
        String  lugAcc = lugar.getText();
        String descAcc = descripcion.getText();
        Date date=fecha.getDate();
        long d = date.getTime(); 
        String fecAcc = new java.sql.Date(d).toString(); 
     
        if(patenteSeleccionada.equals(pat) == false && fecAcc.equals(fecSeleccionada)) {
            if(exists(pat, fecAcc)) {
                JOptionPane.showMessageDialog(null,
                "Ya existe un accidente registrado con ese patente en esa fecha",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
                return;
            }
        }
 
        String sql1 = "update accidentes set fecha='"+fecAcc+"', lugar='"+lugAcc+"',descripción='"+descAcc+"',patente='"+pat+"' where patente ='"+patenteSeleccionada+"' and fecha='"+fecSeleccionada+"'";
        boolean fueModificada = con.setData(sql1);

        if(fueModificada) {
            rs = con.getData(sql);
            llenarTabla(rs);
            JOptionPane.showMessageDialog(null,
                "Vehículo ha sido modificado correctamente",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
            );
            limpiarCampos(true);
        }
    }
    
    private void limpiarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_limpiarBtnMouseClicked
        limpiarCampos(true);
    }//GEN-LAST:event_limpiarBtnMouseClicked

    private void buscarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarBtnActionPerformed
        buscarRegistro();
    }//GEN-LAST:event_buscarBtnActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
       rs = con.getData(sql);
       llenarTabla(rs);
       limpiarCampos(true);
    }//GEN-LAST:event_refreshActionPerformed

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
        if(clickeado == true){
            modificarRegistro();
        } else {
            JOptionPane.showMessageDialog(null,
               "Debe seleccionar un registro para modificar",
               "Atención",
               JOptionPane.INFORMATION_MESSAGE
            );
        }
        
    }//GEN-LAST:event_modificarBtnActionPerformed

    public void eliminarRegistro() {
        if(filaSeleccionada >= 0 && clickeado == true) {
            String sql1 = "delete from accidentes where patente='"+patenteSeleccionada+"' and fecha='"+fecSeleccionada+"'";
            boolean fueEliminado = con.setData(sql1);

            if(fueEliminado) {
                JOptionPane.showMessageDialog(null,
                    "Accidente eliminado correctamente",
                    "Atención",
                    JOptionPane.INFORMATION_MESSAGE
                );
                modelo.removeRow(filaSeleccionada);
                limpiarCampos(true);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                "No ha seleccionado un registro para eliminar",
                "Atención",
                JOptionPane.ERROR_MESSAGE
            );
        }
       
        clickeado = false;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscarBtn;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JButton eliminarBtn;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JTextField lugar;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JComboBox patente;
    private javax.swing.JButton refresh;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField textoBuscar;
    // End of variables declaration//GEN-END:variables
}
