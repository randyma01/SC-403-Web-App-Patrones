package com.caso1.controller;

import com.caso1.domain.Pregunta;
import com.caso1.service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pregunta")
public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;

    @GetMapping("/listado")
    public String listado(Model model) {
        var preguntas = preguntaService.getPreguntas();
        model.addAttribute("preguntas", preguntas);
        model.addAttribute("totalPreguntas", preguntas.size());
        return "/pregunta/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(Pregunta pregunta) {
        preguntaService.delete(pregunta);
        return "redirect:/pregunta/listado";
    }

    @GetMapping("/eliminar/{idPregunta}")
    public String eliminarGet(Pregunta pregunta) {
        preguntaService.delete(pregunta);
        return "redirect:/pregunta/listado";
    }
}
