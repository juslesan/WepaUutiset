/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.repository;

import juslesan.wepauutiset.domain.Kirjoittaja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KirjoittajaRepository extends JpaRepository<Kirjoittaja, Long> {

}
