package ru.brandanalyst.miner;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import ru.brandanalyst.core.db.provider.BrandProvider;
import ru.brandanalyst.core.model.Brand;
import ru.brandanalyst.miner.listener.LentaScraperRuntimeListener;
import ru.brandanalyst.miner.util.DataTransformator;

import java.util.Date;

public class GrabberLenta extends Grabber {
    private static final Logger log = Logger.getLogger(GrabberLenta.class);

    private static final String beginSearchURL = "http://lenta.ru/search/?query=";
    private static final String endSearchURL = "http://lenta.ru/search/?query=";
    private static final String sourceURL = "http://lenta.ru";

    @Override
    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void grab(Date timeLimit) {

        for (Brand b : new BrandProvider(jdbcTemplate).getAllBrands()) {
            try {
                ScraperConfiguration config = new ScraperConfiguration(this.config);
                Scraper scraper = new Scraper(config, ".");
                scraper.setDebug(true);
                scraper.addRuntimeListener(new LentaScraperRuntimeListener(this.jdbcTemplate, timeLimit));
                String query = DataTransformator.stringToHexQueryString(b.getName());
                scraper.addVariableToContext("lentaQueryURL", beginSearchURL + query + endSearchURL);//"$page" - suffix for result page number
                scraper.addVariableToContext("lentaAbsoluteURL", sourceURL);
                scraper.addVariableToContext("brandId", Long.toString(b.getId()));
                scraper.execute();
                log.info("successful processing brand " + b.getName());
            } catch (Exception exception) {
                exception.printStackTrace();
                log.error("cannot process Lenta. brand name = " + b.getName());
            }
        }

    }
}
