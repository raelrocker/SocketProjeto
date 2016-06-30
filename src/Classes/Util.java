package Classes;

import Arquivos.Arquivo;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    
    /**
     * Retorna o caminho atual da aplicação
     * @return caminho da aplicação
     * @throws Exception 
     */
    public static String GetCaminhoAtual() throws Exception {
        String s = "";
        Process p=Runtime.getRuntime().exec("cmd /c cd"); 
        p.waitFor(); 
        BufferedReader reader=new BufferedReader(
            new InputStreamReader(p.getInputStream())
        ); 
        String line; 
        while((line = reader.readLine()) != null) 
        { 
            s += line;
        } 
        return s;
    }
    
    /**
     * Retorna uma lista contendo os arquivos do diretório atual da aplicação
     * @return Lista de arquivos
     * @throws Exception 
     */
    public static ArrayList<Arquivo> GetListaArquivos() throws Exception {
        String s = "";
        ArrayList<Arquivo> ListaArquivos = new ArrayList<Arquivo>();
        String[] infoArquivo;
        String caminho;
        String nomeArquivo = "";
        long tamanhoArquivo = 0;
        try 
        { 
            caminho = GetCaminhoAtual();
            Process p=Runtime.getRuntime().exec("cmd /c dir"); 
            p.waitFor(); 
            BufferedReader reader=new BufferedReader(
                new InputStreamReader(p.getInputStream())
            ); 
            String line; 
            while((line = reader.readLine()) != null) 
            {
                if (!line.contains("<DIR>") && getDate(line)) {
                    infoArquivo = line.split(" ");
                    nomeArquivo = infoArquivo[infoArquivo.length -1];
                    for (int i = infoArquivo.length -2; i >= 0; i--) {
                        try {
                            tamanhoArquivo = Long.parseLong(infoArquivo[i].replace(".", ""));
                            break;
                        } catch (NumberFormatException ex) {
                            nomeArquivo = infoArquivo[i] + " " + nomeArquivo;
                        }
                    }
                    ListaArquivos.add(new Arquivo(nomeArquivo, tamanhoArquivo, caminho));
                }
                //s += line + System.lineSeparator();
            } 
            return ListaArquivos.size() > 0 ? ListaArquivos : null;
        }
        catch(Exception ex) 
        {
            throw ex;
        } 
    }
    
    /**
     * Localiza o padão de data numa string
     * @param linha 
     * @return 
     */
    private static boolean getDate(String linha) {
        boolean dataEncontrada = false;
        Matcher m = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(linha);
        while (m.find()) {
          dataEncontrada = true;
        }
        return dataEncontrada;
    }
}
