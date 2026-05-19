package ra.edu.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ra.edu.dto.ValidationError;
import ra.edu.dto.response.ApiResponse;
import ra.edu.entity.InternshipAssignmentsStatus;
import ra.edu.entity.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationError(
                        err.getField(),
                        err.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Dữ liệu không hợp lệ",
                        errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleJsonParse(
            HttpMessageNotReadableException ex) {

        Throwable throwable = ex;

        while (throwable != null) {

            if (throwable instanceof tools.jackson.databind.exc.InvalidFormatException formatEx) {

                String fieldName = "data";

                String message = formatEx.getMessage();

                Matcher matcher = Pattern
                        .compile("\\[\"(.*?)\"]")
                        .matcher(message);

                if (matcher.find()) {
                    fieldName = matcher.group(1);
                }

                ValidationError error;

                Class<?> targetType = formatEx.getTargetType();

                if (targetType == LocalDate.class) {

                    error = new ValidationError(
                            fieldName,
                            "Ngày không đúng định dạng dd/MM/yyyy");

                } else if (targetType == LocalDateTime.class) {

                    error = new ValidationError(
                            fieldName,
                            "Ngày giờ không đúng định dạng dd/MM/yyyy HH:mm");

                } else if (targetType == Integer.class
                        || targetType == Long.class
                        || targetType == int.class
                        || targetType == long.class) {

                    error = new ValidationError(
                            fieldName,
                            "Phải là số nguyên");

                } else if (targetType == Double.class
                        || targetType == Float.class
                        || targetType == double.class
                        || targetType == float.class
                        || targetType == BigDecimal.class) {

                    error = new ValidationError(
                            fieldName,
                            "Phải là số");

                } else if (targetType == UserRole.class) {

                    error = new ValidationError(
                            fieldName,
                            "Role không hợp lệ. Giá trị hợp lệ: "
                                    + Arrays.stream(UserRole.values())
                                    .map(Enum::name)
                                    .collect(Collectors.joining(", ")));

                } else if (targetType == InternshipAssignmentsStatus.class) {

                    error = new ValidationError(
                            fieldName,
                            "Status không hợp lệ. Giá trị hợp lệ: "
                                    + Arrays.stream(
                                            InternshipAssignmentsStatus.values()
                                    )
                                    .map(Enum::name)
                                    .collect(Collectors.joining(", ")));

                } else {

                    error = new ValidationError(
                            fieldName,
                            "Dữ liệu không đúng định dạng");
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(
                                "Dữ liệu không hợp lệ",
                                List.of(error)));
            }

            throwable = throwable.getCause();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Dữ liệu không hợp lệ",
                        List.of(
                                new ValidationError(
                                        "data",
                                        "Dữ liệu không hợp lệ"))));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        ValidationError error;

        Class<?> type = ex.getRequiredType();

        if (type == LocalDate.class) {

            error = new ValidationError(
                    ex.getName(),
                    "Ngày không đúng định dạng dd/MM/yyyy");

        } else if (type == LocalDateTime.class) {

            error = new ValidationError(
                    ex.getName(),
                    "Ngày giờ không đúng định dạng dd/MM/yyyy HH:mm");

        } else if (type == Integer.class
                || type == Long.class
                || type == int.class
                || type == long.class) {

            error = new ValidationError(
                    ex.getName(),
                    "Phải là số nguyên");

        } else if (type == Double.class
                || type == Float.class
                || type == double.class
                || type == float.class
                || type == BigDecimal.class) {

            error = new ValidationError(
                    ex.getName(),
                    "Phải là số");

        } else if (type == UserRole.class) {

            error = new ValidationError(
                    ex.getName(),
                    "Role không hợp lệ. Giá trị hợp lệ: "
                            + Arrays.stream(UserRole.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));

        } else if (type == InternshipAssignmentsStatus.class) {

            error = new ValidationError(
                    ex.getName(),
                    "Status không hợp lệ. Giá trị hợp lệ: "
                            + Arrays.stream(
                                    InternshipAssignmentsStatus.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));

        } else {

            error = new ValidationError(
                    ex.getName(),
                    "Dữ liệu không hợp lệ");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Dữ liệu không hợp lệ",
                        List.of(error)));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(
            EntityNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<?>> handleConflict(
            IllegalStateException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabled(
            DisabledException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrity(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "Không thể thực hiện thao tác vì dữ liệu đang được liên kết hoặc bị trùng",
                        null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(
            RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        null));
    }
}