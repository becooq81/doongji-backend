package com.find.doongji.search.payload.response;

public enum SimilarityScore {

    HIGH(60.0, 100.0),
    MEDIUM(40.0, 60.0),
    LOW(0.0, 40.0);

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
        return null;
    }
}
