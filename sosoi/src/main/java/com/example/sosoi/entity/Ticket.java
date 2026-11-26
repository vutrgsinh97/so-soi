package com.example.sosoi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table (
  uniqueConstraints = {
    @UniqueConstraint(
      name = "unique_profile_day",
      columnNames = {"profile_id", "day"}
    )
  }
)
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  private String eat;

  @NotNull
  private String drink;

  private String shortDescription;

  @Min(0)
  @Max(5)
  @NotNull
  private Short feeling;

  @Column(nullable = false)
  private LocalDate day;

  @Version 
  private Integer version;

  @CreationTimestamp 
  private LocalDateTime createdAt;

  @UpdateTimestamp 
  private LocalDateTime updatedAt;

  @ManyToOne(optional = false)
  @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = false)
  private Profile profile;

}
