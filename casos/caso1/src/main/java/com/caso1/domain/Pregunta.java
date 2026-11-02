package com.caso1.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "pregunta")
public class Pregunta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long idPregunta;

    @Column(name = "enunciado")
    private String enunciado;

    @Column(name = "tema")
    private int tema;

    @Column(name = "puntos_pregunta")
    private double puntosPregunta;

    @Column(name = "imagen_pregunta")
    private String imagenPregunta;

    public Pregunta() {
    }

    public Pregunta(String enunciado, int tema, double puntosPregunta, String imagenPregunta) {
        this.enunciado = enunciado;
        this.tema = tema;
        this.puntosPregunta = puntosPregunta;
        this.imagenPregunta = imagenPregunta;
    }

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getTema() {
        return tema;
    }

    public void setTema(int tema) {
        this.tema = tema;
    }

    public double getPuntosPregunta() {
        return puntosPregunta;
    }

    public void setPuntosPregunta(double puntosPregunta) {
        this.puntosPregunta = puntosPregunta;
    }

    public String getImagenPregunta() {
        return imagenPregunta;
    }

    public void setImagenPregunta(String imagenPregunta) {
        this.imagenPregunta = imagenPregunta;
    }
}
