package com.practicados.PracticaDos.repository;

import com.practicados.PracticaDos.domain.Arbol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbolRepository extends JpaRepository<Arbol, Integer> {

    /**
     * Retorna únicamente los árboles cuyo estado activo sea verdadero.
     */
    public List<Arbol> findByActivoTrue();

    // ======================================================
    // 1. CONSULTA DERIVADA / AMPLIADA (Derived Query Method 
    // ======================================================
    /**
     * Consulta derivada que recupera los árboles dentro de un rango de dureza
     * de madera y los ordena ascendentemente.
     * Estructura: findBy + Atributo + Between + OrderBy + Atributo + Asc
     */
    public List<Arbol> findByDurezaMaderaBetweenOrderByDurezaMaderaAsc(int durezaInf, int durezaSup);

    public List<Arbol> findByDurezaMaderaBetweenOrderByNombreComun(int durezaInf, int durezaSup);

    public List<Arbol> findByNombreComunContainingIgnoreCaseOrderByNombreComun(String nombreComun);

    // ==================================================
    // 2. CONSULTA JPQL (Java Persistence Query Language
    // ==================================================
    /**
     * Consulta JPQL orientada a la entidad Arbol que recupera los árboles
     * de un rango de dureza y los ordena ascendentemente.
     */
    @Query(value = "SELECT a FROM Arbol a WHERE a.durezaMadera BETWEEN :durezaInf AND :durezaSup ORDER BY a.durezaMadera ASC")
    public List<Arbol> consultaJPQL(@Param("durezaInf") int durezaInf, @Param("durezaSup") int durezaSup);

    @Query(value = "SELECT a FROM Arbol a WHERE LOWER(a.habitat) LIKE LOWER(CONCAT('%', :habitat, '%')) ORDER BY a.nombreComun ASC")
    public List<Arbol> consultaJPQLHabitat(@Param("habitat") String habitat);

    // =====================================
    // 3. CONSULTA SQL NATIVA (Native Query
    // =====================================
    /**
     * Consulta SQL nativa directa contra MySQL que recupera los árboles
     * de un rango de dureza y los ordena ascendentemente.
     */
    @Query(nativeQuery = true, 
           value = "SELECT * FROM arbol a WHERE a.dureza_madera BETWEEN :durezaInf AND :durezaSup ORDER BY a.dureza_madera ASC")
    public List<Arbol> consultaSQL(@Param("durezaInf") int durezaInf, @Param("durezaSup") int durezaSup);

}
