package Src;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static String arq_Csv = "Src/Base de Dados/bd.csv";
    private static String arq_Binario = "Src/Base de Dados/incidentes.db";
    private static String arq_IndiceB = "Src/Base de Dados/indice_arvore_bmais.db";
    private static Scanner sc = new Scanner(System.in);
    private static CRUD crud;
    private static ArvoreBMais arvoreBMais;

    public static void main(String[] args) {
        crud = new CRUD(arq_Binario);
        try {
            arvoreBMais = new ArvoreBMais(arq_IndiceB, 8);
        } catch (IOException e) {
            System.out.println("Aviso: Nao foi possivel inicializar a Arvore B+: " + e.getMessage());
        }

        while (true) {
            exibirMenu();
            System.out.print("Digite uma opcao: ");
            String entrada = sc.nextLine().trim();

            int opcao = -1;
            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            System.out.println();

            switch (opcao) {
                case 1:
                    menuCarregarBase();
                    break;
                case 2:
                    menuCriar();
                    break;
                case 3:
                    menuLer();
                    break;
                case 4:
                    menuAtualizar();
                    break;
                case 5:
                    menuDeletar();
                    break;
                case 6:
                    menuListar();
                    break;
                case 7:
                    menuOrdenacaoExterna();
                    break;
                case 8:
                    menuIndexarArvoreB();
                    break;
                case 9:
                    menuVisualizarArvoreB();
                    break;
                case 10:
                    menuBuscarFaixaArvoreB();
                    break;
                case 0:
                    if (arvoreBMais != null) {
                        try {
                            arvoreBMais.fechar();
                        } catch (IOException ignored) {
                        }
                    }
                    System.out.println("Encerrando o programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n1 - Carregar Base de Dados (CSV -> Binario)");
        System.out.println("2 - Inserir Incidente (Create)");
        System.out.println("3 - Buscar Incidente por ID (Read)");
        System.out.println("4 - Atualizar Incidente (Update)");
        System.out.println("5 - Excluir Incidente (Delete)");
        System.out.println("6 - Listar Registros");
        System.out.println("7 - Ordenacao Externa");
        System.out.println("8 - Indexar Base na Arvore B+");
        System.out.println("9 - Visualizar Estrutura da Arvore B+");
        System.out.println("10 - Buscar por Faixa na Arvore B+");
        System.out.println("0 - Sair\n");
    }

    // Le o CSV e passa os dados pro binario
    private static void menuCarregarBase() {
        System.out.println("Carga da Base de Dados");
        try {
            int total = crud.carregarCSV(arq_Csv);
            System.out.println("Base carregada");
            System.out.println("Total de registros importados: " + total);
            System.out.println("Ultimo ID cadastrado: " + crud.obterUltimoId());

            if (arvoreBMais != null) {
                System.out.print("Deseja tambem indexar a base na Arvore B+ agora? (s/n): ");
                String resp = sc.nextLine().trim().toLowerCase();
                if (resp.equals("s") || resp.equals("sim")) {
                    menuIndexarArvoreB();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar o CSV: " + e.getMessage());
        }
    }

    // Cria um novo incidente manualmente
    private static void menuCriar() {
        System.out.println("Novo Incidente");

        try {
            Incidente novo = new Incidente();

            System.out.print("Incident Date (LocalDate - YYYY-MM-DD): ");
            String strData = sc.nextLine().trim();
            if (!strData.isEmpty()) {
                try {
                    novo.setIncidentDate(LocalDate.parse(strData, DateTimeFormatter.ISO_LOCAL_DATE));
                } catch (DateTimeParseException e) {
                    System.out.println("Formato invalido. Usando a data de hoje.");
                    novo.setIncidentDate(LocalDate.now());
                }
            } else {
                novo.setIncidentDate(LocalDate.now());
            }

            System.out.print("Aircraft Model (String): ");
            novo.setAircraftModel(sc.nextLine().trim());

            System.out.print("Aircraft Registration (String - 10 chars): ");
            novo.setAircraftRegistration(sc.nextLine().trim());

            System.out.print("Aircraft Operator (String): ");
            novo.setAircraftOperator(sc.nextLine().trim());

            System.out.print("Aircraft Nature (String): ");
            novo.setAircraftNature(sc.nextLine().trim());

            System.out.print("Incident Category (String): ");
            novo.setIncidentCategory(sc.nextLine().trim());

            System.out.print("Incident Causes (String - separado por ';'): ");
            novo.setIncidentCauses(sc.nextLine().trim());

            System.out.print("Incident Location (String): ");
            novo.setIncidentLocation(sc.nextLine().trim());

            System.out.print("Aircraft Damage Type (String): ");
            novo.setAircraftDamageType(sc.nextLine().trim());

            System.out.print("Date (String): ");
            novo.setDate(sc.nextLine().trim());

            System.out.print("Time (String): ");
            novo.setTime(sc.nextLine().trim());

            System.out.print("Arit (String): ");
            novo.setArit(sc.nextLine().trim());

            System.out.print("Aircraft Engines (String): ");
            novo.setAircraftEngines(sc.nextLine().trim());

            System.out.print("Onboard Crew (int): ");
            novo.setOnboardCrew(CRUD.somaNumStrings(sc.nextLine().trim()));

            System.out.print("Onboard Passengers (int): ");
            novo.setOnboardPassengers(CRUD.somaNumStrings(sc.nextLine().trim()));

            System.out.print("Onboard Total (int): ");
            novo.setOnboardTotal(CRUD.somaNumStrings(sc.nextLine().trim()));

            System.out.print("Fatalities (int): ");
            String strFatalidades = sc.nextLine().trim();
            int fatalidades = 0;
            if (!strFatalidades.isEmpty()) {
                try {
                    fatalidades = Integer.parseInt(strFatalidades);
                } catch (NumberFormatException e) {
                    fatalidades = 0;
                }
            }
            novo.setFatalities(fatalidades);

            System.out.print("Aircraft First Flight (String): ");
            novo.setAircraftFirstFlight(sc.nextLine().trim());

            System.out.print("Aircraft Phase (String): ");
            novo.setAircraftPhase(sc.nextLine().trim());

            System.out.print("Departure Airport (String): ");
            novo.setDepartureAirport(sc.nextLine().trim());

            System.out.print("Destination Airport (String): ");
            novo.setDestinationAirport(sc.nextLine().trim());

            System.out.print("Ground Casualties (int): ");
            novo.setGroundCasualties(CRUD.somaNumStrings(sc.nextLine().trim()));

            System.out.print("Collision Casualties (int): ");
            novo.setCollisionCasualties(CRUD.somaNumStrings(sc.nextLine().trim()));

            System.out.print("\nMetodo de insercao (1 - Sequencial | 2 - Arvore B+): ");
            String metodo = sc.nextLine().trim();

            int idGerado;
            if (metodo.equals("2") && arvoreBMais != null) {
                idGerado = crud.createComIndiceB(novo, arvoreBMais);
                System.out.println("\nIncidente criado com sucesso (com indice Arvore B+)! ID gerado: " + idGerado);
            } else {
                idGerado = crud.create(novo);
                System.out.println("\nIncidente criado com sucesso (Sequencial)! ID gerado: " + idGerado);
            }

        } catch (IOException e) {
            System.out.println("Erro ao criar incidente: " + e.getMessage());
        }
    }

    // Busca incidente por ID
    private static void menuLer() {
        System.out.println("--- Buscar Incidente ---");
        System.out.print("Digite o ID: ");
        String strId = sc.nextLine().trim();

        try {
            int id = Integer.parseInt(strId);

            System.out.print("Metodo de busca (1 - Sequencial no arquivo | 2 - Indice Arvore B+): ");
            String metodo = sc.nextLine().trim();

            Incidente inc;
            if (metodo.equals("2") && arvoreBMais != null) {
                long tInicio = System.nanoTime();
                inc = crud.readComIndiceB(id, arvoreBMais);
                long tFim = System.nanoTime();
                System.out.printf("Tempo de busca (Arvore B+): %.4f ms\n", (tFim - tInicio) / 1e6);
            } else {
                long tInicio = System.nanoTime();
                inc = crud.read(id);
                long tFim = System.nanoTime();
                System.out.printf("Tempo de busca (Sequencial): %.4f ms\n", (tFim - tInicio) / 1e6);
            }

            if (inc != null) {
                System.out.println(inc.imprimirDetalhado());
            } else {
                System.out.println("Incidente com ID " + id + " nao encontrado ou excluido.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID invalido! Digite um numero inteiro.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    // Atualiza dados de um incidente
    private static void menuAtualizar() {
        System.out.println("--- Atualizar Incidente ---");
        System.out.print("Digite o ID do incidente: ");
        String strId = sc.nextLine().trim();

        try {
            int id = Integer.parseInt(strId);
            Incidente incidente = crud.read(id);

            if (incidente == null) {
                System.out.println("Incidente com ID " + id + " nao encontrado.");
                return;
            }

            System.out.println("\nDados atuais:");
            System.out.println(incidente.imprimirDetalhado());
            System.out.println("Digite os novos valores (Enter vazio mantem o valor atual):\n");

            System.out.print("Aircraft Model [" + incidente.getAircraftModel() + "]: ");
            String modelo = sc.nextLine().trim();
            if (!modelo.isEmpty()) {
                incidente.setAircraftModel(modelo);
            }

            System.out.print("Aircraft Registration (10 chars) [" + incidente.getAircraftRegistration() + "]: ");
            String registroAeronave = sc.nextLine().trim();
            if (!registroAeronave.isEmpty()) {
                incidente.setAircraftRegistration(registroAeronave);
            }

            System.out.print("Aircraft Operator [" + incidente.getAircraftOperator() + "]: ");
            String operador = sc.nextLine().trim();
            if (!operador.isEmpty()) {
                incidente.setAircraftOperator(operador);
            }

            System.out.print("Incident Causes (';') [" + incidente.getIncidentCauses() + "]: ");
            String causas = sc.nextLine().trim();
            if (!causas.isEmpty()) {
                incidente.setIncidentCauses(causas);
            }

            System.out.print("Incident Location [" + incidente.getIncidentLocation() + "]: ");
            String localizacao = sc.nextLine().trim();
            if (!localizacao.isEmpty()) {
                incidente.setIncidentLocation(localizacao);
            }

            System.out.print("Aircraft Damage Type [" + incidente.getAircraftDamageType() + "]: ");
            String tipoDano = sc.nextLine().trim();
            if (!tipoDano.isEmpty()) {
                incidente.setAircraftDamageType(tipoDano);
            }

            System.out.print("Aircraft Phase [" + incidente.getAircraftPhase() + "]: ");
            String faseVoo = sc.nextLine().trim();
            if (!faseVoo.isEmpty()) {
                incidente.setAircraftPhase(faseVoo);
            }

            System.out.print("Fatalities [" + incidente.getFatalities() + "]: ");
            String strFatalidades = sc.nextLine().trim();
            if (!strFatalidades.isEmpty()) {
                try {
                    incidente.setFatalities(Integer.parseInt(strFatalidades));
                } catch (NumberFormatException ignored) {
                }
            }

            System.out.print("\nMetodo de atualizacao (1 - Sequencial | 2 - Arvore B+): ");
            String metodo = sc.nextLine().trim();

            boolean ok;
            if (metodo.equals("2") && arvoreBMais != null) {
                ok = crud.updateComIndiceB(incidente, arvoreBMais);
            } else {
                ok = crud.update(incidente);
            }

            if (ok) {
                System.out.println("\nIncidente " + id + " atualizado com sucesso!");
            } else {
                System.out.println("\nNao foi possivel atualizar o incidente.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID invalido. Digite um numero.");
        } catch (IOException e) {
            System.out.println("Erro na atualizacao: " + e.getMessage());
        }
    }

    // Exclui por ID
    private static void menuDeletar() {
        System.out.print("Digite o ID: ");
        String strId = sc.nextLine().trim();

        try {
            int id = Integer.parseInt(strId);
            System.out.print("Certeza que deseja excluir o incidente ID " + id + "? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();

            if (resp.equals("s") || resp.equals("sim")) {
                System.out.print("Metodo de exclusao (1 - Sequencial | 2 - Arvore B+): ");
                String metodo = sc.nextLine().trim();

                boolean deletado;
                if (metodo.equals("2") && arvoreBMais != null) {
                    deletado = crud.deleteComIndiceB(id, arvoreBMais);
                } else {
                    deletado = crud.delete(id);
                }
                if (deletado) {
                    System.out.println("Incidente " + id + " excluido com sucesso.");
                } else {
                    System.out.println("Incidente " + id + " nao encontrado ou ja excluido.");
                }
            } else {
                System.out.println("Exclusao cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (IOException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
        }
    }

    // Lista os primeiros registros
    private static void menuListar() {
        System.out.print("Quantidade de registros para exibir (padrao 10): ");
        String entrada = sc.nextLine().trim();

        int limite = 10;
        if (!entrada.isEmpty()) {
            try {
                limite = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                limite = 10;
            }
        }

        try {
            List<Incidente> lista = crud.listar(limite);
            if (lista.isEmpty()) {
                System.out.println("Nenhum registro encontrado.");
            } else {
                for (Incidente inc : lista) {
                    System.out.println("\n" + inc.imprimir());
                }
                System.out.println("Total exibido: " + lista.size() + " registro(s)");
            }
        } catch (IOException e) {
            System.out.println("Erro ao listar registros: " + e.getMessage());
        }
    }

    private static void menuOrdenacaoExterna() {
        try {
            System.out.print("Numero de caminhos (minimo 2): ");
            String strCaminhos = sc.nextLine().trim();

            System.out.print("Numero maximo de registros em memoria primaria (minimo 1): ");
            String strTamBloco = sc.nextLine().trim();

            OrdenacaoExterna ord = new OrdenacaoExterna(arq_Binario);
            ord.ordenar(strCaminhos, strTamBloco);

        } catch (IOException e) {
            System.out.println("Erro durante a ordenacao externa: " + e.getMessage());
        }
    }

    // Indexa toda a base existente na Arvore B+
    private static void menuIndexarArvoreB() {
        System.out.println("\n--- Indexando Base com Arvore B+ ---");
        try {
            if (arvoreBMais == null) {
                arvoreBMais = new ArvoreBMais(arq_IndiceB, 8);
            }
            long inicio = System.currentTimeMillis();
            int total = crud.indexarBaseComArvoreB(arvoreBMais);
            long fim = System.currentTimeMillis();
            System.out.println("Indexacao concluida com sucesso!");
            System.out.println("Total de registros indexados: " + total);
            System.out.println("Tempo decorrido: " + (fim - inicio) + " ms");
        } catch (IOException e) {
            System.out.println("Erro ao indexar base: " + e.getMessage());
        }
    }

    // Imprime estrutura por niveis da Arvore B+
    private static void menuVisualizarArvoreB() {
        try {
            if (arvoreBMais == null) {
                arvoreBMais = new ArvoreBMais(arq_IndiceB, 8);
            }
            arvoreBMais.imprimirArvore();
        } catch (IOException e) {
            System.out.println("Erro ao visualizar Arvore B+: " + e.getMessage());
        }
    }

    // Busca por intervalo usando o encadeamento de folhas
    private static void menuBuscarFaixaArvoreB() {
        System.out.println("\n--- Busca por Faixa de IDs (Arvore B+) ---");
        try {
            if (arvoreBMais == null) {
                arvoreBMais = new ArvoreBMais(arq_IndiceB, 8);
            }
            System.out.print("ID inicial: ");
            int idInicio = Integer.parseInt(sc.nextLine().trim());
            System.out.print("ID final: ");
            int idFim = Integer.parseInt(sc.nextLine().trim());

            long inicio = System.nanoTime();
            List<Incidente> lista = crud.buscarFaixaComIndiceB(idInicio, idFim, arvoreBMais);
            long fim = System.nanoTime();

            if (lista.isEmpty()) {
                System.out.println("Nenhum registro encontrado no intervalo [" + idInicio + " - " + idFim + "].");
            } else {
                for (Incidente inc : lista) {
                    System.out.println(inc.imprimir());
                }
                System.out.println("\nTotal encontrado: " + lista.size() + " registro(s)");
                System.out.printf("Tempo de busca por faixa: %.4f ms\n", (fim - inicio) / 1e6);
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (IOException e) {
            System.out.println("Erro na busca por faixa: " + e.getMessage());
        }
    }
}
