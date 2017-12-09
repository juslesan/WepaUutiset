/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.domain;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author santeri
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Uutinen extends AbstractPersistable<Long> {

    private String nimi;
    private int luettu;
    private String teksti;
    private String ingressi;

    @Lob
    private byte[] kuva;
    private LocalDateTime uutinenDate;
    @ManyToMany
    private List<Kirjoittaja> kirjoittajat;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Kategoria> kategoriat;

    public void luettuAdd() {
        luettu++;
    }

    public void addKategoria(Kategoria kategoria) {
        if (kategoria != null && !kategoriat.contains(kategoria)) {
            kategoriat.add(kategoria);
        }
    }

    public void addKirjoittaja(Kirjoittaja kirjoittaja) {
        if (kirjoittaja != null && !kirjoittajat.contains(kirjoittaja)) {
            kirjoittajat.add(kirjoittaja);
        }
    }

//    public void setKuva(byte[] kuva) {
//        try {
//            this.kuva.setBytes(0, kuva);
//        } catch (SQLException e) {
//            System.out.println(e);
//
//        }
//    }
//
//    public byte[] getKuva() {
//        try {
//            return this.kuva.getBytes(0, Integer.MAX_VALUE);
//        } catch (SQLException e) {
//            System.out.println(e);
//
//        }
//        return null;
//    }
}
