package com.greatleaning.tickettracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greatleaning.tickettracker.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findByTitleContainingIgnoreCaseOrShortDescriptionContainingIgnoreCase(String title,
			String shortDescription);

}
