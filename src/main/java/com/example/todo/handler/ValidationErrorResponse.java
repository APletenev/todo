package com.example.todo.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Component
public class ValidationErrorResponse {
    private final List<Violation> violations;
}
