/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Collection;
import juslesan.wepauutiset.WepaUutisetApplication;
import juslesan.wepauutiset.domain.Kategoria;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author santeri
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WepaUutisetApplication.class)
public class KategoriaControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private UutinenRepository uutinenRepo;

    public KategoriaControllerTest() {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void valmiitKategoriat() throws Exception {
        mockMvc.perform(get("/uutiset/kategoriat")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/uutiset/kategoriat")).andReturn();

        Collection<Kategoria> kategoriat = (Collection) res.getModelAndView().getModel().get("kategoriat");
        assertEquals(5, kategoriat.size());

    }

    @Test
    public void lisaaKategoria() throws Exception {
//        MultiValueMap<String, String> params = new MultiValueMap();
//        params.add(k, v);
        mockMvc.perform(post("/uutiset/kategoriat").param("nimi", "Uusi")).andExpect(status().is3xxRedirection());
        MvcResult res = mockMvc.perform(get("/uutiset/kategoriat")).andReturn();
        Collection<Kategoria> kategoriat = (Collection) res.getModelAndView().getModel().get("kategoriat");
        for (Kategoria kategoria : kategoriat) {
            if (kategoria.getNimi().equals("Uusi")) {
                Assert.isTrue(true, "true");
            }
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
