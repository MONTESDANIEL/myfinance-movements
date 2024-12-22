package com.myfinance.backend.movements.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import com.myfinance.backend.movements.entities.ApiResponse;
import com.myfinance.backend.movements.entities.AppTag;
import com.myfinance.backend.movements.entities.AppUser;
import com.myfinance.backend.movements.entities.ViewAppMovements;
import com.myfinance.backend.movements.repositories.MovementsRepository;
import com.myfinance.backend.movements.repositories.TagRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MovementsService {

    private final MovementsRepository movementsRepository;
    private final TagRepository tagRepository;
    private final RestTemplate restTemplate;

    // Ver todos los movimientos
    public ResponseEntity<?> viewAllMovements() {
        try {
            List<ViewAppMovements> movements = StreamSupport.stream(movementsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());

            return createApiResponse(HttpStatus.OK,
                    "Consulta de los movimientos exitosa.", movements);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar el movimiento.", null);
        }
    }

    // Ver los movimientos del usuario
    public ResponseEntity<?> viewUserMovements(String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            List<ViewAppMovements> userMovements = movementsRepository.findByUserId(userId);
            return createApiResponse(HttpStatus.OK,
                    "Consulta de los movimientos del usuario exitosa.", userMovements);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar el movimiento.", null);
        }
    }

    // Nuevo movimiento
    public ResponseEntity<?> newMovement(ViewAppMovements newMovement, String token) {
        try {
            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            newMovement.setUserId(userId);

            // Si el tag es null, no es obligatorio verificar el ID del tag
            if (newMovement.getTag() != null && newMovement.getTag().getId() == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST,
                        "El tagId es obligatorio si se proporciona una etiqueta.", null);
            }

            // Si se proporciona un tag, verificar que exista en la base de datos
            if (newMovement.getTag() != null) {
                Optional<AppTag> tagOpt = tagRepository.findById(newMovement.getTag().getId());

                if (tagOpt.isEmpty()) {
                    return createApiResponse(HttpStatus.CONFLICT, "La etiqueta no fue encontrada.", null);
                }

                AppTag tag = tagOpt.get();

                // Verificar si la etiqueta no es global y si no pertenece al usuario actual
                if (!tag.getIsGlobal() && !userId.equals(tag.getUserId())) {
                    return createApiResponse(HttpStatus.CONFLICT, "La etiqueta no puede ser asignada.", null);
                }

                newMovement.setTag(tag);
            }

            // Guardar el nuevo movimiento
            movementsRepository.save(newMovement);

            // Respuesta exitosa
            return createApiResponse(HttpStatus.CREATED, "Movimiento registrado con éxito.", null);

        } catch (Exception e) {
            // Manejo de errores
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar el movimiento: " + e.getMessage(),
                    null);
        }
    }

    // Actualizar movimiento
    public ResponseEntity<?> updateMovement(ViewAppMovements newMovement, String token) {
        try {
            Long userId = getUserId(token);

            // Verificar que el usuario existe
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Error al cargar el usuario.", null);
            }

            // Verificar que el movimiento exista en la base de datos
            if (movementsRepository.findById(newMovement.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El movimiento no fue encontrado.", null);
            }

            // Obtener el movimiento existente
            ViewAppMovements movement = movementsRepository.findById(newMovement.getId()).get();

            // Actualizar los campos del movimiento
            movement.setDate(newMovement.getDate());
            movement.setDescription(newMovement.getDescription());
            movement.setAmount(newMovement.getAmount());
            movement.setMovementType(newMovement.getMovementType());

            // Si el tag es null, no hacer nada, solo actualizar los campos proporcionados
            if (newMovement.getTag() != null) {
                // Si el tagId no es nulo, se verifica la existencia del tag
                if (newMovement.getTag().getId() != null) {
                    Optional<AppTag> tagOpt = tagRepository.findById(newMovement.getTag().getId());

                    if (tagOpt.isEmpty()) {
                        return createApiResponse(HttpStatus.CONFLICT, "La etiqueta no fue encontrada.", null);
                    }

                    AppTag tag = tagOpt.get();

                    // Verificar si la etiqueta es global o pertenece al usuario
                    if (!tag.getIsGlobal() && !userId.equals(tag.getUserId())) {
                        return createApiResponse(HttpStatus.CONFLICT, "La etiqueta no puede ser asignada.", null);
                    }

                    movement.setTag(tag);
                } else {
                    // Si el tagId es nulo, desasociamos el tag
                    movement.setTag(null);
                }
            }

            // Guardar el movimiento actualizado
            movementsRepository.save(movement);

            // Respuesta exitosa
            return createApiResponse(HttpStatus.OK, "El movimiento fue actualizado con éxito.", null);
        } catch (Exception e) {
            // Manejo de errores
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

            Optional<ViewAppMovements> movementOpt = movementsRepository.findById(id);

            if (movementOpt.isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El movimiento no fue encontrada.", null);
            }

            if (!Long.valueOf(userId).equals(movementsRepository.findById(id).get().getUserId())) {
                return createApiResponse(HttpStatus.UNAUTHORIZED, "Error al eliminar el movimiento.", null);
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
    private Long getUserId(String token) {
        try {

            String userServiceUrl = "http://192.168.1.2:8081/api/users/view";

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

    // Desvincular tag de los movimientos
    @Transactional
    public void deleteTagAndUnlinkMovements(Long tagId) {

        // Verifica que el tag exista antes de continuar
        AppTag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag no encontrado"));

        // Desvincula el tag de los movimientos
        movementsRepository.unlinkTagFromMovements(tagId);

        // Elimina el tag
        tagRepository.delete(tag);
    }

}