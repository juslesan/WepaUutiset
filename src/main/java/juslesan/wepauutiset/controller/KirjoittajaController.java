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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Luo 5 kirjoittajaa
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

    // get pyyntö kirjoittajien listaukseen
    @GetMapping("/uutiset/kirjoittajat")
    public String Kirjoittajat(Model model) {
        model = sivupalkit(model);
        model.addAttribute("kirjoittajat", this.kirjoittajaRepo.findAll());
        return "kirjoittaja";
    }

    // post pyyntö kirjoittajan luontin
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
    
    // Sivuston kaikilla sivuilla näkyvien listauspalkkien lisääminen modeliin
    public Model sivupalkit(Model model) {
        Pageable pageable = PageRequest.of(0, 200, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, 200, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        return model;
    }
}
