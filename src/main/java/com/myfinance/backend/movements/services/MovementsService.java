package com.myfinance.backend.movements.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import com.myfinance.backend.movements.controllers.MovementsController;
import com.myfinance.backend.movements.entities.ApiResponse;
import com.myfinance.backend.movements.entities.AppMovements;
import com.myfinance.backend.movements.repositories.MovementsRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class MovementsService {

    private final MovementsRepository movementsRepository;

    private static final Logger logger = LoggerFactory.getLogger(MovementsController.class);

    public ResponseEntity<?> viewAllMovements() {
        try {
            List<AppMovements> movements = StreamSupport.stream(movementsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            return createApiResponse(HttpStatus.OK, "Movimientos consultados con éxito.", movements);

        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar consultar los movimientos.", null);
        }
    }

    public ResponseEntity<?> newMovement(AppMovements newMovement, String token) {
        try {

            Long userId = getUserId(token);
            newMovement.setUserId(userId);
            AppMovements savedMovement = movementsRepository.save(newMovement);
            return createApiResponse(HttpStatus.CREATED, "Movimiento registrado con éxito.", savedMovement);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar el movimiento.", null);
        }
    }

    public ResponseEntity<?> updateMovement(AppMovements newMovement, String token) {
        try {

            if (movementsRepository.findById(newMovement.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El movimiento no fue encontrada.", null);
            }

            movementsRepository.save(newMovement);
            return createApiResponse(HttpStatus.OK, "El movimiento fue actualizado con éxito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar actualizar el movimiento.", null);
        }
    }

    public ResponseEntity<?> deleteMovement(Long id) {
        try {

            if (movementsRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El movimiento no fue encontrada.", null);
            }

            movementsRepository.deleteById(id);
            return createApiResponse(HttpStatus.NO_CONTENT, "El movimiento fue eliminado con éxito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar actualizar el movimiento.", null);
        }
    }

    private ResponseEntity<?> createApiResponse(HttpStatus status, String message, Object data) {
        ApiResponse<Object> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }

    private final RestTemplate restTemplate; // Cliente HTTP

    // Método privado para obtener el id del usuario usando el token
    public Long getUserId(String token) {
        try {
            // Define la URL del microservicio de usuarios
            String userServiceUrl = "http://192.168.1.5:8081/api/users/view";

            // Configura el encabezado con el token JWT
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            // Crea la solicitud HTTP con los encabezados
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Llama al microservicio de usuarios
            ResponseEntity<Map> response = restTemplate.exchange(
                    userServiceUrl, // URL del microservicio
                    HttpMethod.GET, // Método HTTP
                    entity, // Solicitud con encabezados
                    Map.class // Tipo de respuesta esperada como Map
            );

            // Verifica si la respuesta fue exitosa
            if (response.getStatusCode() == HttpStatus.OK) {
                // Obtén el campo 'data' de la respuesta
                Map<String, Object> userData = (Map<String, Object>) response.getBody().get("data");

                // Extrae el 'id' del mapa y lo devuelve como Long
                return Long.parseLong(userData.get("id").toString());
            } else {
                // Si la respuesta no fue exitosa, puedes manejarlo de otra forma
                return null;
            }

        } catch (Exception e) {
            // Manejo de errores
            return null;
        }
    }

}
