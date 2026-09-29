package Src;

public class Celula {
    private int elemento;
    private Celula prox;

    public Celula(int x){
        elemento = x;
        prox = null;
    }
    public int getElemento(){
        return elemento;
    }
    public void setProx(Celula x){
        prox = x;
    }
    public Celula getProx(){
        return prox;
    }
}