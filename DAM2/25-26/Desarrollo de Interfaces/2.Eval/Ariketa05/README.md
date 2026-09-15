# Protegerrutas - Angular Guards Demoa

Proiektu honek Angular Guards erabiliz ibilbideak nola babestu erakusten du.

## Ezaugarriak

- ✅ **CanActivate Guard**: Orri pribatura sarbidea kontrolatzen du
- 🔐 **Autentifikazio zerbitzua**: Erabiltzailearen egoera kudeatu
- 🏠 **Orri publikoa (Home)**: Edonork ikusi dezake
- 🔒 **Orri pribatua**: Soilik erabiltzaile logeatuek ikusi dezakete

## Instalatu dependentziak

```bash
npm install
```

## Garapen zerbitzaria exekutatu

```bash
ng serve
```

Nabigatu `http://localhost:4200/` helbidera. Aplikazioa automatikoki berriz kargatuko da iturburu fitxategietako edozein aldatzen baduzu.

## Nola erabili

1. **Login egin gabe**: "Joan orri pribatura" sakatzean, /home-ra birbideratuko zaitu
2. **Login egin**: "Login" botoia sakatu erabiltzailea logeatzeko
3. **Orri pribatura sartu**: Orain "Joan orri pribatura" sakatu ahal izango duzu
4. **Logout egin**: "Logout" botoia sakatu deslogeatzeko

## Proiektuaren egitura

```
src/
├── app/
│   ├── guards/
│   │   └── auth-guard-guard.ts    # CanActivate Guard
│   ├── services/
│   │   └── auth.ts                # Autentifikazio zerbitzua
│   ├── home/
│   │   ├── home.ts                # Home osagaia
│   │   ├── home.html
│   │   └── home.css
│   ├── private/
│   │   ├── private.ts             # Private osagaia
│   │   ├── private.html
│   │   └── private.css
│   ├── app.ts                     # App osagai nagusia
│   ├── app.html
│   ├── app.css
│   ├── app.routes.ts              # Ibilbideen konfigurazioa
│   └── app.config.ts
├── index.html
├── main.ts
└── styles.css
```

## Ikasitakoa

- Nola sortu eta erabili **Guards** Angular-en
- **CanActivate** guard-ak nola funtzionatzen duen
- Autentifikazio zerbitzua nola inplementatu
- Ibilbideak nola babestu erabiltzaile logeatuentzat
