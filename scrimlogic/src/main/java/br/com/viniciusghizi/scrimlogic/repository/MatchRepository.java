package br.com.viniciusghizi.scrimlogic.repository;

import br.com.viniciusghizi.scrimlogic.domain.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, String> {
}