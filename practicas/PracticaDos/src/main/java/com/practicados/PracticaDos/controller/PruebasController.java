package com.practicados.PracticaDos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pruebas")
public class PruebasController {

    @GetMapping({"", "/", "/listado"})
    public String redirigirConsultas() {
        return "redirect:/consultas/listado";
    }
}
