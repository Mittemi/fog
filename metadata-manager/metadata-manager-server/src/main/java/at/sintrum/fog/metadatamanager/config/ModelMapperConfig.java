package at.sintrum.fog.metadatamanager.config;

import org.joda.time.DateTime;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        //lambdas wouldn't work here
        modelMapper.addConverter(new Converter<Date, DateTime>() {
            @Override
            public DateTime convert(MappingContext<Date, DateTime> mappingContext) {
                Date source = mappingContext.getSource();
                if (source == null) return null;
                return new DateTime(source);
            }
        });

        modelMapper.addConverter(new Converter<DateTime, Date>() {
            @Override
            public Date convert(MappingContext<DateTime, Date> mappingContext) {
                DateTime source = mappingContext.getSource();
                if (source == null) return null;
                return source.toDate();
            }
        });

        return modelMapper;
    }
}
