# 📝 Markdown Note-taking App with Grammar Check

A RESTful API built with Java that allows users to upload Markdown notes, perform automated grammar checks, persist data to a cloud database, and render notes as clean HTML.

## 🌟 Features

- **Markdown Rendering:** Converts raw Markdown text into structured HTML using the CommonMark library.
- **Grammar Intelligence:** Integrated with the **LanguageTool API** to detect linguistic errors before saving notes.
- **Cloud Persistence:** Uses **Supabase (PostgreSQL)** as a backend-as-a-service for secure and scalable data storage.
- **RESTful Endpoints:**
  - `POST /api/check-grammar`: Analyzes text for grammar mistakes.
  - `POST /api/save`: Saves markdown content to the cloud.
  - `GET /api/notes`: Lists all saved notes from the database.
  - `GET /api/view-note?id={id}`: Fetches and renders a specific note as a full HTML page.

## 🛠 Tech Stack

- **Language:** Java 17
- **Database:** Supabase (PostgreSQL)
- **Libraries:**
  - [CommonMark](https://github.com/commonmark/commonmark-java) (Markdown parsing)
  - [Gson](https://github.com/google/gson) (JSON processing)
- **External API:** LanguageTool API
- **Build Tool:** Maven

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- A Supabase account and a `notes` table with columns: `id`, `title`, `content`.

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/isilulgerr/markdown-note-app.git
   cd markdown-note-app
   ```

2. **Configure Supabase:**
   Update the `SUPABASE_URL` and `SUPABASE_KEY` in `SupabaseService.java` with your project credentials.

3. **Build and Run:**
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="com.noteapp.MainApp"
   ```

## 📊 API Usage Examples

### 1. Render Markdown
**Endpoint:** `POST /api/render`

**Body:** `# Hello World`

**Response:** `<h1>Hello World</h1>`

### 2. Check Grammar
**Endpoint:** `POST /api/check-grammar`

**Body:** `This are a test.`

**Response:** JSON object containing matches and suggested corrections from LanguageTool.

### 3. Save to Cloud
**Endpoint:** `POST /api/save`

**Body:** `Your markdown content.`
