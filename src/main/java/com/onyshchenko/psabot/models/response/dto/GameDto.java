package com.onyshchenko.psabot.models.response.dto;

import java.util.List;


public class GameDto {

    private String name;
    private String id;
    private String releaseDate;
    private String publisher;
    private String url;
    private boolean inWl;
    private boolean exclusive;
    private boolean eaAccess;
    private List<String> genres;
    private String category;

    private Price price;


    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isInWl() {
        return inWl;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isEaAccess() {
        return eaAccess;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getCategory() {
        return category;
    }

    public class Price {

        private String currentPrice;
        private String currentDiscount;
        private String lowestPrice;
        private String highestPrice;
        private String highestDiscount;
        private String currentPercentageDiscount;
        private String currentPsPlusPrice;

        public String getCurrentPrice() {
            return currentPrice;
        }

        public String getCurrentDiscount() {
            return currentDiscount;
        }

        public String getLowestPrice() {
            return lowestPrice;
        }

        public String getHighestPrice() {
            return highestPrice;
        }

        public String getHighestDiscount() {
            return highestDiscount;
        }

        public String getCurrentPercentageDiscount() {
            return currentPercentageDiscount;
        }

        public String getCurrentPsPlusPrice() {
            return currentPsPlusPrice;
        }
    }
}
