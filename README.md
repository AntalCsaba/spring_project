# Books API (Laravel)

Egyszerű **könyvkezelő** alkalmazás Laravel-lel.

- Vendégként: **minden könyv listázható**, de **nem módosítható / nem törölhető**.
- Bejelentkezve: a felhasználó **csak a saját könyveit** hozhatja létre, módosíthatja és törölheti.
- Keresés:
  - **kijelentkezve**: az **összes könyv** között keres,
  - **bejelentkezve**: **csak a saját könyvei** között keres.
- UI: **Kijelentkezés** gomb + **felhasználó köszöntése** a jobb felső sarokban.

---

## Tartalomjegyzék

- [Fő funkciók](#fő-funkciók)
- [Technológiák](#technológiák)
- [Telepítés és futtatás](#telepítés-és-futtatás)
- [API – Auth](#api--auth)
- [API – Könyvek](#api--könyvek)
- [Validációk és hibaüzenetek](#validációk-és-hibaüzenetek)
- [Automata tesztek](#automata-tesztek)
- [Útvonalak (Route összefoglaló)](#útvonalak-route-összefoglaló)
- [UI viselkedés](#ui-viselkedés)

---

## Fő funkciók 

- **Regisztráció & Bejelentkezés** – mindkettő **Bearer tokent** ad vissza (a védett végpontokhoz kötelező).
- **Könyvek listázása** vendégként is elérhető (nincs módosítás/törlés).
- **Keresés** `q` query parammal; **„Keresés törlése”** funkció visszaállítja az összes listát.
- **Jogosultságok**:
  - vendég: listázás + globális keresés,
  - bejelentkezett: create/update/delete **csak saját** könyv, keresés **csak saját** könyvekben.
- **Status** mező **csak** a következő értékeket fogadja el: `planned`, `finished`, `reading`.

---

## Technológiák

- **PHP 8.x**
- **Laravel 12**
- **MySQL**
- Auth: **Bearer Token** (pl. Laravel Sanctum/Passport/JWT – implementációtól függően)

---

## Telepítés és futtatás

1. **Repo letöltése**
   ```bash
   git clone <your-repo-url>
   cd <repo-folder>
