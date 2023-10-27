package com.shop.app.product.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shop.app.common.entity.ImageAttachment;
import com.shop.app.common.entity.ImageAttachmentMapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetail {
	private int productDetailId;
	private int productId;
	private String optionName;
	private String optionValue;
	private int additionalPrice;
	
}
