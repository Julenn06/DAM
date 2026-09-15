@echo off
set base=C:\cursos\informatica\2023

rem 5. Bilatu .JPG C:\Windows direktorioan
dir C:\Windows\*.jpg

rem 6. Berdina baina azpidirektorioetan eta emaitza imágenes.txt fitxategian gorde
dir /s C:\Windows\*.jpg > imagenes.txt

rem 7. Sortu exekutableak.txt fitxategia eta gorde bertan C: unitateko .exe fitxategi guztiak
dir /s C:\*.exe > exekutableak.txt

rem 8. Aurkitu Notepad.exe C: unitatean
dir /s C:\Notepad.exe

rem 9. Kopiatu %base% direktorioko jpg fitxategi guztiak C:\Imagenes direktoriora
md C:\Imagenes 2>nul
xcopy /s %base%\*.jpg C:\Imagenes\

rem 10. Ezabatu %base% direktorioan izenean 2021 duen .bat fitxategi guztiak
del %base%\*2021*.bat

rem 11. Mugitu SO eta otsaila izenean duten .ods fitxategiak %base%\SSOO direktoriora
md %base%\SSOO 2>nul
move %base%\*SO*otsaila*.ods %base%\SSOO\

rem 12. Kopiatu windows-etik hasten diren eta xp izenean duten fitxategi guztiak C:\Win\old direktoriora
md C:\Win\old 2>nul
xcopy /s %base%\*windows*xp* C:\Win\old\

rem 13. Kopiatu Sistema/Linux direktorioko .txt fitxategi guztiak basesdedatos/oracle direktoriora
xcopy %base%\Sistema\Linux\*.txt %base%\basesdedatos\oracle\

echo Komandoak exekutatu dira.
pause