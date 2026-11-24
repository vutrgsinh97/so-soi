package com.example.sosoi.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter 
public class Profile {
  
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private UUID id;

  private String firstName;
  private String lastName;

  /*
    * ManyToOne: Nhiểu Profile có thể tham chiếu đến môt Account
    * optional = false - Profile không thể tồn tại mà không có Account.
    * name = "account_id" = Tên cột trong bảng Profile sẽ là account_id.
    * referencedColumnName = "id": Cột account_id sẽ tham chiếu
   */ 
  @ManyToOne (optional = false)
  @JoinColumn (name = "account_id", referencedColumnName = "id")

  private Account account;

}
