/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.controller;

import juslesan.wepauutiset.repository.UutinenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
