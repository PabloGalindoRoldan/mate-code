import axios from 'axios';

const API = axios.create({
    baseURL: 'https://mate-code-production.up.railway.app',
    headers: {
        'Content-Type': 'application/json',
    },
});

API.interceptors.request.use(
    (config) => {
        const token = sessionStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token.trim()}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);
export default API;

export const mensajeriaApi = {
    // 1. Obtiene el listado de hilos/contactos activos para el panel lateral
    getConversaciones: async () => {
        const response = await API.get('/api/mensajes/conversaciones');
        return response.data; // Retorna List<ConversacionDTO>
    },

    // 2. Obtiene el historial de chat cruzado con un usuario específico
    getHistorial: async (contactoUsername: string) => {
        const response = await API.get(`/api/mensajes/conversacion/${contactoUsername}`);
        return response.data; // Retorna List<Mensaje>
    },

    // 3. Envía un nuevo mensaje al receptor indicado
    enviarMensaje: async (receptorUsername: string, contenido: string) => {
        const response = await API.post('/api/mensajes', {
            receptorUsername,
            contenido
        });
        return response.data; // Retorna el Mensaje guardado
    }
};

export const presupuestoApi = {
    // 1. Obtiene el balance analítico de partidas consolidado por año
    getBalance: async (ejercicio: number) => {
        const response = await API.get(`/api/presupuesto/balance/${ejercicio}`);
        return response.data; // Retorna BalancePartidaDTO[]
    },

    // 2. Registra incrementos o disminuciones (Validado bajo el Art. 27 de la Ley)
    reestructurarPartida: async (data: { presupuestoId: number; tipo: 'INCREMENTO' | 'DISMINUCION'; monto: number; justificacion: string }) => {
        const response = await API.post('/api/presupuesto/reestructurar', data);
        return response.data;
    },

    // 3. Registra afectaciones de gasto (COMPROMISO, DEVENGADO, PAGADO)
    registrarGasto: async (data: { presupuestoId: number; tipoComprobante: string; nroComprobante: string; descripcion: string; fase: 'COMPROMISO' | 'DEVENGADO' | 'PAGADO'; monto: number }) => {
        const response = await API.post('/api/presupuesto/gasto', data);
        return response.data;
    },

    cargarPresupuesto: async (partidas: any[]) => {
        return await API.post('/api/presupuesto/carga-inicial', partidas);
    },

    crearPartida: async (data: {
        codigo: string;
        nombre: string;
        nivel: 'PRINCIPAL' | 'PARCIAL' | 'SUBPARCIAL';
        parentId: number | null
    }) => {
        const response = await API.post('/api/presupuesto/partidas', data);
        return response.data;
    },

    getHistorialPartida: async (presupuestoId: number) => {
        const response = await API.get(`/api/presupuesto/partidas/${presupuestoId}/historial`);
        return response.data; // Debe retornar un array de MovimientoHistorial ordenado por fecha
    },

    getCatalogo: async () => {
        const response = await API.get('/api/presupuesto/partidas-catalogo');
        return response.data;
    },
};

export const lotesApi = {
    getMapaLotes: async (config = {}) => {
        const response = await API.get('/api/lotes', config);
        return response.data;
    }
};

export const inventarioApi = {
    listarInventario: async (soloActivos = false, config = {}) => {
        const response = await API.get('/api/inventario', {
            params: { soloActivos },
            ...config,
        });
        return response.data;
    }
};

export const consumosApi = {
    // Fetches the entire park's consumption logs for a given calendar year
    getReporteGlobal: async (ano: number) => {
        const response = await API.get(`/api/consumos/reporte-global/${ano}`);
        return response.data; // Returns List<ConsumoResponseDTO>
    },

    // Fetches the entire consumption history for a specific enterprise by its CUIT
    getHistorialPorEmpresa: async (cuit: string) => {
        // Apunta al endpoint de administración pasándole el CUIT en la URL
        const response = await API.get(`/api/consumos/historial/${cuit}`);
        return response.data; // Returns List<ConsumoRecord> o List<ConsumoResponseDTO>
    }
};

export const empresasApi = {
    listarEmpresas: async (config = {}) => {
        const response = await API.get('/api/empresas', config);
        return response.data; // Returns List<EmpresaDTO>
    },

    // Links a physical plot asset (idLote) to an enterprise CUIT line
    asignarLote: async (cuit: string, idlote: number | null) => {
        const response = await API.post('/api/empresas/AsignarLote', { cuit, idlote });
        return response.data;
    },

    actualizarEstadoRadicacion: async (cuit: string, radicada: boolean) => {
        const response = await API.put(`/api/empresas/${cuit}/radicacion`, { radicada });
        return response.data;
    },

    ocuparLote: async (cuit: string, idlote: number | null) => {
        const response = await API.post('/api/empresas/ocupar', {
            cuit,
            idlote
        });
        return response.data;
    },

    desocuparLote: async (cuit: string) => {
        const response = await API.post('/api/empresas/desocupar', {
            cuit,
        });
        return response.data;
    },

};

export const authApi = {

    changePassword: async (data: {
        currentPassword: string;
        newPassword: string;
        confirmPassword: string;
    }) => {

        const response = await API.post(
            '/auth/change-password',
            data
        );

        return response.data;
    }
};

export const proyectosApi = {

    crearProyecto: async (data: any) => {
        const response = await API.post(
            '/api/proyectos/crear',
            data
        );

        return response.data;
    },

    proyectosPorCuit: async (cuit: string) => {
        const response = await API.post(
            `/api/proyectos/porCuit`,
            { cuit: cuit }
        );

        return response.data;
    },

    crearProyectoDefinitivo: async (data: any) => {
        const response = await API.post(
            '/api/proyectos/crearDefinitivo',
            data
        );

        return response.data;
    },

    actualizarDefinitivo: async (data: any) => {
        const response = await API.put(
            '/api/proyectos/actualizarDefinitivo',
            data
        );

        return response.data;
    },

    actualizarPreliminar: async (data: any) => {
        const response = await API.put(
            '/api/proyectos/actualizarPreliminar',
            data
        );

        return response.data;
    },

    listarProyectos: async () => {
        const response = await API.get('/api/proyectos');
        return response.data;
    },

    cambiarEstadoPreliminar: async (data: {
        proyectoId: number;
        estado: string;
    }) => {
        const response = await API.put(
            '/api/proyectos/cambiarEstadoPreliminar',
            data
        );
        return response.data;
    },

    cambiarEstadoDefinitivo: async (data: {
        proyectoId: number;
        estado: string;
    }) => {
        const response = await API.put(
            '/api/proyectos/cambiarEstadoDefinitivo',
            data
        );
        return response.data;
    },


};
