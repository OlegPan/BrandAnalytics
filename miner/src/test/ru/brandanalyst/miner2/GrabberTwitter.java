package ru.brandanalyst.miner2;

import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import static java.lang.System.out;

/**
 * Created by IntelliJ IDEA.
 * User: Obus
 * Date: 10.10.11
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class GrabberTwitter extends ExactGrabber{

    private final int ISSUANCE_SIZE = 200;

    List<String> brandNames;

    public void setBrandNames(List<String> brandNames) {
        this.brandNames = brandNames;
    }

    public void grab(){                          //тут надо складывать в БД данные

        List<String> result = new ArrayList<String>();
        try{
            //TODO: make this piece of shit, named as code, BETTER (get rid of <code>pageNumber</code>, for example)
            Twitter twitter = new TwitterFactory().getInstance();
            Query query = new Query(brandNames.get(0));

            Calendar cal = new GregorianCalendar();
            query.setSince("2011"+"-"+"10"+"-"+"11");
            //query.setSince(new SimpleDateFormat("yyy-MM-dd").format(cal.getTime()));
            query.setLang("ru");
            //query.setRpp(100); // it's ok

            List<Tweet> resultTweets;
            QueryResult queryResult;
            int pageNumber = 1;
            do{
                query.setPage(pageNumber);
                queryResult = twitter.search(query);
                resultTweets = queryResult.getTweets();

                for (int i = 0; i < resultTweets.size(); ++i){
                    result.add(resultTweets.get(i).getText());
                }
                pageNumber++;

            } while (ISSUANCE_SIZE == resultTweets.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for(String resultString : result){
                out.println(resultString);
        }
    }
}