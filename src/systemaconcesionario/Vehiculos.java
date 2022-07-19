
package systemaconcesionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Vehiculos extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String patenteSeleccionada;
    boolean clickeado = false;
    static String sql = "select * from vehiculos";
    
    public Vehiculos() {
        initComponents();
        
        rs = con.getData(sql);
        if(modelo.getColumnCount() < 1) {
            agregarCabeceras();
        }
        llenarTabla(rs);     
        String query = "select * from clientes";  
        rs = con.getData(query);
        documento.removeAllItems();
        documento.addItem("Selecciona el dueño");
        try {
            while(rs.next()) {
                String cli = rs.getString("documento");
                String nombre = rs.getString("nombre");
                documento.addItem(cli+" - "+nombre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Vehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean estanLlenos () {
        int index = documento.getSelectedIndex();
        if (index == 0) return false;
        if(  
              patente.getText().isEmpty() 
             && marca.getText().isEmpty()
             && modeloCarro.getText().isEmpty()
             && serialMotor.getText().isEmpty()
        ){
            return false;
        }
        
        return true;
                
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("Patente");
        modelo.addColumn("N° Motor");
        modelo.addColumn("Marca");
        modelo.addColumn("Modelo");
        modelo.addColumn("Dueño");
    }  
      
    public void llenarTabla(ResultSet rs) {
        try {
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("patente");
                fila[1] = rs.getString("nro_motor");
                fila[2] = rs.getString("marca");
                fila[3] = rs.getString("modelo");
                fila[4] = rs.getString("documento_dueno");
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

    public void agregarVehiculo() {
        String doc = documento.getSelectedItem().toString().split(" - ")[0];
        String pat = patente.getText();
        String mar= marca.getText();
        String mod = modeloCarro.getText();
        String motor = serialMotor.getText();

        if(exists(pat)) {
            JOptionPane.showMessageDialog(null,
            "Ya existe un vehiculo con esa patente",
            "Atención",
            JOptionPane.ERROR_MESSAGE
         );
         return;
        
        }
            
        String sql1 = "INSERT INTO vehiculos(patente, nro_motor, marca, modelo, documento_dueno) VALUES('"+pat+"','"+motor+"','"+mar+"','"+mod+"','"+doc+"')";
        boolean seGuardo = con.setData(sql1);

        if(seGuardo) {
            rs = con.getData(sql);
            llenarTabla(rs);
            JOptionPane.showMessageDialog(null,
                "Vehiculo guardado correctamente",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            
        }
    }
     
    public void modificarRegistro() {
        String doc = documento.getSelectedItem().toString().split(" - ")[0];
        String pat = patente.getText();
        String motor= serialMotor.getText();
        String mar = marca.getText();
        String mod = marca.getText();
     
        if(patenteSeleccionada.equals(pat) == false) {
            if(exists(pat)) {
                JOptionPane.showMessageDialog(null,
                "Ya existe un vehiculo con esa patente",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
                return;
            }
        }
 
        String sql1 = "update vehiculos set patente='"+pat+"', nro_motor='"+motor+"',marca='"+mar+"',modelo='"+mod+"', documento_dueno='"+doc+"' where patente ='"+patenteSeleccionada+"'";
        boolean fueModificada = con.setData(sql1);

        if(fueModificada) {
            rs = con.getData(sql);
            llenarTabla(rs);
            JOptionPane.showMessageDialog(null,
                "Vehículo ha sido modificado correctamente",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE
            );
            limpiarCampos(false);
        }
    }

    public boolean exists(String pat) {
        String query = "select * from vehiculos where patente='"+pat+"'";
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
        serialMotor.setText("");
        marca.setText("");
        modeloCarro.setText("");
        patente.setText("");
        documento.setSelectedIndex(0);
        tabla.clearSelection();
        clickeado = false;
        if(esTodo) {
            textoBuscar.setText("");
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        serialMotor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        modeloCarro = new javax.swing.JTextField();
        guardaBtn = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();
        textoBuscar = new javax.swing.JTextField();
        buscarBtn = new javax.swing.JButton();
        documento = new javax.swing.JComboBox();
        patente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        marca = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        limpiarBtn = new javax.swing.JButton();
        refresh = new javax.swing.JButton();

        setClosable(true);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Vehículos");

        jLabel3.setText("Dueño");

        jLabel4.setText("Patente");

        serialMotor.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel5.setText("N° de Motor");

        jLabel6.setText("Modelo");

        modeloCarro.setMinimumSize(new java.awt.Dimension(6, 23));

        guardaBtn.setBackground(new java.awt.Color(51, 51, 255));
        guardaBtn.setForeground(new java.awt.Color(255, 255, 255));
        guardaBtn.setText("Guardar");
        guardaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guardaBtnMouseClicked(evt);
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

        documento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        patente.setMinimumSize(new java.awt.Dimension(6, 23));

        jLabel7.setText("Marca");

        marca.setMinimumSize(new java.awt.Dimension(6, 23));

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
        limpiarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limpiarBtnMouseClicked(evt);
            }
        });

        refresh.setText("Refresh");
        refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(guardaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                .addComponent(modificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(patente, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel3)))
                                        .addGap(94, 94, 94)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(serialMotor, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(jLabel5)))
                            .addComponent(documento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7)
                                    .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(eliminarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6)
                                    .addComponent(modeloCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(limpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refresh)
                                .addGap(18, 18, 18)
                                .addComponent(buscarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(57, 57, 57))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarBtn)
                    .addComponent(documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refresh))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serialMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(patente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(1, 1, 1)
                        .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(1, 1, 1)
                        .addComponent(modeloCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardaBtn)
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

    private void guardaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardaBtnMouseClicked
        if(estanLlenos()){
            agregarVehiculo();
        } else {
            JOptionPane.showMessageDialog(null,
               "Debe rellenar todos los campos o seleccionar dueño",
               "Atención",
               JOptionPane.INFORMATION_MESSAGE
            );
        }
        
    }//GEN-LAST:event_guardaBtnMouseClicked

    private void limpiarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_limpiarBtnMouseClicked
        limpiarCampos(true);
    }//GEN-LAST:event_limpiarBtnMouseClicked

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            filaSeleccionada = indice;
            patenteSeleccionada = modelo.getValueAt(indice, 0).toString();
            String motor = modelo.getValueAt(indice, 1).toString();
            String mar = modelo.getValueAt(indice, 2).toString();
            String mod= modelo.getValueAt(indice, 3).toString();
            String doc= modelo.getValueAt(indice, 4).toString();
     
            patente.setText(patenteSeleccionada);
            serialMotor.setText(motor);
            marca.setText(mar);
            modeloCarro.setText(mod);
            
            int items = documento.getItemCount();
            for (int i = 0; i < items; i++) {     
                String doc_data = documento.getItemAt(i).toString().split("-")[0];
                if(doc_data.replace(" ", "").equals(doc)) {
                    documento.setSelectedIndex(i);
                }
            }

            clickeado = true;    
        }
    }//GEN-LAST:event_tablaMouseClicked

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
        
        String query = "SELECT * FROM vehiculos WHERE patente='"+buscar+"' OR nro_motor='"+buscar+"' OR marca='"+buscar+"' OR documento_dueno='"+buscar+"'";
        rs = con.getData(query);
        llenarTabla(rs);
        
    }
    
    public void eliminarRegistro() {
       if(filaSeleccionada >= 0 && clickeado == true) {
           try {
               String query = "SELECT*FROM vehiculos INNER JOIN polizas ON vehiculos.`patente` = polizas.`patente` WHERE vehiculos.`patente` = '"+patenteSeleccionada+"'";
               rs = con.getData(query);
               boolean tieneDependencia = false;
               while(rs.next()) {
                   tieneDependencia = true;
               }
               
               if(tieneDependencia) {
                   JOptionPane.showMessageDialog(null,
                           "No se puede eliminar, tienes polizas o accidentes vinculados con este vehiculo",
                           "Atención",
                           JOptionPane.WARNING_MESSAGE
                   );
                   tabla.clearSelection();
                   return;
               }
               
               String sql1 = "delete from vehiculos where patente='"+patenteSeleccionada+"'";
               boolean fueEliminado = con.setData(sql1);
               
                if(fueEliminado) {
                    JOptionPane.showMessageDialog(null,
                        "Vehículo eliminado correctamente",
                        "Atención",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    modelo.removeRow(filaSeleccionada);
                }
           } catch (SQLException ex) {
               Logger.getLogger(Vehiculos.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       
       clickeado = false;

    
    }
    
    private void eliminarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBtnMouseClicked
        eliminarRegistro();
        limpiarCampos(true);
        
    }//GEN-LAST:event_eliminarBtnMouseClicked

    private void modificarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificarBtnMouseClicked
        if(estanLlenos()){
            modificarRegistro();
        } else {
            JOptionPane.showMessageDialog(null,
               "Debe rellenar todos los campos o seleccionar dueño",
               "Atención",
               JOptionPane.INFORMATION_MESSAGE
            );
        }
        
    }//GEN-LAST:event_modificarBtnMouseClicked

    private void buscarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarBtnMouseClicked
        buscarRegistro();
        limpiarCampos(false);
    }//GEN-LAST:event_buscarBtnMouseClicked

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseClicked
       rs = con.getData(sql);
       llenarTabla(rs);
       limpiarCampos(true);
    }//GEN-LAST:event_refreshMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscarBtn;
    private javax.swing.JComboBox documento;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JButton guardaBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JTextField marca;
    private javax.swing.JTextField modeloCarro;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField patente;
    private javax.swing.JButton refresh;
    private javax.swing.JTextField serialMotor;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField textoBuscar;
    // End of variables declaration//GEN-END:variables
}
