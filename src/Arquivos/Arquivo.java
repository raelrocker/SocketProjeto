/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arquivos;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Date dataHoraUpload;
    private long tamanhoKB;

    public Arquivo(String nome, long tamanhoKB) {
        this.nome = nome;
        this.tamanhoKB = tamanhoKB;
    }
    
    public Arquivo() {}
    
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
        try {
            fos = new FileOutputStream(this.getDiretorioDestino() + this.getNome());
            fos.write(this.getConteudo());
            fos.close();
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            try { fos.close(); } 
            catch (IOException ex) {}
        }
    }
}