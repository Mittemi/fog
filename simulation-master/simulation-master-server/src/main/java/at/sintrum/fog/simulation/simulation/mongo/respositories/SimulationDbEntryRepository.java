package at.sintrum.fog.simulation.simulation.mongo.respositories;

import at.sintrum.fog.simulation.simulation.mongo.SimulationDbEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
@Service
public interface SimulationDbEntryRepository extends CrudRepository<SimulationDbEntry, String> {

    List<SimulationDbEntry> findBySimulationRunId(String id);
}
