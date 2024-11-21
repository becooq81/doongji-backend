package com.find.doongji.search.enums;

public enum SimilarityScore {

    HIGH(60.0, Double.MAX_VALUE),
    MEDIUM(40.0, 60.0),
    LOW(Double.MIN_VALUE, 40.0),
    NONE(Double.MIN_VALUE, Double.MIN_VALUE);

    private final double lowerBound;
    private final double upperBound;

    SimilarityScore(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public static SimilarityScore classify(double score) {
        for (SimilarityScore level : SimilarityScore.values()) {
            if (score >= level.getLowerBound() && score < level.getUpperBound()) {
                return level;
            }
        }
        throw new IllegalArgumentException("Score " + score + " is out of valid range");
    }
}
