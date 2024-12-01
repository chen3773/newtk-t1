package com.tiktok.task.domain.ov;

import com.tiktok.task.domain.SpOrders;
import com.tiktok.task.domain.SpProduct;

public class OrderProductOV {
    private SpOrders order;
    private SpProduct product;

    public SpOrders getOrder() {
        return order;
    }

    public void setOrder(SpOrders order) {
        this.order = order;
    }

    public SpProduct getProduct() {
        return product;
    }

    public void setProduct(SpProduct product) {
        this.product = product;
    }
}
