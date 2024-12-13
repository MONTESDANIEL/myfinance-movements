package com.myfinance.backend.movements.controllers;

import com.myfinance.backend.movements.entities.AppMovements;
import com.myfinance.backend.movements.entities.UpdateAppMovements;
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

    // Endpoind para ver todos los Movimientos (Admin)
    @GetMapping("/viewAllMovements")
    public ResponseEntity<?> viewAllMovements() {
        ResponseEntity<?> response = movementService.viewAllMovements();
        return response;
    }

    @GetMapping("/viewUserMovements")
    public ResponseEntity<?> viewUserMovements(@RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = movementService.viewUserMovements(authorizationToken);
        return response;
    }

    @PostMapping("/newMovement")
    public ResponseEntity<?> newMovement(
            @Valid @RequestBody AppMovements movement,
            @RequestHeader("Authorization") String authorizationToken) {
        logger.info("Received Authorization Token: " + authorizationToken);
        ResponseEntity<?> response = movementService.newMovement(movement, authorizationToken);
        return response;
    }

    @PutMapping("/updateMovement")
    public ResponseEntity<?> updateMovement(
            @Valid @RequestBody UpdateAppMovements movement,
            @RequestHeader("Authorization") String authorizationToken) {
        logger.info("Received Authorization Token: " + authorizationToken);
        ResponseEntity<?> response = movementService.updateMovement(movement, authorizationToken);
        return response;
    }

    @DeleteMapping("/deleteMovement/{id}")
    public ResponseEntity<?> deleteMovement(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = movementService.deleteMovement(id, authorizationToken);
        return response;
    }
}
