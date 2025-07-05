![image](https://github.com/user-attachments/assets/cb69ddb2-1a5a-4ff9-bfd4-87bd4569f9d7)

flowchart TB
  %% Верхний уровень: UI
  subgraph UI ["UI"]
    direction TB
    AdminUI["AdminUI<br/>(со всеми сервисами)"]
    ClientUI["ClientUI<br/>(со всеми сервисами)"]
  end

  %% Средний уровень: Core-сервисы
  subgraph Core ["Core-сервисы"]
    direction TB
    UserService["UserService"]
    CompetitionService["CompetitionService"]
    StatisticsService["StatisticsService"]
    ChatService["ChatService"]
    FeedBackService["FeedBackService"]
  end

  %% Нижний уровень: Engine
  subgraph Engine ["Engine-сервис"]
    direction TB
    CompetitionEngine["CompetitionEngine"]
  end

  %% Самый-низ: База данных
  subgraph DataBase ["DataBase"]
    direction TB
    DB["База Данных"]
  end

  %% Службы → База данных
  UserService       --> DB
  ChatService       --> DB
  FeedBackService   --> DB
  CompetitionService--> DB
  StatisticsService --> DB
  CompetitionEngine --> DB

  %% Взаимосвязи в Core и Engine
  UserService       --> CompetitionService
  UserService       --> ChatService
  UserService       --> FeedBackService
  CompetitionService--> CompetitionEngine
  CompetitionService--> StatisticsService
  CompetitionEngine --> StatisticsService

  %% UI связи с Core
  AdminUI -.-> UserService
  AdminUI -.-> CompetitionService
  AdminUI -.-> StatisticsService
  AdminUI -.-> ChatService
  AdminUI -.-> FeedBackService

  ClientUI -.-> UserService
  ClientUI -.-> CompetitionService
  ClientUI -.-> StatisticsService
  ClientUI -.-> ChatService
  ClientUI -.-> FeedBackService

  %% Стили
  classDef ui        fill:#e2ffe2,stroke:#333,stroke-width:1.5px
  classDef core      fill:#e3e8ff,stroke:#333,stroke-width:1px
  classDef engine    fill:#fff0cc,stroke:#a97d22,stroke-width:2px
  classDef database  fill:#fff0cc,stroke:#a97d22,stroke-width:2px

  class AdminUI,ClientUI ui
  class UserService,CompetitionService,StatisticsService,ChatService,FeedBackService core
  class CompetitionEngine engine
  class DB database
