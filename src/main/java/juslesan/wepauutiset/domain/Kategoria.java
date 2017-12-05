/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Kategoria extends AbstractPersistable<Long> {

    private String nimi;

    @ManyToMany(mappedBy = "kategoriat", fetch = FetchType.EAGER)
    private List<Uutinen> uutiset;

    public void addUutinen(Uutinen uutinen) {
        if (uutinen != null && !uutiset.contains(uutinen)) {
            uutiset.add(uutinen);
        }
    }
}
