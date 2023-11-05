package com.shop.app.review.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewAvgDto {
	
	private String productName; // product
	private int productId; // product
	private long totalCount;
	private double reviewStarRate; // review
	private String reviewMemberId;
	private LocalDateTime reviewCreatedAt;
	private int reviewId;
	private String reviewTitle;
	private String reviewContent;
	
	public long getTotalCount() {
		return totalCount;
	}
}