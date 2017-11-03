package at.sintrum.fog.simulation.simulation.mongo.respositories;

import at.sintrum.fog.simulation.simulation.mongo.FullSimulationResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 03.11.2017.
 */
@Service
public interface FullSimulationResultRepository extends CrudRepository<FullSimulationResult, String> {
}
