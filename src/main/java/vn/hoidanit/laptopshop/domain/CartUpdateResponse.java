package vn.hoidanit.laptopshop.domain;

public class CartUpdateResponse {
    private boolean success;
    private long quantity;
    private double itemTotal;
    private double cartTotal;
    private boolean deleted;

    public CartUpdateResponse() {
    }

    public CartUpdateResponse(boolean success, long quantity, double itemTotal, double cartTotal, boolean deleted) {
        this.success = success;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
        this.cartTotal = cartTotal;
        this.deleted = deleted;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
