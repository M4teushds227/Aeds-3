package Src;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    private void inserirTermosNaString(String[] termos, int tamanho, String linha){
        Scanner scan = new Scanner(linha);
        //scan.useDelimiter("\0");
        String termo;
        for(int i = 0; i < tamanho && scan.hasNext(); i++){
            termo = scan.next();
            termos[i] = termo;
        }
        scan.close();
    }

    //Adiciona novos termos ao arquivo atribuido a variavel arq
    public void adicionarTermos(String linha, int id)throws IOException{
        RandomAccessFile escrita = new RandomAccessFile(arq, "rw");
        escrita.seek(escrita.length());
        int tam = contarTermos(linha);
        String[] termos = new String[tam];
        //metodo de inserção de termos
        inserirTermosNaString(termos, tam, linha);
        //System.err.println(linha);
        //Inserir termos com os seus respectivos ids
        for(int i = 0; i < termos.length; i++){
            //System.out.print(termos[i] + " ");
            byte[] dado = converteTermo(termos[i]);
            escrita.write(dado);
        }
        //System.out.println();
    }
    private byte[] converteTermo(String dado)throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        //System.out.println(dado);
        if(procuraTermo(dado)){
            for(int i = dado.length(); i < 13; i++){
                dado += " ";
            }
            dos.writeUTF(dado);
        }
        return baos.toByteArray();
    }
    //procura
    private boolean procuraTermo(String dado)throws IOException{
        RandomAccessFile le = new RandomAccessFile(arq, "r");
        boolean resul = true;
        while(le.getFilePointer() < le.length()){
            String comparador = le.readUTF();
            //System.out.println(dado);
            /*if(dado == null) {
                System.out.print(dado);
                System.out.println(" Deu ruim");
            }*/
            Scanner tiraEspaco = new Scanner(comparador);
            comparador = tiraEspaco.next();
            tiraEspaco.close();
            if(dado.compareTo(comparador) == 0){
                resul = false;
                le.close();
                break;
            }
        }
        le.close();
        return resul;
    }

    //Atualiza os termos
    public void atualizarTermos(){
        //infelizmente não sei como ela deveria atualizar os termos ainda
    }

    //Apaga os termos via lapide
    public void apagarTermos(){
        //infelizmente não sei como ela deveria atualizar os termos ainda
    }

    //Procura os termos na lista invertidas
    /*public ??? pesquisarTermos(){
    }*/
}