import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import API from '../api/axios';


interface UserDetails {
    nombreUsuario: string;
    nombre: string;
    apellido: string;
    email: string;
    cuit: string;
    rol: string;
    empresa: any; // You can expand this if you have an Empresa type
}

interface AuthContextType {
    user: UserDetails | null;
    loading: boolean;
    login: (nombreUsuario: string, contrasena: string) => Promise<{ success: boolean; message?: string }>;
    register: (registerData: any) => Promise<{ success: boolean; message?: string }>;
    logout: () => void;
}

// 2. Initialize the context with the explicit type or null
const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<UserDetails | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const storedUser = sessionStorage.getItem('user');
        const storedToken = sessionStorage.getItem('token');

        if (storedUser && storedToken) {
            setUser(JSON.parse(storedUser));
        }
        setLoading(false);
    }, []);

    const login = async (nombreUsuario: string, contrasena: string) => {
        try {
            const response = await API.post('/auth/login', { nombreUsuario, password: contrasena });
            const { token, ...userDetails } = response.data;

            sessionStorage.setItem('token', token);
            sessionStorage.setItem('user', JSON.stringify(userDetails));

            setUser(userDetails);
            return { success: true };
        } catch (error: any) {
            console.error("Login failed:", error);
            return {
                success: false,
                message: error.response?.data || "Error de conexión"
            };
        }
    };

    const register = async (registerData: any) => {
        try {
            await API.post('/auth/register', registerData);
            return { success: true };
        } catch (error: any) {
            console.error("Registration failed:", error);
            return {
                success: false,
                message: error.response?.data || "Error al registrar"
            };
        }
    };

    const logout = () => {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('user');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, register, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used inside an AuthProvider');
    }
    return context;
};