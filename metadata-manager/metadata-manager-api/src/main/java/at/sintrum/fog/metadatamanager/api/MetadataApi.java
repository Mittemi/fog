package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.MetadataBase;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public interface MetadataApi<TModel extends MetadataBase> {

    @RequestMapping(value = "", method = RequestMethod.PUT)
    TModel store(@RequestBody TModel metadata);

    @RequestMapping(value = "getById/{id}", method = RequestMethod.GET)
    TModel getById(@PathVariable("id") String id);

    @RequestMapping(value = "getAll", method = RequestMethod.POST)
    List<TModel> getAll();
}
