package org.example.routeapp.dto;

//Dto for id related responses
public class IdResponseDto {

    private Long id;
    private String message;

    public IdResponseDto(Long id) {
        this.id = id;
    }

    public IdResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IdResponseDto{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
