package com.example.PhotoConnect.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {

    private Long id;
    private String name;
    private String location;
    private String eventType;
    private double rating;
}
