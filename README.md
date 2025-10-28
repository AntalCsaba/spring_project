# Books API (Laravel)

Egyszerű **könyvkezelő** alkalmazás Laravel-lel.

- Vendégként: **minden könyv listázható**, de **nem módosítható / nem törölhető**.
- Bejelentkezve: a felhasználó **csak a saját könyveit** hozhatja létre, módosíthatja és törölheti.
- Keresés:
  - **kijelentkezve**: az **összes könyv** között keres,
  - **bejelentkezve**: **csak a saját könyvei** között keres.
- UI: **Kijelentkezés** gomb + **felhasználó köszöntése** a jobb felső sarokban.
- Bootstrap használata
- REST API Endpointok
- Egyszerű automata tesztek

---

## Tartalomjegyzék

- [Fő funkciók](#fő-funkciók)
- [Technológiák](#technológiák)
- [Telepítés és futtatás](#telepítés-és-futtatás)
- [API – Auth](#api--auth)
- [API – Könyvek](#api--könyvek)
- [Automata tesztek](#automata-tesztek)
- [Útvonalak (Route összefoglaló)](#útvonalak-route-összefoglaló)

---

## Fő funkciók

- **Regisztráció & Bejelentkezés** – mindkettő **Bearer tokent** ad vissza (a védett végpontokhoz kötelező).
- **Könyvek listázása** vendégként is elérhető (nincs módosítás/törlés).
- **Keresés**
- **„Keresés törlése”** funkció visszaállítja az összes listát.
- **Jogosultságok**:
  - vendég: listázás + globális keresés,
  - bejelentkezett: create/update/delete **csak saját** könyv, keresés **csak saját** könyvekben.
- **Status** mező **csak** a következő értékeket fogadja el: `planned`, `finished`, `reading`.

---

## Technológiák

- **PHP 8.x**
- **Laravel 12**
- **MySQL**
- Auth: **Bearer Token** (Laravel Sanctum)

---

## Telepítés és futtatás

1) **Repo letöltése és megnyitás VS Code-ban**
```
git clone <your-repo-url>
cd <repo-folder>
code .
```

2) **Adatbázis létrehozása MySQL-ben**

   Hozz létre egy adatbázist: booksdb
   
3) **`.env` beállítása**

   Az `.env.example` fájlt nevezd át `.env`-re és utána egészitsd ki a saját adataiddal.
   
```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=bookdb
DB_USERNAME=<felhasznalonev>
DB_PASSWORD=<jelszo>
```
4) **Függőségek telepítése és kulcs generálása**
```
composer install
php artisan key:generate
```

5) **Migrációk futtatása**
```
php artisan migrate
```

6) **Fejlesztői szerver indítása**
```
php artisan serve
```
```
Alapértelmezett: http://localhost:8000
```

## API – Auth

A regisztráció és bejelentkezés **Bearer** tokent ad vissza.

A védett (könyv létrehozás / módosítás / törlés) végpontoknál kötelező a fejléc:

**Authorization: Bearer <token>**


**```POST /api/register```** – Új felhasználó regisztrálása + token generálás
```
Request (JSON):

{
  "name": "felhasznalonev",
  "email": "email@email.com",
  "password": "jelszo"
}
```


**Response (példa):**
```
{
  "user": { "id": 1, "name": "felhasznalonev", "email": "email@email.com" },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}
```

A visszaadott tokent mentsd el, ezzel lehet könyvet létrehozni az adott felhasználónak.

**```POST /api/login```** – Bejelentkezés + token generálás
```
Request (JSON):

{ "name": "felhasznalonev", "password": "jelszo" }
```

Hibás adatok esetén (401):
```
{ "message": "Unauthorized." }
```

## API – Könyvek

**```GET /api/books```** - Az összes könyv listázása.


**```POST /api/books```** (Auth szükséges) - Új könyv létrehozása a bejelentkezett felhasználónak.

Headers:
```
Authorization: Bearer <token>
Content-Type: application/json
```

Body (JSON - példa):
```
{ "title": "Konyv 1", "author": "Szerzo 1", "status": "planned" }
```

status csak ezek lehetnek: planned | finished | reading. Egyéb érték esetén hibät dob.


**```PUT /api/books/{book}```** (Auth szükséges)

Meglévő könyv módosítása csak saját könyv esetén. Részleges frissítés támogatott.
```
Body (JSON – példa):

{ "title": "Uj cím" }
```

vagy

```{ "title": "Konyv 1", "author": "Szerzo 1", "status": "reading" }```


**```DELETE /api/books/{book}```** (Auth szükséges)

Könyv törlése csak saját könyv esetén. A `{book}` helyett a k0nyv ID-jét kell megadni.

## Automata tesztek

**4 egyszerű teszt:**
  - összes könyv listázása
  - saját bejelentkezett felhasznaló könyveinek a listázása
  - saját bejelentkezett felhasznaló könyveinek a módosítása
  - más felhasználó könyveinek törlésének elutasítása

## Útvonalak (Route összefoglaló)

**Web:**

```GET /``` → ```BookController@searchBook```

Vendég: minden könyv listázása, keresés globálisan.

Bejelentkezve: saját könyvek listázása/keresése.

```POST /create-book``` → ```BookController@createBook``` (auth szükséges a kontrollerben)

```GET /edit-book/{book}``` → ```BookController@editBook``` (auth + tulajdonjog ellenőrzés)

```PUT /edit-book/{book}``` → ```BookController@updateBook``` (auth + tulajdonjog)

```DELETE /delete-book/{book}``` → ```BookController@deleteBook``` (auth + tulajdonjog)

**API (JSON, Sanctum):**

**Auth:**

```POST /api/register``` → regisztráció + Bearer token

```POST /api/login``` → bejelentkezés név+jelszóval + Bearer token

**Books:**

```GET /api/books``` → publikus: összes könyv listázása (nincs auth)

(auth:sanctum) ```POST /api/books``` → új könyv létrehozása saját usernek

(auth:sanctum) ```PUT /api/books/{book}``` → saját könyv módosítása

(auth:sanctum) ```DELETE /api/books/{book}``` → saját könyv törlése

Védett végpontokhoz: Authorization: Bearer <token>, és Accept: application/json.

