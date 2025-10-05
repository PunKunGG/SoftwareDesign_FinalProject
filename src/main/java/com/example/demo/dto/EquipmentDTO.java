package com.example.demo.dto;

public class EquipmentDTO {
	private Long id;
	private String name;
	private int quantity;
	private String status;

	public EquipmentDTO(Long id, String name, int quantity, String status) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getStatus() {
		return status;
	}
}
