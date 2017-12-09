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
import juslesan.wepauutiset.domain.Kirjoittaja;
import juslesan.wepauutiset.repository.KategoriaRepository;
import juslesan.wepauutiset.repository.KirjoittajaRepository;
import juslesan.wepauutiset.repository.UutinenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KirjoittajaController {

    @Autowired
    private UutinenRepository uutinenRepo;

    @Autowired
    private KirjoittajaRepository kirjoittajaRepo;

    @PostConstruct
    public void init() {
        if (kirjoittajaRepo.findAll().isEmpty()) {
            kirjoittajaRepo.save(new Kirjoittaja("Matti Meikäläinen", new ArrayList()));
            kirjoittajaRepo.save(new Kirjoittaja("Arto", new ArrayList()));
            kirjoittajaRepo.save(new Kirjoittaja("Matti", new ArrayList()));
            kirjoittajaRepo.save(new Kirjoittaja("Supermies", new ArrayList()));
            kirjoittajaRepo.save(new Kirjoittaja("Maija Mehiläinen", new ArrayList()));

        }
    }

    @GetMapping("/uutiset/kirjoittajat")
    public String Kirjoittajat(Model model) {
        model.addAttribute("kirjoittajat", this.kirjoittajaRepo.findAll());
        return "kirjoittaja";
    }

    @PostMapping("/uutiset/kirjoittajat")
    public String addKirjoittaja(@RequestParam String nimi) {
        if (!nimi.isEmpty()) {
            Kirjoittaja kirjoittaja = new Kirjoittaja();
            kirjoittaja.setUutiset(new ArrayList());
            kirjoittaja.setNimi(nimi);
            kirjoittajaRepo.save(kirjoittaja);
        }
        return "redirect:/uutiset/kirjoittajat";

    }
}
