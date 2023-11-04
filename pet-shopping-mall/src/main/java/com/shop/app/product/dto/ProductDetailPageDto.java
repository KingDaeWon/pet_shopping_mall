package com.shop.app.product.dto;

import java.security.Timestamp;
import java.util.List;

import com.shop.app.product.entity.ProductDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailPageDto {
	
	private int productId;
	
	private Timestamp reviewCreatedAt;
	
	
	
	
	
	
	
}
