/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import juslesan.wepauutiset.domain.Uutinen;
import juslesan.wepauutiset.repository.KategoriaRepository;
import juslesan.wepauutiset.repository.UutinenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author santeri
 */
@Controller
public class UutinenController {

    @Autowired
    private UutinenRepository uutinenRepo;

    @Autowired
    private KategoriaRepository kategoriaRepo;

    @GetMapping("/etusivu")
    public String etusivu(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable));
        return "index";
    }

    @GetMapping("/uutiset")
    public String dateSort(Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable));
        return "uutiset";
    }

    @GetMapping("/uutiset/luetuimmat")
    public String luetuimmat(Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable));
        return "uutiset";
    }

    @GetMapping("/uutiset/kategoriat/{kategoriaId}")
    public String kategoria(Model model, @PathVariable Long kategoriaId) {
//        List<Uutinen> uutiset = this.uutinenRepo.FindAllByKategoria(this.kategoriaRepo.getOne(kategoriaId));
//        model.addAttribute("uutiset", uutiset);
        return "uutiset";
    }

    @Transactional
    @GetMapping("/uutinen/{uutinenId}")
    public String uutinen(Model model, @PathVariable Long uutinenId) {
        this.uutinenRepo.findById(uutinenId).get().luettuAdd();
        model.addAttribute("uutinen", this.uutinenRepo.getOne(uutinenId));
        model.addAttribute("kategoriat", this.uutinenRepo.getOne(uutinenId).getKategoriat());
        return "uutinen";
    }

    @GetMapping("/uutinen/add")
    public String addPage(Model model) {
        model.addAttribute("kategoriat", this.kategoriaRepo.findAll());

        return "uutinenAdd";
    }

    @Transactional
    @PostMapping("/uutinen/add")
    public String addUutinen(@RequestParam String nimi, @RequestParam String ingressi, @RequestParam String teksti, @RequestParam Long kategoriaId) {
        Uutinen uutinen = new Uutinen();
        uutinen.setIngressi(ingressi);
        uutinen.setKategoriat(new ArrayList());
        uutinen.setKirjoittajat(new ArrayList());
        uutinen.setTeksti(teksti);
        uutinen.setNimi(nimi);
        uutinen.setUutinenDate(LocalDateTime.now());
        uutinen.addKategoria(this.kategoriaRepo.getOne(kategoriaId));

        this.uutinenRepo.save(uutinen);
//        this.kategoriaRepo.findById(kategoriaId).get().addUutinen(uutinen);
        return "redirect:/etusivu";
    }

}
