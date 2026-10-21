package com.practicauno.PracticaUno.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "arbol")
public class Arbol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arbol")
    private Integer idArbol;

    @NotNull
    @Size(max = 60)
    @Column(name = "nombre_comun", unique = true, nullable = false, length = 60)
    private String nombreComun;

    @NotNull
    @Size(max = 60)
    @Column(name = "tipo_flor", nullable = false, length = 60)
    private String tipoFlor;

    @NotNull
    @Min(1)
    @Max(10)
    @Column(name = "dureza_madera", nullable = false)
    private Integer durezaMadera;

    @NotNull
    @Size(max = 150)
    @Column(name = "habitat", nullable = false, length = 150)
    private String habitat;

    @Size(max = 1024)
    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    @Column(name = "activo")
    private boolean activo;

    public Arbol() {
    }

    public Arbol(String nombreComun, String tipoFlor, Integer durezaMadera, String habitat, String rutaImagen, boolean activo) {
        this.nombreComun = nombreComun;
        this.tipoFlor = tipoFlor;
        this.durezaMadera = durezaMadera;
        this.habitat = habitat;
        this.rutaImagen = rutaImagen;
        this.activo = activo;
    }

    public Integer getIdArbol() {
        return idArbol;
    }

    public void setIdArbol(Integer idArbol) {
        this.idArbol = idArbol;
    }

    public String getNombreComun() {
        return nombreComun;
    }

    public void setNombreComun(String nombreComun) {
        this.nombreComun = nombreComun;
    }

    public String getTipoFlor() {
        return tipoFlor;
    }

    public void setTipoFlor(String tipoFlor) {
        this.tipoFlor = tipoFlor;
    }

    public Integer getDurezaMadera() {
        return durezaMadera;
    }

    public void setDurezaMadera(Integer durezaMadera) {
        this.durezaMadera = durezaMadera;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
