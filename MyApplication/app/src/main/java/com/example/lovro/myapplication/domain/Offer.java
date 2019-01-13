package com.example.lovro.myapplication.domain;

import com.squareup.moshi.Json;

import java.util.List;

public class Offer {

    @Json(name = "id")
    private Long id;

    @Json(name = "name")
    private String name;

    @Json(name = "price")
    private Double price;

    @Json(name = "specification")
    private String specification;

    @Json(name = "description")
    private String description;

    @Json(name = "imageURL")
    private String imageUrl;

    @Json(name = "styles")
    private List<Style> styles;

    @Json(name = "dimensions")
    private List<Dimension> dimensions;

    public Offer(Long id,String name,Double price,String specification,String description,String imageUrl,List<Style> styles,List<Dimension> dimensions){
        this.id = id;
        this.description = description;
        this.dimensions = dimensions;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.styles = styles;
        this.specification = specification;
    }

    public Offer() {
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public List<Style> getStyles() {
        return styles;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSpecification() {
        return specification;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setStyles(List<Style> styles) {
        this.styles = styles;
    }

    public boolean nothingNull(){
        if (this.name!=null && this.description!=null && this.specification!=null && this.price!=null && this.dimensions!=null && this.styles!=null){
            return true;
        }else{
            return false;
        }
    }

    public void addStyle(Style style){
        this.styles.add(style);
    }
    public void addDimension(Dimension dim){
        this.dimensions.add(dim);
    }
}

