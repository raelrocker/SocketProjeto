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
    public static Object getObjectFromByte(byte[] objectAsByte) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
                 bis = new ByteArrayInputStream(objectAsByte);
                 ois = new ObjectInputStream(bis);
                 obj = ois.readObject();

                 bis.close();
                 ois.close();
        } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
        } catch (ClassNotFoundException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();           
        }
        return obj;
    }
    
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
    
    public static ArrayList<Arquivo> GetListaArquivos() throws Exception {
        String s = "";
        ArrayList<Arquivo> ListaArquivos = new ArrayList<Arquivo>();
        String[] infoArquivo;
        try 
        { 
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
                    String nomeArquivo = infoArquivo[infoArquivo.length -1];
                    long tamanhoArquivo = Long.parseLong(infoArquivo[infoArquivo.length -2].replace(".", ""));
                    ListaArquivos.add(new Arquivo(nomeArquivo, tamanhoArquivo));
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
    
    private static boolean getDate(String linha) {
        boolean dataEncontrada = false;
        Matcher m = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(linha);
        while (m.find()) {
          dataEncontrada = true;
        }
        return dataEncontrada;
    }
}
