package DTO;

public class DiscountReportDto {

    private double OverAllAmount;
    private double OverAllDiscount;
    private double OverallSavings;

    public double getOverAllAmount() {
        return OverAllAmount;
    }

    public void setOverAllAmount(double overAllAmount) {
        OverAllAmount = overAllAmount;
    }

    public double getOverAllDiscount() {
        return OverAllDiscount;
    }

    public void setOverAllDiscount(double overAllDiscount) {
        OverAllDiscount = overAllDiscount;
    }

    public double getOverallSavings() {
        return OverallSavings;
    }

    public void setOverallSavings(double overallSavings) {
        OverallSavings = overallSavings;
    }
}
