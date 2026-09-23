package Src;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class OrdenacaoExterna {

    private final String caminhoArquivoOriginal;
    private final byte lapideValido = ' ';

    public OrdenacaoExterna(String caminhoArquivoOriginal) {
        this.caminhoArquivoOriginal = caminhoArquivoOriginal;
    }

    // Validacao dos valores passados
    public void ordenar(String strCaminhos, String strTamBloco) throws IOException {
        int numCaminhos = 2;
        int tamBloco = 100;

        try {
            if (strCaminhos != null && !strCaminhos.trim().isEmpty()) {
                numCaminhos = Integer.parseInt(strCaminhos.trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Foi escrito algo diferente de um numero, usando o valor padrão 2");
        }

        try {
            if (strTamBloco != null && !strTamBloco.trim().isEmpty()) {
                tamBloco = Integer.parseInt(strTamBloco.trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Foi escrito algo diferente de um numero, usando o valor padrão 100");
        }

        if (numCaminhos < 2) {
            System.out.println("Numero de caminhos deve ser no minimo 2. Usando valor padrao 2");
            numCaminhos = 2;
        }
        if (tamBloco < 1) {
            System.out.println("Tamanho de bloco deve ser no minimo 1. Usando valor padrao 100");
            tamBloco = 100;
        }

        ordenar(numCaminhos, tamBloco);
    }

    public void ordenar(int numCaminhos, int tamBlocoMemoria) throws IOException {
        File arqOriginal = new File(this.caminhoArquivoOriginal);
        if (!arqOriginal.exists() || arqOriginal.length() <= 4) {
            System.out.println("Arquivo de dados nao encontrado ou vazio, carregue a base primeiro.");
            return;
        }

        // Criacao da pasta para os arquivos temporarios para não virar bagunça
        File pastaTemp = new File("Src/Base de Dados/temp_ordenacao");
        if (!pastaTemp.exists()) {
            pastaTemp.mkdirs();
        }

        // Criacao dos M arquivos temporarios
        File[] grupoA = new File[numCaminhos];
        File[] grupoB = new File[numCaminhos];
        for (int i = 0; i < numCaminhos; i++) {
            grupoA[i] = new File(pastaTemp, "temp_A_" + i + ".db");
            grupoB[i] = new File(pastaTemp, "temp_B_" + i + ".db");
        }

        // Criacao dos blocos ordenados na memoria
        int totalBlocos = criarBlocosIniciais(numCaminhos, tamBlocoMemoria, grupoA);

        if (totalBlocos == 0) {
            System.out.println("Nenhum registro valido encontrado para ordenar.");
            limparArquivos(pastaTemp);
            return;
        }

        File[] entrada = grupoA;
        File[] saida = grupoB;
        int passada = 0;

        // intercalacao balanceada por ser mais facil de implementar doq a heap
        while (totalBlocos > 1) {
            passada++;
            totalBlocos = intercalarPassada(numCaminhos, entrada, saida);

            // Invertendo os grupos de arquivos para a proxima passada
            File[] temp = entrada;
            entrada = saida;
            saida = temp;
        }

        // Copia o bloco unico do primeiro arquivo do conjunto 'entrada'
        File arqFinalOrdenado = entrada[0];
        copiarParaArquivoOriginal(arqFinalOrdenado);

        // Apaga os arquivos temporarios
        limparArquivos(pastaTemp);

        System.out.printf("Ordenacao concluida (%d passadas)!\n", passada);
    }

    // Le o arquivo original e ordena blocos em memoria para distribuir nos M arquivos
    private int criarBlocosIniciais(int numCaminhos, int tamBlocoMemoria, File[] arqSaida) throws IOException {
        int totalBlocos = 0;
        int caminhoAtual = 0;

        // Limpa os arquivos de saida antes de comecar
        RandomAccessFile[] saidas = new RandomAccessFile[numCaminhos];
        for (int i = 0; i < numCaminhos; i++) {
            if (arqSaida[i].exists()) {
                arqSaida[i].delete();
            }
            saidas[i] = new RandomAccessFile(arqSaida[i], "rw");
        }

        File arqOriginal = new File(this.caminhoArquivoOriginal);
        try (RandomAccessFile leitorOriginal = new RandomAccessFile(arqOriginal, "r")) {
            leitorOriginal.seek(4);

            List<Incidente> memoria = new ArrayList<>(tamBlocoMemoria);

            while (leitorOriginal.getFilePointer() < leitorOriginal.length()) {
                byte lapide = leitorOriginal.readByte();
                int tamanho = leitorOriginal.readInt();

                if (lapide == lapideValido) {
                    byte[] byteArr = new byte[tamanho];
                    leitorOriginal.readFully(byteArr);

                    Incidente inc = new Incidente();
                    inc.bytesEmArr(byteArr);
                    memoria.add(inc);

                    if (memoria.size() >= tamBlocoMemoria) {
                        gravarBlocoOrdenado(saidas[caminhoAtual], memoria);
                        caminhoAtual = (caminhoAtual + 1) % numCaminhos;
                        totalBlocos++;
                        memoria.clear();
                    }
                } else {
                    // Registro excluido
                    leitorOriginal.skipBytes(tamanho);
                }
            }

            // Grava o que sobrou na memoria
            if (!memoria.isEmpty()) {
                gravarBlocoOrdenado(saidas[caminhoAtual], memoria);
                totalBlocos++;
                memoria.clear();
            }
        } finally {
            // fecha todos os arquivos abertos
            for (RandomAccessFile raf : saidas) {
                if (raf != null) {
                    raf.close();
                }
            }
        }

        return totalBlocos;
    }

    // Ordena o bloco em memoria pelo ID e grava no arquivo
    private void gravarBlocoOrdenado(RandomAccessFile gravarNoArqBin, List<Incidente> bloco) throws IOException {

        for (int i = 0; i < bloco.size() - 1; i++) {
            for (int j = i + 1; j < bloco.size(); j++) {
                if (bloco.get(i).getId() > bloco.get(j).getId()) {
                    Incidente temp = bloco.get(i);
                    bloco.set(i, bloco.get(j));
                    bloco.set(j, temp);
                }
            }
        }

        // Gravacao no inicio do bloco da quantidade de registros
        gravarNoArqBin.writeInt(bloco.size());

        for (Incidente inc : bloco) {
            byte[] dados = inc.arrEmBytes();
            gravarNoArqBin.writeByte(lapideValido);
            gravarNoArqBin.writeInt(dados.length);
            gravarNoArqBin.write(dados);
        }
    }

    // Intercalacao dos arquivos
    private int intercalarPassada(int numCaminhos, File[] entrada, File[] saida) throws IOException {
        RandomAccessFile[] Entrada = new RandomAccessFile[numCaminhos];
        RandomAccessFile[] Saida = new RandomAccessFile[numCaminhos];

        for (int i = 0; i < numCaminhos; i++) {
            Entrada[i] = new RandomAccessFile(entrada[i], "r");
            if (saida[i].exists()) {
                saida[i].delete();
            }
            Saida[i] = new RandomAccessFile(saida[i], "rw");
        }

        int blocosGerados = 0;
        int saidaAtual = 0;

        try {
            int[] tamBlocoRestante = new int[numCaminhos];
            Incidente[] candidatos = new Incidente[numCaminhos];

            while (true) {
                // Prepara a proxima rodada de blocos dos M caminhos
                boolean temBlocoNovo = false;

                for (int i = 0; i < numCaminhos; i++) {
                    if (Entrada[i].getFilePointer() < Entrada[i].length()) {
                        tamBlocoRestante[i] = Entrada[i].readInt(); // le quantos registros tem neste bloco
                        if (tamBlocoRestante[i] > 0) {
                            candidatos[i] = lerRegistro(Entrada[i]);
                            tamBlocoRestante[i]--;
                            temBlocoNovo = true;
                        } else {
                            candidatos[i] = null;
                        }
                    } else {
                        tamBlocoRestante[i] = 0;
                        candidatos[i] = null;
                    }
                }

                if (!temBlocoNovo) {
                    break;
                }

                // Intercala os blocos para o arquivo de saida atual
                RandomAccessFile out = Saida[saidaAtual];
                long posQtdBloco = out.getFilePointer();
                out.writeInt(0); // reserva os 4 bytes para gravar a quantidade total depois
                int totalRegistrosNoBloco = 0;

                while (true) {
                    // Encontra o menor ID entre os candidatos atuais dos caminhos
                    int menorIndice = -1;
                    int menorId = Integer.MAX_VALUE;

                    for (int i = 0; i < numCaminhos; i++) {
                        if (candidatos[i] != null) {
                            if (candidatos[i].getId() < menorId) {
                                menorId = candidatos[i].getId();
                                menorIndice = i;
                            }
                        }
                    }

                    if (menorIndice == -1) {
                        break;
                    }

                    // Escreve o menor registro no arquivo de saida
                    Incidente vencedor = candidatos[menorIndice];
                    byte[] dados = vencedor.arrEmBytes();
                    out.writeByte(lapideValido);
                    out.writeInt(dados.length);
                    out.write(dados);
                    totalRegistrosNoBloco++;

                    // Avanca o leitor do caminho que venceu
                    if (tamBlocoRestante[menorIndice] > 0) {
                        candidatos[menorIndice] = lerRegistro(Entrada[menorIndice]);
                        tamBlocoRestante[menorIndice]--;
                    } else {
                        candidatos[menorIndice] = null; // acabou o bloco deste caminho
                    }
                }

                // Atualiza o cabecalho do bloco com a quantidade real de registros intercalados
                long posFim = out.getFilePointer();
                out.seek(posQtdBloco);
                out.writeInt(totalRegistrosNoBloco);
                out.seek(posFim);

                blocosGerados++;
                saidaAtual = (saidaAtual + 1) % numCaminhos;
            }

        } finally {
            for (int i = 0; i < numCaminhos; i++) {
                if (Entrada[i] != null)
                    Entrada[i].close();
                if (Saida[i] != null)
                    Saida[i].close();
            }
        }

        return blocosGerados;
    }

    // Le o proximo registro valido
    private Incidente lerRegistro(RandomAccessFile lerArqBin) throws IOException {
        byte lapide = lerArqBin.readByte();// pulando a lapide
        int tamanho = lerArqBin.readInt();
        byte[] byteArr = new byte[tamanho];
        lerArqBin.readFully(byteArr);

        Incidente inc = new Incidente();
        inc.bytesEmArr(byteArr);
        return inc;
    }

    // Copia o resultado e salva no arquivo original
    private void copiarParaArquivoOriginal(File arqFinal) throws IOException {
        File arqOriginal = new File(this.caminhoArquivoOriginal);

        int ultimoId = 0;

        try (RandomAccessFile leitorFinal = new RandomAccessFile(arqFinal, "r");
                RandomAccessFile escritorOriginal = new RandomAccessFile(arqOriginal, "rw")) {

            escritorOriginal.setLength(0); // limpa o arquivo original
            escritorOriginal.writeInt(0); // reserva cabecalho de 4 bytes pro ultimoId

            if (leitorFinal.getFilePointer() < leitorFinal.length()) {
                int totalRegistros = leitorFinal.readInt(); 
                for (int i = 0; i < totalRegistros; i++) {
                    byte lapide = leitorFinal.readByte();
                    int tamanho = leitorFinal.readInt();
                    byte[] dados = new byte[tamanho];
                    leitorFinal.readFully(dados);

                    Incidente inc = new Incidente();
                    inc.bytesEmArr(dados);
                    if (inc.getId() > ultimoId) {
                        ultimoId = inc.getId();
                    }

                    escritorOriginal.writeByte(lapide);
                    escritorOriginal.writeInt(tamanho);
                    escritorOriginal.write(dados);
                }
            }

            escritorOriginal.seek(0);
            escritorOriginal.writeInt(ultimoId);
        }
    }

    private void limparArquivos(File pasta) {
        if (pasta.exists() && pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles();
            if (arquivos != null) {
                for (File f : arquivos) {
                    f.delete();
                }
            }
            pasta.delete();
        }
    }
}
