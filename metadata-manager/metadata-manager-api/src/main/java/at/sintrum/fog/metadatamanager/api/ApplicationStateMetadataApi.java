package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 01.08.2017.
 */
@RequestMapping(value = "application")
public interface ApplicationStateMetadataApi {

    @RequestMapping(value = "", method = RequestMethod.PUT)
    ApplicationStateMetadata store(@RequestBody ApplicationStateMetadata metadata);

    @RequestMapping(value = "getById/{id}/", method = RequestMethod.GET)
    ApplicationStateMetadata getById(@PathVariable("id") String id);

    @RequestMapping(value = "getUrl/{id}/", method = RequestMethod.GET)
    FogIdentification getApplicationUrl(@PathVariable("id") String id);

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable("id") String id);

    @RequestMapping(value = "", method = RequestMethod.GET)
    List<ApplicationStateMetadata> getAll();

    @RequestMapping(value = "setState/{id}/{state}", method = RequestMethod.POST)
    ApplicationStateMetadata setState(@PathVariable("id") String id, @PathVariable("state") AppState state);

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    void reset();

    @RequestMapping(value = "getByFog", method = RequestMethod.POST)
    List<ApplicationStateMetadata> getByFog(@RequestBody FogIdentification fogIdentification);

    @RequestMapping(value = "deprecateInstance/{instanceId}", method = RequestMethod.POST)
    boolean deprecateInstance(@PathVariable("instanceId") String instanceId);

    @RequestMapping(value = "isActiveInstance/{instanceId}", method = RequestMethod.POST)
    boolean isActiveInstance(@PathVariable("instanceId") String instanceId);
}
