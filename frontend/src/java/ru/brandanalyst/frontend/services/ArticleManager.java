package ru.brandanalyst.frontend.services;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import ru.brandanalyst.core.db.provider.mysqlproviders.MySQLArticleProvider;
import ru.brandanalyst.core.db.provider.InformationSourceProvider;
import ru.brandanalyst.core.model.Article;
import ru.brandanalyst.core.model.InfoSource;
import ru.brandanalyst.frontend.models.WideArticleForWeb;

/**
 * Сервис, извлекающий новости по их идентификатору из БД
 * Created by IntelliJ IDEA.
 * User: Dmitry Batkovich
 * Date: 10/25/11
 * Time: 11:41 PM
 * article service (get article from db and push it to yalet)
 */
public class ArticleManager {

    private final SimpleJdbcTemplate jdbcTemplate;

    public ArticleManager(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public WideArticleForWeb getArticle(long id) {
        MySQLArticleProvider mySQLArticleProvider = new MySQLArticleProvider(jdbcTemplate);
        Article article = mySQLArticleProvider.getArticleById(id);

        InformationSourceProvider informationSourceProvider = new InformationSourceProvider(jdbcTemplate);
        InfoSource infoSource = informationSourceProvider.getInfoSourceById(article.getSourceId());

        return new WideArticleForWeb(article.getLink(),
                article.getTitle(),
                article.getContent(),
                infoSource.getTitle(),
                infoSource.getWebsite(),
                article.getTstamp().toString());
    }
}
