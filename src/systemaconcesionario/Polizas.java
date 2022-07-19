
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

public class Polizas extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String nroSeleccionado;
    boolean clickeado = false;
    static String sql = "SELECT polizas.nro_poliza, polizas.patente, polizas.vendedor, polizas.fecha_inicio, polizas.duracion, tipo_polizas.`nombre` FROM polizas INNER JOIN tipo_polizas ON tipo_polizas.`id` = polizas.`tipo`";
    public Polizas() {
        try {
            initComponents();
            rs = con.getData(sql);
            if(modelo.getColumnCount() < 1) {
                agregarCabeceras();
            }
            
            llenarTabla(rs);
            
            String query = "select * from vendedor";
            rs = con.getData(query);
            vendedor.removeAllItems();
            vendedor.addItem("Selecciona el vendedor");
            while(rs.next()) {
                String cli = rs.getString("documento");
                String nombre = rs.getString("nombre");
                vendedor.addItem(cli+"-"+nombre);
            }
            
            query = "select * from vehiculos";
            rs = con.getData(query);
            patente.removeAllItems();
            patente.addItem("Selecciona una patente");
            while(rs.next()) {
                String cli = rs.getString("patente");
                patente.addItem(cli);
            }
            
            query = "select * from tipo_polizas";
            rs = con.getData(query);
            tipo.removeAllItems();
            tipo.addItem("Selecciona tipo de poliza");
            while(rs.next()) {
                String cli = rs.getString("nombre");
                tipo.addItem(cli);
            }             
            
        } catch (SQLException ex) {
            Logger.getLogger(Polizas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void limpiarCampos(boolean esTodo) {
        tipo.setSelectedIndex(0);
        patente.setSelectedIndex(0);
        vendedor.setSelectedIndex(0);
        fechaInicio.setCalendar(null);
        duracion.setText("");
        tabla.clearSelection();
        if(esTodo) {
            textoBuscar.setText("");
        };
    }
    
    public boolean estanLlenos () {
        int indPat = patente.getSelectedIndex();
        int indVendor = vendedor.getSelectedIndex();
        int indTipo = tipo.getSelectedIndex();
        
        if(indPat == 0 || indVendor == 0 || indTipo == 0) {
            return false;
        }
        
        if(fechaInicio.getDate() == null || duracion.getText().isEmpty()){
            return false;
        }
              
        return true;
                
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("N° Poliza");
        modelo.addColumn("Patente");
        modelo.addColumn("Vendedor");
        modelo.addColumn("Fecha Inicio");
        modelo.addColumn("Duración");
        modelo.addColumn("Tipo");
    }  
    
    public void buscarRegistro() {
        String poliza = textoBuscar.getText();
        if(!poliza.isEmpty()) {
            
            try {
                String query = "SELECT id FROM tipo_polizas WHERE nombre = '"+poliza+"'";
                rs = con.getData(query);
                String id = "";
                
                while (rs.next()) {
                    id = rs.getString("id");
                }
                
                String sql1 = sql + " WHERE nro_poliza='"+poliza+"' OR patente='"+poliza+"' OR vendedor='"+poliza+"' OR fecha_inicio='"+poliza+"' OR duracion='"+poliza+"' OR tipo='"+id+"'";
                rs = con.getData(sql1);
                llenarTabla(rs);
            } catch (SQLException ex) {
                Logger.getLogger(Polizas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
             JOptionPane.showMessageDialog(null,
                "Debe ingresar el parametro a buscar",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
        }
    }
      
    public void llenarTabla(ResultSet rs) {
        try {
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString("nro_poliza");
                fila[1] = rs.getString("patente");
                fila[2] = rs.getString("vendedor");
                fila[3] = rs.getString("fecha_inicio");
                fila[4] = rs.getString("duracion");
                fila[5] = rs.getString("nombre");
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
    
    public void eliminarRegistro() {
        if(filaSeleccionada >= 0 && clickeado == true) {
            String sql1 = "delete from polizas where nro_poliza='"+nroSeleccionado+"'";
            boolean fueEliminado = con.setData(sql1);

            if(fueEliminado) {
                JOptionPane.showMessageDialog(null,
                    "Poliza fue eliminada correctamente",
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
    
    public static boolean esValido(String cadena) {

        boolean resultado;

        try {
            int valor = Integer.parseInt(cadena);
            if(valor <= 0) {
                return false;
            }
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public void guardarRegistro() {
        if(clickeado == true) {
            JOptionPane.showMessageDialog(null,
                "No puede guardar este registro ya que fue seleccionado de la tabla y ya existe su numero",
                "Atención",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        try {
         
            if(exists(nroSeleccionado)) {
                JOptionPane.showMessageDialog(null,
                        "Ya existe una poliza registrada con ese número ",
                        "Atención",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
                
            }
            String dur=  duracion.getText();         
            
            if(!esValido(dur)) {
                JOptionPane.showMessageDialog(null,
                    "La duración debe numérica y mayor a 0",
                    "Atención",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            String pat = patente.getSelectedItem().toString();
            String vendor = vendedor.getSelectedItem().toString().split("-")[0];
            String tipPoliza = tipo.getSelectedItem().toString();
            Date fecIni= fechaInicio.getDate();       
            long dIni = fecIni.getTime();
            String fecGuardar = new java.sql.Date(dIni).toString();
         
            String query = "SELECT id FROM tipo_polizas WHERE nombre='"+tipPoliza+"'";
            rs = con.getData(query);
            String tPoliza = "";
            
            while(rs.next()) {            
                tPoliza = rs.getString("id");
            }
           
            String sql1 = "INSERT INTO polizas(patente, vendedor, fecha_inicio, duracion, tipo) VALUES('"+pat+"','"+vendor+"','"+fecGuardar+"','"+dur+"','"+tPoliza+"')";
            boolean seGuardo = con.setData(sql1);
            
            if(seGuardo) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                        "Poliza registrada correctamente",
                        "Atención",
                        JOptionPane.INFORMATION_MESSAGE
                );
                limpiarCampos(true);
            } else {
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Polizas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modificarRegistro() {     
        
        try {
            
            String dur=  duracion.getText();         
            
            if(!esValido(dur)) {
                JOptionPane.showMessageDialog(null,
                    "La duración debe numérica y mayor a 0",
                    "Atención",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            String pat = patente.getSelectedItem().toString();
            String vendor = vendedor.getSelectedItem().toString().split("-")[0];
            String tipPoliza = tipo.getSelectedItem().toString();
            Date fecIni= fechaInicio.getDate();       
            long dIni = fecIni.getTime();
            String fecGuardar = new java.sql.Date(dIni).toString();
         
            String query = "SELECT id FROM tipo_polizas WHERE nombre='"+tipPoliza+"'";
            rs = con.getData(query);
            String tPoliza = "";
            
            while(rs.next()) {            
                tPoliza = rs.getString("id");
            }
           
            String sql1 = "UPDATE polizas SET patente = '"+pat+"', vendedor='"+vendor+"', fecha_inicio='"+fecGuardar+"', duracion = '"+dur+"', tipo = '"+tPoliza+"' WHERE nro_poliza = '"+nroSeleccionado+"'";
            boolean seGuardo = con.setData(sql1);
            
            if(seGuardo) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                        "Poliza actualizada correctamente",
                        "Atención",
                        JOptionPane.INFORMATION_MESSAGE
                );
                limpiarCampos(true);
            } else {
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Polizas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean exists(String nro) {
        String query = "select * from polizas where nro_poliza='"+nro+"'";
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
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        guardarBtn = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();
        textoBuscar = new javax.swing.JTextField();
        searchData = new javax.swing.JButton();
        patente = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        limpiarBtn = new javax.swing.JButton();
        vendedor = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox();
        fechaInicio = new com.toedter.calendar.JDateChooser();
        duracion = new javax.swing.JTextField();
        refreshBtn = new javax.swing.JButton();

        setClosable(true);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Polizas");

        jLabel3.setText("Patente");

        jLabel4.setText("Fecha inicio");

        jLabel5.setText("Duración");

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

        searchData.setBackground(new java.awt.Color(204, 204, 204));
        searchData.setText("Buscar");
        searchData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchDataActionPerformed(evt);
            }
        });

        patente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Tipo");

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

        limpiarBtn.setBackground(new java.awt.Color(255, 255, 255));
        limpiarBtn.setForeground(new java.awt.Color(102, 102, 255));
        limpiarBtn.setText("Limpiar");
        limpiarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarBtnActionPerformed(evt);
            }
        });

        vendedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Vendedor");

        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        refreshBtn.setText("Refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshBtn)
                        .addGap(18, 18, 18)
                        .addComponent(searchData, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(guardarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(modificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(patente, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(vendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(143, 143, 143)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(156, 156, 156))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(duracion)
                                        .addGap(37, 37, 37)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tipo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(eliminarBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                                .addGap(42, 42, 42)
                                .addComponent(limpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchData)
                    .addComponent(refreshBtn))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(duracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardarBtn)
                    .addComponent(modificarBtn)
                    .addComponent(eliminarBtn)
                    .addComponent(limpiarBtn))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnActionPerformed
        if(estanLlenos()){
             guardarRegistro();
         } else {
             JOptionPane.showMessageDialog(null,
                "Debe rellenar todos los campos",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
             );
         }
    }//GEN-LAST:event_guardarBtnActionPerformed

    private void modificarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarBtnActionPerformed
         if(clickeado == true){
            modificarRegistro();
         } else {
             JOptionPane.showMessageDialog(null,
                "Debe seleccionar el registro a modificar",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
             );
         }
        
    }//GEN-LAST:event_modificarBtnActionPerformed

    private void eliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnActionPerformed
        eliminarRegistro();
    }//GEN-LAST:event_eliminarBtnActionPerformed

    private void limpiarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarBtnActionPerformed
        limpiarCampos(true);
    }//GEN-LAST:event_limpiarBtnActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            try {
                filaSeleccionada = indice;
                nroSeleccionado = modelo.getValueAt(indice, 0).toString();
                String pat = modelo.getValueAt(indice, 1).toString();
                String vendor= modelo.getValueAt(indice, 2).toString();
                String fecInicio = modelo.getValueAt(indice, 3).toString();
                String dur= modelo.getValueAt(indice, 4).toString();
                String tipPoliza= modelo.getValueAt(indice, 5).toString();
                
                duracion.setText(dur);
                
                java.util.Date fec1;                          
                fec1 = new SimpleDateFormat("yyyy-MM-dd").parse(fecInicio);         
                fechaInicio.setDate(fec1);   
                
                int items = patente.getItemCount();
                for (int i = 0; i < items; i++) {
                    String pat_data = patente.getItemAt(i).toString();
                    if(pat_data.equals(pat)) {
                        patente.setSelectedIndex(i);
                    }
                }
                items = vendedor.getItemCount();
                for (int i = 0; i < items; i++) {
                    String pat_data = vendedor.getItemAt(i).toString().split("-")[0];
                    if(pat_data.equals(vendor)) {
                        vendedor.setSelectedIndex(i);
                    }
                }
                items = tipo.getItemCount();
                for (int i = 0; i < items; i++) {
                    String pat_data = tipo.getItemAt(i).toString();
                    if(pat_data.equals(tipPoliza)) {
                        tipo.setSelectedIndex(i);
                    }
                }
                
                clickeado = true;
            } catch (ParseException ex) {
                Logger.getLogger(Polizas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void searchDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchDataActionPerformed
        buscarRegistro();
    }//GEN-LAST:event_searchDataActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        rs = con.getData(sql);
        llenarTabla(rs);
        limpiarCampos(true);
    }//GEN-LAST:event_refreshBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField duracion;
    private javax.swing.JButton eliminarBtn;
    private com.toedter.calendar.JDateChooser fechaInicio;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JComboBox patente;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton searchData;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField textoBuscar;
    private javax.swing.JComboBox tipo;
    private javax.swing.JComboBox vendedor;
    // End of variables declaration//GEN-END:variables
}
