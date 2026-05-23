# AGENTS.md

## Proyecto: SongInfo

Este documento define las reglas de trabajo para el desarrollo y mantenimiento de la aplicación **SongInfo**.

La base del proyecto combina varios estilos arquitectónicos:

- En `home` se usa un enfoque cercano a **MVC**.
- En `moredetails` se usa un enfoque más cercano a **MVP / Clean Architecture ligera**.
- A nivel general existe separación por capas y por features.

La idea principal es **respetar la estructura actual**, evitar mezclas innecesarias y mantener el código fácil de leer, probar y extender.

---

## 1. Principios generales

- Priorizar claridad antes que abstracción excesiva.
- Mantener el estilo existente del proyecto cuando se modifique código ya escrito.
- Evitar introducir nuevas dependencias o patrones si no aportan valor real.
- No mover lógica entre capas sin una razón concreta.
- Preferir soluciones simples, explícitas y fáciles de testear.
- Utiliza buenas prácticas en desarrollo de software y aplicaciones móviles Android e iOS.
- Sigue la guía de estilo más actual de Kotlin.
- La app soporta idioma español e inglés. Siendo inglés el idioma por defecto.
- Los textos deben estar estructurados por pantalla en sus ficheros de `strings.xml`.
---

## 2. Arquitectura del proyecto

### 2.1 `home`
La pantalla principal sigue un estilo MVC:

- `HomeView` actúa como vista.
- `HomeController` coordina acciones.
- `HomeModel` contiene el estado y la lógica asociada.
- `SongRepository` y `SongRepositoryImpl` encapsulan el acceso a datos.

Reglas para esta zona:

- La vista no debe contener lógica de negocio.
- El controlador no debe manipular directamente la UI.
- El modelo debe exponer estado observable y gestionar la información del dominio.
- El repositorio debe concentrar acceso a datos local y remoto.

### 2.2 `moredetails`
La pantalla de detalles sigue un estilo más cercano a MVP y Clean Architecture ligera:

- `presentation` contiene presenter y vista.
- `domain` contiene entidades e interfaces.
- `data` contiene implementaciones concretas, proxies y almacenamiento local.
- `injector` arma dependencias de forma manual.

Reglas para esta zona:

- La capa `presentation` no debe conocer detalles de implementación de datos.
- La capa `domain` no debe depender de `data`.
- La capa `data` implementa las interfaces definidas en `domain`.
- La transformación de modelos a UI state debe hacerse en `presentation` o mediante mappers claros.

---

## 3. Patrones existentes que deben respetarse

### 3.1 Observer
El proyecto usa un sistema de observables en `ayds.observer`.

- Mantener este mecanismo cuando la pantalla o el componente ya lo esté usando.
- Evitar reemplazarlo por otra solución distinta en un mismo flujo sin necesidad.
- Las vistas deben suscribirse y desuscribirse correctamente.

### 3.2 Repository
El acceso a datos debe pasar por repositorios.

- No llamar servicios remotos directamente desde vistas o presentadores.
- No acceder a Room o almacenamiento local desde la UI.
- Los repositorios pueden coordinar fuentes locales y remotas.

### 3.3 Proxy / Broker
En `moredetails` se usan proxies por fuente y un broker que agrega resultados.

- Respetar esta separación si se amplía el sistema.
- Cada proxy debe conocer una sola fuente externa.
- El broker debe coordinar, no contener lógica de presentación.

### 3.4 Inyección manual / Service Locator
El proyecto usa `Injector` como ensambladores manuales.

- No introducir Hilt/Dagger/Koin salvo decisión explícita de refactor global.
- Mantener la creación de dependencias centralizada en los injectors existentes.
- Evitar instanciar dependencias de infraestructura directamente dentro de las vistas o controladores.

### 3.5 Mapper
Cuando se necesite convertir entidades de dominio a estados de UI:

- Usar funciones de mapeo claras.
- No mezclar transformación de datos con lógica de red o persistencia.
- Mantener los mappers simples y testeables.

---

## 4. Reglas de UI y presentación

- La UI debe ser reactiva y recibir estado desde el modelo/presenter.
- Evitar lógica de negocio dentro de `Activity`, `View` o componentes visuales.
- Las pantallas deben reaccionar a cambios de estado, no calcularlos.
- No pasar dependencias pesadas a componentes hijos si basta con pasar estado y callbacks.
- Mantener la navegación encapsulada en utilidades o capas específicas.

---

## 5. Trabajo con datos

### 5.1 Estrategia local-first
En `SongRepositoryImpl` se sigue una estrategia local-first con fallback remoto.

Reglas:

- Consultar primero la fuente local cuando aplique.
- Si no hay datos locales, consultar el origen remoto.
- Persistir en local cuando corresponda.
- No duplicar esta lógica en otras capas.

### 5.2 Room / almacenamiento local
Cuando exista almacenamiento local:

- El acceso debe pasar por DAO, local storage o wrappers equivalentes.
- No exponer Room directamente a la UI.
- Mantener el código de persistencia aislado del resto del sistema.

---

## 6. Concurrencia y threading

El proyecto actualmente usa `Thread { ... }.start()` en algunas partes.

Reglas recomendadas:

- No agregar más lógica concurrente manual si puede evitarse.
- Si se introduce nueva asincronía, preferir una solución consistente con el resto del módulo.
- Mantener la UI libre de bloqueos.
- Toda operación costosa debe ejecutarse fuera del hilo principal.

Si se decide refactorizar concurrencia, hacerlo de forma progresiva y por módulo, no en un cambio masivo.

---

## 7. Organización de paquetes

Seguir la estructura actual por features y capas:

- `home`
- `moredetails`
- `utils`
- `observer`

Dentro de cada feature, mantener subpaquetes por responsabilidad:

- `view` o `presentation`
- `controller` o `presenter`
- `model`
- `domain`
- `data`
- `injector`
- `local`
- `proxy`

Reglas:

- No mezclar responsabilidades entre paquetes.
- No crear paquetes genéricos innecesarios.
- Si aparece una nueva feature, replicar el mismo criterio de separación.

---

## 8. Convenciones de código

- Código en inglés.
- Comentarios en español si hacen falta.
- Nombres descriptivos y consistentes con el rol de cada clase.
- Clases y objetos con nombres explícitos.
- Métodos cortos y con una sola responsabilidad.
- Evitar abreviaturas ambiguas.
- Preferir nombres alineados con el dominio del problema.

### Ejemplos de naming
- `HomeView`
- `HomeController`
- `HomeModel`
- `MoreDetailsPresenter`
- `MoreDetailsRepositoryImpl`
- `SongRepositoryImpl`
- `LastFMProxy`

---

## 9. Dependencias y librerías

- No agregar dependencias nuevas sin justificar su necesidad.
- Antes de incorporar una librería, evaluar si el proyecto ya resuelve ese caso con código propio.
- Mantener consistencia con las bibliotecas ya presentes.
- Si una dependencia se usa solo como adaptador, abstraerla detrás de una interfaz.

---

## 10. Testing

- Toda lógica de dominio debe ser testeable.
- Priorizar tests sobre:
    - repositorios
    - mappers
    - presenters
    - controllers
    - reglas de negocio
- Evitar tests acoplados a detalles de implementación.
- Testear casos felices, nulos, vacíos y fallos de integración.

---

## 11. Reglas para cambios futuros

Cuando se agregue una nueva funcionalidad:

1. Identificar si pertenece a `home`, `moredetails` o a una nueva feature.
2. Definir primero la responsabilidad de cada capa.
3. Crear interfaces antes de implementaciones concretas cuando haya desacople real.
4. Mantener la lógica de datos fuera de la UI.
5. Mantener el ensamblado de dependencias en los injectors.
6. Evitar introducir un patrón nuevo si el ya existente alcanza.

---

## 12. Anti-patrones a evitar

- UI con lógica de negocio.
- Acceso directo a red o base de datos desde la vista.
- Dependencias creadas “a mano” dentro de cada pantalla.
- Clases con demasiadas responsabilidades.
- Reescribir una feature completa sin necesidad.
- Mezclar MVC, MVP y Clean Architecture sin criterio.
- Agregar threading manual sin control.
- Duplicar lógica entre `home` y `moredetails`.

---

## 13. Criterio práctico de mantenimiento

Si una modificación toca varias capas, seguir este orden:

- primero dominio o contrato,
- luego datos,
- luego presentación,
- por último UI.

Si una pantalla ya sigue un estilo concreto, mantener ese estilo dentro de la feature para evitar inconsistencia.

---

## 14. Objetivo del proyecto

El objetivo no es imponer una arquitectura perfecta, sino mantener una base sólida, comprensible y sostenible.

Las prioridades son:

- legibilidad,
- separación razonable de responsabilidades,
- bajo acoplamiento,
- fácil mantenimiento,
- y compatibilidad con la estructura actual del proyecto.