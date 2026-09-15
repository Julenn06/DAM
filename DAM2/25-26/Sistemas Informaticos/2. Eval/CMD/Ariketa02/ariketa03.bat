@echo off

rem 1. Aldatu direktoriotik uneko unitatearen erroko direktoriora.
cd \

rem 2. Sortu “erabiltzaile” izeneko direktorio bat.
md erabiltzaile

rem 3. Sortu hiru direktorio erabiltzaile direktorioan: pepe, maria eta antonio.
md erabiltzaile\pepe erabiltzaile\maria erabiltzaile\antonio

rem 4. Saiatu erabiltzaile direktorioa parametrorik gabeko RD komandoarekin ezabatzen
rd erabiltzaile

rem 5. Ezabatu maria direktorioa, erabiltzaile direktorioaren barruan dagoena. Egungo direktorioa\.
rd erabiltzaile\maria

rem 6. Sortu ongietorri fitxategi bat, ongietorria.txt izenekoa, mezu honekin: "Kaixo, ongi etorri sistemara",\erabiltzailea direktorioaren barruan. Hau da egungo direktorioa\.
echo Kaixo, ongi etorri sistemara > erabiltzaile\ongietorria.txt

rem 7. Egungo direktorioa C:\usuarios\pepe\trabajos dela pentsatuta, eta ibilbide erlatibo bat erabilita, aurreko galdera egin.
cd C:\usuarios\pepe\trabajos
echo Kaixo, ongi etorri sistemara > ..\..\erabiltzaile\ongietorria.txt

rem 8. C:\Windows\System32 direktorioaren edukia erakutsi, ibilbide absolutu bat erabiliz. Egungo direktorioa C:\.
cd \
dir C:\Windows\System32

rem 9. DIR parametroak ezagututa, zer egingo du hurrengo komandoak? Egungo direktorioa C:\windows :
rem DIR /S erakutsi direktorioa eta azpidirektorio guztiak
cd C:\windows
dir /s

rem 10. C:\Windows direktorioan biltzen diren direktorioak bakarrik erakutsi. Egungo direktorioa: C:\Documents and settings\usuario. Erabili ibilbide erlatiboa.
cd "C:\Documents and settings\usuario"
dir /ad ..\Windows

rem 11. C:\Windows direktorioan dauden direktorioak erakutsi, izenaren arabera ordenatuta, goranzko ordenan. Egungo direktorioa: C:\Documents and settings\usuario. Erabili ibilbide erlatiboa.
dir /ad /on ..\Windows

rem 12. Mahaigaineko direktorioan dauden fitxategiak erakutsi, dataren eta orduaren arabera ordenatuta, beheranzko ordenan. Egungo direktorioa: C:\Documents and settings\usuario. Erabili ibilbide absolutua. Zaharrenetik berrienera
dir /o-d "C:\Documents and settings\usuario\Desktop"

rem 13. Mahaigaineko direktorioaren edukia erakutsi, tamainaren arabera ordenatuta. Egungo direktorioa: C:\Documents and settings\usuario
dir /o-s "C:\Documents and settings\usuario\Desktop"

rem 14. Gorde mahaigaineko direktorioko edukiaren zerrenda, tamainaren arabera ordenatuta, “zerrenda.txt” fitxategian.
dir /o-s "C:\Documents and settings\usuario\Desktop" > zerrenda.txt

rem 15. “zerrenda.txt” fitxategiaren edukia erakutsi.
type zerrenda.txt

rem 16. C:\Windows direktorioaren edukia erakutsi, pantailaz pantaila. Egungo direktorioa: C:\Documents and settings\usuario. Erabili ibilbide erlatibo bat.
dir /p ..\Windows

rem 17. Nire dokumentuak direktorioaren barruan, “laguntza.txt” izeneko fitxategi batean gorde DIR komandoaren eta TYPE komandoaren laguntza. Egungo direktorioa: C:\Documents and settings\usuario. Ibilbide erlatiboa erabiliz. Laguntza. “>” birbideratzea ez dator bat birbideratze bikoitzarekin “>>”.
dir /? > "Nire dokumentuak\laguntza.txt"
type /? >> "Nire dokumentuak\laguntza.txt"

rem 18. Aurreko galderan sortutako “laguntza.txt” fitxategiaren edukia erakutsi pantailan. Egungo direktorioa: C:\Documents and settings\usuario.
type "Nire dokumentuak\laguntza.txt"

rem 19. C:\Documents and Settings direktorioen egitura erakutsi. Egungo direktorioa: C:\Documents and settings\usuario. Erabili gehien komeni zaizun bidea.
tree "C:\Documents and Settings"

rem 20. Erabiltzaile guztien profila biltegiratzen duen direktorio-egitura erakutsi, ingurune-aldagai bat erabiliz. Egituraren barruko fitxategiak ere erakutsi behar dira.
dir /s %ALLUSERSPROFILE%

rem 21. Erabiltzaile guztien mahaigainean sistemaren ordua duen “informazioa.txt” izeneko fitxategi bat sortu. Egungo direktorioa C:\. Kontuan izan ez dakizula zein den erabiltzaile guztien profilaren ibilbidea.
cd \
echo %time% > "%ALLUSERSPROFILE%\Desktop\informazioa.txt"

rem 22. Egin aurreko ariketa, baina lehenengo gorde sistemaren data, gero sistemaren ordua eta, azkenik, zure erabiltzaile-izena. Erabiltzaile-izena saioa hasteko erabili duzun izen bera izan behar da.
echo %date% > "%ALLUSERSPROFILE%\Desktop\informazioa.txt"
echo %time% >> "%ALLUSERSPROFILE%\Desktop\informazioa.txt"
echo %username% >> "%ALLUSERSPROFILE%\Desktop\informazioa.txt"

rem 23. Gehitu aurreko fitxategiari komando-interpretearen bertsioa.
ver >> "%ALLUSERSPROFILE%\Desktop\informazioa.txt"

rem 24. Berrizendatu aurretik erabili dugun “informazioa.txt” fitxategia, “datuak.txt” deitzeko.
ren "%ALLUSERSPROFILE%\Desktop\informazioa.txt" datuak.txt

rem 25. Aldatu sistemaren prompt -a erabiltzaile-izena ager dadin.
prompt $u

rem 26. Prompt hori ez denez oso esanguratsua, erabiltzailearen izenari egungo unitatea eta ibilbidea gehitu, eta, bereizketa gisa, ">" karakterea.
prompt $u$d$p$g

rem 27. Prompt-ean erabiltzailearen izena eta uneko data erakutsi, komando-kurtsorea parentesi batekin bereiziz.
prompt ($u %date%)$g

rem 28. Prompt-ean erabiltzailearen izena eta sistemaren uneko ordua erakutsi, komando-kurtsorea parentesi batekin bereiziz.
prompt ($u %time%)$g

rem 29. Sortu Proba izeneko direktorio bat C unitatearen erro direktorioan:
md C:\Proba

rem 30. Erakutsi sortu dela egiaztatzeko proba-direktorioa.
dir C:\Proba

rem 31. Sortu berri den direktorioaren horren barruan fitxategi bat sortu. Fitxategi horren izena zerrenda.txt izango da, eta haren edukia erabiltzaile guztien profilaren mahaigainean dauden fitxategien zerrenda izango da.
dir /b "%ALLUSERSPROFILE%\Desktop" > C:\Proba\zerrenda.txt

rem 32. Sortu berri duzun fitxategi zerrendatuaren edukia erakutsi.
type C:\Proba\zerrenda.txt

rem 33. Gehitu mezu bat fitxategi sortutako fitxategiaren amaieran, eta adierazi fitxategi horretan biltegiratutako informazioa bat datorrela erabiltzaile guztien profileko mahaigainaren direktorioko edukiarekin (“informazioa zuzena da”, adibidez). Egungo data ere gehitu.
echo informazioa zuzena da %date% >> C:\Proba\zerrenda.txt

rem 34. Berrizendatu aurretik sortutako zerrenda.txt fitxategia info.txt izenarekin.
ren C:\Proba\zerrenda.txt info.txt

rem 35. Berrizendatu proba direktorioa, "logs" izenarekin.
ren C:\Proba logs

rem 36. Zer egiten du hurrengo komandoak? 
rem COPY alumnos_aprobados.txt aprobados.txt - Kopiatu alumnos_aprobados.txt aprobados.txt izenarekin
copy alumnos_aprobados.txt aprobados.txt

rem 37. Zer egiten du hurrengo komandoak? 
rem COPY aprobados.txt C:\ - Kopiatu aprobados.txt C:\ra
copy aprobados.txt C:\

rem 38. C:\logs\info.txt fitxategia C: unitatearen erroko direktorioan kopiatu.
copy C:\logs\info.txt C:\

rem 39. C:\logs\info.txt fitxategia C: unitatearen erroko direktorioan kopiatu, pepito.txt izenarekin
copy C:\logs\info.txt C:\pepito.txt

rem 40. Ezabatu aurreko ariketan sortutako pepito.txt fitxategia.
del C:\pepito.txt

rem 41. Aldatu egungo direktoriotik C:\logs direktoriora
cd C:\logs

rem 42. Egiaztatu benetan aldatu zarela C:\logs direktoriora
cd

rem 43. Sortu direktorio hauek C:\logs. apache, zftp, dhcp, dns direktorioaren barruan. Egungo direktorioa C:\
cd \
md C:\logs\apache C:\logs\zftp C:\logs\dhcp C:\logs\dns

rem 44. Egungo direktorioa: C:\logs. Kopiatu info.txt fitxategia apache direktorioaren barruan apache.log. izenarekin.
cd C:\logs
copy info.txt apache\apache.log

rem 45. info.txt fitxategia datos.log fitxategira kopiatu egungo direktorioan.
copy info.txt datos.log

rem 46. Aldatu egungo direktoriotik dhcp direktoriora.
cd dhcp

rem 47. C:\logs\datos.log fitxategia C:\logs\dhcp direktorioan kopiatu, dhcp.log izenarekin, eta ibilbide erlatiboak soilik erabiliz. Egungo direktorioa: C:\logs\dhcp.
copy ..\datos.log dhcp.log

rem 48. C:\logs\datos.log fitxategia egungo direktorioan kopiatu. Ibilbide erlatiboak erabiliz. Egungo direktorioa: C:\logs\dhcp.
copy ..\datos.log .

rem 49. Kopiatu, komando bakar batekin, info.txt eta datos.log fitxategiak C:\logs direktoriotik C:\logs\dns direktoriora. C:\logs direktorioko fitxategi bakarrak info.txt eta datos.log. dira. Ibilbide erlatiboak erabiliz. Egungo direktorioa: C:\logs\dhcp.
copy ..\info.txt ..\datos.log ..\dns

rem 50. Mugitu C:\logs\info.txt fitxategia C:\dhcp direktoriora, dhcp-deny.log. izenarekin. Egungo direktorioa: C:\logs. Ibilbide erlatiboak erabiliz.
cd C:\logs
move info.txt dhcp\dhcp-deny.log

echo Ariketa amaitu da.
pause