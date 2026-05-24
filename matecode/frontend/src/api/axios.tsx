import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8080',
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
    }
};

export const lotesApi = {
    getMapaLotes: async (config = {}) => {
        const response = await API.get('/api/lotes', config);
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

    // Optional handler placeholder: if your backend requires updating the full EmpresaDTO object 
    // to toggle isRadicada, map it here. If you use a custom endpoint, align it accordingly:
    actualizarEstadoRadicacion: async (cuit: string, radicada: boolean) => {
        // Adjust endpoint signature according to backend expectations if different
        const response = await API.put(`/api/empresas/${cuit}/radicacion`, { radicada });
        return response.data;
    },

    ocuparLote: async (cuit: string, idlote: number | null) => {
        const response = await API.post('/api/empresas/ocupar', {
            cuit,
            idlote
        });

        return response.data;
    }

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

