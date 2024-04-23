package com.commsignia.backend.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class NotificationEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "vehicleId")
	private Long vehicleId;

	@Column(name = "message")
	private String message;

	public NotificationEntity(Long vehicleId, String message) {
		this.vehicleId = vehicleId;
		this.message = message;
	}
}
