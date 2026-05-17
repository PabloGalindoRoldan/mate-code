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
            config.headers.Authorization = `Bearer ${token}`;
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