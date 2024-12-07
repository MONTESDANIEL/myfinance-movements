package com.myfinance.backend.movements.controllers;

import com.myfinance.backend.movements.entities.AppMovements;
import com.myfinance.backend.movements.services.MovementsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementsController {

    private static final Logger logger = LoggerFactory.getLogger(MovementsController.class);

    @Autowired
    private final MovementsService movementService;

    @GetMapping("/viewAllMovements")
    public ResponseEntity<?> viewAllMovements() {
        ResponseEntity<?> response = movementService.viewAllMovements();
        return response;
    }

    @PostMapping("/newMovement")
    public ResponseEntity<?> newMovement(@Valid @RequestBody AppMovements movement) {
        ResponseEntity<?> response = movementService.newMovement(movement);
        return response;
    }

    @PutMapping("/updateMovement")
    public ResponseEntity<?> updateMovement(@Valid @RequestBody AppMovements movement) {
        ResponseEntity<?> response = movementService.updateMovement(movement);
        return response;
    }

    @DeleteMapping("/deleteMovement/{id}")
    public ResponseEntity<?> deleteMovement(@PathVariable Long id) {
        ResponseEntity<?> response = movementService.deleteMovement(id);
        return response;
    }

    @GetMapping("/viewUser")
    public ResponseEntity<?> getUser(String authorizationHeader) {
        logger.info("Token de autorizacion: " + authorizationHeader);
        // Llama al servicio para obtener los datos del usuario
        return movementService.getUserDetails(authorizationHeader);
    }

}
