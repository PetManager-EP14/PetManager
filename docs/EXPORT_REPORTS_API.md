# API de Exportación de Reportes de Ventas

## Descripción
Esta funcionalidad permite exportar los reportes de ventas (diarios y semanales) en formato **PDF** o **Excel** (XLSX).

## Endpoints Disponibles

### 1. Exportar Reporte Diario a Excel
**GET** `/api/sales/report/daily/export/excel`

**Descripción:** Descarga el reporte de ventas del día actual en formato Excel.

**Autenticación:** Requiere permiso `sale.read`

**Respuesta:**
- **Content-Type:** `application/octet-stream`
- **Nombre del archivo:** `reporte-diario.xlsx`

**Ejemplo con curl:**
```bash
curl -X GET "http://localhost:8080/api/sales/report/daily/export/excel" \
  -H "Authorization: Bearer <tu-token-jwt>" \
  --output reporte-diario.xlsx
```

---

### 2. Exportar Reporte Diario a PDF
**GET** `/api/sales/report/daily/export/pdf`

**Descripción:** Descarga el reporte de ventas del día actual en formato PDF.

**Autenticación:** Requiere permiso `sale.read`

**Respuesta:**
- **Content-Type:** `application/pdf`
- **Nombre del archivo:** `reporte-diario.pdf`

**Ejemplo con curl:**
```bash
curl -X GET "http://localhost:8080/api/sales/report/daily/export/pdf" \
  -H "Authorization: Bearer <tu-token-jwt>" \
  --output reporte-diario.pdf
```

---

### 3. Exportar Reporte Semanal a Excel
**GET** `/api/sales/report/weekly/export/excel`

**Descripción:** Descarga el reporte de ventas de los últimos 7 días en formato Excel.

**Autenticación:** Requiere permiso `sale.read`

**Respuesta:**
- **Content-Type:** `application/octet-stream`
- **Nombre del archivo:** `reporte-semanal.xlsx`

**Ejemplo con curl:**
```bash
curl -X GET "http://localhost:8080/api/sales/report/weekly/export/excel" \
  -H "Authorization: Bearer <tu-token-jwt>" \
  --output reporte-semanal.xlsx
```

---

### 4. Exportar Reporte Semanal a PDF
**GET** `/api/sales/report/weekly/export/pdf`

**Descripción:** Descarga el reporte de ventas de los últimos 7 días en formato PDF.

**Autenticación:** Requiere permiso `sale.read`

**Respuesta:**
- **Content-Type:** `application/pdf`
- **Nombre del archivo:** `reporte-semanal.pdf`

**Ejemplo con curl:**
```bash
curl -X GET "http://localhost:8080/api/sales/report/weekly/export/pdf" \
  -H "Authorization: Bearer <tu-token-jwt>" \
  --output reporte-semanal.pdf
```

---

## Contenido de los Reportes

Ambos formatos (PDF y Excel) incluyen:

### Resumen General
- **Ingresos Totales:** Suma total de ventas en el período
- **Cantidad Total Vendida:** Suma de todas las unidades vendidas

### Detalle por Producto
Tabla con las siguientes columnas:
- **Producto:** Nombre del producto
- **Cantidad Vendida:** Unidades vendidas del producto
- **Ingresos:** Total generado por ese producto

---

## Formato de los Archivos

### Excel (.xlsx)
- Hoja de cálculo con formato profesional
- Celdas con formato de moneda para valores monetarios
- Celdas con formato numérico para cantidades
- Columnas autoajustadas
- Encabezados con fondo gris y bordes

### PDF
- Documento formateado con tablas
- Títulos y encabezados en negrita
- Alineación centrada para el título principal
- Formato de moneda con símbolo de peso colombiano ($)
- Formato numérico para cantidades

---

## Dependencias Utilizadas

### Apache POI (Excel)
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```

### iText 7 (PDF)
```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
    <type>pom</type>
</dependency>
```

---

## Manejo de Errores

Si ocurre un error durante la generación del reporte, el endpoint retornará:
- **Status Code:** `500 Internal Server Error`
- **Body:** Vacío

**Posibles causas:**
- Error al acceder a la base de datos
- Problema al generar el archivo PDF/Excel
- Falta de memoria para procesar reportes muy grandes

---

## Pruebas desde Swagger

Una vez que la aplicación esté ejecutándose, puedes probar los endpoints desde:
```
http://localhost:8080/swagger-ui/index.html
```

Busca el controlador `sale-controller` y verás los 4 nuevos endpoints de exportación.

---

## Notas Técnicas

1. **Formato de fechas:** `dd/MM/yyyy HH:mm`
2. **Formato de moneda:** Peso colombiano ($)
3. **Locale:** `es_CO` (Español Colombia)
4. **Codificación:** UTF-8
5. **Zona horaria:** La del servidor

---

## Ejemplo de Uso en Frontend (JavaScript)

```javascript
// Descargar reporte diario en Excel
async function downloadDailyExcel() {
    const token = localStorage.getItem('jwt-token');
    
    const response = await fetch('http://localhost:8080/api/sales/report/daily/export/excel', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'reporte-diario.xlsx';
        document.body.appendChild(a);
        a.click();
        a.remove();
    }
}

// Descargar reporte semanal en PDF
async function downloadWeeklyPdf() {
    const token = localStorage.getItem('jwt-token');
    
    const response = await fetch('http://localhost:8080/api/sales/report/weekly/export/pdf', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'reporte-semanal.pdf';
        document.body.appendChild(a);
        a.click();
        a.remove();
    }
}
```
