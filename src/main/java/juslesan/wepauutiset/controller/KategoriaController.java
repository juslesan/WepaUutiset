/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import juslesan.wepauutiset.domain.Kategoria;
import juslesan.wepauutiset.repository.KategoriaRepository;
import juslesan.wepauutiset.repository.UutinenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KategoriaController {

    @Autowired
    private UutinenRepository uutinenRepo;

    @Autowired
    private KategoriaRepository kategoriaRepo;

    @PostConstruct
    public void init() {
        if (kategoriaRepo.findAll().isEmpty()) {
            kategoriaRepo.save(new Kategoria("Urheilu", new ArrayList()));
            kategoriaRepo.save(new Kategoria("Kulttuuri", new ArrayList()));
            kategoriaRepo.save(new Kategoria("Viihde", new ArrayList()));
            kategoriaRepo.save(new Kategoria("Sää", new ArrayList()));
            kategoriaRepo.save(new Kategoria("Terveys", new ArrayList()));
        }
    }

    @GetMapping("/uutiset/kategoriat")
    public String Kategoriat(Model model) {
        model.addAttribute("kategoriat", this.kategoriaRepo.findAll());
        return "kategoria";
    }

    @PostMapping("/uutiset/kategoriat")
    public String addKategoria(@RequestParam String nimi) {
        if (!nimi.isEmpty()) {
            Kategoria kategoria = new Kategoria();
            kategoria.setUutiset(new ArrayList());
            kategoria.setNimi(nimi);
            kategoriaRepo.save(kategoria);
        }
        return "redirect:/uutiset/kategoriat";

    }
}
