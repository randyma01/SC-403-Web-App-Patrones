package com.practicados.PracticaDos.controller;

import com.practicados.PracticaDos.domain.Arbol;
import com.practicados.PracticaDos.service.ArbolService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ArbolService arbolService;

    public ConsultaController(ArbolService arbolService) {
        this.arbolService = arbolService;
    }

    /**
     * Endpoint inicial para listar todos los árboles y presentar los formularios de
     * consulta.
     */
    @GetMapping("/listado")
    public String listado(Model model) {
        var arboles = arbolService.getArboles(false);
        model.addAttribute("arboles", arboles);
        model.addAttribute("totalArboles", arboles.size());
        model.addAttribute("durezaInf", 1);
        model.addAttribute("durezaSup", 10);
        model.addAttribute("tipoConsulta", "Catálogo Completo");
        return "/consultas/listado";
    }

    /**
     * Endpoint para procesar la Consulta Derivada / Ampliada.
     */
    @PostMapping("/consultaDerivada")
    public String consultaDerivada(@RequestParam("durezaInf") int durezaInf,
            @RequestParam("durezaSup") int durezaSup,
            Model model) {
        var arboles = arbolService.consultaDerivada(durezaInf, durezaSup);
        model.addAttribute("arboles", arboles);
        model.addAttribute("totalArboles", arboles.size());
        model.addAttribute("durezaInf", durezaInf);
        model.addAttribute("durezaSup", durezaSup);
        model.addAttribute("tipoConsulta", "Consulta Derivada (Spring Data JPA)");
        model.addAttribute("consultaEjecutada",
                "findByDurezaMaderaBetweenOrderByDurezaMaderaAsc(" + durezaInf + ", " + durezaSup + ")");
        return "/consultas/listado";
    }

    /**
     * Endpoint para procesar la Consulta JPQL.
     */
    @PostMapping("/consultaJPQL")
    public String consultaJPQL(@RequestParam("durezaInf") int durezaInf,
            @RequestParam("durezaSup") int durezaSup,
            Model model) {
        var arboles = arbolService.consultaJPQL(durezaInf, durezaSup);
        model.addAttribute("arboles", arboles);
        model.addAttribute("totalArboles", arboles.size());
        model.addAttribute("durezaInf", durezaInf);
        model.addAttribute("durezaSup", durezaSup);
        model.addAttribute("tipoConsulta", "Consulta JPQL (@Query)");
        model.addAttribute("consultaEjecutada", "SELECT a FROM Arbol a WHERE a.durezaMadera BETWEEN " + durezaInf
                + " AND " + durezaSup + " ORDER BY a.durezaMadera ASC");
        return "/consultas/listado";
    }

    /**
     * Endpoint para procesar la Consulta SQL Nativa.
     */
    @PostMapping("/consultaSQL")
    public String consultaSQL(@RequestParam("durezaInf") int durezaInf,
            @RequestParam("durezaSup") int durezaSup,
            Model model) {
        var arboles = arbolService.consultaSQL(durezaInf, durezaSup);
        model.addAttribute("arboles", arboles);
        model.addAttribute("totalArboles", arboles.size());
        model.addAttribute("durezaInf", durezaInf);
        model.addAttribute("durezaSup", durezaSup);
        model.addAttribute("tipoConsulta", "Consulta SQL Nativa (MySQL direct)");
        model.addAttribute("consultaEjecutada", "SELECT * FROM arbol a WHERE a.dureza_madera BETWEEN " + durezaInf
                + " AND " + durezaSup + " ORDER BY a.dureza_madera ASC");
        return "/consultas/listado";
    }

    /**
     * Consulta adicional de texto para la 4ta tarjeta: Búsqueda por Nombre o
     * Hábitat.
     */
    @PostMapping("/consultaTexto")
    public String consultaTexto(@RequestParam("textoBuscar") String textoBuscar, Model model) {
        List<Arbol> arboles = (textoBuscar == null || textoBuscar.isBlank())
                ? arbolService.getArboles(false)
                : arbolService.consultaPorNombre(textoBuscar);
        model.addAttribute("arboles", arboles);
        model.addAttribute("totalArboles", arboles.size());
        model.addAttribute("textoBuscar", textoBuscar);
        model.addAttribute("tipoConsulta", "Búsqueda por Nombre Común");
        model.addAttribute("consultaEjecutada",
                "findByNombreComunContainingIgnoreCaseOrderByNombreComun('" + textoBuscar + "')");
        return "/consultas/listado";
    }
}
