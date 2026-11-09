# ElorGym — Webgunea

Angular 22 bidez egindako webgunea. ElorGym markaren workout/saio ikuslea: saioak bilakuten ditu, mailaren arabera iragazten ditu, eta hautatutako saioaren ariketak erakusten ditu.

Aplikazioa **orrialde bakarreko** da: ez dago routerrik, ez dago autentikaziorik eta ez du zerbitzari baten aldeko deiturarik. Datuak JSON fitxategi estatico bat irakurtzen ditu.

## Aurretiazkoak

| Tresna      | Bertakoa                                                       |
| ----------- | -------------------------------------------------------------- |
| Node.js     | `^22.22.3`, `^24.15.0` edo `>=26.0.0` (Angularrek eskatutakoa) |
| npm         | `package.json`-ek `npm@12.2.0` adierazten du                   |
| Angular CLI | 22 (`@angular/cli`), instalatzeko `npm install`                |

## Exekutatzea

### Garapen zerbitzaria

Beharrezko dependentziak instalatu:

```bash
npm install
```

Ondoren zerbitzaria piztu:

```bash
ng serve -o
```

`ng serve -o` komandoak zerbitzaria abiatzen du eta arakatzaileari irekitzen dio aplikazioa (http://localhost:4200). Fitxategia eraldatu ondoren automatikoki berritzen da.

### Produkzio eraikuntza

```bash
npm run build
```

Emaitza `dist/webgunea/browser/` karpetan dago.

### Hedapena

[Netlify](https://www.netlify.com/) bidez hedatzen da. Konfigurazioa `netlify.toml` fitxategian dago, proiektuaren erroan

## Datuak

Datuak `public/gimnasioa_komuna.json` fitxategian daude, eta `Datuak` zerbitzuak `HttpClient`-en bidez irakurtzen ditu:

```ts
this.http.get<Gimnasioa>('gimnasioa_komuna.json');
```

JSONaren egitura hierarkikoa da:

```text
Gimnasioa → Saioa → Ariketa → Seriea
```

Fitxategiak 14 workout (maila 0tik 4ra) ditu, eta horien 101 serieko argazki erreferentziak `public/irudiak/` karpetako 42 irudietako batzuei egiten diete erreferentzia. Irudiak gehitzeko edo datuak aldatzeko, JSON-a eta irudiak `public/` karpetan eduki behar dira; asset gisa kopiatzen dira (`angular.json`-eko `assets` konfigurazioa).

## Frogak

[Vitest](https://vitest.dev.) frogatzeko programa erabiltzeko:

```bash
ng test
```

## Proiektuaren estruktura

- `angular.json`: Angularren ezarpenak
- `src/reset.css`: Estiloak garbitzeko kodea
- `src/styles.css`: Estilo nagusiak eta CSS aldagaiak
- `src/index.html`: Webgunearen hasiera
- `public/`: JSON datuak, irudiak eta `favicon.ico`

## Konponenteak

Konponente guztiak standalone-eak dira, seinaleak erabiltzen dituzte eta `src/app/` barruan daude. dependentziak `imports` bidez adierazten dituzte.

| Konponentea       | Selektorea             | Errola                                                              |
| ----------------- | ---------------------- | ------------------------------------------------------------------- |
| `App`             | `app-root`             | Txantiloi nagusia: bilaketa barra, alboko zerrenda eta eduki-eremua |
| `SaioBilaketa`    | `app-saio-bilaketa`    | Goiko barra: ElorGym marka, testu bilaketa eta maila iragazkia      |
| `SaioZerrenda`    | `app-saio-zerrenda`    | Alboko saioen zerrenda, kargatzen eta hutsik dauden egoerak barne   |
| `SaioKarta`       | `app-saio-karta`       | Saio baten karta: izena, maila badge-a, iraupena eta bideo esteka   |
| `AriketaZerrenda` | `app-ariketa-zerrenda` | Aukeratutako saioaren ariketen zerrenda                             |
| `AriketaKarta`    | `app-ariketa-karta`    | Ariketa baten informazioa: irudia, izena, deskribapena eta iraupena |

### Konponente nagusia

- `src/app/app.config.ts`: Konponente nagusiaren ezarpenak. Hornitzaileak (`provideHttpClient`) eta lokalizazioa (`LOCALE_ID: "eu-ES"`) ezartzen ditu.
- `src/app/app.ts`: Konponente nagusia. Hemen aukeratutako saioa kudeatzen da. `saio-xehetasuna` konponenteak hemendik jasotzen du erakutsi beharreko saioa, eta `saio-zerrenda`k, aldatu egiten du, aldi berean aukeratuta dagoena nabarmentzen.
- `src/app/app.html`: Konponente nagusiaren txantiloia. Hiru atal nagusi biltzen ditu: goiko `saio-bilaketa` barra, `saio-zerrenda` alboko barran eta `saio-xehetasuna` eduki-eremuan.

## Ereduak (modeloak)

`src/app/modelo/` karpetako interfaze eta funtzioek datuen egitura deskribatzen dute. Hierarkia `Gimnasioa` → `Saioa` → `Ariketa` → `Seriea` da:

- `Gimnasioa.ts`: erroko edukiontzia. `workoutak: Saioa[]` gordetzen du.
- `Saioa.ts`: saio bat. `izena`, `deskribapena`, `maila` (zenbakia), `bideoa` eta `ariketak: Ariketa[]`.
- `Ariketa.ts`: ariketa bat. `izena`, `deskribapena`, `atsedena` (segundoak) eta `serieak: Seriea[]`.
- `Seriea.ts`: serie bat. `izena`, `argazkia`, `errepikapenak` eta `iraupena`.
- `MailaIragazkia.ts`: `MailaIragazkia` enum-a (`Denak`, `Hasiberria`, `Ertaina`, `Aurreratua`) eta `mailaIzena(maila)` funtzioa, zenbakizko maila izen bihurtzen duena.
- `iraupena.ts`: `ariketaIraupena(ariketa)` eta `saioIraupena(saioa)` funtzioak. Ariketaren serieen iraupenak eta atsedenak batzen ditu, eta saio osoaren batura kalkulatzen du. Hauek modeloan bizi dira, konponenteetatik kanpo, eta hainbat tokitan berrerabiltzen dira.

## Zerbitzuak

`src/app/zerbitzuak/datuak.ts` fitxategiko `Datuak` zerbitzua aplikazioaren datu-iturri bakarra da:

- Eraikitzailean `HttpClient`-ekin `gimnasioa_komuna.json` fitxategia irakurtzen du eta `_gimnasioa` signala betetzen du.
- `bilaketa` eta `maila` signalak argitaratzen ditu; hauek bilaketa-barrak zuzenean aldatzen ditu.
- `iragazitakoak` computed signala: `_gimnasioa`-ren workoutak `bilaketa` testuaren eta `maila` iragazkiaren arabera iragazten ditu. Signal hauetako bat aldatzen den bakoitzean automatikoki berrikalkulatzen da.
- `gimnasioa` getterrak irakurtzeko soilik den signala itzultzen du.
- `_kargatzen` signala eta `kargatzen` getterra: hasieran `true`, eta `gimnasioa_komuna.json` eskaera amaitzen denean (`next` zein `error`) `false` ezartzen da. Horri esker `saio-zerrenda` eta `ariketa-zerrenda` konponenteek hiru egoera bereizten dituzte: eskaera egiten ari den bitartean "Kargatzen..." erakusten dute, datuak iritsitakoan zerrenda marrazten dute, eta emaitzarik ez badago mezu hutsa erakusten dute.

Zerbitzua `inject(Datuak)` bidez eskuragarri dago konponente guztientzat (singleton bakarra), eta horrek ahalbidetzen du bilaketa-barrak eta zerrendak datu berberak partekatzea.

## Pipeak

`src/app/pipes/` karpetako pipeek txantiloian balioak aurkezteko formatua aldatzen dute:

- `maila-pipe.ts` (`maila`): maila-zenbakia (adib. `2`) dagokion izenera (`Ertaina`) bihurtzen du, `mailaIzena` erabiliz.
- `denbora-pipe.ts` (`denbora`): segundo-kopurua `MM:SS` formatura bihurtzen du.

## Konponenteen arteko elkarreragina

Datu-fluxua goitik behera eta gertaeren bidez behetik gora doa:

1. `Datuak` zerbitzuak JSON datuak kargatzen ditu eta `iragazitakoak` eskaintzen du.
2. `SaioBilaketa` bilaketa-barrak zerbitzua `inject`atzen du eta `[(ngModel)]` bidez `bilaketa` eta `maila` signalak eguneratzen ditu. Aldaketa bakoitzak `iragazitakoak` berrikalkularazten du.
3. `SaioZerrenda`-k `iragazitakoak` irakurtzen du eta `SaioKarta` bana marrazten du. `aukeratuta` egoera `aktiboa` inputaren arabera ezartzen du.
4. Erabiltzaileak karta bat klikatzen edo nabigatzen duenean, `SaioKarta`-ren `(click)` gertaerak `SaioZerrenda`-ren `aukeratu(saioa)` deitzen du, eta honek `saioAukeratua` output-a igortzen du.
5. `App` konponenteak `(saioAukeratua)="aukeratua.set($event)"` jasotzen du eta `aukeratua` signala eguneratzen du.
6. `aukeratua` signal bera `SaioXehetasuna`-ri `[saioa]` gisa pasatzen zaio (xehetasunak erakusteko) eta `SaioZerrenda`-ri `[aktiboa]` gisa (aukeratutako karta nabarmentzeko).
7. `SaioXehetasuna`-k hautatutako saioaren ariketak `AriketaZerrenda`-ri pasatzen dizkio, eta honek `AriketaKarta` bana marrazten du.

Beraz, `App` da egoera partekatuaren jabea (`aukeratua`), `Datuak` da datu partekatuaren jabea (`iragazitakoak`, `bilaketa`, `maila`), eta gainerako konponenteak aurkezpen hutsak dira: `input()` eta `output()` bidez komunikatzen dira, eta `Datuak` edo modelo-funtzioak erabiltzen dituzte kalkuluak egiteko.

## Estiloak

Estiloak CSS hutsean idatziak dira

- `src/reset.css`: Meyer reset (2.0)
- `src/styles.css`: estilo globalak, aldagaiak eta layouta
- `src/app/**/*.css`: konponente bakoitzeko estiloak

Erantzuna ere bai: 900px baino gutxiagotan alboko zerrenda lerro horizontal gisa (snap-scroll) bihurtzen da, eta 900px edo gehiagotan bi zutubeko sarea (`minmax(260px, 350px) 1fr`) izaten da.

## Gehigarriak

Angular CLI buruzko dokumentazioa: [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli)
