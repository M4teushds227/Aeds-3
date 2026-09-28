package Src;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ArvoreBMais {

    public static class EntradaIndice {
        public int id;
        public long posicao;

        public EntradaIndice(int id, long posicao) {
            this.id = id;
            this.posicao = posicao;
        }

        public String imprimir() {
            return "[ID: " + id + " - Posicao: " + posicao + "]";
        }
    }

    // Classe para ajudar no split
    private static class RetornoPromocao {
        int chavePromovida;
        long filhoDireitoPos;

        RetornoPromocao(int chavePromovida, long filhoDireitoPos) {
            this.chavePromovida = chavePromovida;
            this.filhoDireitoPos = filhoDireitoPos;
        }
    }

    public static class PaginaBMais {
        public long posicao;
        public boolean isFolha;
        public int n;
        public long proximaFolha;

        public int[] chaves;
        public long[] filhos;
        public long[] dados;         

        public PaginaBMais(boolean isFolha, int ordem) {
            this.posicao = -1;
            this.isFolha = isFolha;
            this.n = 0;
            this.proximaFolha = -1;
            this.chaves = new int[ordem];
            this.filhos = new long[ordem + 1];
            this.dados = new long[ordem];

            for (int i = 0; i <= ordem; i++) {
                this.filhos[i] = -1;
            }
            for (int i = 0; i < ordem; i++) {
                this.dados[i] = -1;
            }
        }
    }

    private int tamanhoCabecalho = 32;

    private String caminhoArquivo;
    private RandomAccessFile arq;
    private int ordem;
    private long raiz;
    private int tamanhoPagina;
    private long primeiraFolha;

    public ArvoreBMais(String caminhoArquivo, int ordemParametrizada) throws IOException {
        if (ordemParametrizada < 3) {
            throw new IllegalArgumentException("A ordem da arvore B+ deve ser no minimo 3.");
        }
        this.caminhoArquivo = caminhoArquivo;
        this.ordem = ordemParametrizada;
        this.tamanhoPagina = calcularTamanhoPagina(this.ordem);
        inicializarArquivo();
    }

    public ArvoreBMais(String caminhoArquivo) throws IOException {
        this(caminhoArquivo, 8);
    }

    private int calcularTamanhoPagina(int m) {
        // 1 byte (isFolha) + 4 bytes (n) + 8 bytes (proximaFolha) + (m * 4 bytes de chaves) + ((m + 1) * 8 bytes de filhos) + (m * 8 bytes de dados)
        return 1 + 4 + 8 + (m * 4) + ((m + 1) * 8) + (m * 8);
    }

    // Inicializa o arquivo ou carrega cabecalho existente
    private void inicializarArquivo() throws IOException {
        File file = new File(this.caminhoArquivo);
        File pasta = file.getParentFile();
        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        boolean existe = file.exists() && file.length() >= tamanhoCabecalho;
        this.arq = new RandomAccessFile(file, "rw");

        if (existe) {
            this.arq.seek(0);
            this.ordem = this.arq.readInt();
            this.raiz = this.arq.readLong();
            this.tamanhoPagina = this.arq.readInt();
            this.primeiraFolha = this.arq.readLong();
        } else {
            this.raiz = -1;
            this.primeiraFolha = -1;
            atualizarCabecalho();
        }
    }

    private void atualizarCabecalho() throws IOException {
        this.arq.seek(0);
        this.arq.writeInt(this.ordem);
        this.arq.writeLong(this.raiz);
        this.arq.writeInt(this.tamanhoPagina);
        this.arq.writeLong(this.primeiraFolha);
    }

    private void escreverPagina(PaginaBMais pag) throws IOException {
        this.arq.seek(pag.posicao);
        this.arq.writeByte(pag.isFolha ? 1 : 0);
        this.arq.writeInt(pag.n);
        this.arq.writeLong(pag.proximaFolha);

        for (int i = 0; i < this.ordem; i++) {
            this.arq.writeInt(pag.chaves[i]);
        }
        for (int i = 0; i <= this.ordem; i++) {
            this.arq.writeLong(pag.filhos[i]);
        }
        for (int i = 0; i < this.ordem; i++) {
            this.arq.writeLong(pag.dados[i]);
        }
    }

    private PaginaBMais lerPagina(long pos) throws IOException {
        if (pos < 0 || pos >= this.arq.length()) {
            return null;
        }

        this.arq.seek(pos);
        PaginaBMais pag = new PaginaBMais(false, this.ordem);
        pag.posicao = pos;
        pag.isFolha = (this.arq.readByte() == 1);
        pag.n = this.arq.readInt();
        pag.proximaFolha = this.arq.readLong();

        for (int i = 0; i < this.ordem; i++) {
            pag.chaves[i] = this.arq.readInt();
        }
        for (int i = 0; i <= this.ordem; i++) {
            pag.filhos[i] = this.arq.readLong();
        }
        for (int i = 0; i < this.ordem; i++) {
            pag.dados[i] = this.arq.readLong();
        }

        return pag;
    }

    private long alocarNovaPagina() throws IOException {
        long tam = this.arq.length();
        if (tam < tamanhoCabecalho) {
            tam = tamanhoCabecalho;
        }
        return tam;
    }

    public long buscar(int chave) throws IOException {
        if (this.raiz == -1) {
            return -1;
        }

        long posAtual = this.raiz;
        while (posAtual != -1) {
            PaginaBMais pag = lerPagina(posAtual);
            if (pag == null) {
                return -1;
            }

            if (pag.isFolha) {
                for (int i = 0; i < pag.n; i++) {
                    if (pag.chaves[i] == chave) {
                        return pag.dados[i];
                    }
                }
                return -1; // Nao encontrado na folha
            } else {
                int i = 0;
                while (i < pag.n && chave >= pag.chaves[i]) {
                    i++;
                }
                posAtual = pag.filhos[i];
            }
        }

        return -1;
    }

    public boolean atualizar(int chave, long novaPosicao) throws IOException {
        if (this.raiz == -1) {
            return false;
        }

        long posAtual = this.raiz;
        while (posAtual != -1) {
            PaginaBMais pag = lerPagina(posAtual);
            if (pag == null) {
                return false;
            }

            if (pag.isFolha) {
                for (int i = 0; i < pag.n; i++) {
                    if (pag.chaves[i] == chave) {
                        pag.dados[i] = novaPosicao;
                        escreverPagina(pag);
                        return true;
                    }
                }
                return false;
            } else {
                int i = 0;
                while (i < pag.n && chave >= pag.chaves[i]) {
                    i++;
                }
                posAtual = pag.filhos[i];
            }
        }

        return false;
    }

    public void inserir(int chave, long posicaoDados) throws IOException {
        if (this.raiz == -1) {
            PaginaBMais novaRaiz = new PaginaBMais(true, this.ordem);
            novaRaiz.posicao = alocarNovaPagina();
            novaRaiz.n = 1;
            novaRaiz.chaves[0] = chave;
            novaRaiz.dados[0] = posicaoDados;
            novaRaiz.proximaFolha = -1;

            escreverPagina(novaRaiz);

            this.raiz = novaRaiz.posicao;
            this.primeiraFolha = novaRaiz.posicao;
            atualizarCabecalho();
            return;
        }

        RetornoPromocao promo = inserirRecursivo(this.raiz, chave, posicaoDados);

        // Se a raiz dividiu, cria uma nova raiz interna
        if (promo != null) {
            PaginaBMais novaRaiz = new PaginaBMais(false, this.ordem);
            novaRaiz.posicao = alocarNovaPagina();
            novaRaiz.n = 1;
            novaRaiz.chaves[0] = promo.chavePromovida;
            novaRaiz.filhos[0] = this.raiz;
            novaRaiz.filhos[1] = promo.filhoDireitoPos;

            escreverPagina(novaRaiz);

            this.raiz = novaRaiz.posicao;
            atualizarCabecalho();
        }
    }

    private RetornoPromocao inserirRecursivo(long posPagina, int chave, long posicaoDados) throws IOException {
        PaginaBMais pag = lerPagina(posPagina);
        if (pag == null) {
            return null;
        }

        if (pag.isFolha) {
            // Se a chave ja existe na folha, apenas atualiza a posicao
            for (int i = 0; i < pag.n; i++) {
                if (pag.chaves[i] == chave) {
                    pag.dados[i] = posicaoDados;
                    escreverPagina(pag);
                    return null;
                }
            }

            // Insere ordenado na folha
            int i = pag.n - 1;
            while (i >= 0 && pag.chaves[i] > chave) {
                pag.chaves[i + 1] = pag.chaves[i];
                pag.dados[i + 1] = pag.dados[i];
                i--;
            }
            pag.chaves[i + 1] = chave;
            pag.dados[i + 1] = posicaoDados;
            pag.n++;

            // Se nao excedeu a capacidade maxima de chaves (ordem - 1)
            if (pag.n < this.ordem) {
                escreverPagina(pag);
                return null;
            }

            // realiza split da folha
            PaginaBMais novaFolha = new PaginaBMais(true, this.ordem);
            novaFolha.posicao = alocarNovaPagina();

            int meio = this.ordem / 2;
            int qtdDireita = pag.n - meio;

            for (int j = 0; j < qtdDireita; j++) {
                novaFolha.chaves[j] = pag.chaves[meio + j];
                novaFolha.dados[j] = pag.dados[meio + j];
                pag.chaves[meio + j] = 0;
                pag.dados[meio + j] = -1;
            }
            novaFolha.n = qtdDireita;
            pag.n = meio;

            // Ajusta o encadeamento de folhas
            novaFolha.proximaFolha = pag.proximaFolha;
            pag.proximaFolha = novaFolha.posicao;

            escreverPagina(pag);
            escreverPagina(novaFolha);

            // A chave promovida para o pai é uma COPIA da primeira chave da folha direita
            int chavePromovida = novaFolha.chaves[0];
            return new RetornoPromocao(chavePromovida, novaFolha.posicao);

        } else {
            // Desce recursivamente para o filho apropriado
            int i = 0;
            while (i < pag.n && chave >= pag.chaves[i]) {
                i++;
            }

            RetornoPromocao promoFilho = inserirRecursivo(pag.filhos[i], chave, posicaoDados);
            if (promoFilho == null) {
                return null; // Nao houve divisao no filho
            }

            // Insere a chave promovida e o ponteiro do novo filho nesta pagina interna
            int j = pag.n - 1;
            while (j >= i) {
                pag.chaves[j + 1] = pag.chaves[j];
                pag.filhos[j + 2] = pag.filhos[j + 1];
                j--;
            }
            pag.chaves[i] = promoFilho.chavePromovida;
            pag.filhos[i + 1] = promoFilho.filhoDireitoPos;
            pag.n++;

            // Se nao excedeu a capacidade de chaves (ordem - 1)
            if (pag.n < this.ordem) {
                escreverPagina(pag);
                return null;
            }

            // realiza split do no interno
            PaginaBMais novoInterno = new PaginaBMais(false, this.ordem);
            novoInterno.posicao = alocarNovaPagina();

            int meio = this.ordem / 2;
            int chaveSubindo = pag.chaves[meio]; // O elemento do meio sobe e sai do no interno

            int qtdDireita = pag.n - meio - 1;
            for (int k = 0; k < qtdDireita; k++) {
                novoInterno.chaves[k] = pag.chaves[meio + 1 + k];
                novoInterno.filhos[k] = pag.filhos[meio + 1 + k];
                pag.chaves[meio + 1 + k] = 0;
                pag.filhos[meio + 1 + k] = -1;
            }
            novoInterno.filhos[qtdDireita] = pag.filhos[pag.n];
            pag.filhos[pag.n] = -1;
            novoInterno.n = qtdDireita;

            pag.chaves[meio] = 0;
            pag.n = meio;

            escreverPagina(pag);
            escreverPagina(novoInterno);

            return new RetornoPromocao(chaveSubindo, novoInterno.posicao);
        }
    }

    public boolean excluir(int chave) throws IOException {
        if (this.raiz == -1) {
            return false;
        }

        long posAtual = this.raiz;
        while (posAtual != -1) {
            PaginaBMais pag = lerPagina(posAtual);
            if (pag == null) {
                return false;
            }

            if (pag.isFolha) {
                int indice = -1;
                for (int i = 0; i < pag.n; i++) {
                    if (pag.chaves[i] == chave) {
                        indice = i;
                        break;
                    }
                }

                if (indice == -1) {
                    return false;
                }

                // Desloca elementos a esquerda
                for (int i = indice; i < pag.n - 1; i++) {
                    pag.chaves[i] = pag.chaves[i + 1];
                    pag.dados[i] = pag.dados[i + 1];
                }
                pag.chaves[pag.n - 1] = 0;
                pag.dados[pag.n - 1] = -1;
                pag.n--;

                escreverPagina(pag);

                // Se a raiz era uma folha e ficou vazia
                if (pag.posicao == this.raiz && pag.n == 0) {
                    this.raiz = -1;
                    this.primeiraFolha = -1;
                    atualizarCabecalho();
                }

                return true;
            } else {
                int i = 0;
                while (i < pag.n && chave >= pag.chaves[i]) {
                    i++;
                }
                posAtual = pag.filhos[i];
            }
        }

        return false;
    }

    public List<EntradaIndice> listarTodos() throws IOException {
        List<EntradaIndice> lista = new ArrayList<>();
        long posAtual = this.primeiraFolha;

        while (posAtual != -1) {
            PaginaBMais folha = lerPagina(posAtual);
            if (folha == null) {
                break;
            }
            for (int i = 0; i < folha.n; i++) {
                lista.add(new EntradaIndice(folha.chaves[i], folha.dados[i]));
            }
            posAtual = folha.proximaFolha;
        }

        return lista;
    }

    public List<Long> buscarFaixa(int idInicio, int idFim) throws IOException {
        List<Long> posicoes = new ArrayList<>();
        if (this.raiz == -1 || idInicio > idFim) {
            return posicoes;
        }

        // Desce ate a folha onde idInicio deveria estar
        long posAtual = this.raiz;
        while (posAtual != -1) {
            PaginaBMais pag = lerPagina(posAtual);
            if (pag == null) {
                break;
            }

            if (pag.isFolha) {
                // A partir desta folha, percorre sequencialmente ate ultrapassar idFim
                boolean parar = false;
                long folhaOffset = posAtual;

                while (folhaOffset != -1 && !parar) {
                    PaginaBMais folha = lerPagina(folhaOffset);
                    if (folha == null) break;

                    for (int i = 0; i < folha.n; i++) {
                        if (folha.chaves[i] >= idInicio && folha.chaves[i] <= idFim) {
                            posicoes.add(folha.dados[i]);
                        } else if (folha.chaves[i] > idFim) {
                            parar = true;
                            break;
                        }
                    }
                    folhaOffset = folha.proximaFolha;
                }
                break;
            } else {
                int i = 0;
                while (i < pag.n && idInicio >= pag.chaves[i]) {
                    i++;
                }
                posAtual = pag.filhos[i];
            }
        }

        return posicoes;
    }

    public void limpar() throws IOException {
        this.arq.setLength(0);
        this.raiz = -1;
        this.primeiraFolha = -1;
        atualizarCabecalho();
    }


    public void imprimirArvore() throws IOException {
        if (this.raiz == -1) {
            System.out.println("Arvore B+ esta vazia.");
            return;
        }

        System.out.println("Estrutura da Arvore B+ (Ordem " + this.ordem + ")");
        Queue<Long> fila = new LinkedList<>();
        Queue<Integer> niveis = new LinkedList<>();

        fila.add(this.raiz);
        niveis.add(0);

        int nivelAtual = -1;

        while (!fila.isEmpty()) {
            long pos = fila.poll();
            int nivel = niveis.poll();

            if (nivel != nivelAtual) {
                nivelAtual = nivel;
                System.out.print("\nNivel " + nivelAtual + ": ");
            }

            PaginaBMais pag = lerPagina(pos);
            if (pag != null) {
                System.out.print("[");
                for (int i = 0; i < pag.n; i++) {
                    System.out.print(pag.chaves[i]);
                    if (pag.isFolha) {
                        System.out.print("(@" + pag.dados[i] + ")");
                    }
                    if (i < pag.n - 1) System.out.print(" | ");
                }
                System.out.print("] ");

                if (!pag.isFolha) {
                    for (int i = 0; i <= pag.n; i++) {
                        if (pag.filhos[i] != -1) {
                            fila.add(pag.filhos[i]);
                            niveis.add(nivel + 1);
                        }
                    }
                }
            }
        }
        System.out.println("\n");
    }

    public int getOrdem() {
        return this.ordem;
    }

    public long getRaiz() {
        return this.raiz;
    }

    public long getPrimeiraFolha() {
        return this.primeiraFolha;
    }

    public int getTamanhoPagina() {
        return this.tamanhoPagina;
    }

    public void fechar() throws IOException {
        if (this.arq != null) {
            this.arq.close();
        }
    }

    public void close() throws IOException {
        fechar();
    }
}
