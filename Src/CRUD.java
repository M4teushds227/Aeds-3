package Src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CRUD {

    private final String caminhoArquivo;
    // Marcadores de lapide para saber se o registro foi excluido
    private byte lapideValido = ' ';
    private byte lapideExcluido = '*';

    public CRUD(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        inicializarArquivo();
    }

    // Cria o arquivo se ainda nao existir e inicializa o cabecalho com id 0
    private void inicializarArquivo() {
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists()) {
            try {
                try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "rw")) {
                    escritaArqBin.writeInt(0);
                }
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo: " + e.getMessage());
            }
        }
    }

    // Le o CSV e passa os registros pro arquivo binario
    public int carregarCSV(String caminhoCSV) throws IOException {
        File csvArq = new File(caminhoCSV);
        if (!csvArq.exists()) {
            throw new IOException("CSV nao encontrado: " + caminhoCSV);
        }

        File binArq = new File(this.caminhoArquivo);
        if (binArq.exists()) {
            binArq.delete(); // apaga o antigo pra recriar do zero
        }

        int totalRegistros = 0;

        try (RandomAccessFile escritaArqBin = new RandomAccessFile(binArq, "rw");
                BufferedReader entArqCsv = new BufferedReader(new FileReader(csvArq))) {

            // reserva os primeiros 4 bytes pro cabecalho
            escritaArqBin.writeInt(0);

            // pula a linha com os nomes das colunas
            String linha = entArqCsv.readLine();
            if (linha == null) {
                return 0;
            }

            while ((linha = entArqCsv.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Incidente temp = CriarIncidente(linha, totalRegistros + 1);
                    if (temp != null) {
                        totalRegistros++;
                        byte[] dados = temp.arrEmBytes();

                        // grava lapide, tamanho e os bytes do registro
                        escritaArqBin.writeByte(lapideValido);
                        escritaArqBin.writeInt(dados.length);
                        escritaArqBin.write(dados);
                    }
                }
            }

            // volta no comeco e grava o maior id no cabecalho
            escritaArqBin.seek(0);
            escritaArqBin.writeInt(totalRegistros);
        }

        return totalRegistros;
    }

    // Função pra verificar se não esta faltando alguma coluna
    private String lerColuna(List<String> colunas, int indice) {
        if (indice < colunas.size()) {
            return colunas.get(indice).trim();
        }
        return "";
    }

    // o texto vem com palavras e numeros ex: "Fatalities: 0 / Occupants: 7" essa
    // função soma eles
    public static int somaNumStrings(String texto) {
        if (texto == null || texto.isEmpty()) {
            return 0;
        }
        int soma = 0;
        String[] partes = texto.split("[^0-9]+");
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                try {
                    soma += Integer.parseInt(parte);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return soma;
    }

    // Criação do Incidente
    private Incidente CriarIncidente(String linha, int id) {
        try {
            List<String> colunas = parseCsv(linha);
            if (colunas.size() < 17) {
                return null;
            }

            LocalDate incidentDate = null;
            String strData = colunas.get(0).trim();
            if (!strData.isEmpty()) {
                try {
                    incidentDate = LocalDate.parse(strData, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    incidentDate = null;
                }
            }

            String aircraftModel = colunas.get(1).trim();
            String aircraftRegistration = colunas.get(2).trim();
            String aircraftOperator = colunas.get(3).trim();
            String aircraftNature = colunas.get(4).trim();
            String incidentCategory = colunas.get(5).trim();
            String incidentCauses = colunas.get(6).trim();
            String incidentLocation = colunas.get(7).trim();
            String aircraftDamageType = colunas.get(8).trim();
            String date = colunas.get(9).trim();
            String time = colunas.get(10).trim();
            String arit = colunas.get(11).trim();
            String aircraftEngines = colunas.get(12).trim();
            int onboardCrew = somaNumStrings(colunas.get(13));
            int onboardPassengers = somaNumStrings(colunas.get(14));
            int onboardTotal = somaNumStrings(colunas.get(15));
            int fatalities = 0;
            String strFatalities = colunas.get(16).trim();
            if (!strFatalities.isEmpty()) {
                try {
                    fatalities = Integer.parseInt(strFatalities);
                } catch (NumberFormatException e) {
                    fatalities = 0;
                }
            }
            String aircraftFirstFlight = lerColuna(colunas, 17);
            String aircraftPhase = lerColuna(colunas, 18);
            String departureAirport = lerColuna(colunas, 19);
            String destinationAirport = lerColuna(colunas, 20);
            int groundCasualties = somaNumStrings(lerColuna(colunas, 21));
            int collisionCasualties = somaNumStrings(lerColuna(colunas, 22));
            return new Incidente(id, incidentDate, aircraftModel, aircraftRegistration,
                    aircraftOperator, aircraftNature, incidentCategory, incidentCauses,
                    incidentLocation, aircraftDamageType, date, time, arit,
                    aircraftEngines, onboardCrew, onboardPassengers, onboardTotal,
                    fatalities, aircraftFirstFlight, aircraftPhase, departureAirport,
                    destinationAirport, groundCasualties, collisionCasualties);

        } catch (Exception e) {
            return null;
        }
    }

    // Separa as colunas da linha tratando virgulas dentro de aspas
    public static List<String> parseCsv(String linha) {
        List<String> colunas = new ArrayList<>();
        // guarda a string atual antes da virgula
        StringBuilder stringAtual = new StringBuilder();
        boolean entreAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') {
                entreAspas = !entreAspas;
            } else if (c == ',' && !entreAspas) {
                colunas.add(stringAtual.toString());
                stringAtual.setLength(0);
            } else {
                stringAtual.append(c);
            }
        }
        colunas.add(stringAtual.toString());
        return colunas;
    }

    // Insere o novo registro no fim do arquivo e incrementa o cabecalho
    public int create(Incidente incidente) throws IOException {
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists()) {
            inicializarArquivo();
        }

        try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "rw")) {
            // le o ultimo id do cabecalho (primeiros 4 bytes)
            escritaArqBin.seek(0);
            int ultimoId = 0;
            if (escritaArqBin.length() >= 4) {
                ultimoId = escritaArqBin.readInt();
            }

            int novoId = ultimoId + 1;
            incidente.setId(novoId);

            byte[] dados = incidente.arrEmBytes();

            // grava no fim do arquivo
            escritaArqBin.seek(escritaArqBin.length());
            escritaArqBin.writeByte(lapideValido);
            escritaArqBin.writeInt(dados.length);
            escritaArqBin.write(dados);

            // atualiza o cabecalho com o novo id
            escritaArqBin.seek(0);
            escritaArqBin.writeInt(novoId);

            return novoId;
        }
    }

    // Read
    public Incidente read(int id) throws IOException {
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists() || arq.length() < 4) {
            return null;
        }

        try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "r")) {
            escritaArqBin.seek(4); // pula o cabecalho

            while (escritaArqBin.getFilePointer() < escritaArqBin.length()) {
                byte lapide = escritaArqBin.readByte();
                int tamanho = escritaArqBin.readInt();

                if (lapide == lapideValido) {
                    byte[] ba = new byte[tamanho];
                    escritaArqBin.readFully(ba);

                    Incidente inc = new Incidente();
                    inc.bytesEmArr(ba);

                    if (inc.getId() == id) {
                        return inc;
                    }
                } else {
                    escritaArqBin.skipBytes(tamanho); // registro excluido, so pula
                }
            }
        }
        return null;
    }

    // Update
    public boolean update(Incidente novoIncidente) throws IOException {
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists() || arq.length() < 4) {
            return false;
        }

        try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "rw")) {
            escritaArqBin.seek(4); // pula cabecalho

            while (escritaArqBin.getFilePointer() < escritaArqBin.length()) {
                long posAtual = escritaArqBin.getFilePointer();
                byte lapide = escritaArqBin.readByte();
                int tamanhoOriginal = escritaArqBin.readInt();

                if (lapide == lapideValido) {
                    byte[] ba = new byte[tamanhoOriginal];
                    escritaArqBin.readFully(ba);

                    Incidente inc = new Incidente();
                    inc.bytesEmArr(ba);

                    if (inc.getId() == novoIncidente.getId()) {
                        byte[] novosBytes = novoIncidente.arrEmBytes();

                        if (novosBytes.length == tamanhoOriginal) {
                            // mesmo tamanho: sobrescreve no mesmo lugar
                            escritaArqBin.seek(posAtual + 1 + 4); // pula lapide (1) e tamanho (4)
                            escritaArqBin.write(novosBytes);
                        } else {
                            // tamanho diferente: marca o antigo como excluido e grava o novo no final
                            escritaArqBin.seek(posAtual);
                            escritaArqBin.writeByte(lapideExcluido);

                            escritaArqBin.seek(escritaArqBin.length());
                            escritaArqBin.writeByte(lapideValido);
                            escritaArqBin.writeInt(novosBytes.length);
                            escritaArqBin.write(novosBytes);
                        }
                        return true;
                    }
                } else {
                    escritaArqBin.skipBytes(tamanhoOriginal);
                }
            }
        }

        return false;
    }

    // Delete
    public boolean delete(int id) throws IOException {
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists() || arq.length() < 4) {
            return false;
        }

        try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "rw")) {
            escritaArqBin.seek(4); // pula cabecalho

            while (escritaArqBin.getFilePointer() < escritaArqBin.length()) {
                long posAtual = escritaArqBin.getFilePointer();
                byte lapide = escritaArqBin.readByte();
                int tamanho = escritaArqBin.readInt();
                if (lapide == lapideValido) {
                    byte[] ba = new byte[tamanho];
                    escritaArqBin.readFully(ba);
                    Incidente inc = new Incidente();
                    inc.bytesEmArr(ba);
                    if (inc.getId() == id) {
                        escritaArqBin.seek(posAtual);
                        escritaArqBin.writeByte(lapideExcluido);
                        return true;
                    }
                } else {
                    escritaArqBin.skipBytes(tamanho);
                }
            }
        }

        return false;
    }

    // Le o ultimo id gravado no cabecalho
    public int obterUltimoId() throws IOException {
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists() || arq.length() < 4) {
            return 0;
        }
        try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "r")) {
            escritaArqBin.seek(0);
            return escritaArqBin.readInt();
        }
    }

    // Lista os primeiros 'limite' registros validos
    public List<Incidente> listar(int limite) throws IOException {
        if (limite <= 0) {
            limite = 10;
        }
        List<Incidente> lista = new ArrayList<>();
        File arq = new File(this.caminhoArquivo);
        if (!arq.exists() || arq.length() < 4) {
            return lista;
        }

        try (RandomAccessFile escritaArqBin = new RandomAccessFile(arq, "r")) {
            escritaArqBin.seek(4);

            while (escritaArqBin.getFilePointer() < escritaArqBin.length() && lista.size() < limite) {
                byte lapide = escritaArqBin.readByte();
                int tamanho = escritaArqBin.readInt();

                if (lapide == lapideValido) {
                    byte[] ba = new byte[tamanho];
                    escritaArqBin.readFully(ba);

                    Incidente inc = new Incidente();
                    inc.bytesEmArr(ba);
                    lista.add(inc);
                } else {
                    escritaArqBin.skipBytes(tamanho);
                }
            }
        }

        return lista;
    }
}
