package com.dalexiv.rssreader.domain.parsers;

import android.support.annotation.IntDef;
import android.util.Log;

import com.dalexiv.rssreader.data.RssChannel;
import com.dalexiv.rssreader.data.RssData;
import com.dalexiv.rssreader.data.RssItem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by dalexiv on 8/13/16.
 */

public class ChannelParserImpl implements ChannelParser {
    private static String TAG = ChannelParserImpl.class.getSimpleName();

    @Override
    public RssChannel parse(String uri) {
        try {
            final RssHandler parsingHandler = new RssHandler();
            SAXParserFactory.newInstance()
                    .newSAXParser()
                    .parse(uri, parsingHandler);
            return parsingHandler.getResult();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.e(TAG, "Exception while parsing", e);
            return null;
        }
    }

    public static class RssHandler extends DefaultHandler {
        private List<RssItem> rssItems;

        private @ParsingStatus int status;
        private boolean parsingChannelInfo;

        private RssData headerRssData;
        private String currentLink;
        private String currentTitle;
        private String currentDescription;
        private long currentPubDate;
        private String currentImageLink;
        private String currentFullImageLink;


        DateFormat formatter;

        public RssHandler() {
            status = NO_PARSING;
            parsingChannelInfo = true;
            this.rssItems = new ArrayList<>();
            formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        }


        public RssChannel getResult() {
            return RssChannel.create(headerRssData, rssItems);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            // If we are within image tag,
            // we will skip all the other tags until we reach closing image tag
            if (status == PARSING_CHANNEL_IMAGE) {
                if (qName.equals(IMAGE_URL_TAG))
                    status = PARSING_CHANNEL_IMAGE_URL;
                return;
            }

            switch (qName) {
                case ITEM:
                    if (parsingChannelInfo) {
                        headerRssData = RssData.create(currentTitle,
                                currentLink, currentDescription, currentImageLink);
                        parsingChannelInfo = false;
                    }
                case TITLE:
                    status = PARSING_TITLE;
                    break;
                case LINK:
                    status = PARSING_LINK;
                    break;
                case DESCRIPTION:
                    status = PARSING_DESCRIPTION;
                    break;
                case CHANNEL_IMAGE:
                    status = PARSING_CHANNEL_IMAGE;
                    break;
                case PUB_DATE:
                    status = PARSING_PUB_DATE;
                    break;
            }

            if (IMAGE_TAGS.contains(qName))
                if (attributes.getValue(IMAGE_URL_TAG) != null) {
                    if (currentFullImageLink == null)
                        currentFullImageLink = attributes.getValue(IMAGE_URL_TAG);
                    else
                        currentImageLink = attributes.getValue(IMAGE_URL_TAG);

                }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // We are not changing status, until we reach the closing image tag
            if (status == PARSING_CHANNEL_IMAGE || status == PARSING_CHANNEL_IMAGE_URL) {
                if (!qName.equals(CHANNEL_IMAGE)) {
                    status = PARSING_CHANNEL_IMAGE;
                    return;
                }
            }

            switch (qName) {
                case ITEM:
                    // Handling various image cases
                    if ((currentImageLink == null
                            || currentImageLink.equals(headerRssData.getImageLink())
                            && currentFullImageLink != null))
                        currentImageLink = currentFullImageLink;

                    final RssData rssData = RssData.create(currentTitle, currentLink,
                            currentDescription, currentImageLink);
                    rssItems.add(RssItem.create(rssData, currentFullImageLink, currentPubDate));
                    currentFullImageLink = null;
                    currentImageLink = null;
                    break;
                default:
                    status = NO_PARSING;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            final String text = new String(ch, start, length);

            // Avoiding some new-line pitfalls
            if (text.equals("\n"))
                return;

            switch (status) {
                case PARSING_TITLE:
                    currentTitle = text;
                    break;
                case PARSING_LINK:
                    currentLink = text;
                    break;
                case PARSING_DESCRIPTION:
                    currentDescription = text;
                    break;
                case PARSING_CHANNEL_IMAGE_URL:
                    currentImageLink = text;
                    break;
                case PARSING_PUB_DATE:
                    try {
                        currentPubDate = formatter.parse(text).getTime();
                    }
                    catch (ParseException ex)  {
                        currentPubDate = 0;
                    }
                    break;
            }
        }

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({NO_PARSING, PARSING_TITLE, PARSING_LINK,
                PARSING_DESCRIPTION, PARSING_CHANNEL_IMAGE,
                PARSING_CHANNEL_IMAGE_URL, PARSING_PUB_DATE
        })
        public @interface ParsingStatus {}
        public static final int NO_PARSING = -1;
        public static final int PARSING_TITLE = 0;
        public static final int PARSING_LINK = 1;
        public static final int PARSING_DESCRIPTION = 2;
        public static final int PARSING_CHANNEL_IMAGE= 3;
        public static final int PARSING_CHANNEL_IMAGE_URL= 4;
        public static final int PARSING_PUB_DATE = 5;
    }
}
