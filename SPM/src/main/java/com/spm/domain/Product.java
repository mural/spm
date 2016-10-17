package com.spm.domain;

import com.spm.common.domain.EntityInterface;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Agustin Sgatlata
 */
@RealmClass
public class Product implements Serializable, Comparable<Product>, RealmModel, EntityInterface {

    /**
     *
     */
    private static final long serialVersionUID = -7474271126633756035L;

    @PrimaryKey
    private Long id;

    private String name;
    private Long lineId;
    private Integer priceList;
    private Double price1;
    private Double price1ant;
    private Double price2;
    private Double price2ant;
    private Double price3;
    private Double price3ant;
    private Double price4;
    private Double price4ant;
    private Double price5;
    private Double price5ant;
    private int quantity;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id, String name, Long lineId, Integer priceList) {
        this(id);
        this.name = name;
        this.lineId = lineId;
        this.priceList = priceList;
    }

    public void modify(String name, Integer priceList, Double price1,
                       Double price1ant, Double price2, Double price2ant, Double price3,
                       Double price3ant, Double price4, Double price4ant, Double price5,
                       Double price5ant) {
        this.name = name;
        this.priceList = priceList;
        this.price1 = price1;
        this.price1ant = price1ant;
        this.price2 = price2;
        this.price2ant = price2ant;
        this.price3 = price3;
        this.price3ant = price3ant;
        this.price4 = price4;
        this.price4ant = price4ant;
        this.price5 = price5;
        this.price5ant = price5ant;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getPriceList() {
        return priceList;
    }

    /**
     * @return the price1
     */
    public Double getPrice1() {
        return price1;
    }

    /**
     * @param price1 the price1 to set
     */
    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    /**
     * @return the price1ant
     */
    public Double getPrice1ant() {
        return price1ant;
    }

    /**
     * @param price1ant the price1ant to set
     */
    public void setPrice1ant(Double price1ant) {
        this.price1ant = price1ant;
    }

    /**
     * @return the price2
     */
    public Double getPrice2() {
        return price2;
    }

    /**
     * @param price2 the price2 to set
     */
    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    /**
     * @return the price2ant
     */
    public Double getPrice2ant() {
        return price2ant;
    }

    /**
     * @param price2ant the price2ant to set
     */
    public void setPrice2ant(Double price2ant) {
        this.price2ant = price2ant;
    }

    /**
     * @return the price3
     */
    public Double getPrice3() {
        return price3;
    }

    /**
     * @param price3 the price3 to set
     */
    public void setPrice3(Double price3) {
        this.price3 = price3;
    }

    /**
     * @return the price3ant
     */
    public Double getPrice3ant() {
        return price3ant;
    }

    /**
     * @param price3ant the price3ant to set
     */
    public void setPrice3ant(Double price3ant) {
        this.price3ant = price3ant;
    }

    /**
     * @return the price4
     */
    public Double getPrice4() {
        return price4;
    }

    /**
     * @param price4 the price4 to set
     */
    public void setPrice4(Double price4) {
        this.price4 = price4;
    }

    /**
     * @return the price4ant
     */
    public Double getPrice4ant() {
        return price4ant;
    }

    /**
     * @param price4ant the price4ant to set
     */
    public void setPrice4ant(Double price4ant) {
        this.price4ant = price4ant;
    }

    /**
     * @return the price5
     */
    public Double getPrice5() {
        return price5;
    }

    /**
     * @param price5 the price5 to set
     */
    public void setPrice5(Double price5) {
        this.price5 = price5;
    }

    /**
     * @return the price5ant
     */
    public Double getPrice5ant() {
        return price5ant;
    }

    /**
     * @param price5ant the price5ant to set
     */
    public void setPrice5ant(Double price5ant) {
        this.price5ant = price5ant;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Product another) {
        return name.compareToIgnoreCase(another.getName());
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
