package DealsFinder;

public class DealsFinderBean {

    private int propertyId;
    private String sellerMobile;
    private String sellerName;
    private String buyerMobile;
    private String buyerName;
    private String dealDate;
    private String registryDate;
    private double finalAmount;
    private double advance;
    private double balance;
    private double commission;
    private String dealStatus;

    public DealsFinderBean(int propertyId, String sellerMobile, String sellerName,
                     String buyerMobile, String buyerName,
                     String dealDate, String registryDate,
                     double finalAmount, double advance,
                     double balance, double commission,
                     String dealStatus) {

        this.propertyId = propertyId;
        this.sellerMobile = sellerMobile;
        this.sellerName = sellerName;
        this.buyerMobile = buyerMobile;
        this.buyerName = buyerName;
        this.dealDate = dealDate;
        this.registryDate = registryDate;
        this.finalAmount = finalAmount;
        this.advance = advance;
        this.balance = balance;
        this.commission = commission;
        this.dealStatus = dealStatus;
    }

    public int getPropertyId() { return propertyId; }
    public String getSellerMobile() { return sellerMobile; }
    public String getSellerName() { return sellerName; }
    public String getBuyerMobile() { return buyerMobile; }
    public String getBuyerName() { return buyerName; }
    public String getDealDate() { return dealDate; }
    public String getRegistryDate() { return registryDate; }
    public double getFinalAmount() { return finalAmount; }
    public double getAdvance() { return advance; }
    public double getBalance() { return balance; }
    public double getCommission() { return commission; }
    public String getDealStatus() { return dealStatus; }
}