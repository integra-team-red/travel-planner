package cloudflight.integra.backend.proposal;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByStatus(Status status);
}
