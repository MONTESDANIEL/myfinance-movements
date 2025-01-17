package com.myfinance.backend.movements.controllers;

import com.myfinance.backend.movements.entities.ViewAppMovements;
import com.myfinance.backend.movements.services.MovementsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementsController {

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

    @GetMapping("/viewGoalMovements/{goalId}")
    public ResponseEntity<?> viewGoalMovements(
            @RequestHeader("Authorization") String authorizationToken,
            @PathVariable Long goalId) {
        ResponseEntity<?> response = movementService.viewGoalMovements(authorizationToken, goalId);
        return response;
    }

    @PostMapping("/newMovement")
    public ResponseEntity<?> newMovement(
            @Valid @RequestBody ViewAppMovements movement,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = movementService.newMovement(movement, authorizationToken);
        return response;
    }

    @PutMapping("/updateMovement")
    public ResponseEntity<?> updateMovement(
            @Valid @RequestBody ViewAppMovements movement,
            @RequestHeader("Authorization") String authorizationToken) {
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
