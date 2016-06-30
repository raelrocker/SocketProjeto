package Cliente;

import Arquivos.Arquivo;
import Arquivos.BaixarArquivo;
import Arquivos.EnviarArquivo;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe responsável pelas requisições do CLIENTE
 */
public class ClienteSocket {

    private Socket socket;
    
    public ClienteSocket(Socket socket) {
        this.socket = socket;
    }
    
    /**
     * Envia um arquivo para o SERVIDOR
     * @param arquivo
     * @return
     * @throws Exception 
     */
    public boolean EnviarArquivoServidor(Arquivo arquivo) throws Exception {
        try {
            EnviarArquivo t = new EnviarArquivo(socket, false);
            return t.Enviar(arquivo);
        } catch (Exception ex) {
            throw ex;
        } 
    }
    
    /**
     * Baixa um arquivo do servidor
     * @param arquivo
     * @return
     * @throws Exception 
     */
    public boolean BaixarArquivoServidor(Arquivo arquivo) throws Exception {
        try {
            BaixarArquivo t = new BaixarArquivo(socket, false);
            return t.Baixar(arquivo);
        } catch (Exception ex) {
            throw ex;
        } 
    }
    
    /**
     * Requisita o caminho do SERVIDOR
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Lista os arquivos do SERVIDOR
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Efetua o login no SERVIDOR
     * @param usuario
     * @param senha
     * @return
     * @throws Exception 
     */
    public boolean Login(String usuario, String senha) throws Exception {
        ObjectOutputStream objectOut = null;
        ObjectInputStream objectIn = null;
        boolean resposta;
        try {
            // Envia solicitação
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
            objectOut.writeUTF("1@" + usuario + "_" + senha);
            objectOut.flush();
            
            // Recebe resposta
            objectIn = new ObjectInputStream(this.socket.getInputStream());
            resposta = objectIn.readBoolean();
            return resposta;
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            objectIn.close();
            objectOut.close();
            this.socket.close();
        }
    }
    
    /**
     * Desloga do SERVIDOR
     * @return
     * @throws Exception 
     */
    public boolean Logoff() throws Exception {
        ObjectOutputStream objectOut = null;
        ObjectInputStream objectIn = null;
        boolean resposta;
        try {
            // Envia solicitação
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
            objectOut.writeUTF("6");
            objectOut.flush();
            
            // Recebe resposta
            objectIn = new ObjectInputStream(this.socket.getInputStream());
            resposta = objectIn.readBoolean();
            return resposta;
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            objectIn.close();
            objectOut.close();
            this.socket.close();
        }
    }
    
}
