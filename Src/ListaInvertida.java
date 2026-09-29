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

    //Função para pular uma quantidade fixa de bytes N vezes
    private long funcaoPular(long tamanhoBytes, long quantPula){
        return tamanhoBytes * quantPula;
    }

    //Adiciona novos termos ao arquivo atribuido a variavel arq
    public void criarTermos(String linha, int id)throws IOException{
        RandomAccessFile escrita = new RandomAccessFile(arq, "rw");
        //ponteiros
        boolean[] podeEscrever = new boolean[1];
        long[] comeco = new long[1];
        long[] pegaTamanhoArray = new long[1];
        escrita.seek(escrita.length());
        //metodo de contagem de termos
        int tam = contarTermos(linha);
        String[] termos = new String[tam];
        //metodo de inserção de termos no array de strings
        inserirTermosNaString(termos, tam, linha);
        //
        System.err.println(id);
        //
        //Extração dos termos propriamente dita
        for(int i = 0; i < termos.length; i++){
           //Converte os bytes se tiver converção
           byte[] dado = converterTermos(termos[i], id, podeEscrever, comeco, pegaTamanhoArray);
           //
           long inicio = escrita.getFilePointer() - 1;
           escrita.seek(inicio);
           escrita.write(dado);
            if(!podeEscrever[0]){
                //Tratamento do que já existe
                byte[] resto = pegaRestoDosBytes(comeco[0]);
                escrita.seek(pegaTamanhoArray[0]);
                int tamanhoArray = escrita.readInt();
                //talvez isso possa ser retirado
                escrita.seek(pegaTamanhoArray[0] + 4);
                //
                long ponteiro;
                escrita.seek(comeco[0]);
                //detalhe que por enquanto estou escrevendo a lapide desse jeito só para ficar mais facíl de diferenciar em testes, isso será alterado depois
                escrita.writeByte('*');
                escrita.writeInt(id);
                ponteiro = escrita.getFilePointer();
                //alterações finais da iteração
                escrita.seek(pegaTamanhoArray[0]);
                escrita.writeInt(tamanhoArray + 1);
                escrita.seek(ponteiro);
                escrita.write(resto);
            }
            escrita.seek(escrita.length());
        }
        //escrita.close();
    }

    private byte[] converterTermos(String dado, int id, boolean[] podeEscrever, long[] comeco, long[] pegaTamanhoArray)throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        //
        if(pesquisarTermo(dado, podeEscrever, comeco, pegaTamanhoArray)){
            for(int i = dado.length(); i < 20; i++){
                dado += " ";
            }
            //Escrita no array
            //
            dos.writeBoolean(true);
            //Escreve a palavra formatada
            dos.writeUTF(dado);
            //quantos indices existem
            dos.writeInt(1);
            //Lapide
            dos.writeByte('*');
            //sera necessario mudar depois para ser long para poder extrair a localização
            dos.writeInt(id);
            //
            dos.writeBoolean(false);
        }
        return baos.toByteArray();
    }
    //metodo privado que procura o termo dentro do arquivo
    private boolean pesquisarTermo(String dado, boolean[] podeEscrever, long[] comeco, long[] pegaTamanhoArray)throws IOException{
        RandomAccessFile le = new RandomAccessFile(arq, "r");
        podeEscrever[0] = true;
        boolean ocupado = le.readBoolean();
        //Implementar uma forma mais eficiente de fazer pesquisas pois é insuficiente a comlexidade de Teta(N) que aumenta a cada inserção de novo termo
        while(ocupado && le.getFilePointer() < le.length()){
            String comparador = le.readUTF();
            pegaTamanhoArray[0] = le.getFilePointer();
            int tamanhoArray = le.readInt();
            le.seek(le.getFilePointer() + funcaoPular(5, tamanhoArray));
            //Tira o espaço do termo lido do arquivo binario
            Scanner tiraEspaco = new Scanner(comparador);
            comparador = tiraEspaco.next();
            tiraEspaco.close();
            //
            if(dado.compareTo(comparador) == 0){
                podeEscrever[0] = false;
                comeco[0] = le.getFilePointer();
                le.close();
                break;
            }
            ocupado = le.readBoolean();
        }
        le.close();
        return podeEscrever[0];
    }

    //Pega todos os bytes antes da alteração na função de inserção de termos e retorna o array de bytes
    private byte[] pegaRestoDosBytes(long comeco) throws IOException{
        RandomAccessFile le = new RandomAccessFile(arq, "r");
        le.seek(comeco);
        //Extrai bytes e cria arrays
        byte[] resto = new byte[(int)le.length()];
        le.readFully(resto, (int)comeco, (int) (le.length() - comeco));
        byte[] substituto = new byte[(int) (le.length() - comeco)];
        //Formata os bytes de forma adequada
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

    //A implementar depois

    //pesquisa por termos
    public boolean pesquisarTermo(String termos) throws IOException{
        boolean resul = true;
        int tam = contarTermos(termos);
        String[] termo = new String[tam];
        inserirTermosNaString(termo, tam, termos);
        int[] tamanhos = new int[tam];
        long[] iniciosDeArray = new long[tam];

        RandomAccessFile leitura = new RandomAccessFile(arq, "r");
        boolean verificador = leitura.readBoolean();
        for(int i = 0; i < tam; i++){
            while(verificador){
                String dado = leitura.readUTF();
                int tamanhoArray = leitura.readInt();
                Scanner tiraEspaco = new Scanner(dado);
                dado = tiraEspaco.next();
                tiraEspaco.close();
                if(dado.compareTo(termo[i]) == 0){
                    //pega a localização do array e o tamanho
                    iniciosDeArray[i] = leitura.getFilePointer();
                    tamanhos[i] = tamanhoArray;
                    /*leitura.seek(0);
                    verificador = leitura.readBoolean();*/
                    break;
                }
                //
                leitura.seek(leitura.getFilePointer() + funcaoPular(5, tamanhoArray));
                verificador = leitura.readBoolean();
            }
            //Reseta o verificador
            leitura.seek(0);
            verificador = leitura.readBoolean();
        }
        //Operação depois que acha todos os elementos
        //int[][] listasDeId = new int[tam][];
        Pilha[] pilhas = new Pilha[tam];
        //inicializa as pilhas
        for(int i = 0; i < tam; i++){
            pilhas[i] = new Pilha();
        }

        for(int i = 0; i < tam; i++){
            leIds(tamanhos[i], iniciosDeArray[i], pilhas[i]);
            //listasDeId[i] = array;
            //System.out.println("Deu certo");
        }
        //função que concatena as duas coisas e depois funde
        Pilha resultado = concatenar(pilhas, tam);
        for(int i = 0; i < resultado.getTamanho(); i++){
            System.out.print(resultado.mostrarTopo() + " ");
            resultado.remover();
        }
        leitura.close();
        return resul;
    }

    private void leIds(int tamanho, long inicioDeArray, Pilha pilha) throws IOException{
        /*int[] arrayIds = new int[tamanho];*/
        //chama o arquivo
        RandomAccessFile leitor = new RandomAccessFile(arq, "r");
        //coloca o ponteiro do arquivo no lugar certo
        leitor.seek(inicioDeArray);
        //Começa o loop para inserir os ids nas posições corretas
        for(int i = 0; i < tamanho; i++){
            leitor.readByte();
            /*arrayIds[i] = */pilha.inseir(leitor.readInt());
        }
        leitor.close();
    }
    private Pilha concatenar(Pilha[] pilhas, int tam){
        //
        Pilha iguais = new Pilha();
        for(int i = 1; i < tam; i++){
            while(pilhas[i].getTopo().getProx() != null){
                /*if(pilhas[0] == pilhas[i]){
                    if(pilhas[0] != null && ){
                    }
                }
                else */if(pilhas[0].mostrarTopo() != pilhas[i].mostrarTopo()){
                    pilhas[i].remover();
                    pilhas[0].remover();
                    if(pilhas[0].getTopo().getProx() == null){
                        pilhas[0] = pilhas[1];
                        break;
                    }
                }
                else{
                    iguais.inseir(pilhas[i].mostrarTopo());
                    pilhas[i].remover();
                    //pilhas[0].remover();
                }
            }

            /*if(){
            }*/
        }
        return iguais;
    }

    /*public boolean pesquisarNasDuasListas(String termo1, String termo2, File arq2){
        boolean resul;
        RandomAccessFile leitura = new RandomAccessFile(arq, "r");
        leitura.close();
        return resul;
    }*/

    //Atualiza os termos
    public void atualizarTermos(){
        //infelizmente não sei como ela deveria atualizar os termos ainda
    }

    //Apaga os termos via lapide
    public void apagarTermos(){
        //infelizmente não sei como ela deveria atualizar os termos ainda
    }
}