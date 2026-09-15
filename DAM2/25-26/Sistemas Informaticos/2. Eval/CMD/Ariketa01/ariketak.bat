@echo off
echo 1. TXT luzapena duten fitxategi guztiak:
echo 2. Lau karaktereko izena eta edozein luzapen duten fitxategi guztiak:
echo 3. "a" letrarekin hasi eta "d" letrarekin amaitzen diren fitxategi guztiak, EXE luzapena dutenak.
echo 4. Gutxienez lau karaktere eta PDF luzapena duten fitxategi guztiak:
echo 5. Izena 01 zenbakiarekin hasi eta JPG luzapena duten fitxategi guztiak:
echo 6. "Oporrak" katearen bidez amaitzen diren JPG fitxategi guztiak.
echo 7. "Uda" hitza izenean eta PDF luzapena duten fitxategi guztiak
echo 8. "Faktura" hitza izenean eta ODS luzapena duten fitxategi guztiak
echo 9. "Blues" katea bere izenean, "01" izenarekin amaitzen direnak eta MP3 motakoak diren fitxategiak.
echo 10."01" -z amaitzen den eta "h" -z hasten den fitxategi guztiak.
echo 11."a" letrarekin hasten diren fitxategi guztiak, azken-aurreko letra "d" bat dutenak eta EXE luzapena dutenak.
echo 12."Uda" hitza izenean duten fitxategi guztiek, berdin da zein luzapen eduki

echo 13. Zabaldu terminala eta erakutsi C:/ fitxategiko direktorio guztiak pantailatik
echo 14. Konprobatu C:\ikastola fitxategia existitzen den.
echo 15. Ez bada existitzen, sortu karpeta C:\ikastola.
echo 16. Aldatu C:\ikastola fitxategira ruta erlatiboak erabilita.
echo 17. Sortu enero, febrero eta marzo karpetak 2023 karpetaren barnean C:\ikastolaren barnean, bakarrik komando bat erabilita
echo 18. Erakutsi terminalean ondorengo testua “Nire izena XXX da”.
echo 19. Sortu artxibo bat usuario.txt deiturikoa C:\ikastola karpetaren barruan, non bere edukia ondorengo testu katea den "Nire izena XXX da eta nire abizena YYYY".
echo 20. Kopiatu C:\ikastola\usuario.txt artxiboa C:\ikastola\2023 karpetara ruta erlatiboak erabilita.
echo 21. Aldatu izena C:\ikastola\usuario.txt artxiboari eta usuarios.log deitu
echo 22.Aldatu hurrengo direktoriora C:\ikastola\2023
echo 23. Erakutsi terminalaren pantailatik C:\ikastola fitxategien estruktura ruta erlatiboak erabiliz.
echo 24.Gorde C:\ikastola ko fitxategien estruktura ondorengo artxiboan C:\ikastola\2023\listado.txt. Erabili ruta erlatiboak eta fitxeroa existitzen bada berridatzi (sobreescribir) gainean.
echo 25.Gehitu C:\ikastola fitxategien zuhaitza C:\ikastola\usuarios.log artxiboan ruta erlatiboak erabilita (existitzen bada artxiboa gehitu datuak)
set /p zbk=Aukeratu zbk:


set "zbk=%zbk: =%"

if "%zbk%"=="1" dir *.txt /s
if "%zbk%"=="2" dir ????*.* /s
if "%zbk%"=="3" dir a*d.exe /s
if "%zbk%"=="4" dir ????*.pdf /s
if "%zbk%"=="5" dir 01*.jpg /s
if "%zbk%"=="6" dir *Oporrak.jpg /s
if "%zbk%"=="7" dir *Uda*.pdf /s
if "%zbk%"=="8" dir *Faktura*.ods /s
if "%zbk%"=="9" dir *Blues*01*.mp3 /s
if "%zbk%"=="10" dir h*01*.* /s
if "%zbk%"=="11" dir a*d?.exe /s
if "%zbk%"=="12" dir *Uda*.* /s

if "%zbk%"=="13" dir C:\ /s
if "%zbk%"=="14" if exist ikastola echo ikastola existitzen da. else echo ikastola ez dago.
if "%zbk%"=="15" if not exist ikastola mkdir ikastola
if "%zbk%"=="16" cd ikastola
if "%zbk%"=="17" mkdir ikastola\2023\enero ikastola\2023\febrero ikastola\2023\marzo
if "%zbk%"=="18" echo Nire izena XXX da.
if "%zbk%"=="19" echo Nire izena XXX da eta nire abizena YYYY > ikastola\usuario.txt
if "%zbk%"=="20" copy ikastola\usuario.txt ikastola\2023\
if "%zbk%"=="21" ren ikastola\usuario.txt usuarios.log
if "%zbk%"=="22" cd ikastola\2023
if "%zbk%"=="23" dir /s
if "%zbk%"=="24" dir /s > ikastola\2023\listado.txt
if "%zbk%"=="25" dir /s >> ikastola\usuarios.log
pause