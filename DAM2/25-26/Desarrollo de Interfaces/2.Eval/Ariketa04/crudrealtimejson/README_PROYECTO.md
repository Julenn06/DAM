# CRUD Firebase Realtime Database - Angular

Proiektu hau CRUD aplikazio bat da (Create, Read, Update, Delete) Firebase Realtime Database erabiltzen duena, MAUI Realtime Database eskuliburua jarraituz.

## Proiektuaren Egitura

```
src/
├── app/
│   ├── components/
│   │   └── home/              # CRUD duen osagai nagusia
│   ├── models/
│   │   └── usuario.ts         # Erabiltzaile datu-eredua
│   ├── services/
│   │   └── firebase.service.ts # Firebase zerbitzua
│   ├── app.config.ts          # Aplikazioaren konfigurazioa
│   ├── app.routes.ts          # Aplikazioaren bideak
│   ├── app.ts                 # Erro osagaia
│   └── app.html               # Erro txantiloia
```

## Firebase Konfigurazioa

Proiektua exekutatu aurretik:

1. Sortu Firebase proiektu bat: https://console.firebase.google.com
2. Sortu Realtime Database datu-base bat
3. Konfiguratu segurtasun arauak proba moduan
4. Inportatu JSON adibide fitxategia (hasierako datuak nahi badituzu)
5. Eguneratu URLa `src/app/services/firebase.service.ts` fitxategian:

```typescript
private baseUrl = 'https://ZURE-PROIEKTUA.firebaseio.com/';
```

## JSON Adibide Fitxategia

```json
{
  "usuarios": {
    "u1": {
      "nombre": "Juan",
      "email": "juan@mail.com"
    },
    "u2": {
      "nombre": "María",
      "email": "maria@mail.com"
    }
  }
}
```

## Instalazioa

```bash
npm install
```

## Garapena

```bash
ng serve
```

Nabigatu `http://localhost:4200/` helbidera

## Funtzionalitateak

- **Erabiltzailea Sortu**: Sartu izena eta emaila, eta klikatu "Gehitu"
- **Erabiltzaileak Zerrendatu**: Ikusi datu-baseko erabiltzaile guztiak
- **Erabiltzailea Eguneratu**: Aldatu eremuak eta klikatu "Gorde"
- **Erabiltzailea Ezabatu**: Klikatu "Ezabatu" botoia

## Erabilitako Teknologiak

- Angular 19
- Firebase Realtime Database
- HttpClient HTTP eskaeretarako
- FormsModule formularioetarako
- Standalone Components

## Datu-Eredua

```typescript
interface Usuario {
  id: string;      // Firebase IDa (nodoaren gakoa)
  nombre: string;
  email: string;
}
```

## Ohar Garrantzitsuak

- Erabiltzailearen IDa Firebaseko **nodoaren gakoa** da, ez barne-eremu bat
- Eskaerak Firebase REST APIaren bidez egiten dira `.json` URLaren amaieran erabiliz
- Zerbitzuak RxJS Observables erabiltzen ditu erantzun asinkronoak kudeatzeko

## Oinarrituta

Proiektu hau "13-Maui realtime database-Manual.md" eskuliburuan oinarritzen da, Firebase Realtime Database-rekin CRUD bat nola inplementatu azaltzen duena.
