/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import juslesan.wepauutiset.WepaUutisetApplication;
import juslesan.wepauutiset.domain.Kategoria;
import juslesan.wepauutiset.domain.Kirjoittaja;
import juslesan.wepauutiset.domain.Uutinen;
import juslesan.wepauutiset.repository.KategoriaRepository;
import juslesan.wepauutiset.repository.KirjoittajaRepository;
import juslesan.wepauutiset.repository.UutinenRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author santeri
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WepaUutisetApplication.class)
public class UutinenTest {

    @Autowired
    private UutinenRepository uutisRepo;

    @Autowired
    private KategoriaRepository kategoriaRepo;

    @Autowired
    private KirjoittajaRepository kirjoittajaRepo;

    public UutinenTest() {

    }

    @Test
    public void uutinenLuettuTest() {
        Uutinen uutinen = new Uutinen();
        uutinen.setLuettu(0);
        assertEquals(0, uutinen.getLuettu());
        uutinen.luettuAdd();
        uutinen.luettuAdd();
        Uutinen uutinen2 = new Uutinen();
        uutinen2.setLuettu(0);
        uutinen2.luettuAdd();
        assertEquals(2, uutinen.getLuettu());
        assertEquals(1, uutinen2.getLuettu());
    }

    @Test
    public void uutinenSetKuva() throws FileNotFoundException, IOException {
        Uutinen uutinen = new Uutinen();
        File file = new File("src/test/java/BibFrog.jpg");
        byte[] picInBytes = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(picInBytes);
        stream.close();
        uutinen.setKuva(picInBytes);
        assertEquals(true, uutinen.getKuva().equals(picInBytes));
    }

    @Test
    public void uutisetJaKategoriat() {
        Uutinen uutinen = new Uutinen();
        Kategoria kategoria = new Kategoria();
        kategoria.setNimi("nimi");

        uutinen.setKategoriat(new ArrayList<Kategoria>());
        this.uutisRepo.save(uutinen);
        kategoria = this.kategoriaRepo.save(kategoria);
        uutinen.addKategoria(kategoria);

        assertEquals("nimi", uutinen.getKategoriat().get(0).getNimi());
    }

    @Test
    public void uutisetJaKirjoittajat() {
        Uutinen uutinen = new Uutinen();
        Kirjoittaja kirjoittaja = new Kirjoittaja();
        kirjoittaja.setNimi("nimi");

        uutinen.setKirjoittajat(new ArrayList<Kirjoittaja>());
        this.uutisRepo.save(uutinen);
        kirjoittaja = this.kirjoittajaRepo.save(kirjoittaja);
        uutinen.addKirjoittaja(kirjoittaja);

        assertEquals("nimi", uutinen.getKirjoittajat().get(0).getNimi());
    }
}
