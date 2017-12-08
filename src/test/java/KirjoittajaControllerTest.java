/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Collection;
import juslesan.wepauutiset.WepaUutisetApplication;
import juslesan.wepauutiset.domain.Kategoria;
import juslesan.wepauutiset.domain.Kirjoittaja;
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
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author santeri
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WepaUutisetApplication.class)
public class KirjoittajaControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private UutinenRepository uutinenRepo;

    public KirjoittajaControllerTest() {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void valmiitKirjoittajat() throws Exception {
        mockMvc.perform(get("/uutiset/kirjoittajat")).andExpect(status().isOk());
        MvcResult res = mockMvc.perform(get("/uutiset/kirjoittajat")).andReturn();

        Collection<Kirjoittaja> kirjoittajat = (Collection) res.getModelAndView().getModel().get("kirjoittajat");
        assertEquals(5, kirjoittajat.size());
    }

    @Test
    public void lisaaKirjoittaja() throws Exception {
        mockMvc.perform(post("/uutiset/kirjoittajat").param("nimi", "Uusi")).andExpect(status().is3xxRedirection());
        MvcResult res = mockMvc.perform(get("/uutiset/kirjoittajat")).andReturn();
        Collection<Kirjoittaja> kirjoittajat = (Collection) res.getModelAndView().getModel().get("kirjoittajat");
        for (Kirjoittaja kirjoittaja : kirjoittajat) {
            if (kirjoittaja.getNimi().equals("Uusi")) {
                Assert.isTrue(true, "true");
            }
        }
    }

}
