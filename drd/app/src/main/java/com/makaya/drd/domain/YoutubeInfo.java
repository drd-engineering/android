package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xbudi on 17/09/2017.
 */

public class YoutubeInfo {

    public String kind;
    public String etag;
    public PageInfo pageInfo;
    public ArrayList<Item> items;

    public class PageInfo {
        public int totalResults;
        public int resultsPerPage;
    }

    public class Default {
        public String url;
        public int width;
        public int height;
    }

    public class Medium {
        public String url;
        public int width;
        public int height;
    }

    public class High {
        public String url;
        public int width;
        public int height;
    }

    public class Standard {
        public String url;
        public int width;
        public int height;
    }

    public class Maxres {
        public String url;
        public int width;
        public int height;
    }

    public class Thumbnails {
        public Default default_;
        public Medium medium;
        public High high;
        public Standard standard;
        public Maxres maxres;
    }

    public class Localized {
        public String title;
        public String description;
    }

    public class Snippet {
        public Date publishedAt;
        public String channelId;
        public String title;
        public String description;
        public Thumbnails thumbnails;
        public String channelTitle;
        public ArrayList<String> tags;
        public String categoryId;
        public String liveBroadcastContent;
        public Localized localized;
    }

    public class Item {
        public String kind;
        public String etag;
        public String id;
        public Snippet snippet;
    }


}
