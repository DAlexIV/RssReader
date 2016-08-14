package com.dalexiv.rssreader.data.parsers;

import com.dalexiv.rssreader.data.RssChannel;
import com.dalexiv.rssreader.data.RssItem;
import com.dalexiv.rssreader.domain.parsers.ChannelParserImpl;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by dalexiv on 8/13/16.
 */
public class ChannelParserImplOverTest {
    private String testOverclockersXml = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n" +
            "\n" +
            "<rss version=\"2.0\" xmlns:blogChannel=\"http://backend.userland.com/blogChannelModule\">\n" +
            "\n" +
            "<channel>\n" +
            "<title>Overclockers.ru / Общая лента</title>\n" +
            "<link>http://www.overclockers.ru</link>\n" +
            "<description>Российский оверклокерский портал. Справочник по разгону. Пользовательская и лабораторная статистика разгона процессоров. Обзоры материнских плат, видеокарт, кулеров. Новости из мира оверклокинга. Экстремальный разгон. Файлы, конференция, голосования.</description>\n" +
            "<language>ru</language>\n" +
            "<lastBuildDate>Sat, 13 Aug 2016 18:11:48 +0300</lastBuildDate>\n" +
            "<webMaster>webmaster@overclockers.ru</webMaster>\n" +
            "\n" +
            "<image>\n" +
            "<title>Overclockers.ru</title>\n" +
            "<url>http://www.overclockers.ru/images/88x31/overclockers.gif</url>\n" +
            "<link>http://www.overclockers.ru</link>\n" +
            "<width>88</width>\n" +
            "<height>31</height>\n" +
            "<description>Overclockers.ru</description>\n" +
            "</image>\n" +
            "\n" +
            "<item>\n" +
            "<title>Платформа и игры для Microsoft Project Scorpio уже разрабатываются</title>\n" +
            "<link>http://www.overclockers.ru/hardnews/78385/platforma-i-igry-dlya-microsoft-project-scorpio-uzhe-razrabatyvajutsya.html</link>\n" +
            "<description>До выхода консоли осталось всего около года.</description>\n" +
            "<pubDate>Sat, 13 Aug 2016 18:07:00 +0300</pubDate>\n" +
            "</item>\n" +
            "\n" +
            "<item>\n" +
            "<title>Corsair представила видеокарту Hydro GFX GTX 1080 с гибридной системой охлаждения</title>\n" +
            "<link>http://www.overclockers.ru/hardnews/78384/corsair-predstavila-videokartu-hydro-gfx-gtx-1080-s-gibridnoj-sistemoj-ohlazhdeniya.html</link>\n" +
            "<description>Новинка создана в сотрудничестве с MSI и имеет весьма значительный заводской разгон.</description>\n" +
            "<pubDate>Sat, 13 Aug 2016 15:45:00 +0300</pubDate>\n" +
            "</item>\n" + "</channel>\n" +
            "</rss>";

    @Test
    public void parse() throws Exception {
        final ChannelParserImpl.RssHandler parsingHandler = new ChannelParserImpl.RssHandler();
        SAXParserFactory.newInstance()
                .newSAXParser()
                .parse(new InputSource(new StringReader(testOverclockersXml)), parsingHandler);
        RssChannel channel = parsingHandler.getResult();

        // TODO extract assert method
        Assert.assertEquals("Overclockers.ru / Общая лента",
                channel.getRssData().getTitle());
        Assert.assertEquals("http://www.overclockers.ru",
                channel.getRssData().getLink());

        Assert.assertEquals("Российский оверклокерский портал. Справочник по разгону. Пользовательская и лабораторная статистика разгона процессоров. Обзоры материнских плат, видеокарт, кулеров. Новости из мира оверклокинга. Экстремальный разгон. Файлы, конференция, голосования.",
                channel.getRssData().getDescription());

        Assert.assertEquals("http://www.overclockers.ru/images/88x31/overclockers.gif",
                channel.getRssData().getImageLink());
        Assert.assertEquals(2, channel.getItems().size());

        RssItem rssItem = channel.getItems().get(0);

        Assert.assertEquals("Платформа и игры для Microsoft Project Scorpio уже разрабатываются",
                rssItem.getRssData().getTitle());

        Assert.assertEquals("http://www.overclockers.ru/hardnews/78385/platforma-i-igry-dlya-microsoft-project-scorpio-uzhe-razrabatyvajutsya.html",
                rssItem.getRssData().getLink());

        Assert.assertEquals("До выхода консоли осталось всего около года.",
                rssItem.getRssData().getDescription());
    }

}