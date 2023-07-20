package com.greatleaning.tickettracker.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greatleaning.tickettracker.entity.Ticket;
import com.greatleaning.tickettracker.repository.TicketRepository;
import com.greatleaning.tickettracker.service.TicketService;

@Controller
@RequestMapping("/admin/tickets")
public class TicketController {

	@Autowired
	private TicketRepository ticketRepository;
	private final TicketService ticketService;

	// @Autowired
	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping
	public String home(Model model) {
		List<Ticket> tickets = ticketRepository.findAll();
		List<String> createdOnDates = tickets.stream()
				.map(ticket -> ticket.getCreatedOn().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
				.collect(Collectors.toList());

		model.addAttribute("tickets", tickets);
		model.addAttribute("createdOnDates", createdOnDates);

		return "home";
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("ticket", new Ticket());
		return "create";
	}

	@PostMapping("/create")
	public String createTicket(@ModelAttribute("ticket") Ticket ticket) {

		ticket.setCreatedOn(LocalDateTime.now());
		ticketRepository.save(ticket);
		return "redirect:/admin/tickets";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID: " + id));
		model.addAttribute("ticket", ticket);
		return "edit";
	}

	@PostMapping("/edit/{id}")
	public String updateTicket(@PathVariable("id") Long id, @ModelAttribute("ticket") Ticket updatedTicket) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID: " + id));

		ticket.setTitle(updatedTicket.getTitle());
		ticket.setShortDescription(updatedTicket.getShortDescription());
		ticket.setContent(updatedTicket.getContent());
		ticketRepository.save(ticket);

		return "redirect:/admin/tickets";
	}

	@GetMapping("/delete/{id}")
	public String deleteTicket(@PathVariable("id") Long id) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID: " + id));

		ticketRepository.delete(ticket);
		return "redirect:/admin/tickets";
	}

	@GetMapping("/search")
	public String searchTickets(@RequestParam("keyword") String keyword, Model model) {
		List<Ticket> searchResults = ticketRepository
				.findByTitleContainingIgnoreCaseOrShortDescriptionContainingIgnoreCase(keyword, keyword);
		model.addAttribute("searchResults", searchResults);
		return "search";
	}

	@GetMapping("/view/{id}")
	public String viewTicket(@PathVariable("id") Long id, Model model) {

		Ticket ticket = ticketService.getTicketById(id);
		model.addAttribute("ticket", ticket);
		return "view";
	}
}
