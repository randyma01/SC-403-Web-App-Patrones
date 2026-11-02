# Universidad Fidélitas
## Facultad de Ciencias de la Computación
### SC-403: Desarrollo de Aplicaciones Web y Patrones

---

# Informe de Procedimientos y Resultados
## Práctica Programada #2: Consultas Especializadas (Ampliadas, JPQL y SQL Nativo)

**Curso:** SC-403 Desarrollo de Aplicaciones Web y Patrones  
**Valor:** 5%  
**Periodo:** III Cuatrimestre 2026  
**Tecnologías:** Java 17/21, Spring Boot 4.1.x, Spring Data JPA, Hibernate, MySQL 8, Thymeleaf, WebJars (Bootstrap 5.3, Font Awesome 6.5, jQuery 3.7)  

---

## 1. Introducción y Objetivos

El presente documento expone de forma detallada y rigurosa el desarrollo, arquitectura y evidencia de funcionamiento de la **Práctica Programada #2**, cuyo propósito es implementar y comparar los tres paradigmas de consulta a base de datos relacionales vistos entre las semanas 6 y 8 del curso:
1. **Consultas Ampliadas (Derived Query Methods):** Basadas en la convención de nomenclatura de Spring Data JPA.
2. **Consultas JPQL (Java Persistence Query Language):** Consultas orientadas a objetos y entidades Java gestionadas por Hibernate.
3. **Consultas SQL Nativas (Native Queries):** Sentencias SQL directas ejecutadas contra el motor relacional MySQL mediante `nativeQuery = true`.

Se tomó como base la arquitectura del proyecto de clase (*TechShop*), donde se filtra la entidad `Producto` por rangos de precio, y se replicó de manera análoga sobre la entidad `Arbol` (Árboles de Costa Rica), utilizando como atributo numérico de filtrado la **Dureza de Madera** (escala del 1 al 10), además de búsquedas de texto por nombre común y hábitat.

---

## 2. Modelo de Datos y Entidad Elegida

### 2.1 Tabla `arbol` (Esquema `practica`)
La entidad elegida modela especies forestales representativas de Costa Rica con los siguientes atributos:

| Campo | Tipo | Restricción | Propósito en Consultas |
|---|---|---|---|
| `id_arbol` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `nombre_comun` | VARCHAR(60) | NOT NULL, UNIQUE | Búsqueda por texto y ordenamiento alfabético |
| `tipo_flor` | VARCHAR(60) | NOT NULL | Características morfológicas |
| `dureza_madera` | INT | NOT NULL, CHECK (1 BETWEEN 10) | **Atributo numérico clave para filtrado por rangos** (equivalente a `precio`) |
| `habitat` | VARCHAR(150) | NOT NULL | Filtrado por región ecológica / texto |
| `ruta_imagen` | VARCHAR(1024) | NULL | Fotografía en almacenamiento |
| `activo` | BOOLEAN | DEFAULT TRUE | Estado lógico de visualización |

### 2.2 Script de Base de Datos (`creaTablas.sql`)
```sql
CREATE DATABASE IF NOT EXISTS practica
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE practica;

CREATE TABLE arbol (
  id_arbol INT NOT NULL AUTO_INCREMENT,
  nombre_comun VARCHAR(60) NOT NULL,
  tipo_flor VARCHAR(60) NOT NULL,
  dureza_madera INT NOT NULL CHECK (dureza_madera BETWEEN 1 AND 10),
  habitat VARCHAR(150) NOT NULL,
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_arbol),
  UNIQUE (nombre_comun),
  INDEX ndx_arbol_nombre (nombre_comun),
  INDEX ndx_arbol_dureza (dureza_madera)
) ENGINE = InnoDB;
```

---

## 3. Implementación de los Tres Tipos de Consultas

La práctica implementa la totalidad de los tres tipos de consultas requeridos en la consigna:

### 3.1 a) Consulta Ampliada (Spring Data JPA Query Method)
* **Concepto:** Spring Data JPA interpreta automáticamente el nombre del método de la interfaz repository y deriva la consulta correspondiente sin requerir sentencias explícitas.
* **Firma en `ArbolRepository`:**
  ```java
  public List<Arbol> findByDurezaMaderaBetweenOrderByNombreComun(int durezaInf, int durezaSup);
  ```
* **Consulta SQL generada en consola por Hibernate:**
  ```sql
  select a1_0.id_arbol, a1_0.activo, a1_0.dureza_madera, a1_0.habitat, 
         a1_0.nombre_comun, a1_0.ruta_imagen, a1_0.tipo_flor 
  from arbol a1_0 
  where a1_0.dureza_madera between ? and ? 
  order by a1_0.nombre_comun;
  ```

### 3.2 b) Consulta JPQL (Java Persistence Query Language)
* **Concepto:** Consulta declarada con `@Query` que hace referencia directa a la entidad Java `Arbol` y sus atributos, independientemente del nombre físico de las columnas en la base de datos.
* **Firma en `ArbolRepository`:**
  ```java
  @Query(value = "SELECT a FROM Arbol a WHERE a.durezaMadera BETWEEN :durezaInf AND :durezaSup ORDER BY a.nombreComun ASC")
  public List<Arbol> consultaJPQL(@Param("durezaInf") int durezaInf, @Param("durezaSup") int durezaSup);
  ```
* **Traducción SQL efectuada por Hibernate:**
  ```sql
  select a1_0.id_arbol, a1_0.activo, a1_0.dureza_madera, a1_0.habitat, 
         a1_0.nombre_comun, a1_0.ruta_imagen, a1_0.tipo_flor 
  from arbol a1_0 
  where a1_0.dureza_madera between ? and ? 
  order by a1_0.nombre_comun asc;
  ```

### 3.3 c) Consulta SQL Nativa (Native Query)
* **Concepto:** Sentencia SQL estándar enviada directamente al dialecto MySQL mediante `nativeQuery = true`, lo que permite aprovechar características específicas del motor relacional.
* **Firma en `ArbolRepository`:**
  ```java
  @Query(nativeQuery = true, value = "SELECT * FROM arbol a WHERE a.dureza_madera BETWEEN :durezaInf AND :durezaSup ORDER BY a.nombre_comun ASC")
  public List<Arbol> consultaSQL(@Param("durezaInf") int durezaInf, @Param("durezaSup") int durezaSup);
  ```
* **Ejecución directa en MySQL:**
  ```sql
  SELECT * FROM arbol a 
  WHERE a.dureza_madera BETWEEN ? AND ? 
  ORDER BY a.nombre_comun ASC;
  ```

---

## 4. Capas del Patrón MVC

### 4.1 Capa de Modelo (Domain y Repository)
- **`Arbol.java`:** Entidad anotada con `@Entity`, `@Table(name = "arbol")`, validaciones `@NotNull`, `@Min(1)`, `@Max(10)`, `@Size`, getters, setters y serialización.
- **`ArbolRepository.java`:** Extiende `JpaRepository<Arbol, Integer>` con los métodos de consulta ampliada, JPQL y SQL nativo.

### 4.2 Capa de Servicio (`ArbolService.java`)
Contiene la lógica de negocio y transaccionalidad `@Transactional(readOnly = true)`:
```java
@Transactional(readOnly = true)
public List<Arbol> consultaAmpliada(int durezaInf, int durezaSup) {
    return arbolRepository.findByDurezaMaderaBetweenOrderByNombreComun(durezaInf, durezaSup);
}

@Transactional(readOnly = true)
public List<Arbol> consultaJPQL(int durezaInf, int durezaSup) {
    return arbolRepository.consultaJPQL(durezaInf, durezaSup);
}

@Transactional(readOnly = true)
public List<Arbol> consultaSQL(int durezaInf, int durezaSup) {
    return arbolRepository.consultaSQL(durezaInf, durezaSup);
}
```

### 4.3 Capa de Controlador (`PruebasController.java`)
Mapeado a la ruta `/pruebas` conforme al estándar de TechShop (Semana 8):
- `GET /pruebas/listado`: Carga el catálogo inicial sin filtro y los formularios de consulta.
- `POST /pruebas/query1`: Procesa la **Consulta Ampliada**.
- `POST /pruebas/query2`: Procesa la **Consulta JPQL**.
- `POST /pruebas/query3`: Procesa la **Consulta SQL Nativa**.
- `POST /pruebas/queryNombre` y `POST /pruebas/queryHabitat`: Búsquedas por texto complementarias.

### 4.4 Capa de Vista (Thymeleaf)
- **`templates/pruebas/listado.html`:** Página orquestadora que integra los fragmentos modulares.
- **`templates/pruebas/fragmentos.html`:**
  - `filtrosConsultas`: Tres tarjetas con formularios diferenciados visualmente (Azul para Ampliada, Verde para JPQL, Amarillo para SQL Nativo), inputs numéricos con valores por defecto y validación HTML5.
  - `infoConsulta`: Banner informativo que muestra el tipo de consulta ejecutada, la sentencia o método invocado, el contador de resultados y un botón para limpiar filtros.
  - `listadoResultados`: Tabla interactiva con badges coloreados según la dureza de la madera, imágenes y estado activo/inactivo.

---

## 5. Procedimiento para Ejecutar en NetBeans

1. Abrir **Apache NetBeans IDE**.
2. Ir a **File -> Open Project** y seleccionar la carpeta `practicas/PracticaDos`.
3. Asegurarse de que el servicio MySQL esté iniciado y ejecutar el script `src/main/resources/creaTablas.sql` en MySQL Workbench.
4. Hacer clic derecho sobre el proyecto `PracticaDos` y seleccionar **Run** (o presionar `F6`).
5. Abrir el navegador web en: `http://localhost:80/` (o el puerto configurado).
6. Navegar a **Consultas y Pruebas** (`http://localhost:80/pruebas/listado`).
7. Probar cada una de las tres consultas modificando los rangos de dureza (ejemplo: 5 a 8, 9 a 10, 1 a 4).
8. Verificar en la consola de NetBeans cómo Hibernate imprime y formatea la sentencia SQL generada en cada caso.

---

## 6. Conclusiones

1. **Equivalencia Funcional:** Los tres métodos (Consulta Ampliada, JPQL y SQL Nativo) resuelven la misma necesidad de negocio pero ofrecen distintos equilibrios entre productividad, portabilidad y control directo de la base de datos.
2. **Productividad con Spring Data JPA:** Las consultas ampliadas reducen significativamente las líneas de código para consultas comunes basadas en rangos (`Between`) y ordenamientos (`OrderBy`).
3. **Flexibilidad con JPQL:** JPQL proporciona seguridad de tipos y portabilidad entre diferentes motores de bases de datos relacionales sin acoplar la aplicación a la sintaxis propietaria de MySQL.
4. **Control con SQL Nativo:** El soporte nativo garantiza que consultas de alta complejidad o dependientes de optimizadores específicos de MySQL puedan ser integradas limpiamente en la arquitectura Spring Boot.
