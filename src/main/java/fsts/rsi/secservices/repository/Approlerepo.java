package fsts.rsi.secservices.repository;

import fsts.rsi.secservices.entities.AppUser;
import fsts.rsi.secservices.entities.Approle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Approlerepo extends JpaRepository<Approle,Long> {
    Approle findByName(String name);

}
