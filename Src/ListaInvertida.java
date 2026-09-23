package Src;
import java.io.File;
import java.util.Scanner;
public class ListaInvertida {
    //private String[] termos;
    private File arq;
    public ListaInvertida(String caminho) {
        arq = new File(caminho);
    }

    //Função para contar os termos
    private int contarTermos(String linha){
        int numPalavras = 0;
        Scanner scan = new Scanner(linha);
        scan.useDelimiter(" ");
        for(String lixo = null; scan.hasNext(); numPalavras++){
            lixo = scan.next();
        }
        scan.close();
        return numPalavras;
    }
    //Metodo para inserir termos
    private void inserirTermos(String[] termos, int tamanho, String linha){
        Scanner scan = new Scanner(linha);
        scan.useDelimiter(" ");
        String termo;
        for(int i = 0; i < tamanho && scan.hasNext(); i++){
            termo = scan.next();
            termos[i] = termo;
        }
        scan.close();
    }

    //Adiciona novos termos ao arquivo atribuido a variavel arq
    public void adicionarTermos(String linha){
        int tam = contarTermos(linha);
        String[] termos = new String[tam];
        //metodo de inserção de termos
        inserirTermos(termos, tam, linha);
        //metodeo de pesquisa de termos no arquivo
    }
}