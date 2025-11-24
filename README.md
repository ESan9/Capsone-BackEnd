Capstone Project - Vetrina negozio di artigianato

**Corso Full Stack Developer - Epicode 2025**
**Autore:** Emanuele Sanna

## Descrizione del Progetto

Questo progetto rappresenta il lavoro conclusivo (Capstone) del percorso di studi Full Stack. L'obiettivo è stato sviluppare un sito vetrina per un negozio di artigianato, diviso in due applicativi distinti (Backend API e Frontend Client).

Il sistema permette la gestione completa di un catalogo prodotti, categorie e upload di immagini, con un'area riservata protetta per l'amministrazione.

## Stack Tecnologico

# Backend (Server-side)

L'architettura backend è costruita secondo il pattern RESTful.

- **Java 21** & **Spring Boot 3**: Framework principale.
- **Spring Data JPA / Hibernate**: Per l'interazione con il database e ORM.
- **Spring Security + JWT**: Gestione autenticazione e autorizzazione (Ruoli ADMIN/USER).
- **PostgreSQL**: Database relazionale(./path/to/screenshot1.png)
- **Cloudinary**: Servizio esterno per lo storage e l'ottimizzazione delle immagini.
- **Maven**: Gestione delle dipendenze.
- **Docker**: Containerizzazione per il deploy.

# Frontend (Client-side)

Interfaccia utente reattiva e performante.

- **React** (Vite): Libreria UI.
- **TypeScript**: Per la tipizzazione statica e robustezza del codice.
- **Tailwind CSS**: Framework CSS utility-first per lo styling.
- **React Router DOM**: Gestione della navigazione client-side.
- **Axios**: Client HTTP per le chiamate API.
- **Heroicons**: Icon set.

# Deployment & DevOps (IN PROGRESS)

Il progetto al momento non è online ma sarà accessibile pubblicamente grazie a una suite di servizi Cloud gratuiti:

- **Database:** Neon (PostgreSQL Serverless).
- **Backend Hosting:** Render (Container Docker).
- **Frontend Hosting:** Vercel.

---

# Funzionalità Principali

# Lato Pubblico (Store)

- Visualizzazione prodotti con paginazione.
- Filtro prodotti per categoria.
- Dettaglio singolo prodotto con galleria immagini.
- Design responsive per mobile e desktop.

# Pannello Amministratore (Dashboard)

- **Autenticazione Sicura:** Login tramite token JWT.
- **Gestione Categorie:** CRUD completo (Crea, Leggi, Aggiorna, Elimina) con upload immagine di copertina.
- **Gestione Prodotti:** CRUD completo con dettagli (prezzo, disponibilità, materiali).
- **Multi-Image Upload:** Caricamento multiplo di immagini per singolo prodotto.
- **Validazione Dati:** Gestione errori lato server con feedback visivo immediato per l'utente.

---

## 🚀 Installazione e Setup Locale

# Prerequisiti

- Java 17 o superiore.
- Node.js e npm.
- PostgreSQL installato localmente (o un'istanza cloud accessibile).

# 1. Configurazione Backend

1.  Clona il repository.

2.  Apri la cartella `backend`.

3.  Crea un file `.env` nella root del frontend dove conserverai le variabili d'ambiente:
    spring.datasource.url=jdbc:postgresql://localhost:5432/tuo_db
    spring.datasource.username=tuo_user
    spring.datasource.password=tua_password
    cloudinary.cloud_name=...
    cloudinary.api_key=...
    cloudinary.api_secret=...
    jwt.secret=...

4.  Questo servirà a popolare il file application properties:
    spring.config.import=file:env.properties
    spring.application.name=capstone
    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    server.port=3001
    spring.jpa.hibernate.ddl-auto=update
    cloudinary.name=${CLOUDINARY_NAME}
    cloudinary.key=${CLOUDINARY_KEY}
    cloudinary.secret=${CLOUDINARY_SECRET}
    jwt.secret=${JWT_SECRET}
    spring.datasource.driver=org.postgresql.Driver
    admin.email=${ADMIN_EMAIL}
    admin.password=${ADMIN_PASSWORD}
    spring.servlet.multipart.resolve-lazily=true

5.  Avvia l'applicazione:
    ```bash
    mvn spring-boot:run
    ```

### 2. Configurazione Frontend

1. https://github.com/ESan9/capstone-frontend.git
2. Clona il repository.
3. Apri la cartella `frontend` nel terminale.
4. Installa le dipendenze:
   ```bash
   npm install
   ```

````

5. Crea un file `.env` nella root del frontend:
   ```env
   VITE_API_URL=http://localhost:8080/api
   ```
6. Avvia il server di sviluppo:
   ```bash
   npm run dev
   ```

---

## Contatti

**Emanuele Sanna**

- [LinkedIn](https://www.linkedin.com/in/emanuelesanna/)
- [GitHub](https://github.com/ESan9)

---

_Progetto realizzato per il corso Epicode 2025._
````
