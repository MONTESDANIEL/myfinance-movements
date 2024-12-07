package com.myfinance.backend.movements.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myfinance.backend.movements.entities.ApiResponse;
import com.myfinance.backend.movements.entities.AppTag;
import com.myfinance.backend.movements.repositories.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public ResponseEntity<?> viewAllTags() {
        try {
            // Guardamos la etiqueta directamente
            List<AppTag> tags = StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            // Creando respuesta con el ID del movimiento guardado
            return createApiResponse(HttpStatus.CREATED, "Etiquetas consultadas con éxito.", tags);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar consultar las etiquetas.", null);
        }
    }

    public ResponseEntity<?> newTag(AppTag appTag) {
        try {
            // Guardamos la etiqueta directamente
            tagRepository.save(appTag);
            // Creando respuesta con el ID del movimiento guardado
            return createApiResponse(HttpStatus.CREATED, "Nueva etiqueta registrada con éxito.", null);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar registrar una etiqueta.", null);
        }
    }

    public ResponseEntity<?> updateTag(AppTag appTag) {
        try {
            // Guardamos la etiqueta directamente
            tagRepository.save(appTag);
            // Creando respuesta con el ID del movimiento guardado
            return createApiResponse(HttpStatus.OK, "Etiqueta actualizada con éxito.", null);

        } catch (Exception e) {

            // Manejo de excepciones en caso de error
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar actualizar la etiqueta.", null);

        }
    }

    public ResponseEntity<?> deleteTag(Long id) {

        try {

            if (tagRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "La etiqueta no fue encontrada.", null);
            }

            tagRepository.deleteById(id);
            return createApiResponse(HttpStatus.NO_CONTENT, "La etiquta fue eliminada con exito.", null);

        } catch (Exception e) {

            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al intentar eliminar la etiqueta.", null);

        }

    }

    public ResponseEntity<?> createApiResponse(HttpStatus status, String message, Object data) {
        ApiResponse<Object> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }

}
