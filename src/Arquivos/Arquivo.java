/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arquivos;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Arquivo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String nome;
    private byte[] conteudo;
    private String diretorioDestino;
    private String diretorioAtual;
    private Date dataHoraUpload;
    private long tamanhoKB;

    public Arquivo(String nome, long tamanhoKB, String caminho) {
        this.nome = nome;
        this.tamanhoKB = tamanhoKB;
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
    
   public long getTamanhoKB() {
       return tamanhoKB;
   }

   public void setTamanhoKB(long tamanhoKB) {
       this.tamanhoKB = tamanhoKB;
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
    
    public boolean VerificarArquivo() {
        return this.tamanhoKB == this.conteudo.length;
    }
    
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
    
    public byte[] SerializarArquivo() throws Exception {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream ous;
        ous = new ObjectOutputStream(bao);
        ous.writeObject(this);
        return bao.toByteArray();
    }
}