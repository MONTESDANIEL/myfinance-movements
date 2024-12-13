package com.myfinance.backend.movements.services;

import org.springframework.core.ParameterizedTypeReference;
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
import com.myfinance.backend.movements.entities.AppUser;
import com.myfinance.backend.movements.entities.UpdateAppMovements;
import com.myfinance.backend.movements.repositories.MovementsRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class MovementsService {

    private final MovementsRepository movementsRepository;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MovementsController.class);

    // Ver todos los movimientos
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

    // Ver los movimientos del usuario
    public ResponseEntity<?> viewUserMovements(String token) {
        try {

            Long userId = getUserId(token);

            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Error al cargar el usuario.", null);
            }

            List<AppMovements> userMovements = movementsRepository.findByUserId(userId);
            return createApiResponse(HttpStatus.OK, "Movimientos del usuario consultados con éxito.", userMovements);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar consultar los movimientos.", null);
        }
    }

    // Nuevo movimiento
    public ResponseEntity<?> newMovement(AppMovements newMovement, String token) {
        try {

            Long userId = getUserId(token);

            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Error al cargar el usuario.", null);
            }

            newMovement.setUserId(userId);
            AppMovements savedMovement = movementsRepository.save(newMovement);
            return createApiResponse(HttpStatus.CREATED, "Movimiento registrado con éxito.", savedMovement);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar el movimiento.", null);
        }
    }

    // Actualizar movimiento
    public ResponseEntity<?> updateMovement(UpdateAppMovements newMovement, String token) {
        try {

            Long userId = getUserId(token);

            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Error al cargar el usuario.", null);
            }

            if (movementsRepository.findById(newMovement.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El movimiento no fue encontrada.", null);
            }

            AppMovements movement = movementsRepository.findById(newMovement.getId()).get();

            movement.setDate(newMovement.getDate());
            movement.setDescription(newMovement.getDescription());
            movement.setAmount(newMovement.getAmount());
            movement.setMovementType(newMovement.getMovementType());

            logger.info("Movimiento antes de registrarse: " + movement);

            movementsRepository.save(movement);
            return createApiResponse(HttpStatus.OK, "El movimiento fue actualizado con éxito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar actualizar el movimiento.", null);
        }
    }

    // Eliminar movimiento
    public ResponseEntity<?> deleteMovement(Long id, String token) {
        try {

            Long userId = getUserId(token);

            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Error al cargar el usuario.", null);
            }

            Optional<AppMovements> movementOpt = movementsRepository.findById(id);

            if (movementOpt.isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El movimiento no fue encontrada.", null);
            }

            if (!Long.valueOf(userId).equals(movementsRepository.findById(id).get().getUserId())) {
                return createApiResponse(HttpStatus.CONFLICT, "Error al eliminar el movimiento.", null);
            }

            movementsRepository.deleteById(id);
            return createApiResponse(HttpStatus.NO_CONTENT, "El movimiento fue eliminado con éxito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar actualizar el movimiento.", null);
        }
    }

    // Metodo para generar el formato de respuesta adecuado
    private ResponseEntity<?> createApiResponse(HttpStatus status, String message, Object data) {
        ApiResponse<Object> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }

    // Metodo para solicitar el id del usuario correspondiente segun el id
    public Long getUserId(String token) {
        try {

            String userServiceUrl = "http://192.168.1.5:8081/api/users/view";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<AppUser>> response = restTemplate.exchange(
                    userServiceUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<AppUser>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ApiResponse<AppUser> apiResponse = response.getBody();

                if (apiResponse != null && apiResponse.getData() != null) {
                    AppUser user = apiResponse.getData();
                    return user.getId();
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

}
