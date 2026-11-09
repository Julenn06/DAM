# EXAMEN SIMULADO — Procesos y Servicios (1ª Evaluación)

**Duración recomendada:** 1 h 30 min · **Puntuación:** 10 puntos
**Material permitido:** ninguno (ni apuntes, ni tus proyectos anteriores, ni internet). Hazlo como si fuera el examen de verdad.

Proyecto Eclipse: `Azterketa_Simulatua` (impórtalo con *File → Import → Existing Projects into Workspace*).
Ya tiene creados los paquetes `prozesuak` y `hariak`. Las clases las creas tú.

Al terminar, responde a las preguntas de cada ejercicio en un fichero `ERANTZUNAK.txt` en la raíz del proyecto y avísame para corregir.

---

## EJERCICIO 1 — PROCESOS (5 puntos) · paquete `prozesuak`

Vas a construir una "tubería" (pipe) a mano entre un comando de Windows y un programa Java tuyo.

### Clase `Kontatzailea` (proceso hijo)

Es un programa independiente con su propio `main`. Funciona así:

1. Recibe **por argumento** (`args[0]`) el nombre de un programa a buscar, por ejemplo `svchost.exe`.
   - Si **no** recibe argumento, escribe por la **salida de error** el mensaje
     `Errorea: bilatzeko programaren izena falta da` y termina con **código de salida 2**.
2. Lee **todas** las líneas que le lleguen por su **entrada estándar** hasta que ya no lleguen más.
3. Cuenta:
   - `N` = cuántas líneas contienen `.exe` (sin distinguir mayúsculas/minúsculas).
   - `M` = cuántas líneas contienen el programa recibido en `args[0]` (sin distinguir mayúsculas/minúsculas).
4. Cuando ya no hay más líneas, escribe por su salida estándar exactamente estas dos líneas:
   ```
   PROZESUAK: N
   svchost.exe: M
   ```
5. Termina con **código de salida 0**.
  
### Clase `Nagusia` (proceso padre)

1. Lanza el comando `tasklist` con `ProcessBuilder`.
2. Lanza `Kontatzailea` como **proceso Java independiente** con `ProcessBuilder`, pasándole `svchost.exe` como argumento.
3. Lee la salida de `tasklist` línea a línea y **envía cada línea** a la entrada de `Kontatzailea`.
4. Cuando termines de enviar, haz lo necesario para que `Kontatzailea` **sepa que no van a llegar más datos**.
5. Lee lo que responde `Kontatzailea` y muéstralo por pantalla con el prefijo `[Kontatzailea] `.
6. Lee los errores de `Kontatzailea` y muéstralos con el prefijo `[ERROREA] `.
7. Espera a que `Kontatzailea` termine **como máximo 5 segundos**:
   - Si termina: muestra `Kontatzailea amaitu da. Irteera kodea: X`.
   - Si no termina a tiempo: **mátalo** y muestra `Denbora agortuta: Kontatzailea hil da`.
8. Gestiona correctamente las excepciones (no vale `throws Exception` en el `main`).

**Salida esperada (los números cambiarán):**
```
[Kontatzailea] PROZESUAK: 243
[Kontatzailea] svchost.exe: 71
Kontatzailea amaitu da. Irteera kodea: 0
```

**Prueba extra:** cambia el `Nagusia` para que lance `Kontatzailea` **sin** argumento y comprueba que se ve `[ERROREA] ...` y código de salida `2`. Tu programa no debe acabar mostrando una traza de excepción en ningún caso.

### Preguntas (responder en ERANTZUNAK.txt)
- **1a.** ¿Qué pasaría si te olvidas del paso 4? Explica exactamente qué proceso se queda esperando a qué.
- **1b.** En el padre, ¿qué método de `Process` usas para *escribir* al hijo y cuál para *leer* lo que el hijo escribe? ¿Por qué los nombres parecen "al revés"?

### Criterios de corrección
| Concepto | Puntos |
|---|---|
| `Kontatzailea`: lectura hasta fin de datos + conteo correcto | 1 |
| `Kontatzailea`: control de argumento, `System.err` y códigos de salida | 0,75 |
| `Nagusia`: lanzar los dos procesos correctamente (`-cp`, nombre con paquete, argumento) | 0,75 |
| `Nagusia`: pasar las líneas de uno a otro + señal de fin | 1 |
| `Nagusia`: leer salida y errores del hijo | 0,5 |
| `Nagusia`: espera con límite de tiempo + matar + código de salida | 0,5 |
| Preguntas 1a y 1b | 0,5 |

---

## EJERCICIO 2 — HILOS / SINCRONIZACIÓN (5 puntos) · paquete `hariak`

Simulación de un **aparcamiento** con plazas limitadas.

### Clase `Aparkalekua` (objeto compartido)

- Se crea con un número de plazas (`edukiera`). En el examen serán **3**.
- Lleva la cuenta de las plazas ocupadas y del **total de entradas** realizadas desde que se abrió.
- Método `sartu(String autoa)`:
  - Si está lleno, muestra `<autoa> zain dago (beteta)` y el coche **espera** hasta que haya sitio.
  - Al entrar, muestra `<autoa> sartu da. Libre: X`.
- Método `irten(String autoa)`:
  - Libera la plaza, muestra `<autoa> irten da. Libre: X` y avisa a los coches que estén esperando.
- Método `getOkupatuta()` que devuelve cuántas plazas hay ocupadas.
- Método `getSarrerak()` que devuelve el total de entradas.
- **Nunca** puede haber más coches dentro que plazas, ni plazas libres negativas.

### Clase `Autoa` (hereda de `Thread`)

- Recibe en el constructor su nombre y el aparcamiento. `getName()` debe devolver el nombre del coche.
- Hace **2 vueltas**. En cada vuelta:
  1. Entra en el aparcamiento.
  2. Se queda dentro un tiempo **aleatorio entre 500 y 1500 ms**.
  3. Sale.
  4. Da una vuelta por fuera un tiempo **aleatorio entre 200 y 800 ms**.

### Clase `Zaindaria` (implementa `Runnable`)

- Es el vigilante. Cada **700 ms** muestra `[Zaindaria] Okupatuta: X/3`.
- Tiene un método `gelditu()` que lo para **de forma correcta** (prohibido `stop()`), y debe pararse **al momento**, aunque esté en mitad de una espera de 700 ms.
- Al pararse muestra `[Zaindaria] Txanda amaituta.`

### Clase `Nagusia`

1. Crea **un** aparcamiento de 3 plazas.
2. Crea y arranca el vigilante.
3. Crea **6 coches**: `Auto-1` … `Auto-5` y `Anbulantzia`. Al coche `Anbulantzia` ponle **prioridad máxima**.
4. Arranca todos los coches **en paralelo**.
5. Espera a que terminen **todos** los coches.
6. Para el vigilante y espera a que termine.
7. Muestra `Aparkalekua itxita. Sarrera guztiak: X` (X debe dar **siempre 12**).

**Salida esperada (el orden cambiará en cada ejecución):**
```
Auto-1 sartu da. Libre: 2
Anbulantzia sartu da. Libre: 1
Auto-3 sartu da. Libre: 0
Auto-2 zain dago (beteta)
Auto-4 zain dago (beteta)
[Zaindaria] Okupatuta: 3/3
Auto-1 irten da. Libre: 1
Auto-2 sartu da. Libre: 0
...
[Zaindaria] Txanda amaituta.
Aparkalekua itxita. Sarrera guztiak: 12
```

### Preguntas (responder en ERANTZUNAK.txt)
- **2a.** ¿Garantiza la prioridad máxima que `Anbulantzia` entre siempre la primera o no espere nunca? ¿Por qué?
- **2b.** En `sartu`, ¿usas `if` o `while` para comprobar si está lleno? Justifica por qué el otro estaría mal.
- **2c.** ¿Debería ser `synchronized` también `getOkupatuta()`? ¿Qué pasaría si no lo fuera?

### Criterios de corrección
| Concepto | Puntos |
|---|---|
| `Aparkalekua`: exclusión mutua correcta (`synchronized`) | 1 |
| `Aparkalekua`: espera/aviso correctos (`wait`/`notifyAll`, en el sitio correcto) | 1 |
| `Autoa`: herencia, nombre, 2 vueltas con tiempos aleatorios | 0,5 |
| `Zaindaria`: `Runnable`, parada correcta e inmediata | 0,75 |
| `Nagusia`: objeto compartido, prioridad, arranque en paralelo, `join` correcto | 0,75 |
| Total de entradas siempre 12, gestión de `InterruptedException` | 0,25 |
| Preguntas 2a, 2b y 2c | 0,75 |

---

### Consejos de examen
- Primero haz que **compile y funcione lo básico**; luego pule los detalles.
- Ejecuta el ejercicio 2 **varias veces**: si a veces falla, tienes un problema de concurrencia.
- Comenta brevemente en el código las decisiones importantes: el profesor puntúa que sepas **por qué** lo haces.
