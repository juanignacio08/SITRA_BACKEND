package com.sitra.sitra.entity.turnos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ordenatencion", schema = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenAtencionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
