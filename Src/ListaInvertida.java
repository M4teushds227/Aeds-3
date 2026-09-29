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
        long[] comeco = new long[1];
        long[] pegaTamanhoArray = new long[1];
        escrita.seek(escrita.length());
        int tam = contarTermos(linha);
        String[] termos = new String[tam];
        //metodo de inserção de termos
        inserirTermosNaString(termos, tam, linha);
        System.err.println(id);

        for(int i = 0; i < termos.length; i++){
            //Começo da verificação para a inserção no arquivo
           byte[] dado = converteTermoParaBinario(termos[i], id, podeEscrever, comeco, pegaTamanhoArray);

           long inicio = escrita.getFilePointer() - 1;
           escrita.seek(inicio);
           escrita.write(dado);

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
            else{
                //Tratamento do que já existe
                byte[] resto = pegaResto(comeco[0]);
                escrita.seek(pegaTamanhoArray[0]);
                int tamanhoArray = escrita.readInt();
                escrita.seek(pegaTamanhoArray[0] + 12);
                char[] lapidesTemporarias = new char[tamanhoArray + 1];
                int[] idsTemporarios = new int[tamanhoArray + 1];
                long ponteiro = escrita.getFilePointer();
                for(int x = 0; x < tamanhoArray; x++){
                    lapidesTemporarias[x] = escrita.readChar();
                    idsTemporarios[x] = escrita.readInt();
                    ponteiro = escrita.getFilePointer();
                }
                escrita.seek(ponteiro - 1);

                for(int x = 0; x < (tamanhoArray + 1); x++){
                    if(x == tamanhoArray){
                        //ponteiro = escrita.getFilePointer();
                        escrita.writeByte('*');
                        //ponteiro = escrita.getFilePointer();
                        escrita.writeInt(id);
                        ponteiro = escrita.getFilePointer();
                    }
                }
                escrita.seek(pegaTamanhoArray[0]);
                escrita.writeInt(tamanhoArray + 1);
                escrita.seek(ponteiro);
                escrita.write(resto);

                //System.err.println("");
            }
            escrita.seek(escrita.length());
        }
        //System.out.println();
    }
    private byte[] converteTermoParaBinario(String dado, int id, boolean[] podeEscrever, long[] comeco, long[] pegaTamanhoArray)throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        //System.out.println(dado);
        if(procuraTermo(dado, podeEscrever, comeco, pegaTamanhoArray)){
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
    private boolean procuraTermo(String dado, boolean[] podeEscrever, long[] comeco, long[] pegaTamanhoArray)throws IOException{
        RandomAccessFile le = new RandomAccessFile(arq, "r");
        podeEscrever[0] = true;
        boolean ocupado = le.readBoolean();
        while(ocupado && le.getFilePointer() < le.length()){
            String comparador = le.readUTF();
            le.seek(le.getFilePointer());
            pegaTamanhoArray[0] = le.getFilePointer();
            int tamanhoArray = le.readInt();
            //4
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
                le.seek(localizacao);
                comeco[0] = le.getFilePointer();
                le.close();
                break;
            }
            long ponteiro = le.getFilePointer();
            le.seek(ponteiro);
            for(int i = 0; i < tamanhoArray; i++){
                le.readByte();
                le.readInt();
            }
            //le.seek(localizacao);
            ocupado = le.readBoolean();
        }
        le.close();
        return podeEscrever[0];
    }
    private byte[] pegaResto(long comeco) throws IOException{
        RandomAccessFile le = new RandomAccessFile(arq, "r");
        le.seek(comeco);
        byte[] resto = new byte[(int)le.length()/*(int) (le.length() - comeco) + 10*/];
        le.readFully(resto, (int)comeco, (int) (le.length() - comeco));
        byte[] substituto = new byte[(int) (le.length() - comeco)];
        int j = 0;
        for(int i = 0; i < resto.length; i++){
            if(i > (int) comeco - 1){
                substituto[j] = resto[i];
                j++;
            }
        }
        le.close();
        return substituto;
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