package com.caso1.service;

import com.caso1.domain.Pregunta;
import com.caso1.repository.PreguntaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Transactional(readOnly = true)
    public List<Pregunta> getPreguntas() {
        return preguntaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Pregunta> getPreguntas(boolean activos) {
        return preguntaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pregunta getPregunta(Pregunta pregunta) {
        return preguntaRepository.findById(pregunta.getIdPregunta()).orElse(null);
    }

    @Transactional
    public void save(Pregunta pregunta) {
        preguntaRepository.save(pregunta);
    }

    @Transactional
    public void delete(Pregunta pregunta) {
        preguntaRepository.delete(pregunta);
    }
}
