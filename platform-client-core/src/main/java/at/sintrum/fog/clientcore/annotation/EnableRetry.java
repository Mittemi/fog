package at.sintrum.fog.clientcore.annotation;

import java.lang.annotation.*;

/**
 * Created by Michael Mittermayr on 25.05.2017.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableRetry {

}
