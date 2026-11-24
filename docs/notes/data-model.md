``` Chốt quan hệ:
  Account 1:N Profile
  Profile 1:N Ticket
  Profile 1:N Tag
  Ticket N:N Tag

  Technique: spring-boot (java), nextjs
  Database: PostgreSQL
  Environment: Docker, CLI
```

### Constraints
- ACCOUNT: `UNIQUE (username)`
- TICKET: `UNIQUE (day, profile_id)`
- TAG: `UNIQUE (profile_id, name)`

```mermaid
erDiagram
    ACCOUNT {
        uuid id PK
        string username "NOT NULL"
        string password_hash "NOT NULL"
    }

    PROFILE {
        uuid id PK
        uuid account_id FK
        string first_name
        string last_name
    }

    TAG {
        uuid id PK
        uuid profile_id FK
        string name "NOT NULL"

        timestamptz created_at 
        timestamptz updated_at 
    }

    TICKET {
        uuid id PK
        uuid profile_id FK

        string eat "NOT NULL"
        string drink "NOT NULL"
        string short_description
        int feeling "NOT NULL"
        date day
        int version
        
        timestamptz created_at
        timestamptz updated_at
    }

    TAG_TICKET {
      uuid ticket_id PK, FK
      uuid tag_id PK, FK
    }

   ACCOUNT ||--o{ PROFILE : "belong to"
   PROFILE ||--o{ TICKET : "create"
   PROFILE ||--o{ TAG : "create"
   TICKET ||--o{ TAG_TICKET : ""
   TAG ||--o{ TAG_TICKET : ""
```

### Notes
- Done: đã tạm xong phần data-mode, thiết kế ERD (done data-model, design ERD)
- Learned: cách viết mermaid (how to create ERD in mermaid)
- Stuck: lẫn lộn giữa postgreSQL và java trong datatype, không biết cú pháp mermaid (mix datatype between postgreSQL and Java, don't know syntax mermaid, so this is a main reason speed a lot of time in day 0)
- Next: create backend + tạo docker + tạo V0 đầu tiên cho database (create BE spring-boot (/src), int docker, create V0 for db)

--- Fix notes:
- Done: Finished the data model and drew the ERD.
- Learned: How to write an ERD in Mermaid.
- Stuck: I mixed up PostgreSQL and Java data types, and I didn't know the Mermaid syntax. That's why Day 0 took so long.
- Next: Create the Spring Boot backend, set up Docker, and create the first database migration.