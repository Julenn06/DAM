@echo off
set base=C:\cursos\informatica\2024

md "%base%\Sistema\linux"
md "%base%\Sistema\Windows"
md "%base%\Sistema\macOS"
md "%base%\Sistema\android"
md "%base%\Sistema\ios"

md "%base%\redes\servicios"
md "%base%\redes\arquitecturas"
md "%base%\redes\implantación"
md "%base%\redes\inalambrico"

md "%base%\basesdedatos\oracle"
md "%base%\basesdedatos\sqlServer"
md "%base%\basesdedatos\nosql"
md "%base%\basesdedatos\mySql"

md "%base%\desarrollo\IDEs"
md "%base%\desarrollo\lenguajes\java"
md "%base%\desarrollo\lenguajes\python"
md "%base%\desarrollo\lenguajes\c"
md "%base%\desarrollo\lenguajes\react"

md "%base%\web\html"
md "%base%\web\css"
md "%base%\web\js"
md "%base%\web\php"

rem Ezabatu zehaztutako karpetak
rmdir /s /q "%base%\redes\servicios"
rmdir /s /q "%base%\web\html"
rmdir /s /q "%base%\desarrollo\lenguajes"
rmdir /s /q "%base%\desarrollo"

rem Birsortu beharrezko karpetak fitxeroentzat
md "%base%\redes\servicios"
md "%base%\desarrollo\IDEs"

rem Sortu fitxeroak
type nul > "%base%\Sistema\linux\apuntes.txt"
type nul > "%base%\Sistema\Windows\foto.jpg"
type nul > "%base%\Sistema\Windows\windowsParaXP.jpg"
type nul > "%base%\Sistema\Windows\windowsComXP.jpg"
type nul > "%base%\redes\servicios\foto2021.jpg"
type nul > "%base%\redes\implantación\SOEnero.ods"
type nul > "%base%\redes\implantación\SOFebrero.ods"
type nul > "%base%\redes\implantación\SOMarzo.ods"

echo Egitura eta fitxeroak sortuta.
pause