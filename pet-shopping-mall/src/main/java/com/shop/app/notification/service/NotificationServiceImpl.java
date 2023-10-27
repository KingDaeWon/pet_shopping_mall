package com.shop.app.notification.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.shop.app.coupon.entity.MemberCoupon;
import com.shop.app.notification.entity.Notification;
import com.shop.app.notification.repository.NotificationRepository;
import com.shop.app.order.entity.Order;
import com.shop.app.order.repository.OrderRepository;
import com.shop.app.payment.dto.PaymentCompleteNotificationDto;
import com.shop.app.point.entity.Point;
import com.shop.app.servicecenter.inquiry.entity.Question;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	OrderRepository orderRepository;
	
	
	/**
    * @author 김대원
    * 주문확정되면 알림을 보내는 메서드
    */
	@Override
	public int updateOrderStatusNotification() {
		// 실시간 알림을 보낸다.
		// 1. 작성자의 구독자 조회
		// 2. 각 사용자에게 알림메세지 발송(stomp)
		int result = 0;
		List<Order> orders = orderRepository.findOrdersWithExpiredStatus();
		for (Order order : orders) {
			// 주문상태를 주문확정으로 바꾸고
			String to = order.getMemberId();
	        Notification insertNotification = Notification.builder()
	            .notiCategory(1)
	            .notiContent(order.getOrderNo() + "번 주문이 구매확정되었습니다.")
	            .notiCreatedAt(formatTimestampNow())
	            .memberId(to) 
	            .build();
			
	        // db에 알림저장
			result = notificationRepository.insertNotification(insertNotification);
			// db에서 가장 최신 알림 꺼내서
			Notification notification = notificationRepository.latestNotification();
			// 메세지 보냄
			simpMessagingTemplate.convertAndSend("/pet/notice/" + to, notification);
		}
		return result;
	}
	
	/**
    * @author 김대원
    * 리팩토링 회원가입 쿠폰발급 알림 
    */
	public int memberCreateNotification(MemberCoupon memberCoupon) {
		int result = 0;
		String to = memberCoupon.getMemberId();
		Notification insertNotification = Notification.builder()
				.notiCategory(3)
				.notiContent("회원가입 배송비 할인 쿠폰이 발급됐습니다.")
				.notiCreatedAt(formatTimestampNow())
				.memberId(to) 
				.build();
		
		result = notificationRepository.insertNotification(insertNotification);
		Notification notification = notificationRepository.latestNotification();
		simpMessagingTemplate.convertAndSend("/pet/notice/" + to, notification);
		
		return result;
	}
	
	/**
    * @author 김대원
    * 리팩토링 리뷰 적립금 알림
    */
	public int reviewCreateNotification(Point newPoint) {
		int result = 0;
		String to = newPoint.getPointMemberId();
		Notification insertNotification = Notification.builder()
				.notiCategory(3)
				.notiContent("리뷰 작성 포인트가 적립되었습니다.")
				.notiCreatedAt(formatTimestampNow())
				.memberId(to) 
				.build();
		
		notificationRepository.insertNotification(insertNotification);
		Notification notification = notificationRepository.latestNotification();
		simpMessagingTemplate.convertAndSend("/pet/notice/" + to, notification);
		
		return result;
	}
	
	/**
    * @author 김대원
    * 리팩토링 질문답변글 알림
    */
	public int adminAnswerCreateNotification(Question questions) {
		int result = 0;
		String to = questions.getQuestionMemberId();
		Notification insertNotification = Notification.builder()
				.notiCategory(3)
				.notiContent(questions.getQuestionTitle()+ " 질문에 답변이 달렸습니다.")
				.notiCreatedAt(formatTimestampNow())
				.memberId(to) 
				.build();
		
		notificationRepository.insertNotification(insertNotification);
		Notification notification = notificationRepository.latestNotification();
		simpMessagingTemplate.convertAndSend("/pet/notice/" + to, notification);
		
		return result;
	}
	
	/**
    * @author 김대원
    * 알림 조회
    */
	@Override // db에서 알림 가져오기
	public List<Notification> findAllNotification(String memberId) {
	    List<Notification> notifications = notificationRepository.findAllNotification(memberId);
	    if (notifications == null) {
	        return Collections.emptyList();
	    }
	    return notifications;
	}
	
	private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }

    private String formatTimestampNow() {
        return formatTimestamp(new Timestamp(System.currentTimeMillis()));
    }
    
    @Override
    public int deleteNotification(int id) {
    	return notificationRepository.deleteNotification(id);
    }
}
