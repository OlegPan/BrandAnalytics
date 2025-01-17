package ru.brandanalyst.analyzer;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import ru.brandanalyst.core.db.provider.mysqlproviders.MySQLArticleProvider;
import ru.brandanalyst.core.db.provider.BrandProvider;
import ru.brandanalyst.core.db.provider.GraphProvider;
import ru.brandanalyst.core.model.Article;
import ru.brandanalyst.core.model.Brand;
import ru.brandanalyst.core.model.Graph;
import ru.brandanalyst.core.model.SingleDot;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, строящий основные временные ряды по данным из новостных источников
 * Created by IntelliJ IDEA.
 * User: Dmitry Batkovich
 * Date: 10/26/11
 * Time: 3:14 PM
 *
 * @version 1.0
 */
public class GraphsAnalyzer {
    private static final Logger log = Logger.getLogger(GraphsAnalyzer.class);

    /**
     * шаблон первичной БД
     */
    private SimpleJdbcTemplate dirtyJdbcTemplate;
    /**
     * шаблон вторичной БД
     */
    private SimpleJdbcTemplate pureJdbcTemplate;

    /**
     * @param pureJdbcTemplate  шаблон вторичной базы данных
     * @param dirtyJdbcTemplate шаблон первичной базы данных
     */
    public GraphsAnalyzer(SimpleJdbcTemplate pureJdbcTemplate, SimpleJdbcTemplate dirtyJdbcTemplate) {
        this.dirtyJdbcTemplate = dirtyJdbcTemplate;
        this.pureJdbcTemplate = pureJdbcTemplate;
    }

    /*
    * метод, где проводится построение рядов
    */
    public void analyze() {

        log.info("graph analyzing started...");
        BrandProvider dirtyBrandProvider = new BrandProvider(dirtyJdbcTemplate);
        BrandProvider pureBrandProvider = new BrandProvider(pureJdbcTemplate);
        MySQLArticleProvider dirtyMySQLArticleProvider = new MySQLArticleProvider(dirtyJdbcTemplate);
        MySQLArticleProvider pureMySQLArticleProvider = new MySQLArticleProvider(pureJdbcTemplate);
        GraphProvider pureGraphProvider = new GraphProvider(pureJdbcTemplate);

        Map<Long, Double> graphMap = new HashMap<Long, Double>(); //out of memory

        //counting value
        for (Brand b : dirtyBrandProvider.getAllBrands()) {
            pureBrandProvider.writeBrandToDataStore(b); //it shouldn't be here

            for (Article a : dirtyMySQLArticleProvider.getAllOfficialArticlesByBrand(b.getId())) {

                Timestamp timestamp = a.getTstamp();
                if (graphMap.containsKey(timestamp.getTime())) {
                    graphMap.put(timestamp.getTime(), graphMap.get(timestamp.getTime()) + 1.0);
                } else {
                    graphMap.put(timestamp.getTime(), 1.0);
                }

                pureMySQLArticleProvider.writeArticleToDataStore(a);
            }
            //map to graph
            Graph graph = new Graph("");
            for(Long time: graphMap.keySet()) {
                graph.addPoint(new SingleDot(new Timestamp(time), graphMap.get(time)));
            }

            pureGraphProvider.writeGraph(graph, b.getId(), 1);
        }
        log.info("graph analazing finished succesful");
    }
}
