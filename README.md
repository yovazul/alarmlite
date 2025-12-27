# Alarm Lite

Alarm Lite es una aplicación de alarma personalizada para Android 9.0 (API 28) y versiones superiores.

## Características

- ✅ **Múltiples alarmas**: Crea y gestiona varias alarmas simultáneamente
- ✅ **Selección de días**: Configura alarmas para días específicos de la semana o alarmas únicas
- ✅ **Sonido personalizado**: Selecciona archivos MP3 personalizados desde tu dispositivo
- ✅ **Duración personalizada**: Establece cuánto tiempo debe sonar la alarma (en segundos)
- ✅ **Prioridad de auriculares**: El audio se reproduce automáticamente a través de auriculares si están conectados
- ✅ **Interfaz intuitiva**: Lista de alarmas con opciones para editar, eliminar y activar/desactivar
- ✅ **Pantalla completa**: La alarma se muestra en pantalla completa, incluso sobre la pantalla de bloqueo
- ✅ **Persistencia después de reinicio**: Las alarmas se reprograman automáticamente después de reiniciar el dispositivo

## Requisitos del sistema

- Android 9.0 (API level 28) o superior
- Android Studio Arctic Fox (2020.3.1) o superior para compilar
- Gradle 7.5 o superior

## Compilación del proyecto

1. Clona este repositorio:
```bash
git clone https://github.com/yovazul/alarmlite.git
cd alarmlite
```

2. Abre el proyecto en Android Studio:
   - File → Open → Selecciona la carpeta raíz del proyecto (alarmlite)
   - Asegúrate de seleccionar la carpeta que contiene `build.gradle` y `settings.gradle`

3. Sincroniza el proyecto con Gradle:
   - Android Studio detectará el proyecto Gradle automáticamente
   - Aparecerá una notificación "Gradle files have changed since last project sync"
   - Haz clic en "Sync Now" en la notificación
   - O manualmente: File → Sync Project with Gradle Files
   - Espera a que termine la sincronización (descargará dependencias la primera vez)
   - **Si los archivos no aparecen**: Ver [ANDROID_STUDIO_TROUBLESHOOTING.md](ANDROID_STUDIO_TROUBLESHOOTING.md)

4. Compila y ejecuta:
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en Run (▶️) o presiona Shift+F10

## Estructura del proyecto

```
alarmlite/
├── app/
│   ├── src/main/
│   │   ├── java/com/yovazul/alarmlite/
│   │   │   ├── data/           # Modelos de datos (Alarm entity)
│   │   │   ├── database/       # Capa de base de datos (Room DAO, Database, Repository)
│   │   │   ├── receiver/       # BroadcastReceivers (AlarmReceiver, BootReceiver)
│   │   │   ├── service/        # Servicios (AlarmSoundService para reproducción)
│   │   │   ├── ui/             # Activities, Adapters y ViewModels
│   │   │   └── util/           # Utilidades (AlarmScheduler)
│   │   ├── res/
│   │   │   ├── layout/         # Archivos de diseño XML
│   │   │   ├── values/         # Strings, colores, temas
│   │   │   ├── drawable/       # Iconos y recursos gráficos
│   │   │   └── mipmap/         # Iconos de la aplicación
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## Permisos necesarios

La aplicación solicita los siguientes permisos:

- **SCHEDULE_EXACT_ALARM / USE_EXACT_ALARM**: Para programar alarmas exactas
- **READ_EXTERNAL_STORAGE / READ_MEDIA_AUDIO**: Para seleccionar archivos MP3
- **POST_NOTIFICATIONS**: Para mostrar notificaciones durante la reproducción
- **WAKE_LOCK**: Para mantener el dispositivo activo cuando suena la alarma
- **VIBRATE**: Para vibración durante la alarma
- **FOREGROUND_SERVICE**: Para ejecutar el servicio de sonido en primer plano
- **BOOT_COMPLETED**: Para reprogramar alarmas después de reiniciar

## Uso de la aplicación

### Crear una alarma

1. Presiona el botón flotante "+" en la pantalla principal
2. Configura la hora usando el selector de tiempo
3. (Opcional) Agrega una etiqueta descriptiva
4. (Opcional) Selecciona un archivo MP3 personalizado
5. Configura la duración en segundos
6. Selecciona los días de la semana (o déjalos sin marcar para alarma única)
7. Presiona "Guardar"

### Editar una alarma

1. Toca cualquier alarma de la lista
2. Modifica los parámetros deseados
3. Presiona "Guardar"

### Activar/Desactivar una alarma

- Usa el interruptor en la tarjeta de alarma

### Eliminar una alarma

- Presiona el icono de papelera en la tarjeta de alarma

### Cuando suena la alarma

- La alarma se mostrará en pantalla completa
- Presiona "Desactivar" para detener la alarma

## Características técnicas

### Base de datos
- **Room Database**: Almacenamiento persistente de alarmas
- **LiveData**: Observación reactiva de cambios en la base de datos
- **Coroutines**: Operaciones asíncronas eficientes

### Programación de alarmas
- **AlarmManager**: Programación precisa de alarmas del sistema
- **BroadcastReceiver**: Gestión de eventos de alarma
- **Reprogramación automática**: Después de reiniciar el dispositivo

### Reproducción de audio
- **MediaPlayer**: Reproducción de archivos MP3
- **AudioAttributes**: Configuración de audio con prioridad para auriculares
- **AudioFocusRequest**: Gestión apropiada del foco de audio
- **Foreground Service**: Reproducción ininterrumpida con notificación

### Interfaz de usuario
- **Material Design Components**: UI moderna y consistente
- **RecyclerView**: Lista eficiente de alarmas
- **ViewBinding**: Acceso seguro a vistas
- **MVVM Architecture**: Separación clara de responsabilidades

## Licencia

Este proyecto está bajo licencia MIT.

## Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o pull request para sugerencias o mejoras.