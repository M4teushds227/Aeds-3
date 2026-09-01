package Src;

import java.time.LocalDate;

public class Incidente {
    private int id;
    private LocalDate dataIncidente;
    private String modeloAeronave;
    private String registroAeronave; // String de tamanho fixo (10 caracteres)
    private String operador;
    private String naturezaAeronave;
    private String categoriaIncidente;
    private String causas; // Lista de valores com separador ';'
    private String localizacao;
    private String tipoDano;
    private String dataTexto;
    private String horario;
    private String arit;
    private String motores;
    private String tripulacao;
    private String passageiros;
    private String totalAboard;
    private int fatalidades;
    private String primeiroVoo;
    private String faseVoo;
    private String aeroportoPartida;
    private String aeroportoDestino;
    private String vitimasSolo;
    private String vitimasColisao;

    // Construtor Vazio
    public Incidente() {
    }

    // Construtor Completo
    public Incidente(int id, LocalDate dataIncidente, String modeloAeronave, String registroAeronave,
                     String operador, String naturezaAeronave, String categoriaIncidente, String causas,
                     String localizacao, String tipoDano, String dataTexto, String horario, String arit,
                     String motores, String tripulacao, String passageiros, String totalAboard,
                     int fatalidades, String primeiroVoo, String faseVoo, String aeroportoPartida,
                     String aeroportoDestino, String vitimasSolo, String vitimasColisao) {
        this.id = id;
        this.dataIncidente = dataIncidente;
        this.modeloAeronave = modeloAeronave;
        setRegistroAeronave(registroAeronave);
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

    public void setRegistroAeronave(String registroAeronave) {
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
        return "Incidente{" +
                "id=" + id +
                ", dataIncidente=" + dataIncidente +
                ", modeloAeronave='" + modeloAeronave + ''' +
                ", registroAeronave='" + registroAeronave + ''' +
                ", operador='" + operador + ''' +
                ", causas='" + causas + ''' +
                ", faseVoo='" + faseVoo + ''' +
                ", fatalidades=" + fatalidades +
                '}';
    }
}
