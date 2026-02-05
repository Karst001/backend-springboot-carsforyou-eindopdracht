# Garage WebAPI

## Inhoudsopgave
- [Inleiding](#inleiding)
- [Projectoverzicht](#projectoverzicht)
    - [1. Projectstructuur](#1-projectstructuur)
    - [2. Benodigde software](#2-benodigde-software)
    - [3. Gebruikte technieken](#3-gebruikte-technieken)
- [Installatie en configuratie](#installatie-en-configuratie)
    - [1. Project clonen in IntelliJ](#1-project-clonen-in-intelliJ)
    - [2. Database configureren](#2-database-configureren)
    - [3. Keycloak configureren](#3-keycloak-configureren)
    - [4. Applicatie starten](#4-applicatie-starten)
    - [5. Postman configureren](#5-postman-configureren)

- [API](#api)
  - [1. API-documentatie (Swagger)](#1-api-documentatie-swagger)
  - [2. Authenticatie en autorisatie](#2-authenticatie-en-autorisatie)
- [Testen](#testen)
  - [1. Geautomatiseerde tests](#1-geautomatiseerde-tests)
  - [2. Rollen en testgebruikers in KeyCloak](#2-rollen-en-testgebruikers)
  - [3. Testen met Postman](#3-testen-met-postman)
- [Afronding](#afronding)

---

## Inleiding
Deze handleiding beschrijft hoe de **Garage WebAPI** lokaal geïnstalleerd, geconfigureerd en gestart kan worden.  
De handleiding is bedoeld voor developers die het project willen draaien, testen of verder ontwikkelen.

De applicatie is een **Spring Boot REST API** met **PostgreSQL** als database en maakt gebruik van **JWT en Keycloak** voor authenticatie en autorisatie.

---

## Projectoverzicht

### 1. Projectstructuur
De applicatie is opgebouwd volgens een gelaagde architectuur:

```
infrastructure/
    ├─ keycloak/
    │   └─ Garage-realm.json
    └─ postman/
        └─ Garage API (OAuth2 using Keycloak).postman_collection.json
src/
    └── main/
    ├──── java/
    │      └── nl/carsforyou/garage/
    |         ├── config/
    │         ├── controllers/
    │         ├── dtos/
    │         ├── entities/
    │         ├── exceptionHandler/
    │         ├── helpers
    │         ├── mappers/
    │         ├── repositories/
    │         ├── services/
    │         ├── validation/
    └── resources/
           └── application.properties 
           └── data.sql
    └── test/
    ├──── java/
    │      └── nl/carsforyou/garage/
    |         ├── controllers/
    |         ├── services/
    └── resources/
           └── application.test.properties
```

---

### 2. Benodigde software
Zorg dat de volgende software is geïnstalleerd op je systeem:

| Software            | Versie           |
|---------------------|------------------|
| Java JDK            | 17 of hoger      |
| IntelliJ IDEA       | 2023.x of hoger  |
| PostgreSQL          | 15.x of hoger    |
| pgAdmin | 4                |
| Maven               | 3.9.x            |
| Git                 | Laatste versie   |
| Browser             | Chrome / Firefox |

---

### 3. Gebruikte technieken
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (JSON Web Tokens)
- Keycloak
- PostgreSQL Database
- Swagger / OpenAPI
- JUnit 5 voor geautomatiseerde testen

---

## Installatie en configuratie

### 1. Project clonen in IntelliJ
```bash
git clone https://github.com/Karst001/backend-springboot-carsforyou-eindopdracht
cd garage
```

### 2. Database configureren

Voordat deze Web-API kan samenwerken met de database moet de database eerst aangemaakt worden.

```sql
CREATE DATABASE carsforyou;

-- De postgres-gebruiker bestaat standaard in PostgreSQL
ALTER USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE carsforyou TO postgres;
```

PS: Voor deze eindexamenopdracht draait de applicatie lokaal en gebruikt zij de standaard PostgreSQL superuser om configuratiecomplexiteit te beperken. 
In productie zou een dedicated database user worden gebruikt, het gebruik van het Superuser account in productie is niet aan te raden.

Een Superuser kan werkelijk ieder commando uitvoeren wat gevaarlijk kan zijn, bijvoorbeeld verwijderen van entiteiten of al dan niet uitvoeren van stored functions.  Bij een dedicated user voor een database kun je deze rechten ontnemen. 

Aangezien deze Web-API ook gebruik maakt van stored functions zouden er, gebruik makende van een dedicated user, verschillende configuratie instellingen moeten worden uitgevoerd m.b.t. rechten, voor dit project is de standaard `postgres` gebruiker dus de meest eenvoudige oplossing.

De applicatie maakt bij het opstarten automatisch de tabellen aan middels de instelling 'spring.jpa.hibernate.ddl-auto=create' en initialiseert testdata.  In productie zal deze instelling worden gewijzigd in 'update' omdat anders live-data overschreven wordt.  Tevens is er gebruik gemaakt van een Stored Function, zie `../helpers/DatabaseFunctionInitializer`, deze wordt gebruikt voor het PDF rapport wat kan worden gedownload.


### 3. Keycloak configureren

De applicatie gebruikt **Keycloak** voor authenticatie en autorisatie.  
De volledige Keycloak-configuratie (realm, client, rollen en testgebruikers)
is geëxporteerd en opgenomen in het project.

#### Keycloak starten
Start Keycloak lokaal in development mode vanuit de bin folder waar je Keycloak geïnstalleerd hebt:

```bash
.\bin\kc.bat start-dev --http-port 9090
```

**Keycloak configuratie bestand importeren:**

In de map `infrastructure/keycloak/` van dit project bevindt zich een **realm export** van Keycloak.

Dit exportbestand bevat:
- Realm: `Garage`
- Clients:
    - `Garage`
    - `Garage-Swagger`
- Rollen
- Testgebruikers

**Importeer deze configuratie in Keycloak via de admin console;**

Ga met een web browser naar: http://localhost:9090

- Log in op de Keycloak Admin Console en maak je gebruik van je eigen lokale admin login credentials

- Kies Create realm

- Selecteer Import van het `Garage-realm.json` en upload het meegeleverde exportbestand

- Bevestig de import


**Ingestelde clients**

De map `infrastructure/keycloak/` bevat een volledige realm-export van Keycloak, inclusief beide clients (`garage` en `garage-swagger`).

**Client: garage**

- Type: Confidential 

- Wordt gebruikt door de applicatie en voor API-tests via Postman

- De bijbehorende Postman-collection is opgenomen in het project

- Authenticatie verloopt via OAuth2 met access tokens

**Client: garage-swagger**

- Type: Public

- Wordt gebruikt voor het testen van de API via Swagger UI

- Vereenvoudigde configuratie zodat tijdens handmatig testen niet steeds opnieuw hoeft te worden ingelogd of tokens vernieuwd



**Rollen**
- Vooraf ingestelde testgebruikers met bijbehorende rollen:

  - ADMIN

  - USER

> PS: In een productieomgeving zou Keycloak doorgaans als aparte service worden uitgerold
> (bijv. via Docker of Kubernetes), maar dit valt buiten de scope van deze opdracht.

Testgebruikers zijn vooraf aangemaakt en hebben de juiste rollen toegewezen,  [zie de sectie Rollen en testgebruikers](#2-rollen-en-testgebruikers) voor login gegevens.

Na het importeren van de Keycloak-configuratie kan de applicatie direct worden gebruikt zonder aanvullende handmatige configuratie.


## 4. Applicatie starten
De applicatie vereist Java 17 of hoger.
```
mvn spring-boot:run
```
Afhankelijk van de lokale configuratie met Maven Wrapper gebruik dan dit commando:
```
.\mvnw spring-boot:run
```
of maak gebruik van de IntelliJ UI om de applicatie te starten, of rechter-muis klik op GarageApplication > Run


## 5. Postman configureren
Start Postman op je lokale systeem

- Klik op Import

- Selecteer bestaand `Garage API (OAuth2 using Keycloak).postman_collection.json`

- Binnen Postman in het linker paneel zal nu `Garage API (OAuth2 using Keycloak` zichtbaar zijn
- Alle controllers en endpoints kunnen vanuit hier getest worden.
- Klik  [hier](#3-testen-met-postman) voor de test procedure in Postman

## API
### 1. API-documentatie (Swagger)
Zodra de applicatie is opgestart kun de gebruiker testen met de Swagger UI en/of de entity schema documentatie inzien.
```
http://localhost:8080/swagger-ui.html
```
Via Swagger kunnen alle endpoints getest worden.
Om als gebruiker te authenticeren;
- Klik één maal op `Authorize`
- Een popup wordt zichtbaar
- Klik nogmaals op `Authorize`
- Vul de gebruikernaam en wachtwoord
- Klik op  `Sign In`
- Klik daarna op `Close`

Vanaf dit moment kunnen alle controllers getest worden, het token blijft 2 uur geldig, In Postman moet voor elk endpoint eerst een access token worden opgehaald. 

### 2. Authenticatie en autorisatie

- Authenticatie wordt afgehandeld door **Keycloak**
- De API valideert **JWT access tokens**
- Rollen worden uit het token gehaald en gemapt naar Spring Security authorities
- Autorisatie is centraal geregeld in `SecurityConfig`
- Controllers bevatten geen beveiligingslogica
- Endpoint-toegang is gebaseerd op rollen (USER / ADMIN)

## Testen
## 1. Geautomatiseerde tests
Tests kunnen uitgevoerd worden met:
```
mvn test
```
Afhankelijk van de lokale configuratie met Maven Wrapper gebruik dan dit commando:
```
.\mvnw test
```
of maak gebruik van de IntelliJ UI om de applicatie te starten, of rechter-muis klik op Java in folder test > More Run/Debug > Run tests with Coverage

De applicatie bevat unit tests, integratietests en security tests.

## 2. Rollen en testgebruikers

| Omschrijving                    | Rol | Login              | Wachtwoord |
|------------------------|-----------------|--------------------|------------|
| Gebruiker                   | USER            | garage_user        | user       |
| Administrator| ADMIN           | garage_admin | admin      |

## 3. Testen met Postman

- Open bijvoorbeeld `Appointments` binnen Postman
- Klik op `getAllAppointments`
- Klik op `Send`
- Er zal nu een 401 'not authorized' verschijnen en dit klopt, immers je bent nog niet ingelogd
- Klik op tab `Authorization` en scroll naar beneden
- Klik op `Get New Access Token`
- Login met gebruiker naam en wachtwoord, zie vorige paragraaf
- Daarna kom je terug in Postman
- Klik op `Proceed`
- In scherm `Manage Access Tokens` klik op `Use Token`
- Nu is de authenticatie volbracht
- Klik nu opnieuw op `Send` 
- Je ontvangt nu een `status 200 OK` en de data 
- Dit process van authorizatie geldt voor alle endpoints in iedere controller

## Afronding
Na het volgen van deze handleiding is de Garage WebAPI lokaal operationeel en klaar voor verdere ontwikkeling en testen.