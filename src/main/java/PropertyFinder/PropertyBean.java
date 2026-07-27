package PropertyFinder;

public class PropertyBean {

    private String area;
    private String city;
    private String size;
    private String frontSide;
    private String rearSide;
    private String leftSide;
    private String rightSide;
    private String direction;
    private String propertyType;
    private String constructionType;
    private String approvedBy;
    private double price;
    private String otherInfo;
    private byte[] image1;
    private byte[] image2;
    private String property_status;

    public PropertyBean(String area, String city, String size,
                        String frontSide, String rearSide,
                        String leftSide, String rightSide,
                        String direction, String propertyType,
                        String constructionType, String approvedBy,
                        double price, String otherInfo,
                        byte[] image1, byte[] image2, String property_status) {

        this.area = area;
        this.city = city;
        this.size = size;
        this.frontSide = frontSide;
        this.rearSide = rearSide;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.direction = direction;
        this.propertyType = propertyType;
        this.constructionType = constructionType;
        this.approvedBy = approvedBy;
        this.price = price;
        this.otherInfo = otherInfo;
        this.image1 = image1;
        this.image2 = image2;
        this.property_status = property_status;
    }

    public String getArea() { return area; }
    public String getCity() { return city; }
    public String getSize() { return size; }
    public String getFrontSide() { return frontSide; }
    public String getRearSide() { return rearSide; }
    public String getLeftSide() { return leftSide; }
    public String getRightSide() { return rightSide; }
    public String getDirection() { return direction; }
    public String getPropertyType() { return propertyType; }
    public String getConstructionType() { return constructionType; }
    public String getApprovedBy() { return approvedBy; }
    public double getPrice() { return price; }
    public String getOtherInfo() { return otherInfo; }
    public byte[] getImage1() { return image1; }
    public byte[] getImage2() { return image2; }
    public String getProperty_status() { return property_status; }
}