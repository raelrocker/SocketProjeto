package Classes;

import Arquivos.Arquivo;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/**
 *
 * @author Rael
 */
public class ListaArquivosModel extends AbstractListModel implements ListModel {
    
    private Arquivo itemSelecionado;
    private ArrayList<Arquivo> lista;
    
    public ListaArquivosModel(ArrayList<Arquivo> lista) {
        this.lista = lista;
    }

    public ListaArquivosModel() {
        this.lista = new ArrayList<Arquivo>();
    }
    
    public Arquivo getItemSelecionado() {
        return this.itemSelecionado;
    }
    
    public void setItemSelecionado(Arquivo item) {
        this.itemSelecionado = item;
    }
        
    @Override
    public int getSize() {
        return this.lista.size();
    }

    @Override
    public Arquivo getElementAt(int index) {
        return this.lista.get(index);
    }
    
}
