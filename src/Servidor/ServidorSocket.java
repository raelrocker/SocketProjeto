package Servidor;

import Interfaces.frmPrincipal;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ServidorSocket implements Runnable {

    private int porta;
    private ServerSocket serverSocket;
    private JTextArea txtServidorLog;
    private boolean pararServidor;
    private Socket socket;

    public ServidorSocket(int porta, JTextArea txtServidorLog) {
        this.porta = porta;
        this.txtServidorLog = txtServidorLog;
        this.pararServidor = false;
    }
    
    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(porta);
            this.AdicionarLog("Servidor rodando na porta " + this.porta);
            
            while (!this.pararServidor) {
                this.socket = this.serverSocket.accept();
            }
            
        } catch (Exception ex) {
            if (ex.getMessage().contains("closed")) {
                this.AdicionarLog("Servidor parado");
            } else {
                this.AdicionarLog("ERRO: " + ex.getMessage());
            }
        }
        finally {
            try {
                this.serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void AdicionarLog(String msg) {
        this.txtServidorLog.setText(txtServidorLog.getText() + msg + "\n");
    }
    
    public void PararServidor() throws IOException {
        this.pararServidor = true;
        this.serverSocket.close();
    }
    
}
