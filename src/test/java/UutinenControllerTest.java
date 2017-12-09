
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import juslesan.wepauutiset.WepaUutisetApplication;
import juslesan.wepauutiset.domain.Kategoria;
import juslesan.wepauutiset.domain.Kirjoittaja;
import juslesan.wepauutiset.domain.Uutinen;
import juslesan.wepauutiset.repository.KategoriaRepository;
import juslesan.wepauutiset.repository.KirjoittajaRepository;
import juslesan.wepauutiset.repository.UutinenRepository;
import org.eclipse.jetty.util.MultiMap;
import org.hibernate.collection.internal.PersistentBag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author santeri
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WepaUutisetApplication.class)
public class UutinenControllerTest {

    private MockMvc mockMvc;

    private Long kategoriaId;
    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private UutinenRepository uutinenRepo;

    @Autowired
    private KategoriaRepository kategoriaRepo;

    @Autowired
    private KirjoittajaRepository kirjoittajaRepo;

    public UutinenControllerTest() {

    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/etusivu"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/uutiset")).andExpect(status().isOk());
        mockMvc.perform(get("/uutiset/luetuimmat")).andExpect(status().isOk());

    }

    @Test
    public void lisaaUutinen() throws Exception {
        Long id = lisaaUutinenRepoon();
        mockMvc.perform(get("/uutinen/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/uutinen/" + id + "/kuva")).andExpect(status().isOk());
        this.uutinenRepo.deleteAll();

    }

    @Test
    public void lisaaMontaUutistaEtusivullaVainViisi() throws IOException, Exception {
        this.uutinenRepo.deleteAll();

        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        mockMvc.perform(get("/etusivu")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/etusivu")).andReturn();
        PageImpl uutiset = (PageImpl) res.getModelAndView().getModel().get("uutiset");
        assertEquals(5, uutiset.getPageable().getPageSize());
        this.uutinenRepo.deleteAll();
    }

    @Test
    public void useanLisaamisenJalkeenListaus() throws IOException, Exception {
        this.uutinenRepo.deleteAll();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();

        mockMvc.perform(get("/uutiset")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/uutiset")).andReturn();
        PageImpl uutiset = (PageImpl) res.getModelAndView().getModel().get("uutiset");
        assertEquals(8, uutiset.getTotalElements());
        this.uutinenRepo.deleteAll();
    }

    @Test
    public void uusinListattuEnsimmäisenä() throws IOException, Exception {
        this.uutinenRepo.deleteAll();

        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        Long id = lisaaUutinenRepoon();

        mockMvc.perform(get("/uutiset")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/uutiset")).andReturn();
        PageImpl uutiset = (PageImpl) res.getModelAndView().getModel().get("uutiset");

        List<Uutinen> uutisetLista = (List<Uutinen>) uutiset.getContent();
        List<Long> idList = new ArrayList();
        for (Uutinen uutinen : uutisetLista) {
            idList.add(uutinen.getId());
        }

        assertEquals(true, idList.get(0).equals(id));

        this.uutinenRepo.deleteAll();

    }

    @Test
    public void uusinListattuEnsimmäisenäSivupalkissa() throws IOException, Exception {
        lisaaUutinenRepoon();
        lisaaUutinenRepoon();
        Long id = lisaaUutinenRepoon();

        mockMvc.perform(get("/etusivu")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/etusivu")).andReturn();
        PageImpl uutiset = (PageImpl) res.getModelAndView().getModel().get("kaikki");

        List<Uutinen> uutisetLista = (List<Uutinen>) uutiset.getContent();
        List<Long> idList = new ArrayList();
        for (Uutinen uutinen : uutisetLista) {
            idList.add(uutinen.getId());
        }

        assertEquals(true, idList.get(0).equals(id));

        this.uutinenRepo.deleteAll();

    }

    @Test
    public void luetuinListaus() throws IOException, Exception {
        Long id = lisaaUutinenRepoon();
        Long id2 = lisaaUutinenRepoon();
        Long id3 = lisaaUutinenRepoon();

        mockMvc.perform(get("/uutinen/" + id)).andExpect(status().isOk());

        mockMvc.perform(get("/etusivu")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/etusivu")).andReturn();
        PageImpl uutiset = (PageImpl) res.getModelAndView().getModel().get("luetuimmat");

        List<Uutinen> uutisetLista = (List<Uutinen>) uutiset.getContent();
        List<Long> idList = new ArrayList();
        for (Uutinen uutinen : uutisetLista) {
            idList.add(uutinen.getId());
        }

        assertEquals(true, idList.get(0).equals(id));

        //
        mockMvc.perform(get("/uutinen/" + id2)).andExpect(status().isOk());
        mockMvc.perform(get("/uutinen/" + id2)).andExpect(status().isOk());

        mockMvc.perform(get("/etusivu")).andExpect(status().isOk());
        res = mockMvc.perform(get("/etusivu")).andReturn();
        uutiset = (PageImpl) res.getModelAndView().getModel().get("luetuimmat");

        uutisetLista = (List<Uutinen>) uutiset.getContent();
        idList = new ArrayList();
        for (Uutinen uutinen : uutisetLista) {
            idList.add(uutinen.getId());
        }

        assertEquals(true, idList.get(0).equals(id2));

        //
        mockMvc.perform(get("/uutinen/" + id3)).andExpect(status().isOk());
        mockMvc.perform(get("/uutinen/" + id3)).andExpect(status().isOk());
        mockMvc.perform(get("/uutinen/" + id3)).andExpect(status().isOk());

        mockMvc.perform(get("/etusivu")).andExpect(status().isOk());
        res = mockMvc.perform(get("/etusivu")).andReturn();
        uutiset = (PageImpl) res.getModelAndView().getModel().get("luetuimmat");

        uutisetLista = (List<Uutinen>) uutiset.getContent();
        idList = new ArrayList();
        for (Uutinen uutinen : uutisetLista) {
            idList.add(uutinen.getId());
        }

        assertEquals(true, idList.get(0).equals(id3));

        this.uutinenRepo.deleteAll();
    }

    @Test
    public void uutisetKategoriaListaus() throws IOException, Exception {
        Long id = this.kategoriaRepo.save(new Kategoria()).getId();
        Long id2 = this.kategoriaRepo.save(new Kategoria()).getId();
        Long uutinenId = lisaaUutinenRepoon();

        mockMvc.perform(get("/uutiset/kategoriat")).andExpect(status().isOk());
        mockMvc.perform(get("/uutiset/kategoriat/" + kategoriaId)).andExpect(status().isOk());

        MvcResult res = mockMvc.perform(get("/uutiset/kategoriat/" + kategoriaId)).andReturn();
        PersistentBag uutiset = (PersistentBag) res.getModelAndView().getModel().get("uutiset");

        assertEquals(false, uutiset.empty());

        res = mockMvc.perform(get("/uutiset/kategoriat/" + id)).andReturn();
        uutiset = (PersistentBag) res.getModelAndView().getModel().get("uutiset");
        assertEquals(true, uutiset.empty());

        res = mockMvc.perform(get("/uutiset/kategoriat/" + id2)).andReturn();
        uutiset = (PersistentBag) res.getModelAndView().getModel().get("uutiset");
        assertEquals(true, uutiset.empty());
    }

    @Transactional
    public Long lisaaUutinenRepoon() throws FileNotFoundException, IOException {
        Uutinen uutinen = new Uutinen();
        uutinen.setNimi("nimi");
        uutinen.setIngressi("ingressi");
        uutinen.setTeksti("teksti");
        uutinen.setKategoriat(new ArrayList());
        uutinen.setKirjoittajat(new ArrayList());
        File file = new File("src/test/java/BibFrog.jpg");
        byte[] picInBytes = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(picInBytes);
        stream.close();
        uutinen.setKuva(picInBytes);
        Kategoria kate = this.kategoriaRepo.save(new Kategoria());
        this.kategoriaId = kate.getId();
        uutinen.addKategoria(kate);
        uutinen.addKirjoittaja(this.kirjoittajaRepo.save(new Kirjoittaja()));
        return this.uutinenRepo.save(uutinen).getId();
    }
}
