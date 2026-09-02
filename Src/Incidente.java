

import java.time.LocalDate;
//Porque você decidiu que no registro do avião(o tipo de dado) deveria ter tab no final? O que exatamente isso adiciona? Foi para poder dar barriga ao dado? Se sim talvez isso tenha cido desnecessario.
class Incidente {
    private int id;
    private LocalDate dataIncidente; // Acabei de reparar em um problema desse tipo que você está utilizando, tem ??? e sabendo que essa classe não armazena isso então não faz sentido usar-la, se você quiser fazer uma analise depois tudo bem mas se a solução é colocar as interrogações como 0 talvez não faça sentido pois é desconhecido então talvez não fique muito legal eu acho. Sem contar que tera que ser inicializado um objeto LocalDate antes de pegar a informação do texto para ai sim passar ele como parametro então você deixou o tratamento de dados para o main? Seria melhor que tivesse outra classe que contesse todos os acidentes e ela que saberia tratar os dados antes de passar, chamo a atenção para isso pois não tem nenhum comentario no código mais aprofundado falando quais classes vai terou o que planeja caso tivesse inacabado.
    private String modeloAeronave;
    private String registroAeronave; // String de tamanho fixo (10 caracteres) //talvez sabendo que só pode ter até no maximo 10 characteres seria melhor ter um char fixo de 11 posições.
    private String operador;
    private String naturezaAeronave;
    private String categoriaIncidente;
    private String causas; // Lista de valores com separador ';' //talvez seria melhor clissificar isso como um array de strings já que podem ter mais de uma e elas separadas por;
    private String localizacao;
    private String tipoDano;
    private String dataTexto; // Eu pesquisei um pouco sobre o LocalDate que você quis usar e ele seria ideal, só que tem um pequeno problema, ainda tem aquele problema de strings(talvez nem precisaria de outra variavel já que localdate faz isso aparentemente).
    private String horario; // Percebi que tem horarios que estão vazios na base de dados então como deve ser tratado isso?
    private String arit; // Eles tem o mesmo problema de ter ??
    private String motores;
    private String tripulacao; // Mesma sugestão/observação de fatalidades.
    private String passageiros; // Mesma sugestão/observação de fatalidades.
    private String totalAboard; // Mesma sugestão/observação de fatalidades.
    private int fatalidades; // Não faz muito sentido termos todos esses dados fragmentados desssa forma, o melhor nesse caso seria criar uma nova classe chamada DadosFatalidades, porque? pois tudo poderia ser acessado dentro dessa classe assim sendo necessario apenas passar os valores e chamar os metodos ao invez de deixar tudo separado, ai o que devemos fazer quando tiver que passar para bytes? Só pensar na organização dos bytes e extrair dessa classe sem muitos prolemas.
    private String primeiroVoo;
    private String faseVoo;
    private String aeroportoPartida;
    private String aeroportoDestino;
    private String vitimasSolo; // Pelo que eu vi na base poderia ser do tipo int vitimasSolo e vitimasColisao, sugiro que seja um atriuto da classe DadosFatalidades
    private String vitimasColisao;

    // Construtor Vazio
    public Incidente() { // Porque esse construtor existe sendo que ele faz nada? O certo seria comentar-lo para ai criar alguma utilidade para ele depois, porque? E se o programador sem querer ver que é possivel criar um ojeto do tipo Incidente sem argumento nenhum como ele será capaz de inserir os dados depois? Principalmente dependendo de como for feito a declaração os acessos pode ser impossivel de alterar.
    }

    // Construtor Completo
    public Incidente(
    int id, 
    LocalDate dataIncidente, 
    String modeloAeronave, 
    String registroAeronave,
    String operador, 
    String naturezaAeronave, 
    String categoriaIncidente, 
    String causas,
    String localizacao, 
    String tipoDano, 
    String dataTexto, 
    String horario, 
    String arit,
    String motores, 
    String tripulacao, 
    String passageiros, 
    String totalAboard,
    int fatalidades, 
    String primeiroVoo, 
    String faseVoo, 
    String aeroportoPartida,
    String aeroportoDestino, 
    String vitimasSolo, 
    String vitimasColisao){
        this.id = id;
        this.dataIncidente = dataIncidente;
        this.modeloAeronave = modeloAeronave;
        setRegistroAeronave(registroAeronave); // Infelizmente sinto informar que esse metodo não faz o menor sentido pois o tratamento de dados NÃO deveria ser feito dentro do metodo construtor da classe, o que poderia ser feito é a criação de um metodo estatico que trataria os erros e ai sim esse metodo chama o construtor, mas eu acredito que não seria uma boa alternativa a outra classe que engloasse a classe acidentes pois sera necessario de qualquer forma criar um array para armazenar cada acidente então melhor ter uma classe que faz isso e a cada inserção trate os dados do que deixar tudo para o main ou alguma coisa se quer para a classe acidentes.
        this.operador = operador;
        this.naturezaAeronave = naturezaAeronave;
        this.categoriaIncidente = categoriaIncidente;
        this.causas = causas;
        this.localizacao = localizacao;
        this.tipoDano = tipoDano;
        this.dataTexto = dataTexto;
        this.horario = horario;
        this.arit = arit;
        this.motores = motores;
        this.tripulacao = tripulacao;
        this.passageiros = passageiros;
        this.totalAboard = totalAboard;
        this.fatalidades = fatalidades;
        this.primeiroVoo = primeiroVoo;
        this.faseVoo = faseVoo;
        this.aeroportoPartida = aeroportoPartida;
        this.aeroportoDestino = aeroportoDestino;
        this.vitimasSolo = vitimasSolo;
        this.vitimasColisao = vitimasColisao;
    }

    // Getters e Setters

    // Outra recomendação, eu acredito que não é muito legal de certa forma usar os exatos mesmos nomes nos argumentos dos sets porque eu acredito que isso piora a legibilidade e eu acredito que ela piora de verdade quando é necesssario fazer um set mais complexo envolvendo essa variavel de mesmo nome, e tamém porque eu não gosto de ter que usar this mas até ai isso é preferencia.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataIncidente() {
        return dataIncidente;
    }

    public void setDataIncidente(LocalDate dataIncidente) {
        this.dataIncidente = dataIncidente;
    }

    public String getModeloAeronave() {
        return modeloAeronave;
    }

    public void setModeloAeronave(String modeloAeronave) {
        this.modeloAeronave = modeloAeronave;
    }

    public String getRegistroAeronave() {
        return registroAeronave;
    }

    public void setRegistroAeronave(String registroAeronave) { // Me desculpe mas esse metodo é totalmente inutil aqui
        if (registroAeronave == null || registroAeronave.trim().isEmpty()) {
            this.registroAeronave = "N/A       ";
        } else if (registroAeronave.length() > 10) {
            this.registroAeronave = registroAeronave.substring(0, 10);
        } else {
            this.registroAeronave = String.format("%-10s", registroAeronave);
        }
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getNaturezaAeronave() {
        return naturezaAeronave;
    }

    public void setNaturezaAeronave(String naturezaAeronave) {
        this.naturezaAeronave = naturezaAeronave;
    }

    public String getCategoriaIncidente() {
        return categoriaIncidente;
    }

    public void setCategoriaIncidente(String categoriaIncidente) {
        this.categoriaIncidente = categoriaIncidente;
    }

    public String getCausas() {
        return causas;
    }

    public void setCausas(String causas) {
        this.causas = causas;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getTipoDano() {
        return tipoDano;
    }

    public void setTipoDano(String tipoDano) {
        this.tipoDano = tipoDano;
    }

    public String getDataTexto() {
        return dataTexto;
    }

    public void setDataTexto(String dataTexto) {
        this.dataTexto = dataTexto;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getArit() {
        return arit;
    }

    public void setArit(String arit) {
        this.arit = arit;
    }

    public String getMotores() {
        return motores;
    }

    public void setMotores(String motores) {
        this.motores = motores;
    }

    public String getTripulacao() {
        return tripulacao;
    }

    public void setTripulacao(String tripulacao) {
        this.tripulacao = tripulacao;
    }

    public String getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(String passageiros) {
        this.passageiros = passageiros;
    }

    public String getTotalAboard() {
        return totalAboard;
    }

    public void setTotalAboard(String totalAboard) {
        this.totalAboard = totalAboard;
    }

    public int getFatalidades() {
        return fatalidades;
    }

    public void setFatalidades(int fatalidades) {
        this.fatalidades = fatalidades;
    }

    public String getPrimeiroVoo() {
        return primeiroVoo;
    }

    public void setPrimeiroVoo(String primeiroVoo) {
        this.primeiroVoo = primeiroVoo;
    }

    public String getFaseVoo() {
        return faseVoo;
    }

    public void setFaseVoo(String faseVoo) {
        this.faseVoo = faseVoo;
    }

    public String getAeroportoPartida() {
        return aeroportoPartida;
    }

    public void setAeroportoPartida(String aeroportoPartida) {
        this.aeroportoPartida = aeroportoPartida;
    }

    public String getAeroportoDestino() {
        return aeroportoDestino;
    }

    public void setAeroportoDestino(String aeroportoDestino) {
        this.aeroportoDestino = aeroportoDestino;
    }

    public String getVitimasSolo() {
        return vitimasSolo;
    }

    public void setVitimasSolo(String vitimasSolo) {
        this.vitimasSolo = vitimasSolo;
    }

    public String getVitimasColisao() {
        return vitimasColisao;
    }

    public void setVitimasColisao(String vitimasColisao) {
        this.vitimasColisao = vitimasColisao;
    }

    public String formatarRegistro() {
        //Eu entendo o porque você fez desse jeito mas deculpa ter que informar que eu acredito que esse jeito não é muito bom
        //Porque? Porque tinha uma forma MUITO mais organizada de fazer isso, simplesmente daria para inserir tudo em uma só string ao invez de retornar a operação inteira
        //o que ganhamos? a operação fica muito mais legivel com a função que escreve na string, o texto fica em um lado enquanto os valores ficam na direita
        //assim facilitando a legibilidade e escrita pois fica muito ruim de escrever um monte de +, confunde string com valor ai você não sabe onde que fica cada coisa sem barra n,
        //sem contar que para escrever fica muito confuso, então o melhor era usar aquela função que escreve na string inteira
        return "Incidente{id=" + id +
        ", dataIncidente=" + dataIncidente +
        ", modeloAeronave=\'" + modeloAeronave + "\'" +
        ", registroAeronave=\'" + registroAeronave + "\'" +
        ", operador=\'" + operador + "\'" +
        ", causas=\'" + causas + "\'" +
        ", faseVoo=\'" + faseVoo + "\'" +
        ", fatalidades=" + fatalidades + '}';
    }
}
