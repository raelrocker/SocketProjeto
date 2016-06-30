package Arquivos;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;


/**
 * Classe resposável por armazenar os dados dos arquivos
 */
public class Arquivo implements Serializable {

    private String nome; // Nome do arquivo com extensão
    private byte[] conteudo; // arquivo em bytes
    private String diretorioDestino; // Diretório de destino do arquivo quando transmitido
    private String diretorioAtual; // Diretório atual do arquivo
    private Date dataHoraUpload; // Data e hora do upload/download
    private long tamanho; // tamanho do arquivo em bytes

    public Arquivo(String nome, long tamanho, String caminho) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.diretorioAtual = caminho;
    }
    
    public Arquivo() {}

    public String getDiretorioAtual() {
        return diretorioAtual;
    }

    public void setDiretorioAtual(String diretorioAtual) {
        this.diretorioAtual = diretorioAtual;
    }
    
    public String getCaminhoNome() {
        return this.diretorioAtual + "/" + this.getNome();
    }
    
   public long getTamanho() {
       return tamanho;
   }

   public void setTamanho(long tamanho) {
       this.tamanho = tamanho;
   }

    public Date getDataHoraUpload() {
              return dataHoraUpload;
    }
    
    public void setDataHoraUpload(Date dataHoraUpload) {
              this.dataHoraUpload = dataHoraUpload;
    }
    
    public String getNome() {
              return nome;
    }
    
    public void setNome(String nome) {
              this.nome = nome;
    }
    
    public byte[] getConteudo() {
              return conteudo;
    }
    
    public void setConteudo(byte[] conteudo) {
              this.conteudo = conteudo;
    }
    
    public String getDiretorioDestino() {
              return diretorioDestino;
    }
    
    public void setDiretorioDestino(String diretorioDestino) {
              this.diretorioDestino = diretorioDestino;
    }
    
    /**
     * Verifica se o tamanho informado corresponde ao tamanho do arquivo em bytes
     * @return TRUE se o arquivo estiver OK
     */
    public boolean VerificarArquivo() {
        return this.tamanho == this.conteudo.length;
    }
    
    /**
     * Salva o arquivo no diretório de destino informado
     * @return TRUE se o arquivo for salvo com sucesso
     */
    public boolean SalvarArquivo() {
        FileOutputStream fos = null;
        int bytesEscritos = 0;
        int tam = 51200;
        try {
            fos = new FileOutputStream(this.diretorioDestino + "/" + this.getNome());
            if (conteudo.length < tam) {
                tam = conteudo.length;
            }
            while (bytesEscritos < conteudo.length) {
                fos.write(conteudo, bytesEscritos, tam);
                bytesEscritos += tam;
                if ((conteudo.length - bytesEscritos) < tam) {
                    tam = conteudo.length - bytesEscritos;
                }
            }
            fos.close();
            return true;
        } catch (IOException ex) {
            System.err.println("Erro: " + ex.getMessage());
            return false;
        } finally {
            try { fos.close(); } 
            catch (IOException ex) {}
        }
    }
    
    public String toString() {
        return this.getNome();
    }
}