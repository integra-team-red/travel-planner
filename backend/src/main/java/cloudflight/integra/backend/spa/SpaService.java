package cloudflight.integra.backend.spa;

import java.util.List;

public interface SpaService {
    Spa addSpa(Spa spa);

    Spa updateSpa(Long id, Spa Spa);

    void deleteSpa(Long id);

    Spa getSpa(Long id);

    List<Spa> getAllSpas();

    List<Spa> getSpasByCity(Long id);
}
