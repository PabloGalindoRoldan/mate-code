## 🚀 Sitio de despliegue:
https://pablogalindoroldan.github.io/mate-code/

## 🚀 Cómo empezar

Seguí estos pasos para clonar el repositorio y tener una copia local funcionando en tu máquina.

### 1. Clonar el repositorio
Abrí una terminal y ejecutá el siguiente comando:

```bash
git clone https://github.com/PabloGalindoRoldan/mate-code.git
```

### 2. Ingresar al directorio
Entrá en la carpeta del proyecto:

```bash
cd mate-code
```

### 3. Instalar dependencias
Asegurate de tener [Node.js](https://nodejs.org/) instalado y luego ejecutá:

```bash
npm install
```

### 4. Iniciar el servidor de desarrollo
Una vez instaladas las dependencias, levantá el proyecto con:

```bash
npm run dev
```

### 5. CONVENCIONES A SEGUIR
- Nombrar las carpetas con camelCase y sin espacios ni guiones (camelCase arranca en minuscula y separa las palabras con mayusculas; mate y code seria mateYCode)
- Nombrar los archivos con PascalCase (arranca con mayuscula y separa las palabras con mayusculas, mate y code seria MateYCode)
- NO HACER PUSH A MAIN. La rama colectora es develop, pero tampoco se pushea a develop. Cada feature se pushea a su propia rama nombrada "nroX-tituloDelTicket" y despues se hace el pull-request a develop (importante chequear que el pull request sea a develop!!)
- 
