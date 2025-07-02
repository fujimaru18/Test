package Model;

public class RevenueDTO {

    private String label;
    private int invoiceCount;
    private double total;

    public RevenueDTO(String label, int invoiceCount, double total) {
        this.label = label;
        this.invoiceCount = invoiceCount;
        this.total = total;
    }

    public String getLabel() {
        return label;
    }

    public int getInvoiceCount() {
        return invoiceCount;
    }

    public double getTotal() {
        return total;
    }
}
