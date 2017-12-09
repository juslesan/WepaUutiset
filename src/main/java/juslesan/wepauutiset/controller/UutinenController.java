/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
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

    //Get pyynö etusivun listaukseen
    @GetMapping("/etusivu")
    public String etusivu(Model model) {
        model = sivupalkit(model);
        Pageable pageable3 = PageRequest.of(0, 5, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable3));
        return "index";
    }

    // Get pyyntö kaikkien uutisten listaukseen
    @GetMapping("/uutiset")
    public String dateSort(Model model) {
        model = sivupalkit(model);

        Pageable pageable3 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable3));
        return "uutiset";
    }

    // Get pyyntö luetuimpien uutisten listaukseen
    @GetMapping("/uutiset/luetuimmat")
    public String luetuimmat(Model model) {
        model = sivupalkit(model);

        Pageable pageable3 = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "luettu");
        model.addAttribute("uutiset", uutinenRepo.findAll(pageable3));
        return "uutiset";
    }

    //Get pyyntö kategorioiden perusteella listaukselle
    @GetMapping("/uutiset/kategoriat/{kategoriaId}")
    public String kategoria(Model model, @PathVariable Long kategoriaId) {
        model = sivupalkit(model);
        model.addAttribute("uutiset", this.kategoriaRepo.getOne(kategoriaId).getUutiset());
        return "uutiset";
    }

    // Get pyyntö yksittäiselle uutiselle
    @Transactional
    @GetMapping("/uutinen/{uutinenId}")
    public String uutinen(Model model, @PathVariable Long uutinenId) {
        model = sivupalkit(model);

        this.uutinenRepo.findById(uutinenId).get().luettuAdd();
        model.addAttribute("uutinen", this.uutinenRepo.getOne(uutinenId));
//        model.addAttribute("kategoriat", this.uutinenRepo.getOne(uutinenId).getKategoriat());

        return "uutinen";
    }

    // Get pyyntö uutislisäyssivulle
    @GetMapping("/uutinen/add")
    public String addPage(Model model) {
        model = sivupalkit(model);

        model.addAttribute("kategoriat", this.kategoriaRepo.findAll());
        model.addAttribute("kirjoittajat", this.kirjoittajaRepo.findAll());

        return "uutinenAdd";
    }

    // Post pyyntö uutiksen luonnille
    @Transactional
    @PostMapping("/uutinen/add")
    public String addUutinen(@RequestParam String nimi, @RequestParam String ingressi, @RequestParam String teksti, @RequestParam("kategoria") Long[] kategoriat, @RequestParam("kirjoittaja") Long[] kirjoittajat, @RequestParam("file") MultipartFile file) throws IOException, SQLException {
        if (!nimi.isEmpty() && !ingressi.isEmpty() && !teksti.isEmpty() && kategoriat.length > 0 && kirjoittajat.length > 0 && !file.isEmpty()) {
            Uutinen uutinen = luoUutinen(nimi, ingressi, teksti, kategoriat, kirjoittajat, file);
            return "redirect:/uutinen/" + uutinen.getId();
        }
        return "redirect:/uutinenAdd";
    }

    // Get pyyntö kuvan tuottamiselle
    @GetMapping(path = "/uutinen/{uutinenId}/kuva", produces = "image/jpeg")
    @ResponseBody
    public byte[] getKuva(@PathVariable Long uutinenId) throws SQLException {
        return this.uutinenRepo.getOne(uutinenId).getKuva();
    }

//    
//    @GetMapping("/uutinen/{uutinenId}/edit")
//    public String editPage(Model model, @PathVariable Long uutinenId) {
//        model.addAttribute("kategoriat", this.kategoriaRepo.findAll());
//        model.addAttribute("kirjoittajat", this.kirjoittajaRepo.findAll());
//        model.addAttribute("uutinen", this.uutinenRepo.getOne(uutinenId));
//        return "uutinenEdit";
//    }
//
//    @Transactional
//    @PostMapping("/uutinen/{uutinenId}/edit")
//    public String editUutinen(@PathVariable Long uutinenId, @RequestParam String nimi, @RequestParam String ingressi, @RequestParam String teksti) {
//        Uutinen uutinen = this.uutinenRepo.getOne(uutinenId);
//        uutinen.setNimi(nimi);
//        uutinen.setIngressi(ingressi);
//        uutinen.setTeksti(teksti);
//        return "redirect:/uutinen/" + uutinen.getId();
//    }
    // 
    // Sivuston kaikilla sivuilla näkyvien palkkien lisääminen modeliin
    public Model sivupalkit(Model model) {
        Pageable pageable = PageRequest.of(0, 200, Sort.Direction.DESC, "luettu");
        model.addAttribute("luetuimmat", uutinenRepo.findAll(pageable));
        Pageable pageable2 = PageRequest.of(0, 200, Sort.Direction.DESC, "uutinenDate");
        model.addAttribute("kaikki", uutinenRepo.findAll(pageable2));
        return model;
    }

    // Uutisen luominen ja lisääminen repositoryyn
    @Transactional
    public Uutinen luoUutinen(String nimi, String ingressi, String teksti, Long[] kategoriat, Long[] kirjoittajat, MultipartFile file) throws IOException {
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
        if (file.getContentType().equals("image/jpeg")) {

            uutinen.setKuva(file.getBytes());

        }
        uutinen = this.uutinenRepo.save(uutinen);
        for (Long kategoria : kategoriat) {
            this.kategoriaRepo.findById(kategoria).get().addUutinen(uutinen);
        }
        return uutinen;
    }

}
