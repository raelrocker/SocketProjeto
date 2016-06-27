package Cliente;

import Arquivos.Arquivo;
import Arquivos.BaixarArquivo;
import Arquivos.EnviarArquivo;
import Classes.ListaArquivosModel;
import Enum.ExecutarAcao;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClienteSocket {

    private Socket socket;
    
    public ClienteSocket(Socket socket) {
        this.socket = socket;
    }
    
    public boolean EnviarArquivoServidor(Arquivo arquivo) throws Exception {
        try {
            EnviarArquivo t = new EnviarArquivo(socket, false);
            return t.Enviar(arquivo);
        } catch (Exception ex) {
            throw ex;
        } 
    }
    
    public boolean BaixarArquivoServidor(Arquivo arquivo) throws Exception {
        try {
            BaixarArquivo t = new BaixarArquivo(socket, false);
            return t.Baixar(arquivo);
        } catch (Exception ex) {
            throw ex;
        } 
    }
    
    public String GetCaminhoServidor() throws Exception {
        ObjectOutputStream objectOut = null;
        ObjectInputStream objectIn = null;
        String caminho;
        try {
            // Envia solicitação
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
            objectOut.writeUTF("3");
            objectOut.flush();
            
            // Recebe resposta
            objectIn = new ObjectInputStream(this.socket.getInputStream());
            caminho = objectIn.readUTF();
            return caminho;
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            objectIn.close();
            objectOut.close();
            this.socket.close();
        }
    }
    
    public ArrayList<Arquivo> GetListaArquivos() throws Exception {
        ObjectOutputStream objectOut = null;
        ObjectInputStream objectIn = null;
        ArrayList<Arquivo> lista;
        try {
            // Envia solicitação
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
            objectOut.writeUTF("2");
            objectOut.flush();
            
            // Recebe resposta
            objectIn = new ObjectInputStream(this.socket.getInputStream());
            lista = (ArrayList<Arquivo>)objectIn.readObject();
            return lista;
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            objectIn.close();
            objectOut.close();
            this.socket.close();
        }
    }
}
