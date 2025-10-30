package cloudflight.integra.backend.nightlife;

import java.util.List;

public interface NightlifeService {
    Nightlife add(Nightlife nightlife);

    Nightlife update(Long id, Nightlife nightlife);

    void delete(Long id);

    Nightlife get(Long id);

    List<Nightlife> getAll();

    List<Nightlife> getAllByCity(Long id);
}
