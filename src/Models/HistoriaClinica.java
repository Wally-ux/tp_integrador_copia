package Models;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author agust
 */
public class HistoriaClinica {
    private long id;
    private String nroHistoria;
    private GrupoSanguineo grupoSanguineo;
    private String antecedentes;
    private String medicacionActual;
    private String observaciones;
    private boolean eliminado;
    
    //constructor vacio
    public HistoriaClinica(){}
    
    //constructor completo
    public HistoriaClinica(String nroHistoria, GrupoSanguineo grupoSanguineo,
                           String antecedentes, String medicacionActual, String observaciones){
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
        this.eliminado = false;
    }
    
       // getters y setters 

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNroHistoria() {
        return nroHistoria;
    }

    public void setNroHistoria(String nroHistoria) {
        this.nroHistoria = nroHistoria;
    }

    public GrupoSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getMedicacionActual() {
        return medicacionActual;
    }

    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
        //to String

    @Override
    public String toString() {
        return "Models.HistoriaClinica{" + "id=" + id +
                ", nroHistoria=" + nroHistoria + 
                ", grupoSanguineo=" + grupoSanguineo + 
                ", antecedentes=" + antecedentes + 
                ", medicacionActual=" + medicacionActual +
                ", observaciones=" + observaciones + 
                ", eliminado=" + eliminado + '}';
    }
 }
