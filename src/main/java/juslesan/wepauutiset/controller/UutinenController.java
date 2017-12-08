/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import juslesan.wepauutiset.domain.Kategoria;
import juslesan.wepauutiset.domain.Kirjoittaja;
import juslesan.wepauutiset.domain.Uutinen;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private KirjoittajaRepository kirjoittajaRepo;

    @GetMapping("/etusivu")
    public String etusivu(Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        Pageable pageable3 = PageRequest.of(0, 5, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable3));
        return "index";
    }

    @GetMapping("/uutiset")
    public String dateSort(Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        Pageable pageable3 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable3));
        return "uutiset";
    }

    @GetMapping("/uutiset/luetuimmat")
    public String luetuimmat(Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        Pageable pageable3 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable3));
        return "uutiset";
    }

    @GetMapping("/uutiset/kategoriat/{kategoriaId}")
    public String kategoria(Model model, @PathVariable Long kategoriaId) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        model.addAttribute("uutiset", this.kategoriaRepo.getOne(kategoriaId).getUutiset());
        return "uutiset";
    }

    @Transactional
    @GetMapping("/uutinen/{uutinenId}")
    public String uutinen(Model model, @PathVariable Long uutinenId) {
        this.uutinenRepo.findById(uutinenId).get().luettuAdd();
        model.addAttribute("uutinen", this.uutinenRepo.getOne(uutinenId));
//        model.addAttribute("kategoriat", this.uutinenRepo.getOne(uutinenId).getKategoriat());
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        return "uutinen";
    }

    @GetMapping("/uutinen/add")
    public String addPage(Model model) {
        model.addAttribute("kategoriat", this.kategoriaRepo.findAll());
        model.addAttribute("kirjoittajat", this.kirjoittajaRepo.findAll());
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        return "uutinenAdd";
    }

    @Transactional
    @PostMapping("/uutinen/add")
    public String addUutinen(@RequestParam String nimi, @RequestParam String ingressi, @RequestParam String teksti, @RequestParam("kategoria") Long[] kategoriat, @RequestParam("kirjoittaja") Long[] kirjoittajat, @RequestParam("file") MultipartFile file) throws IOException {
        Uutinen uutinen = new Uutinen();
        uutinen.setIngressi(ingressi);
        ArrayList<Kategoria> kategoriat2 = new ArrayList();
        for (Long kategoria : kategoriat) {
            kategoriat2.add(this.kategoriaRepo.getOne(kategoria));
        }
        uutinen.setKategoriat(kategoriat2);

        ArrayList<Kirjoittaja> kirjoittajat2 = new ArrayList();
        for (Long kirjoittaja : kirjoittajat) {
            kirjoittajat2.add(this.kirjoittajaRepo.getOne(kirjoittaja));
        }
        uutinen.setKirjoittajat(kirjoittajat2);
        uutinen.setTeksti(teksti);
        uutinen.setNimi(nimi);
        uutinen.setUutinenDate(LocalDateTime.now());
//        if (file.getContentType().equals("image/jpeg")) {
            uutinen.setKuva(file.getBytes());
//        }
//        if (file.getContentType().equals("image/png")) {
//            uutinen.setKuva(file.getBytes());
//        }
        uutinen = this.uutinenRepo.save(uutinen);
        for (Long kategoria : kategoriat) {
            this.kategoriaRepo.findById(kategoria).get().addUutinen(uutinen);
        }
        return "redirect:/uutinen/" + uutinen.getId();
    }

//    @GetMapping("/uutinen/{uutinenId}/kuva")
//    public String kuvaPage(@PathVariable Long uutinenId, Model model) {
//        model.addAttribute("uutinenId", uutinenId);
//        return "";
//    }
    @GetMapping(path = "/uutinen/{uutinenId}/kuva", produces = "image/jpeg")
    @ResponseBody
    public byte[] getKuva(@PathVariable Long uutinenId) {
        return this.uutinenRepo.getOne(uutinenId).getKuva();
    }

    @GetMapping("/uutinen/{uutinenId}/edit")
    public String editPage(Model model, @PathVariable Long uutinenId) {
        model.addAttribute("kategoriat", this.kategoriaRepo.findAll());
        model.addAttribute("kirjoittajat", this.kirjoittajaRepo.findAll());
        model.addAttribute("uutinen", this.uutinenRepo.getOne(uutinenId));
        return "uutinenEdit";
    }

    @Transactional
    @PostMapping("/uutinen/{uutinenId}/edit")
    public String editUutinen(@PathVariable Long uutinenId, @RequestParam String nimi, @RequestParam String ingressi, @RequestParam String teksti) {
        Uutinen uutinen = this.uutinenRepo.getOne(uutinenId);
        uutinen.setNimi(nimi);
        uutinen.setIngressi(ingressi);
        uutinen.setTeksti(teksti);
        return "redirect:/uutinen/" + uutinen.getId();
    }

}
