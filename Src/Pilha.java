package Src;

public class Pilha {
    private Celula topo;
    private int tamanhoDaPilha;

    public Pilha(){
        topo = new Celula(-1);
        tamanhoDaPilha = 0;
    }

    public void inseir(int elemento){
        Celula novo = new Celula(elemento);
        novo.setProx(topo.getProx());
        topo.setProx(novo);
        tamanhoDaPilha++;
    }

    public void remover(){
        topo.setProx(topo.getProx().getProx());
        tamanhoDaPilha--;
    }

    public Celula getTopo(){
        return topo;
    }

    public int mostrarTopo(){
        return topo.getProx().getElemento();
    }

    public int getTamanho(){
        return tamanhoDaPilha;
    }
}