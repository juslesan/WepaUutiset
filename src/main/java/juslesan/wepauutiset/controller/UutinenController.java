/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.controller;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import juslesan.wepauutiset.domain.Uutinen;
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

    @GetMapping("/uutiset/kategoria/{kategoriaId}")
    public String kategoria(Model model) {

        return "uutiset";
    }

    @Transactional
    @GetMapping("/uutinen/{uutinenId}")
    public String uutinen(Model model, @PathVariable Long uutinenId) {
        this.uutinenRepo.findById(uutinenId).get().luettuAdd();
        model.addAttribute("uutinen", this.uutinenRepo.getOne(uutinenId));
        return "uutinen";
    }

    @GetMapping("/uutinen/add")
    public String addPage(Model model) {

        return "uutinenAdd";
    }

    @Transactional
    @PostMapping("/uutinen/add")
    public String addUutinen(@RequestParam String nimi, @RequestParam String ingressi, @RequestParam String teksti) {
        Uutinen uutinen = new Uutinen();
        uutinen.setIngressi(ingressi);
        uutinen.setTeksti(teksti);
        uutinen.setNimi(nimi);
        uutinen.setUutinenDate(LocalDateTime.now());
        this.uutinenRepo.save(uutinen);
        return "redirect:/etusivu";
    }

}
