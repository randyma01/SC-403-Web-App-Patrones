package com.practicados.PracticaDos.service;

import com.practicados.PracticaDos.domain.Arbol;
import com.practicados.PracticaDos.repository.ArbolRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArbolService {

    private final ArbolRepository arbolRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ArbolService(ArbolRepository arbolRepository, FirebaseStorageService firebaseStorageService) {
        this.arbolRepository = arbolRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    /**
     * Retorna la lista de árboles. Si activo es true, retorna solo los activos.
     */
    @Transactional(readOnly = true)
    public List<Arbol> getArboles(boolean activo) {
        if (activo) {
            return arbolRepository.findByActivoTrue();
        }
        return arbolRepository.findAll();
    }

    /**
     * Retorna un árbol por su identificador.
     */
    @Transactional(readOnly = true)
    public Optional<Arbol> getArbol(Integer idArbol) {
        return arbolRepository.findById(idArbol);
    }

    /**
     * Guarda o actualiza un árbol y sube su fotografía a Firebase si se proporciona una.
     */
    @Transactional
    public void save(Arbol arbol, MultipartFile imagenFile) {
        arbol = arbolRepository.save(arbol);
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile,
                        "arbol",
                        arbol.getIdArbol()
                );
                arbol.setRutaImagen(rutaImagen);
                arbolRepository.save(arbol);
            } catch (IOException e) {
                // Si la subida de imagen falla, se mantiene la información del árbol guardada
            }
        }
    }

    /**
     * Elimina un árbol por su identificador con validación previa de existencia.
     */
    @Transactional
    public void delete(Integer idArbol) {
        if (!arbolRepository.existsById(idArbol)) {
            throw new IllegalArgumentException("El árbol con ID " + idArbol + " no existe.");
        }
        try {
            arbolRepository.deleteById(idArbol);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el árbol porque tiene registros asociados.", e);
        }
    }

    // =========================================================================
    // 1. Consulta Derivada (PDF V.02 diapositiva 4)
    // =========================================================================
    @Transactional(readOnly = true)
    public List<Arbol> consultaDerivada(int durezaInf, int durezaSup) {
        return arbolRepository.findByDurezaMaderaBetweenOrderByDurezaMaderaAsc(durezaInf, durezaSup);
    }

    // Alias para compatibilidad
    @Transactional(readOnly = true)
    public List<Arbol> consultaAmpliada(int durezaInf, int durezaSup) {
        return consultaDerivada(durezaInf, durezaSup);
    }

    // =========================================================================
    // 2. Consulta JPQL (PDF V.02 diapositiva 5)
    // =========================================================================
    @Transactional(readOnly = true)
    public List<Arbol> consultaJPQL(int durezaInf, int durezaSup) {
        return arbolRepository.consultaJPQL(durezaInf, durezaSup);
    }

    // =========================================================================
    // 3. Consulta SQL Nativa (PDF V.02 diapositiva 6)
    // =========================================================================
    @Transactional(readOnly = true)
    public List<Arbol> consultaSQL(int durezaInf, int durezaSup) {
        return arbolRepository.consultaSQL(durezaInf, durezaSup);
    }

    // Consultas complementarias por texto
    @Transactional(readOnly = true)
    public List<Arbol> consultaPorNombre(String nombreComun) {
        return arbolRepository.findByNombreComunContainingIgnoreCaseOrderByNombreComun(nombreComun);
    }

    @Transactional(readOnly = true)
    public List<Arbol> consultaPorHabitat(String habitat) {
        return arbolRepository.consultaJPQLHabitat(habitat);
    }
}
