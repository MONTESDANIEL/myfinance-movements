package com.myfinance.backend.movements.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myfinance.backend.movements.entities.ApiResponse;
import com.myfinance.backend.movements.entities.AppTag;
import com.myfinance.backend.movements.entities.AppUser;
import com.myfinance.backend.movements.repositories.MovementsRepository;
import com.myfinance.backend.movements.repositories.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final MovementsRepository movementsRepository;
    private final TagRepository tagRepository;
    private final RestTemplate restTemplate;

    // Ver todas las etiquetas
    public ResponseEntity<?> viewAllTags() {
        try {

            List<AppTag> tags = StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());

            return createApiResponse(HttpStatus.CREATED, "Etiquetas consultadas con éxito.", tags);

        } catch (Exception e) {

            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar consultar las etiquetas.", null);

        }
    }

    // Ver todas las etiquetas
    public ResponseEntity<?> viewUserTags(String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            // Obtener etiquetas globales
            List<AppTag> globalTags = tagRepository.findByIsGlobal(true);

            // Obtener etiquetas del usuario
            List<AppTag> userTags = tagRepository.findByUserId(userId);

            // Combinar listas, colocando las etiquetas globales primero
            List<AppTag> combinedTags = new ArrayList<>();
            combinedTags.addAll(globalTags);
            combinedTags.addAll(userTags);

            return createApiResponse(HttpStatus.OK, "Etiquetas del usuario consultadas con éxito.", combinedTags);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar consultar las etiquetas del usuario.", null);
        }
    }

    // Agregar una nueva etiqueta del usuario
    public ResponseEntity<?> newTag(AppTag appTag, String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            appTag.setIsGlobal(false);
            appTag.setUserId(userId);

            tagRepository.save(appTag);

            return createApiResponse(HttpStatus.CREATED, "Nueva etiqueta registrada con éxito.", null);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar una etiqueta.", null);
        }
    }

    // Actualizar una etiqueta del usuario
    public ResponseEntity<?> updateTag(AppTag appTag, String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            appTag.setIsGlobal(false);
            appTag.setUserId(userId);

            tagRepository.save(appTag);

            return createApiResponse(HttpStatus.OK, "Etiqueta actualizada con éxito.", null);

        } catch (Exception e) {

            // Manejo de excepciones en caso de error
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar actualizar la etiqueta.", null);

        }
    }

    // Eliminar una etiqueta del usuario
    public ResponseEntity<?> deleteTag(Long id, String token) {

        try {

            Long userId = getUserId(token);

            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Error al cargar el usuario.", null);
            }

            if (tagRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "La etiqueta no fue encontrada.", null);
            }

            AppTag appTag = tagRepository.findById(id).get();

            if (!Long.valueOf(userId).equals(appTag.getUserId())) {
                return createApiResponse(HttpStatus.UNAUTHORIZED, "Error al eliminar el movimiento.", null);
            }

            movementsRepository.unlinkTagFromMovements(id);
            tagRepository.deleteById(id);

            return createApiResponse(HttpStatus.NO_CONTENT, "La etiquta fue eliminada con exito.", null);

        } catch (Exception e) {

            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar eliminar la etiqueta.", null);

        }

    }

    // Metodo para generar el formato de respuesta adecuado
    public ResponseEntity<?> createApiResponse(HttpStatus status, String message, Object data) {
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

}
