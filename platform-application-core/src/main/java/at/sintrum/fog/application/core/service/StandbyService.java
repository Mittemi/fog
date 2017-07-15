package at.sintrum.fog.application.core.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
public interface StandbyService extends ApplicationListener<ApplicationReadyEvent> {
}
