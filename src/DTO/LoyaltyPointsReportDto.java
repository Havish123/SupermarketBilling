package DTO;

public class LoyaltyPointsReportDto {
    private int PointsEarned;
    private int PointsSpend;
    private int CurrentPoints;

    public int getPointsEarned() {
        return PointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        PointsEarned = pointsEarned;
    }

    public int getPointsSpend() {
        return PointsSpend;
    }

    public void setPointsSpend(int pointsSpend) {
        PointsSpend = pointsSpend;
    }

    public int getCurrentPoints() {
        return CurrentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        CurrentPoints = currentPoints;
    }
}
