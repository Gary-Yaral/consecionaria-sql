
package systemaconcesionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TipoPolizas extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String tipoSeleccionado;
    boolean clickeado = false;
    static String sql = "select * from tipo_polizas";
    public TipoPolizas() {
        initComponents();  
        rs = con.getData(sql);
        if(modelo.getColumnCount() < 1) {
            agregarCabeceras();
        }
       
        llenarTabla(rs);
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("Polizas");
    }  
      
    public void llenarTabla(ResultSet rs) {
        try {
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[1];
                fila[0] = rs.getString("nombre");
                modelo.addRow(fila);
            }  
            tabla.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(TipoPolizas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarRegistro() {
        String tipoPoliza = tipo.getText();
        
        if(estanLlenos()){
            if(exists(tipoPoliza)) {
                JOptionPane.showMessageDialog(null,
                "Ya existe un tipo de poliza con ese nombre",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
                return;
            }
            
            String sql1 = "INSERT INTO tipo_polizas(nombre) VALUES('"+tipoPoliza+"')";
            boolean wasInserted = con.setData(sql1);
            
            if(wasInserted) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                    "Tipo de poliza agregado correctamente",
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
    
    public boolean exists(String tipo_poliza) {
        String query = "select * from tipo_polizas where nombre='"+tipo_poliza+"'";
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
           String sql1 = "delete from tipo_polizas where nombre='"+tipoSeleccionado+"'";
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
        String tipo_poliza = textoBuscar.getText();
        if(!tipo_poliza.isEmpty()) {
            String sql1 = "SELECT * FROM tipo_polizas WHERE nombre='"+tipo_poliza+"'";
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
        String tipoPoliza = tipo.getText();
        if( tipoPoliza.isEmpty() ){
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
        String tipoPoliza = tipo.getText();
        if(estanLlenos()) {     
            if(tipoSeleccionado.equals(tipoPoliza) == false) {
                if(exists(tipoPoliza)) {
                    JOptionPane.showMessageDialog(null,
                    "Ya existe un tipo de poliza con ese nombre",
                    "Atención",
                    JOptionPane.ERROR_MESSAGE
                 );
                    return;
                }
            }
 
            String sql1 = "update tipo_polizas set nombre='"+tipoPoliza+"' where nombre ='"+tipoSeleccionado+"'";
            boolean seGuardo = con.setData(sql1);
            
            if(seGuardo) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                    "Tipo de poliza modificada correctamente",
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
        tipo.setText("");
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
        tipo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
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
        jLabel2.setText("Tipo de polizas");

        tipo.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel3.setText("Tipo");

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
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(docSel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 56, Short.MAX_VALUE)
                                .addComponent(refrescarBtn)
                                .addGap(18, 18, 18)
                                .addComponent(buscarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3)
                                    .addComponent(tipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(guardarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(modificarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(eliminarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(limpiarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(docSel))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarBtn)
                    .addComponent(refrescarBtn))
                .addGap(19, 19, 19)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(guardarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(limpiarBtn))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            filaSeleccionada = indice;
            tipoSeleccionado = modelo.getValueAt(indice, 0).toString();
            tipo.setText(tipoSeleccionado);
            clickeado = true;    
        }
        
    }//GEN-LAST:event_tablaMouseClicked

    
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

    private void limpiarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarBtnActionPerformed
        limpiarCampos(true);
    }//GEN-LAST:event_limpiarBtnActionPerformed

    private void eliminarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBtnMouseClicked
        eliminarRegistro();
        limpiarCampos(true);
    }//GEN-LAST:event_eliminarBtnMouseClicked

    private void modificarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificarBtnMouseClicked
        if(clickeado = false) return;
        modificarRegistro();
    }//GEN-LAST:event_modificarBtnMouseClicked

    private void guardarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarBtnMouseClicked
        agregarRegistro();
    }//GEN-LAST:event_guardarBtnMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscarBtn;
    private javax.swing.JLabel docSel;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JButton refrescarBtn;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField textoBuscar;
    private javax.swing.JTextField tipo;
    // End of variables declaration//GEN-END:variables
}
