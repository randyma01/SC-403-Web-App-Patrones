package com.practicauno.PracticaUno.repository;

import com.practicauno.PracticaUno.domain.Arbol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbolRepository extends JpaRepository<Arbol, Integer> {

    public List<Arbol> findByActivoTrue();

}
