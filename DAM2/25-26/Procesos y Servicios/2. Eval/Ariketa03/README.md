# Socket Objektuen Aplikazioa

## Deskribapena
Aplikazio honek Socket bidezko komunikazioa erabiltzen du Java objektuak (Pertsona) zerbitzari eta bezero artean bidaltzeko.

## Egitura
- **Pertsona.java**: Serializagarria den klase sinplea (izena, adina, herria)
- **Zerbitzaria.java**: Bezeroengandik objektuak jasotzen dituen zerbitzaria
- **Bezeroa.java**: Zerbitzarira objektuak bidaltzen dituen bezeroa

## Nola exekutatu

### 1. Konpilatu kodea
```bash
javac src/*.java -d bin
```

### 2. Exekutatu zerbitzaria (lehen terminal batean)
```bash
java -cp bin Zerbitzaria
```

### 3. Exekutatu bezeroa (bigarren terminal batean)
```bash
java -cp bin Bezeroa
```

## Funtzionamendu
1. Zerbitzaria 5000 portuan entzuten hasten da
2. Bezeroa zerbitzariarekin konektatzen da
3. Bezeroak erabiltzaileak sartu dituen datuak (izena, adina, herria) Pertsona objektu bat sortzen du
4. Objektua zerbitzarira bidaltzen da ObjectOutputStream erabiliz
5. Zerbitzariak objektua jasotzen du eta pantailan bistaratzen du
6. Zerbitzariak baieztapen mezu bat bidaltzen du bezeroari
7. Konexioa ixten da

## Ezaugarriak
- ✅ Objektuen serializazioa eta deserializazioa
- ✅ Socket bidezko komunikazioa
- ✅ Multi-threading: bezero anitz aldi berean kudeatzeko gai
- ✅ Euskerazko interfaze eta mezuak
- ✅ Errore kudeaketa egokia
