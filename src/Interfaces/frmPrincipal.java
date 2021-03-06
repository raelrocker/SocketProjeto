package Interfaces;

import Arquivos.Arquivo;
import Classes.ListaArquivosModel;
import Classes.Util;
import Cliente.ClienteSocket;
import Servidor.ServidorSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class frmPrincipal extends javax.swing.JFrame {

    private ServidorSocket servidorSocket;    
    private Thread servidor;
    private ListaArquivosModel model;
    private ListaArquivosModel modelServidor;
    private Socket socket = null;
    private String usuario = "";
    private String senha = "";
    private String IpServidor = "";
    private int portaServidor = 0;
    private boolean logado = false;
    
    public frmPrincipal() {
        initComponents();
        listArquivosLocais.setModel(new ListaArquivosModel());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Servidor = new javax.swing.JTabbedPane();
        panelCliente = new javax.swing.JPanel();
        panelConfigCliente = new javax.swing.JPanel();
        lblIpServidor = new javax.swing.JLabel();
        txtIpServidor = new javax.swing.JTextField();
        lblIpServidor1 = new javax.swing.JLabel();
        txtPorta = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        txtSenha = new javax.swing.JTextField();
        btnConectar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnSolicitarCaminhoLocal = new javax.swing.JButton();
        btnListarArquivos = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listArquivosLocais = new javax.swing.JList<>();
        txtCaminhoLocal = new javax.swing.JTextField();
        btnEnviarServidor = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnSolicitarCaminhoServidor = new javax.swing.JButton();
        txtCaminhoServidor = new javax.swing.JTextField();
        btnListarArquivosServidor = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listArquivosServidor = new javax.swing.JList<>();
        btnBaixarServidor = new javax.swing.JButton();
        panelServidor = new javax.swing.JPanel();
        panelConfigServidor = new javax.swing.JPanel();
        lblServidorPorta = new javax.swing.JLabel();
        txtServidorPorta = new javax.swing.JTextField();
        btnServidorIniciar = new javax.swing.JButton();
        btnServidorParar = new javax.swing.JButton();
        panelServidorLog = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtServidorLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelConfigCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar ao servidor"));

        lblIpServidor.setText("IP:");

        lblIpServidor1.setText("Porta:");

        lblUsuario.setText("Usuário:");

        lblSenha.setText("Senha");

        btnConectar.setText("Conectar");
        btnConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConectarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelConfigClienteLayout = new javax.swing.GroupLayout(panelConfigCliente);
        panelConfigCliente.setLayout(panelConfigClienteLayout);
        panelConfigClienteLayout.setHorizontalGroup(
            panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelConfigClienteLayout.createSequentialGroup()
                        .addComponent(lblIpServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIpServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelConfigClienteLayout.createSequentialGroup()
                        .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblIpServidor1, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(lblSenha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConfigClienteLayout.createSequentialGroup()
                        .addComponent(txtPorta, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelConfigClienteLayout.setVerticalGroup(
            panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigClienteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsuario)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSenha)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelConfigClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIpServidor)
                    .addComponent(txtIpServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIpServidor1)
                    .addComponent(txtPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConectar))
                .addGap(47, 47, 47))
        );

        btnSolicitarCaminhoLocal.setText("Solicitar caminho local");
        btnSolicitarCaminhoLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolicitarCaminhoLocalActionPerformed(evt);
            }
        });

        btnListarArquivos.setText("Listar arquivos local");
        btnListarArquivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarArquivosActionPerformed(evt);
            }
        });

        listArquivosLocais.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listArquivosLocais.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listArquivosLocaisValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listArquivosLocais);

        txtCaminhoLocal.setEditable(false);

        btnEnviarServidor.setText("Enviar para Servidor");
        btnEnviarServidor.setEnabled(false);
        btnEnviarServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarServidorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnListarArquivos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSolicitarCaminhoLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnviarServidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(txtCaminhoLocal))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSolicitarCaminhoLocal)
                    .addComponent(txtCaminhoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnListarArquivos)
                        .addGap(31, 31, 31)
                        .addComponent(btnEnviarServidor)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ações Local", jPanel1);

        btnSolicitarCaminhoServidor.setText("Solicitar caminho servidor");
        btnSolicitarCaminhoServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolicitarCaminhoServidorActionPerformed(evt);
            }
        });

        txtCaminhoServidor.setEditable(false);

        btnListarArquivosServidor.setText("Listar arquivos servidor");
        btnListarArquivosServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarArquivosServidorActionPerformed(evt);
            }
        });

        listArquivosServidor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listArquivosServidor.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listArquivosServidorValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listArquivosServidor);

        btnBaixarServidor.setText("Baixar para local");
        btnBaixarServidor.setEnabled(false);
        btnBaixarServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaixarServidorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnListarArquivosServidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSolicitarCaminhoServidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBaixarServidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(txtCaminhoServidor))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSolicitarCaminhoServidor)
                    .addComponent(txtCaminhoServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnListarArquivosServidor)
                        .addGap(31, 31, 31)
                        .addComponent(btnBaixarServidor)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ações Servidor", jPanel2);

        javax.swing.GroupLayout panelClienteLayout = new javax.swing.GroupLayout(panelCliente);
        panelCliente.setLayout(panelClienteLayout);
        panelClienteLayout.setHorizontalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelConfigCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        panelClienteLayout.setVerticalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addComponent(panelConfigCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        Servidor.addTab("Cliente", panelCliente);

        panelConfigServidor.setBorder(javax.swing.BorderFactory.createTitledBorder("Configurar Servidor"));

        lblServidorPorta.setText("Porta:");

        btnServidorIniciar.setText("Iniciar Servidor");
        btnServidorIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServidorIniciarActionPerformed(evt);
            }
        });

        btnServidorParar.setText("Parar Servidor");
        btnServidorParar.setEnabled(false);
        btnServidorParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServidorPararActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelConfigServidorLayout = new javax.swing.GroupLayout(panelConfigServidor);
        panelConfigServidor.setLayout(panelConfigServidorLayout);
        panelConfigServidorLayout.setHorizontalGroup(
            panelConfigServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigServidorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblServidorPorta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtServidorPorta, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnServidorIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnServidorParar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelConfigServidorLayout.setVerticalGroup(
            panelConfigServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigServidorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConfigServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblServidorPorta)
                    .addComponent(txtServidorPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnServidorIniciar)
                    .addComponent(btnServidorParar))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelServidorLog.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));

        txtServidorLog.setColumns(20);
        txtServidorLog.setRows(5);
        jScrollPane1.setViewportView(txtServidorLog);

        javax.swing.GroupLayout panelServidorLogLayout = new javax.swing.GroupLayout(panelServidorLog);
        panelServidorLog.setLayout(panelServidorLogLayout);
        panelServidorLogLayout.setHorizontalGroup(
            panelServidorLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        panelServidorLogLayout.setVerticalGroup(
            panelServidorLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelServidorLayout = new javax.swing.GroupLayout(panelServidor);
        panelServidor.setLayout(panelServidorLayout);
        panelServidorLayout.setHorizontalGroup(
            panelServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelConfigServidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelServidorLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelServidorLayout.setVerticalGroup(
            panelServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServidorLayout.createSequentialGroup()
                .addComponent(panelConfigServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelServidorLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Servidor.addTab("Servidor", panelServidor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Servidor)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Servidor)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Inicia o servidor.
     * @param evt 
     */
    private void btnServidorIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServidorIniciarActionPerformed
        
        try {
            if (Integer.parseInt(txtServidorPorta.getText()) < 1024) {
                JOptionPane.showMessageDialog(panelCliente, "A porta deve ser maior ou igual a 1024.", "AVISO", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panelCliente, "Porta inválida.", "AVISO", JOptionPane.ERROR_MESSAGE);
            txtServidorPorta.setText("");
            txtServidorPorta.requestFocus();            
            return;
        }
        
        
        this.servidorSocket = new ServidorSocket(Integer.parseInt(txtServidorPorta.getText()), txtServidorLog);
        this.servidor = new Thread(this.servidorSocket);
        servidor.start();
        this.IniciarPararServidor(true);
    }//GEN-LAST:event_btnServidorIniciarActionPerformed

    /**
     * Para o servidor socket.
     * @param evt 
     */
    private void btnServidorPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServidorPararActionPerformed
        try {
            this.servidorSocket.PararServidor();
        } catch (IOException ex) {
            Logger.getLogger(frmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        IniciarPararServidor(false);
    }//GEN-LAST:event_btnServidorPararActionPerformed

    /**
     * Solicita o caminho local
     * @param evt 
     */
    private void btnSolicitarCaminhoLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolicitarCaminhoLocalActionPerformed
        try {
            txtCaminhoLocal.setText("");
            String caminho = Util.GetCaminhoAtual();
            txtCaminhoLocal.setText(caminho);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelCliente, "Erro: " + ex.getMessage());
        }
        
    }//GEN-LAST:event_btnSolicitarCaminhoLocalActionPerformed

    /**
     * Lista arquivo locais
     * @param evt 
     */
    private void btnListarArquivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarArquivosActionPerformed
        try {
            ArrayList<Arquivo> lista = Util.GetListaArquivos();
            model = new ListaArquivosModel(lista);
            listArquivosLocais.setModel(model);
            listArquivosLocais.updateUI();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelCliente, "Erro: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnListarArquivosActionPerformed

    /**
     * Habilita botão para envio do arquivo para o servidor
     * @param evt 
     */
    private void listArquivosLocaisValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listArquivosLocaisValueChanged
        if (listArquivosLocais.getSelectedIndex() >= 0) {
            btnEnviarServidor.setEnabled(true);
        } else {
            btnEnviarServidor.setEnabled(false);
        }
    }//GEN-LAST:event_listArquivosLocaisValueChanged

    /**
     * Envia arquivo para o servidors
     * @param evt 
     */
    private void btnEnviarServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarServidorActionPerformed
        this.EnviarArquivoServidor();
    }//GEN-LAST:event_btnEnviarServidorActionPerformed

    /**
     * Solicita o caminho do servidor
     * @param evt 
     */
    private void btnSolicitarCaminhoServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolicitarCaminhoServidorActionPerformed
       try {
            this.VerificarLogado();
            txtCaminhoServidor.setText("");
            this.ConectarSocket();
            ClienteSocket cliente = new ClienteSocket(this.socket);
            txtCaminhoServidor.setText(cliente.GetCaminhoServidor());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelCliente, "Erro: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSolicitarCaminhoServidorActionPerformed

    /**
     * Lista arquivos do servidor
     * @param evt 
     */
    private void btnListarArquivosServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarArquivosServidorActionPerformed
        try {
            this.VerificarLogado();
            this.ConectarSocket();
            ClienteSocket cliente = new ClienteSocket(this.socket);
            ArrayList<Arquivo> lista = cliente.GetListaArquivos();
            modelServidor = new ListaArquivosModel(lista);
            listArquivosServidor.setModel(modelServidor);
            listArquivosServidor.updateUI();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelCliente, "Erro: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnListarArquivosServidorActionPerformed

    /**
     * Habilita botão para download do arquivo para o local
     * @param evt 
     */
    private void listArquivosServidorValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listArquivosServidorValueChanged
        if (listArquivosServidor.getSelectedIndex() >= 0) {
            btnBaixarServidor.setEnabled(true);
        } else {
            btnBaixarServidor.setEnabled(false);
        }
    }//GEN-LAST:event_listArquivosServidorValueChanged

    /**
     * Baixa o arquivo do servidor
     * @param evt 
     */
    private void btnBaixarServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaixarServidorActionPerformed
        try {
            this.VerificarLogado();
            Arquivo arquivo = modelServidor.getElementAt(listArquivosServidor.getSelectedIndex());
            if (arquivo == null) {
                return;
            }
            this.ConectarSocket();
            ClienteSocket clienteSocket = new ClienteSocket(this.socket);
            if (clienteSocket.BaixarArquivoServidor(arquivo)) {
                JOptionPane.showMessageDialog(null, "Arquivo baixado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao baixar arquivo.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnBaixarServidorActionPerformed

    /**
     * Conecta ao servidor
     * @throws Exception 
     */
    private void ConectarSocket() throws Exception {
        if (usuario.isEmpty()      ||
            senha.isEmpty()        ||
            IpServidor.isEmpty()   ||
            portaServidor == 0) {
            throw new Exception("Informe os dados para conectar ao servidor");
        }
        try {
            this.socket = new Socket(this.IpServidor, this.portaServidor);
        } catch (UnknownHostException ex) {
            throw new Exception("Servidor não encontrado");
        }
    }
    
    /**
     * Verifica se o cliente está logado
     * @throws Exception 
     */
    private void VerificarLogado() throws Exception {
        if (!this.logado) {
            throw  new Exception("É preciso se conectar ao servidor");
        }
    }
    
    /**
     * Faz o login do cliente no servidor
     * @param evt 
     */
    private void btnConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConectarActionPerformed
        try {
            
            if (this.logado) {
                this.ConectarSocket();
                ClienteSocket cliente = new ClienteSocket(this.socket);
                if (cliente.Logoff()) {
                    JOptionPane.showMessageDialog(null, "Cliente desconectado no servidor" );
                    this.logado = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao desconectar no servidor" );
                }
                this.usuario = "";
                this.senha = "";
                this.IpServidor = "";
                this.portaServidor = 0;
                this.socket = null;
                btnConectar.setText("Conectar");
            } else {
                this.usuario = txtUsuario.getText();
                this.senha = txtSenha.getText();
                this.IpServidor = txtIpServidor.getText();
                this.portaServidor = Integer.parseInt(txtPorta.getText());            
                this.ConectarSocket();
                ClienteSocket cliente = new ClienteSocket(this.socket);
                if (cliente.Login(this.usuario, this.senha)) {
                    JOptionPane.showMessageDialog(null, "Cliente conectado no servidor" );
                    this.logado = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar no servidor" );
                }
                btnConectar.setText("Desconectar");
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Erro: a porta do servidor é inválida" );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnConectarActionPerformed
    
    /**
     * Envia arquivo para o servidor
     */
    private void EnviarArquivoServidor() {
        try {
            Arquivo arquivo = model.getElementAt(listArquivosLocais.getSelectedIndex());
            if (arquivo == null) {
                return;
            }
            this.ConectarSocket();
            ClienteSocket clienteSocket = new ClienteSocket(this.socket);
            if (clienteSocket.EnviarArquivoServidor(arquivo)) {
                JOptionPane.showMessageDialog(null, "Arquivo enviado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao enviar arquivo.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
    }
    
    /**
     * Ativa/Desativa os botões de configuração do servidor.
     * @param iniciar 
     */
    private void IniciarPararServidor(boolean iniciar) {
        if (iniciar) {
            btnServidorIniciar.setEnabled(false);
            btnServidorParar.setEnabled(true);
        } else {
            btnServidorIniciar.setEnabled(true);
            btnServidorParar.setEnabled(false);
        }
    }
    
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
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmPrincipal().setVisible(true);
            }
        });
    }
    
    /**
     * Adiciona mensagem ao log do servidor
     * @param mensagem 
     */
    public void AdicionarMensagem(String mensagem) {
        this.txtServidorLog.setText(txtServidorLog.getText() + "\n" + mensagem);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Servidor;
    private javax.swing.JButton btnBaixarServidor;
    private javax.swing.JButton btnConectar;
    private javax.swing.JButton btnEnviarServidor;
    private javax.swing.JButton btnListarArquivos;
    private javax.swing.JButton btnListarArquivosServidor;
    private javax.swing.JButton btnServidorIniciar;
    private javax.swing.JButton btnServidorParar;
    private javax.swing.JButton btnSolicitarCaminhoLocal;
    private javax.swing.JButton btnSolicitarCaminhoServidor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblIpServidor;
    private javax.swing.JLabel lblIpServidor1;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblServidorPorta;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JList<String> listArquivosLocais;
    private javax.swing.JList<String> listArquivosServidor;
    private javax.swing.JPanel panelCliente;
    private javax.swing.JPanel panelConfigCliente;
    private javax.swing.JPanel panelConfigServidor;
    private javax.swing.JPanel panelServidor;
    private javax.swing.JPanel panelServidorLog;
    private javax.swing.JTextField txtCaminhoLocal;
    private javax.swing.JTextField txtCaminhoServidor;
    private javax.swing.JTextField txtIpServidor;
    private javax.swing.JTextField txtPorta;
    private javax.swing.JTextField txtSenha;
    private javax.swing.JTextArea txtServidorLog;
    private javax.swing.JTextField txtServidorPorta;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
