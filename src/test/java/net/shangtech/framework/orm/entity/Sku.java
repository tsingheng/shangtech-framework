package net.shangtech.framework.orm.entity;

import javax.persistence.Column;
import javax.persistence.Lob;

import org.hibernate.annotations.Index;

import net.shangtech.framework.orm.dao.support.BaseEntity;

public class Sku extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	@Column(name = "sku_name")
	private String name;
	
    @Column(name = "sku_code")
	@Index(name = "idx_sku_code")
	private String code;
	
    @Column
	private String image;
	
	@Column(name = "market_price")
	private Double marketPrice;
	
	@Column(name = "sell_price")
	private Double sellPrice;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "brand_id")
	@Index(name = "idx_sku_brand_id")
	private Long brandId;
	
	@Column(name = "brand_code")
	private String brandCode;
	
	@Column(name = "category_id")
	@Index(name = "idx_sku_category_id")
	private Long categoryId;
	
	@Column(name = "status")
	private String status;
	
	@Lob
	@Column(name = "images")
	private String images;
	
	@Lob
	@Column(name = "content")
	private String content;
	
	@Column(name = "vid")
	@Index(name = "idx_sku_vid")
	private String vid;
	
	@Column(name = "colors")
	private String colors;
	
	@Column(name = "tags")
	private String tags;
	
	@Column(name = "min")
	private Integer min;
	
	@Column(name = "max")
	private Integer max;
	
	@Column(name = "image_num")
	private Integer imageNum;
	
	@Column(name = "weight")
	private Double weight;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getImageNum() {
		return imageNum;
	}

	public void setImageNum(Integer imageNum) {
		this.imageNum = imageNum;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
}
