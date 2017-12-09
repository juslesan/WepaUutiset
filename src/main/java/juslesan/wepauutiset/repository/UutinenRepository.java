/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juslesan.wepauutiset.repository;

import java.util.List;
import juslesan.wepauutiset.domain.Kategoria;
import juslesan.wepauutiset.domain.Uutinen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UutinenRepository extends JpaRepository<Uutinen, Long> {
}
