package com.practicauno.PracticaUno.service;

import com.practicauno.PracticaUno.domain.Arbol;
import com.practicauno.PracticaUno.repository.ArbolRepository;
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
}
