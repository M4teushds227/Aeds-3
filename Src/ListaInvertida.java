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
        boolean[] podeEscrever = new boolean[1];
        escrita.seek(escrita.length());
        int tam = contarTermos(linha);
        String[] termos = new String[tam];
        //metodo de inserção de termos
        inserirTermosNaString(termos, tam, linha);
        System.err.println(id);
        //Inserir termos com os seus respectivos ids
        //System.out.println(linha + " " + id);
        for(int i = 0; i < termos.length; i++){
            byte[] dado = converteTermo(termos[i], id, podeEscrever);
            //inico do arquivo
           long inicio = escrita.getFilePointer() - 1;
           escrita.seek(inicio);
           escrita.write(dado);

           //escrita.seek(inicio + 1);
           //System.out.println(escrita.readUTF());
           if(podeEscrever[0]){
               escrita.seek(escrita.length() - 1);
               long localizacao = escrita.getFilePointer();
               escrita.seek(inicio /*+ 27*/);
               //Avança indepententemente de qualquer coisa
               escrita.readBoolean();
               escrita.readUTF();
               escrita.readInt();
               //
               escrita.writeLong(localizacao);
            }
            escrita.seek(escrita.length());
            //escrita.seek(inicio + 20);
            //escrita.seek(escrita.readLong());
            //boolean resultado = escrita.readBoolean();
            //System.out.println(resultado);
        }
        //System.out.println();
    }
    private byte[] converteTermo(String dado, int id, boolean[] podeEscrever)throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        //System.out.println(dado);
        if(procuraTermo(dado, podeEscrever)){
            for(int i = dado.length(); i < 20; i++){
                dado += " ";
            }
            //System.out.println(id);
            dos.writeBoolean(true);
            dos.writeUTF(dado);
            //quantos indices existem
            dos.writeInt(1);
            //aqui que deve ser escrito quantos bytes devem ser pulados até o proximo registro
            dos.writeLong(1);
            dos.writeByte('*');
            dos.writeInt(id);
            //deve ser pego a localização dessa escrita
            dos.writeBoolean(false);
        }
        return baos.toByteArray();
    }
    //procura
    private boolean procuraTermo(String dado, boolean[] podeEscrever)throws IOException{
        RandomAccessFile le = new RandomAccessFile(arq, "r");
        podeEscrever[0] = true;
        boolean ocupado = le.readBoolean();
        while(ocupado && le.getFilePointer() < le.length()){
            String comparador = le.readUTF();
            //int lixo = le.readInt();
            le.seek(le.getFilePointer() + 4);
            long localizacao = le.readLong();
            //System.out.println(dado);
            /*if(dado == null) {
                System.out.print(dado);
                System.out.println(" Deu ruim");
            }*/
            Scanner tiraEspaco = new Scanner(comparador);
            comparador = tiraEspaco.next();
            tiraEspaco.close();
            if(dado.compareTo(comparador) == 0){
                podeEscrever[0] = false;
                le.close();
                break;
            }
            le.seek(localizacao);
            ocupado = le.readBoolean();
        }
        le.close();
        return podeEscrever[0];
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