/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String teksti;
    private String ingressi;
    private byte[] kuva;
    private LocalDateTime uutinenDate;
}
