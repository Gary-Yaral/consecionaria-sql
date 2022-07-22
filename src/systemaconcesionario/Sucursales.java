
package systemaconcesionario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Sucursales extends javax.swing.JInternalFrame {
    static DefaultTableModel modelo = new DefaultTableModel();
    static ConnectionSQL con = new ConnectionSQL();
    static PreparedStatement ps;
    static ResultSet rs;
    static int filaSeleccionada;
    static String nroSeleccionado;
    boolean clickeado = false;
    static String sql = "select * from sucursal";
    
    public Sucursales() {
        initComponents();
        
        rs = con.getData(sql);
        if(modelo.getColumnCount() < 1) {
            agregarCabeceras();
        }
       
        llenarTabla(rs);
    }
    
    public void agregarCabeceras() {
        modelo.addColumn("N°");
        modelo.addColumn("Nombre");
        modelo.addColumn("Dirección");
        modelo.addColumn("Teléfono");
    } 
        
    public void llenarTabla(ResultSet rs) {
        try {
            limpiarTabla();
            while(rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("nro");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("domicilio");
                fila[3] = rs.getString("telefono");
                modelo.addRow(fila);
            }  
            tabla.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(TipoPolizas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
    
    public void agregarRegistro() {
        String nom = nombre.getText();
        String dom = domicilio.getText();
        String tel = telefono.getText();
        String num = numero.getText();
        if(!isNumeric(num)) {
            JOptionPane.showMessageDialog(null,
                "El numero de sucursal debe ser un numero",
                "Atención",
                JOptionPane.ERROR_MESSAGE
            );
            
            return;
        }
        
        if(estanLlenos()){
            if(exists(num)) {
                JOptionPane.showMessageDialog(null,
                "Ya existe una sucursal con ese numero",
                "Atención",
                JOptionPane.ERROR_MESSAGE
             );
                return;
            }
            
            String sql1 = "INSERT INTO sucursal(nro, nombre, domicilio, telefono) VALUES('"+num+"','"+nom+"','"+dom+"','"+tel+"')";
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
    
    public boolean exists(String nro) {
        String query = "select * from sucursal where nro='"+nro+"'";
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
            try {
                String buscar = "SELECT*FROM sucursal INNER JOIN vendedor ON vendedor.`nro_sucursal` = sucursal.`id` WHERE sucursal.`nro`='"+nroSeleccionado+"'";
                rs = con.getData(buscar);
               
                boolean tieneDependencia = false;
                while(rs.next()) {
                    tieneDependencia = true;
                }
                
                if(tieneDependencia) {
                    JOptionPane.showMessageDialog(null,
                            "No se puede eliminar, tienes vendedores vinculados a esta sucursal",
                            "Atención",
                            JOptionPane.WARNING_MESSAGE
                    );
                    tabla.clearSelection();
                    return;
                }
                
                String sql1 = "delete from sucursal where nro='"+nroSeleccionado+"'";
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
                Logger.getLogger(Sucursales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
       clickeado = false;

    }
    
    public void buscarRegistro() {
        String suc = textoBuscar.getText();
        if(!suc.isEmpty()) {
            String sql1 = "SELECT * FROM sucursal WHERE nro='"+suc+"' OR nombre='"+suc+"' OR domicilio='"+suc+"' OR telefono='"+suc+"'";
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
        String num = numero.getText();
        
        if( nom.isEmpty() &&  dom.isEmpty() && num.isEmpty() && tel.isEmpty()){
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
        String dom = domicilio.getText();
        String tel = telefono.getText();
        String num = numero.getText();
        
        if(estanLlenos()) {     
            if(nroSeleccionado.equals(num) == false) {
                if(exists(num)) {
                    JOptionPane.showMessageDialog(null,
                    "Ya existe una sucursal con ese número",
                    "Atención",
                    JOptionPane.ERROR_MESSAGE
                 );
                    return;
                }
            }
 
            String sql1 = "update sucursal set nro='"+num+"', nombre='"+nom+"', domicilio='"+dom+"',telefono='"+tel+"' where nro = '"+nroSeleccionado+"'";
            boolean seGuardo = con.setData(sql1);
            
            if(seGuardo) {
                rs = con.getData(sql);
                llenarTabla(rs);
                JOptionPane.showMessageDialog(null,
                    "Sucursal modificada correctamente",
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
        domicilio.setText("");
        telefono.setText("");
        numero.setText("");
        clickeado = false;
        tabla.clearSelection();
        if (esBuscar) {        
            textoBuscar.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        domicilio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        agregarBtn = new javax.swing.JButton();
        modificarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();
        textoBuscar = new javax.swing.JTextField();
        buscarBtn = new javax.swing.JButton();
        limpiarBtn = new javax.swing.JButton();
        numero = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        refresh = new javax.swing.JButton();

        setClosable(true);
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Sucursales");

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

        agregarBtn.setBackground(new java.awt.Color(51, 51, 255));
        agregarBtn.setForeground(new java.awt.Color(255, 255, 255));
        agregarBtn.setText("Guardar");
        agregarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarBtnMouseClicked(evt);
            }
        });
        agregarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBtnActionPerformed(evt);
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

        jLabel1.setText("Número");

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
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(domicilio, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(telefono, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(agregarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(modificarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(eliminarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(limpiarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numero)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(refresh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buscarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(50, Short.MAX_VALUE))
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
                    .addComponent(jLabel1)
                    .addComponent(refresh))
                .addGap(9, 9, 9)
                .addComponent(jLabel4)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                        .addComponent(agregarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(limpiarBtn)
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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
                .addGap(0, 30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int indice = tabla.getSelectedRow();
        if(indice >= 0) {        
            filaSeleccionada = indice;
            nroSeleccionado = modelo.getValueAt(indice, 0).toString();
            String nom = modelo.getValueAt(indice, 1).toString();
            String dom = modelo.getValueAt(indice, 2).toString();
            String tel= modelo.getValueAt(indice, 3).toString();

            numero.setText(nroSeleccionado);
            nombre.setText(nom);
            domicilio.setText(dom);
            telefono.setText(tel);

            clickeado = true;    
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void agregarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarBtnMouseClicked
        agregarRegistro();
    }//GEN-LAST:event_agregarBtnMouseClicked

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

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        rs = con.getData(sql);
        llenarTabla(rs);
        clickeado = false;
        limpiarCampos(true);
    }//GEN-LAST:event_refreshActionPerformed

    private void agregarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBtnActionPerformed
        agregarRegistro();
    }//GEN-LAST:event_agregarBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarBtn;
    private javax.swing.JButton buscarBtn;
    private javax.swing.JTextField domicilio;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarBtn;
    private javax.swing.JButton modificarBtn;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField numero;
    private javax.swing.JButton refresh;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField telefono;
    private javax.swing.JTextField textoBuscar;
    // End of variables declaration//GEN-END:variables
}
