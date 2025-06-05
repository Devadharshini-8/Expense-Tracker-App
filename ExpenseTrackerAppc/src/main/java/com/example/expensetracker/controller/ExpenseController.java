package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/")
    public String viewExpenses(Model model) {
        logger.info("Fetching all expenses");
        model.addAttribute("expenses", expenseRepository.findAll());
        model.addAttribute("newExpense", new Expense());
        return "index";
    }

    @PostMapping("/addExpense")
    public String addExpense(@Valid @ModelAttribute("newExpense") Expense expense, BindingResult result, Model model) {
        logger.info("Adding new expense: {}", expense);
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred: {}", result.getAllErrors());
            model.addAttribute("expenses", expenseRepository.findAll());
            return "index";
        }
        expenseRepository.save(expense);
        logger.info("Expense saved successfully: {}", expense);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        logger.info("Deleting expense with ID: {}", id);
        if (!expenseRepository.existsById(id)) {
            logger.warn("Expense with ID {} not found", id);
            return "redirect:/?error=ExpenseNotFound";
        }
        expenseRepository.deleteById(id);
        logger.info("Expense with ID {} deleted successfully", id);
        return "redirect:/";
    }
}