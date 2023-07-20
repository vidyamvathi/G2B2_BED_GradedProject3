package com.greatleaning.tickettracker.service;

import com.greatleaning.tickettracker.entity.Ticket;
import com.greatleaning.tickettracker.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TicketService {

	private final TicketRepository ticketRepository;

	// @Autowired
	public TicketService(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	public Ticket getTicketById(Long id) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(id);
		return ticketOptional.orElse(null);
	}

	public void createTicket(Ticket ticket) {
		ticket.setCreatedOn(LocalDateTime.now());
		ticketRepository.save(ticket);
	}

	public void updateTicket(Ticket ticket) {
		ticketRepository.save(ticket);
	}

	public void deleteTicket(Long id) {
		ticketRepository.deleteById(id);
	}

}
