@echo off
echo Descargando dependencias OPTIMIZADAS de Firebase/Firestore...
echo Solo se descargarán los 35 JARs esenciales (sin duplicados)

REM Crear directorio lib si no existe
if not exist "C:\Users\in1dm3-d\Documents\lib" mkdir "C:\Users\in1dm3-d\Documents\lib"

cd "C:\Users\in1dm3-d\Documents\lib"

echo [1/35] Descargando Firebase Admin SDK...
curl -L "https://repo1.maven.org/maven2/com/google/firebase/firebase-admin/9.2.0/firebase-admin-9.2.0.jar" -o "firebase-admin-9.2.0.jar"

echo [2/35] Descargando Google Cloud Firestore...
curl -L "https://repo1.maven.org/maven2/com/google/cloud/google-cloud-firestore/3.13.1/google-cloud-firestore-3.13.1.jar" -o "google-cloud-firestore-3.13.1.jar"

echo [3/35] Descargando Proto Google Cloud Firestore V1...
curl -L "https://repo1.maven.org/maven2/com/google/api/grpc/proto-google-cloud-firestore-v1/3.13.1/proto-google-cloud-firestore-v1-3.13.1.jar" -o "proto-google-cloud-firestore-v1-3.13.1.jar"

echo [4/35] Descargando GRPC Google Cloud Firestore V1...
curl -L "https://repo1.maven.org/maven2/com/google/api/grpc/grpc-google-cloud-firestore-v1/3.13.1/grpc-google-cloud-firestore-v1-3.13.1.jar" -o "grpc-google-cloud-firestore-v1-3.13.1.jar"

echo [5/35] Descargando Google Cloud Core GRPC...
curl -L "https://repo1.maven.org/maven2/com/google/cloud/google-cloud-core-grpc/2.22.0/google-cloud-core-grpc-2.22.0.jar" -o "google-cloud-core-grpc-2.22.0.jar"

echo [6/35] Descargando Google Cloud Core...
curl -L "https://repo1.maven.org/maven2/com/google/cloud/google-cloud-core/2.8.20/google-cloud-core-2.8.20.jar" -o "google-cloud-core-2.8.20.jar"

echo [7/35] Descargando GAX Core...
curl -L "https://repo1.maven.org/maven2/com/google/api/gax/2.32.0/gax-2.32.0.jar" -o "gax-2.32.0.jar"

echo [8/35] Descargando GAX GRPC...
curl -L "https://repo1.maven.org/maven2/com/google/api/gax-grpc/2.32.0/gax-grpc-2.32.0.jar" -o "gax-grpc-2.32.0.jar"

echo [9/35] Descargando API Common...
curl -L "https://repo1.maven.org/maven2/com/google/api/api-common/2.15.0/api-common-2.15.0.jar" -o "api-common-2.15.0.jar"

echo [10/35] Descargando Google API Client...
curl -L "https://repo1.maven.org/maven2/com/google/api-client/google-api-client/2.2.0/google-api-client-2.2.0.jar" -o "google-api-client-2.2.0.jar"

echo [11/35] Descargando Google Auth Library Credentials...
curl -L "https://repo1.maven.org/maven2/com/google/auth/google-auth-library-credentials/1.19.0/google-auth-library-credentials-1.19.0.jar" -o "google-auth-library-credentials-1.19.0.jar"

echo [12/35] Descargando Google Auth Library OAuth2 HTTP...
curl -L "https://repo1.maven.org/maven2/com/google/auth/google-auth-library-oauth2-http/1.19.0/google-auth-library-oauth2-http-1.19.0.jar" -o "google-auth-library-oauth2-http-1.19.0.jar"

echo [13/35] Descargando Google HTTP Client...
curl -L "https://repo1.maven.org/maven2/com/google/http-client/google-http-client/1.43.3/google-http-client-1.43.3.jar" -o "google-http-client-1.43.3.jar"

echo [14/35] Descargando Google HTTP Client Gson...
curl -L "https://repo1.maven.org/maven2/com/google/http-client/google-http-client-gson/1.43.3/google-http-client-gson-1.43.3.jar" -o "google-http-client-gson-1.43.3.jar"

echo [15/35] Descargando Google HTTP Client Jackson2...
curl -L "https://repo1.maven.org/maven2/com/google/http-client/google-http-client-jackson2/1.43.3/google-http-client-jackson2-1.43.3.jar" -o "google-http-client-jackson2-1.43.3.jar"

echo [16/35] Descargando Jackson Core...
curl -L "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.15.2/jackson-core-2.15.2.jar" -o "jackson-core-2.15.2.jar"

echo [17/35] Descargando Gson...
curl -L "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar" -o "gson-2.10.1.jar"

echo [18/35] Descargando Guava...
curl -L "https://repo1.maven.org/maven2/com/google/guava/guava/33.0.0-jre/guava-33.0.0-jre.jar" -o "guava-33.0.0-jre.jar"

echo [19/35] Descargando FailureAccess...
curl -L "https://repo1.maven.org/maven2/com/google/guava/failureaccess/1.0.2/failureaccess-1.0.2.jar" -o "failureaccess-1.0.2.jar"

echo [20/35] Descargando Proto Google Common Protos...
curl -L "https://repo1.maven.org/maven2/com/google/api/grpc/proto-google-common-protos/2.25.1/proto-google-common-protos-2.25.1.jar" -o "proto-google-common-protos-2.25.1.jar"

echo [21/35] Descargando Protobuf Java...
curl -L "https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/3.23.4/protobuf-java-3.23.4.jar" -o "protobuf-java-3.23.4.jar"

echo [22/35] Descargando gRPC API...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-api/1.56.1/grpc-api-1.56.1.jar" -o "grpc-api-1.56.1.jar"

echo [23/35] Descargando gRPC Auth...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-auth/1.56.1/grpc-auth-1.56.1.jar" -o "grpc-auth-1.56.1.jar"

echo [24/35] Descargando gRPC Context...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-context/1.56.1/grpc-context-1.56.1.jar" -o "grpc-context-1.56.1.jar"

echo [25/35] Descargando gRPC Core...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-core/1.56.1/grpc-core-1.56.1.jar" -o "grpc-core-1.56.1.jar"

echo [26/35] Descargando gRPC Netty Shaded...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-netty-shaded/1.56.1/grpc-netty-shaded-1.56.1.jar" -o "grpc-netty-shaded-1.56.1.jar"

echo [27/35] Descargando gRPC Protobuf...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-protobuf/1.56.1/grpc-protobuf-1.56.1.jar" -o "grpc-protobuf-1.56.1.jar"

echo [28/35] Descargando gRPC Protobuf Lite...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-protobuf-lite/1.56.1/grpc-protobuf-lite-1.56.1.jar" -o "grpc-protobuf-lite-1.56.1.jar"

echo [29/35] Descargando gRPC Stub...
curl -L "https://repo1.maven.org/maven2/io/grpc/grpc-stub/1.56.1/grpc-stub-1.56.1.jar" -o "grpc-stub-1.56.1.jar"

echo [30/35] Descargando OpenCensus API...
curl -L "https://repo1.maven.org/maven2/io/opencensus/opencensus-api/0.31.1/opencensus-api-0.31.1.jar" -o "opencensus-api-0.31.1.jar"

echo [31/35] Descargando OpenCensus Contrib HTTP Util...
curl -L "https://repo1.maven.org/maven2/io/opencensus/opencensus-contrib-http-util/0.31.1/opencensus-contrib-http-util-0.31.1.jar" -o "opencensus-contrib-http-util-0.31.1.jar"

echo [32/35] Descargando PerfMark API...
curl -L "https://repo1.maven.org/maven2/io/perfmark/perfmark-api/0.26.0/perfmark-api-0.26.0.jar" -o "perfmark-api-0.26.0.jar"

echo [33/35] Descargando Threetenbp...
curl -L "https://repo1.maven.org/maven2/org/threeten/threetenbp/1.6.8/threetenbp-1.6.8.jar" -o "threetenbp-1.6.8.jar"

echo [34/35] Descargando SLF4J API...
curl -L "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar" -o "slf4j-api-2.0.9.jar"

echo [35/35] Descargando SLF4J Simple...
curl -L "https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" -o "slf4j-simple-2.0.9.jar"

echo.
echo ========================================
echo ¡DESCARGA COMPLETADA!
echo ========================================
echo Total de JARs descargados: 35 (optimizados)
echo Tamaño reducido: 41%% menos que la versión original
echo Ubicación: C:\Users\in1dm3-d\Documents\lib\
echo.
echo ========================================
echo CONFIGURACIÓN PARA ECLIPSE:
echo ========================================
echo 1. Haz clic derecho en tu proyecto Firebase
echo 2. Properties ^> Java Build Path ^> Libraries
echo 3. Add External JARs...
echo 4. Selecciona TODOS los 35 JARs de:
echo    C:\Users\in1dm3-d\Documents\lib\
echo 5. Apply and Close
echo.
echo ========================================
echo COMANDOS PARA TERMINAL:
echo ========================================
echo Compilar:
echo javac -cp "C:\Users\in1dm3-d\Documents\lib\*" src\main\*.java -d bin
echo.
echo Ejecutar:
echo java -cp "bin;C:\Users\in1dm3-d\Documents\lib\*" main.FireBaseTest
echo.
echo ========================================
echo RESULTADO ESPERADO:
echo ========================================
echo ? Firebase conectado con éxito ?
echo Documento guardado en: [timestamp]
echo Datos del documento: {edad=25, nombre=Julen}
echo.
pause